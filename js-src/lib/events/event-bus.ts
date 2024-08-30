type EventListener = (...args: any[]) => void
export type Events = Record<string, EventListener>

export interface EventBus<T extends Events> {
    on<E extends keyof T>(event: E, listener: T[E]): void
    off<E extends keyof T>(event: E, listener: T[E]): void
    emit<E extends keyof T>(event: E, ...args: Parameters<T[E]>): void
}

export function createEventBus<T extends Events>(): EventBus<T> {
    const listeners = {} as Record<keyof T, Set<EventListener>>

    return {
        on: <E extends keyof T>(event: E, listener: T[E]) => {
            if (!listeners[event]) {
                listeners[event] = new Set()
            }
            listeners[event].add(listener)
        },
        off: <E extends keyof T>(event: E, listener: T[E]) => {
            if (!listeners[event]) return

            listeners[event].delete(listener)
        },
        emit: <E extends keyof T>(event: E, ...args: Parameters<T[E]>) => {
            (listeners[event] ?? []).forEach(listener => listener(...args));
        }
    }
}