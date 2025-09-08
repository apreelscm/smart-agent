import {Panel} from "../panels.ts";
import {EVENTS} from "../../events/events.ts";
import {EventBus} from "../../events/event-bus.ts";
import $ from 'jquery';

declare global {
    function showLoadingDialog(msg: string): void;
    function closeDialog(): void;
}

type DocumentsFormatPanelProps = {
    documentsFormat: DocumentsFormat,
    contactEmail?: string;
    emailForDocuments?: string;
}
type OnChangeFn = (ev: JQuery.ChangeEvent) => void;

export type DocumentsFormat = 'ELECTRONIC' | 'PAPER' | 'TEMPLATES' | 'NONE';

export class DocumentsFormatPanel extends Panel<EVENTS, DocumentsFormatPanelProps> {
    private readonly electronicFormat: JQuery;
    private readonly paperFormat: JQuery;
    private readonly templatesFormat: JQuery;
    private readonly documentsFormat: DocumentsFormat;

    constructor(eventBus: EventBus<EVENTS>, props: DocumentsFormatPanelProps, rootSelector: string) {
        super(eventBus, props, rootSelector);

        this.documentsFormat = props.documentsFormat;

        this.electronicFormat = this.$root.find("#requestVersionElectronical");
        this.paperFormat = this.$root.find("#requestVersionPaper");
        this.templatesFormat = this.$root.find("#requestVersionTemplates");
    }

    mount(): void {
        this.uncheck(this.electronicFormat);
        this.uncheck(this.paperFormat);
        this.uncheck(this.templatesFormat);

        switch (this.documentsFormat) {
            case "ELECTRONIC": {
                if (this.isEmailProvided()) {
                    this.check(this.electronicFormat);
                }
                break;
            }
            case "PAPER": {
                this.check(this.paperFormat); break;
            }
            case "TEMPLATES": {
                if (this.isEmailProvided()) {
                    this.check(this.templatesFormat);
                }
                break;
            }
        }

        if (!this.isEmailProvided()) {
            this.disable(this.electronicFormat);
            this.disable(this.templatesFormat);

            this.events.emit('ONLY_PAPER_DOCUMENTS_FORMAT_ALLOWED');
        }

        this.electronicFormat.on("change", this.createChangeHandler('ELECTRONIC'));
        this.paperFormat.on("change", this.createChangeHandler('PAPER'));
        this.templatesFormat.on("change", this.createChangeHandler('TEMPLATES'));

        this.events.emit('DOCUMENTS_FORMAT_SELECTED', this.documentsFormat);
    }

    unmount(): void {
    }

    private createChangeHandler(documentsFormat: DocumentsFormat): OnChangeFn {
        return (ev: JQuery.ChangeEvent): void => {
            if (jQuery(ev.target).is(":checked")) {
                showLoadingDialog("Aktualizacja...")
                this.saveDocumentsFormat(documentsFormat).then(() => {
                    closeDialog();
                    this.events.emit('DOCUMENTS_FORMAT_SELECTED', documentsFormat);
                });
            }
        };
    }

    private saveDocumentsFormat(documentsFormat: string): Promise<void> {
        return new Promise((resolve, reject) => {
            const url = $(window.location).attr('href')!!;
            $.post(url, {_eventId_saveDocumentsFormat: "", documentsFormat: documentsFormat})
                .done(function() {
                    resolve();
                })
                .fail(function() {
                    reject('Wystąpił błąd podczas zmiany formy dokumentów. Sprawdź swoje połączenie internetowe i spróbuj ponownie później.');
                });
        });
    }

    private check(radio: JQuery): void {
        radio.attr("checked","checked");
    }

    private uncheck(radio: JQuery): void {
        radio.removeAttr("checked");
    }

    private disable(radio: JQuery): void {
        radio.attr("disabled","disabled");
    }

    private isEmailProvided(): boolean {
        return !isEmpty(this.props.contactEmail) || !isEmpty(this.props.emailForDocuments);
    }

}

function isEmpty(s?: string): boolean {
    return s === undefined || s === null || s === "";
}
