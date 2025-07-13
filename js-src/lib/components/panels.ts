import $ from 'jquery';
import {Component} from "./components.ts";
import {EventBus, Events} from "../events/event-bus.ts";

function throwSelectorNotFound(selector: string): never {
    throw new Error(`Selector ${selector} didn't match any elements.`)
}

export abstract class Panel<T extends Events, P> extends Component<T, P> {
    protected root: HTMLElement
    protected $root: JQuery

    public constructor(eventBus: EventBus<T>, props: P, rootSelector: string) {
        super(eventBus, props)

        this.root = document.querySelector(rootSelector) ?? throwSelectorNotFound(rootSelector)
        this.$root = $(this.root)
    }
}

export type PanelConstructor<T extends Panel<E, P>, E extends Events, P> = new (eventBus: EventBus<E>, props: P, rootSelector: string) => T
