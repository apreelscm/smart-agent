import {EventBus, Events} from "../events/event-bus.ts";

export interface ComponentMount {
    mount(): void
    unmount(): void
}

export abstract class Component<T extends Events, P> implements ComponentMount {
    protected constructor(protected events: EventBus<T>, protected props: P) {}

    abstract mount(): void

    abstract unmount(): void
}
