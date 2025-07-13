import {Panel} from "../panels.ts";
import {EVENTS} from "../../events/events.ts";
import {EventBus} from "../../events/event-bus.ts";
import $ from 'jquery';

type DocumentsFormatPanelProps = {
    contactEmail?: string;
    emailForDocuments?: string;
}
type OnChangeFn = (ev: JQuery.ChangeEvent) => void;

export type DocumentsFormat = 'ELECTRONIC' | 'PAPER' | 'TEMPLATES';

export class DocumentsFormatPanel extends Panel<EVENTS, DocumentsFormatPanelProps> {
    private readonly electronicFormat: JQuery;
    private readonly paperFormat: JQuery;
    private readonly templatesFormat: JQuery;

    constructor(eventBus: EventBus<EVENTS>, props: DocumentsFormatPanelProps, rootSelector: string) {
        super(eventBus, props, rootSelector);

        this.electronicFormat = this.$root.find("#requestVersionElectronical");
        this.paperFormat = this.$root.find("#requestVersionPaper");
        this.templatesFormat = this.$root.find("#requestVersionTemplates");
    }

    mount(): void {
        if (this.isEmailProvided()) {
            this.check(this.electronicFormat);
        } else {
            this.check(this.paperFormat);

            this.uncheck(this.electronicFormat);
            this.disable(this.electronicFormat);

            this.uncheck(this.templatesFormat);
            this.disable(this.templatesFormat);

            this.events.emit('ONLY_PAPER_DOCUMENTS_FORMAT_ALLOWED');
        }

        this.electronicFormat.on("change", this.createChangeHandler('ELECTRONIC'));
        this.paperFormat.on("change", this.createChangeHandler('PAPER'));
        this.templatesFormat.on("change", this.createChangeHandler('TEMPLATES'));
    }

    unmount(): void {
    }

    private createChangeHandler(documentsFormat: DocumentsFormat): OnChangeFn {
        return (ev: JQuery.ChangeEvent): void => {
            if (jQuery(ev.target).is(":checked")) {
                this.events.emit('DOCUMENTS_FORMAT_SELECTED', documentsFormat);
            }
        };
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
