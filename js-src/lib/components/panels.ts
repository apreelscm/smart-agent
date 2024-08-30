import $ from 'jquery';
import {Component} from "./components.ts";
import {EventBus, Events} from "../events/event-bus.ts";

function throwSelectorNotFound(selector: string): never {
    throw new Error(`Selector ${selector} didn't match any elements.`)
}

export abstract class Panel<T extends Events> extends Component<T> {
    protected root: HTMLElement
    protected $root: JQuery

    public constructor(eventBus: EventBus<T>, rootSelector: string) {
        super(eventBus)

        this.root = document.querySelector(rootSelector) ?? throwSelectorNotFound(rootSelector)
        this.$root = $(this.root)
    }
}

export type PanelConstructor<T extends Panel<E>, E extends Events> = new (eventBus: EventBus<E>, rootSelector: string) => T
