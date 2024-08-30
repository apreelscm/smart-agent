import {Panel, PanelConstructor} from "./components/panels.ts";
import {EVENTS} from "./events/events.ts";
import {createEventBus, EventBus, Events} from "./events/event-bus.ts";
import {MarketingConsentsPanel} from "./components/MarketingConsentsPanel/MarketingConsentsPanel.ts";

export class App {
    private panels: Panel<EVENTS>[] = []

    constructor(
        private events: EventBus<EVENTS> = createEventBus(),
        public readonly Panels = {}
    ) {
        this.Panels = {
            MarketingConsents: this.registerPanel(MarketingConsentsPanel)
        }
    }

    public init() {
        console.log("EUMOWY JS Lib initialized")
    }

    private registerPanel<T extends Panel<E>, E extends Events>(ctor: PanelConstructor<T, E>) {
        return (rootSelector: string) => {
            let panel = new ctor(this.events, rootSelector)
            this.panels.push(panel)
            return panel
        }
    }
}