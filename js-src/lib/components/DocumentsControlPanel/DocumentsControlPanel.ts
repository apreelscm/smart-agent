import {Panel} from "../panels.ts";
import {EVENTS} from "../../events/events.ts";
import {EventBus} from "../../events/event-bus.ts";
import {DocumentsSignedEvent, Signature} from "../DocumentsSigningPanel/DocumentsSigningPanel.ts";
import {DocumentsFormat} from "../DocumentsFormatPanel/DocumentsFormatPanel.ts";
import $ from 'jquery';

declare global {
    function showNoAcceptDialog(callback: (url: string) => void): void;
    function showSubmitWithoutSigningDialog(callback: (url: string) => void): void;
    function showLoadingDialog(msg: string): void;
    function showErrorDialog(msg: string): void;
}

type OnClickFn = (ev: JQuery.ClickEvent) => void;

type DocumentsControlPanelProps = {
    documentsFormat: DocumentsFormat;
    signatures: Signature[];
    requiredSignatures: Signature[];
    rejectUrl: string;
    prevUrl: string;
    inProgressMsg: string;
    errorMsg: string;
};

export class DocumentsControlPanel extends Panel<EVENTS, DocumentsControlPanelProps> {

    private readonly backButton: JQuery;
    private readonly noAcceptButton: JQuery;
    private readonly continueButton: JQuery;

    private requiredSignatures: Signature[];
    private signaturesDone: Signature[];
    private documentsFormat: DocumentsFormat;

    constructor(eventBus: EventBus<EVENTS>, props: DocumentsControlPanelProps, rootSelector: string) {
        super(eventBus, props, rootSelector);

        this.signaturesDone = this.props.signatures;
        this.requiredSignatures = this.props.requiredSignatures;
        this.documentsFormat = this.props.documentsFormat;

        this.backButton = this.$root.find('#clientSignatureBackButton');
        this.noAcceptButton = this.$root.find('#noaccept');
        this.continueButton = this.$root.find('#continueButton');

        this.events.on('DOCUMENTS_FORMAT_SELECTED', this.handleDocumentsFormatSelected.bind(this));
        this.events.on('DOCUMENTS_SIGNED', this.handleDocumentsSigned.bind(this));
    }

    mount(): void {
        this.noAcceptButton.on("click", this.handleNoAcceptClicked.bind(this));
        this.continueButton.on("click", this.handleContinueClicked.bind(this));
    }

    unmount(): void {
    }

    private handleNoAcceptClicked(ev: JQuery.ClickEvent): void {
        ev.preventDefault();
        const rejectUrl = this.props.rejectUrl;
        showNoAcceptDialog((url: string) => {
            $.post(url, {_eventId_noaccept: ""}, function (data) {
                window.location.href = rejectUrl;
            });
        });
    }

    private handleContinueClicked(ev: JQuery.ClickEvent): boolean {
        ev.preventDefault();
        const prevUrl = this.props.prevUrl;
        const inProgressMsg = this.props.inProgressMsg;
        const errorMsg = this.props.errorMsg;
        const signaturesDoneCount = this.signaturesDone.length;

        let result = false;
        if (!this.isAllSignaturesDone() && this.documentsFormat == "ELECTRONIC") {
            result = false;
            showSubmitWithoutSigningDialog(() => {
                const url = $(window.location).attr('href')!!;
                $.post(url, {
                    _eventId_submit: "",
                    requestVersion: $("input[name=requestVersion]:checked").val(),
                    numberOfSubscriptions: signaturesDoneCount
                }, function (data, textStatus, jqXHR) {
                    window.location.href = prevUrl;
                }).fail(function () {
                    showErrorDialog(errorMsg);
                });
            });
        } else {
            showLoadingDialog(inProgressMsg);

            const url = $(window.location).attr('href')!!;
            $.post(url, {
                _eventId_submit: "",
                requestVersion: $("input[name=requestVersion]:checked").val(),
                numberOfSubscriptions: signaturesDoneCount
            }, function (data, textStatus, jqXHR) {
                window.location.href = prevUrl;
            }).fail(function () {
                showErrorDialog(errorMsg);
            });
        }

        return result;
    }

    private handleDocumentsFormatSelected(documentsFormat: DocumentsFormat): void {
        this.documentsFormat = documentsFormat;
    }

    private handleDocumentsSigned(ev: DocumentsSignedEvent): void {
        this.signaturesDone = ev.signatures;
    }

    private isAllSignaturesDone(): boolean {
        return this.requiredSignatures.length === this.signaturesDone.length &&
            this.requiredSignatures.every(sig => this.signaturesDone.some(s => s.personRole === sig.personRole));
    }

}
