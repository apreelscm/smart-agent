import {Panel} from "../panels.ts";
import {EVENTS} from "../../events/events.ts";
import {EventBus} from "../../events/event-bus.ts";
import {DocumentSignature, DocumentsSigningApi} from "../../api/documents-signing.ts";
import {DocumentsFormat} from "../DocumentsFormatPanel/DocumentsFormatPanel.ts";
import $ from 'jquery';

declare global {
    function showLoadingDialog(msg: string): void;
    function showErrorDialog(msg: string): void;
    function closeDialog(): void;
}

type DocumentsSigningPanelProps = {
    processId: number;
    signatures: Signature[];
    signUrl: string;
    refreshSigningCodeUrl: string;
}
export type Signature = {
    personRole: string;
}
type SignatureControls = {
    container: JQuery,
    input: JQuery,
    signDocumentBtn: JQuery,
    refreshSigningCodeBtn: JQuery,
};
type OnClickFn = (ev: JQuery.ClickEvent) => void;
type OnChangeFn = (ev: JQuery.ChangeEvent) => void;

export type DocumentsSignedEvent = {
    signatures: Signature[];
};

export class DocumentsSigningPanel extends Panel<EVENTS, DocumentsSigningPanelProps> {
    private readonly api: DocumentsSigningApi;
    private signatureControls: SignatureControls[] = [];
    private rolesThatSigned: string[] = [];

    constructor(eventBus: EventBus<EVENTS>, props: DocumentsSigningPanelProps, rootSelector: string) {
        super(eventBus, props, rootSelector);

        this.api = new DocumentsSigningApi(this.props.signUrl, this.props.refreshSigningCodeUrl);
        this.querySignatureControls("representative1");
        this.querySignatureControls("representative2");
        this.querySignatureControls("representative3");
        this.querySignatureControls("representative4");
        this.querySignatureControls("ph");

        this.events.on('DOCUMENTS_FORMAT_SELECTED', this.handleDocumentsFormatSelected.bind(this));
        this.events.on('ONLY_PAPER_DOCUMENTS_FORMAT_ALLOWED', () => this.disableSigning());
    }

    mount(): void {
        this.rolesThatSigned = this.props.signatures.map(s => s.personRole);
        this.signatureControls.forEach(ctrl => {
            const personRole = ctrl.container.attr("data-personRole");
            if (personRole && this.rolesThatSigned.includes(personRole)) {
                this.disableControls(ctrl);
            } else {
                ctrl.input.on('change', this.createCodeEditedHandler(ctrl));
                ctrl.signDocumentBtn.on("click", this.createSignDocumentsHandler(ctrl));
                ctrl.refreshSigningCodeBtn.on("click", this.createRefreshSigningCodeHandler(ctrl));
            }
        });
    }

    unmount(): void {
    }

    private handleDocumentsFormatSelected(documentsFormat: DocumentsFormat): void {
        if (documentsFormat == 'TEMPLATES') {
            this.disableSigning();
        } else {
            this.enableSigning();
        }
    }

    private handleCodeEditedHandler(ev: JQuery.ChangeEvent, ctrl: SignatureControls): void {
        this.resetValidation(ctrl.input);
    }

    private handleSignDocumentsClicked(ev: JQuery.ClickEvent, ctrl: SignatureControls): void {
        ev.preventDefault();
        const isValid = this.validate(ctrl.input);
        const personRole = ctrl.container.attr("data-personRole");
        if (!isValid || !personRole) {
            return;
        }

        showLoadingDialog('Składanie podpisu...');
        this.api.signDocuments({
            processId: this.props.processId,
            personRole: personRole,
            code: ctrl.input.val() as string
        }).then(docSignature => {
            this.refreshProcessStatus(docSignature)
                .then(() => {
                    this.documentsSigned(personRole);
                    this.disableControls(ctrl);
                })
                .catch(reason => alert(reason))
                .finally(() => closeDialog());
        }).catch(() => {
            this.error(ctrl.input);
            closeDialog();
        });
    }

    private handleRefreshSigningCodeClicked(ev: JQuery.ClickEvent, ctrl: SignatureControls): void {
        ev.preventDefault();
        ctrl.input.val("");
        this.resetValidation(ctrl.input);
        const personRole = ctrl.container.attr("data-personRole");
        if (!personRole) {
            return;
        }
        this.disableButton(ctrl.refreshSigningCodeBtn);
        this.refreshSigningCode(personRole)
            .then(() => {
                setTimeout(() => this.enableButton(ctrl.refreshSigningCodeBtn), 15 * 1000);
            }).catch(() => this.enableButton(ctrl.refreshSigningCodeBtn));
        // this.api.refreshSigningCode({
        //     processId: this.props.processId,
        //     personRole: personRole,
        // }).then(() => {
        //     setTimeout(() => this.enableButton(ctrl.refreshSigningCodeBtn), 15 * 1000);
        // }).catch(() => this.enableButton(ctrl.refreshSigningCodeBtn));
    }

    private documentsSigned(role: string): void {
        this.rolesThatSigned.push(role);
        this.events.emit('DOCUMENTS_SIGNED', {
            signatures: this.rolesThatSigned.map(role => ({ personRole: role }))
        });
    }

    /**
     * Disable signing for those who didn't sign yet
     * @private
     */
    private disableSigning(): void {
        this.signatureControls.forEach(ctrl => {
            const personRole = ctrl.container.attr("data-personRole");
            if (personRole && !this.rolesThatSigned.includes(personRole)) {
                this.disableControls(ctrl);
            }
        });
    }

    /**
     * Enable signing, but only for those who didn't sign yet
     * @private
     */
    private enableSigning(): void {
        this.signatureControls.forEach(ctrl => {
            const personRole = ctrl.container.attr("data-personRole");
            if (personRole && !this.rolesThatSigned.includes(personRole)) {
                this.enableControls(ctrl);
            }
        });
    }

    private createCodeEditedHandler(ctrl: SignatureControls): OnChangeFn {
        return (ev: JQuery.ChangeEvent) => {
            this.handleCodeEditedHandler(ev, ctrl);
        }
    }

    private createSignDocumentsHandler(ctrl: SignatureControls): OnClickFn {
        return (ev: JQuery.ClickEvent) => {
            this.handleSignDocumentsClicked(ev, ctrl);
        }
    }

    private createRefreshSigningCodeHandler(ctrl: SignatureControls): OnClickFn {
        return (ev: JQuery.ClickEvent) => {
            this.handleRefreshSigningCodeClicked(ev, ctrl);
        }
    }

    private querySignatureControls(prefix: string): void {
        const container = this.$root.find(`#${prefix}SignatureContainer`);
        if (container) {
            this.signatureControls.push({
                container: container,
                input: container.find(`#${prefix}SigningCode`),
                signDocumentBtn: container.find(`#${prefix}SignDocuments`),
                refreshSigningCodeBtn: container.find(`#${prefix}RefreshSigningCode`),
            });
        }
    }

    private validate(input: JQuery): boolean {
        const isEmpty = !input.val() || input.val() === "";
        if (isEmpty) {
            this.error(input);
        } else {
            this.resetValidation(input);
        }
        return !isEmpty;
    }

    private resetValidation(input: JQuery): void {
        input.removeClass("error");
    }

    private error(input: JQuery): void {
        input.addClass("error");
    }

    private enableControls(ctrl: SignatureControls): void {
        ctrl.container.removeClass("disabled");
        ctrl.input.css("visibility", "visible");
        ctrl.signDocumentBtn.css("visibility", "visible");
        ctrl.refreshSigningCodeBtn.css("visibility", "visible");
    }

    private disableControls(ctrl: SignatureControls): void {
        ctrl.container.addClass("disabled");
        ctrl.input.css("visibility", "hidden");
        ctrl.signDocumentBtn.css("visibility", "hidden");
        ctrl.refreshSigningCodeBtn.css("visibility", "hidden");
    }

    private disableButton(btn: JQuery): void {
        btn.addClass("disabled");
    }

    private enableButton(btn: JQuery): void {
        btn.removeClass("disabled");
    }

    private refreshProcessStatus(docSignature: DocumentSignature): Promise<void> {
        return new Promise((resolve, reject) => {
            const url = $(window.location).attr('href')!!;
            $.post(url, {_eventId_refreshProcessStatus: "", signatureId: docSignature.signatureId})
                .done(function() {
                    resolve();
                })
                .fail(function() {
                    reject('Wystąpił błąd podczas zapisywania podpisu. Sprawdź swoje połączenie internetowe i spróbuj ponownie później.');
                });
        });
    }

    private refreshSigningCode(personRole: string): Promise<void> {
        return new Promise((resolve, reject) => {
           const url = $(window.location).attr('href')!!;
           $.post(url, {_eventId_refreshPin: "", personRole: personRole})
               .done(function() {
                   resolve();
               })
               .fail(function() {
                  reject('Wystąpił błąd podczas odnawiania kodu do podpisu. Sprawdź swoje połączenie internetowe i spróbuj ponownie później.');
               });
        });
    }
}
