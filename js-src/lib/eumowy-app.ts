import {Panel, PanelConstructor} from "./components/panels.ts";
import {EVENTS} from "./events/events.ts";
import {createEventBus, EventBus, Events} from "./events/event-bus.ts";
import {MarketingConsentsPanel} from "./components/MarketingConsentsPanel/MarketingConsentsPanel.ts";
import {DocumentsSigningPanel} from "./components/DocumentsSigningPanel/DocumentsSigningPanel.ts";
import {DocumentsFormatPanel} from "./components/DocumentsFormatPanel/DocumentsFormatPanel.ts";
import {DocumentsControlPanel} from "./components/DocumentsControlPanel/DocumentsControlPanel.ts";

export class App {
    private panels: Panel<EVENTS, unknown>[] = []

    constructor(
        private events: EventBus<EVENTS> = createEventBus(),
        public readonly Panels = {}
    ) {
        this.Panels = {
            MarketingConsents: this.registerPanel(MarketingConsentsPanel),
            DocumentsSigningPanel: this.registerPanel(DocumentsSigningPanel),
            DocumentsFormatPanel: this.registerPanel(DocumentsFormatPanel),
            DocumentsControlPanel: this.registerPanel(DocumentsControlPanel),
        }
    }

    public init() {
        console.log("EUMOWY JS Lib initialized")
    }

    private registerPanel<T extends Panel<E, P>, E extends Events, P>(ctor: PanelConstructor<T, E, P>) {
        return (rootSelector: string, props: P) => {
            let panel = new ctor(this.events, props, rootSelector)
            this.panels.push(panel)
            return panel
        }
    }
}
