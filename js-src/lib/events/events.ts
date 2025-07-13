import {DocumentsFormat} from "../components/DocumentsFormatPanel/DocumentsFormatPanel.ts";
import {DocumentsSignedEvent} from "../components/DocumentsSigningPanel/DocumentsSigningPanel.ts";

export type EVENTS = {
    PANEL_VALIDATION_ERRORS: (...errors: any[]) => void;
    DOCUMENTS_FORMAT_SELECTED: (format: DocumentsFormat) => void;
    ONLY_PAPER_DOCUMENTS_FORMAT_ALLOWED: () => void;
    DOCUMENTS_SIGNED: (ev: DocumentsSignedEvent) => void;
}
