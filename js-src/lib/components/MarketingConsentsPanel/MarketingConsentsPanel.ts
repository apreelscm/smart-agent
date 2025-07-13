import {Panel} from "../panels.ts";
import $ from 'jquery';
import {EventBus} from "../../events/event-bus.ts";
import {EVENTS} from "../../events/events.ts";

interface ConsentCheckboxes {
    global: {
        channelAll: JQuery
        channelNone: JQuery
    }
    specific: {
        channelClientPortal: JQuery
        channelEmail: JQuery
        channelSMS: JQuery
        channelPhone: JQuery
    }
}

export class MarketingConsentsPanel extends Panel<EVENTS, never> {
    private checkboxes: ConsentCheckboxes

    constructor(eventBus: EventBus<EVENTS>, props: never, rootSelector: string) {
        super(eventBus, props, rootSelector);
        this.checkboxes = {
            global: {
                channelAll: this.$root.find('#consentsChannelAll'),
                channelNone: this.$root.find('#consentsChannelNone'),
            },
            specific: {
                channelClientPortal: this.$root.find('#consentsChannelClientPortal'),
                channelEmail: this.$root.find('#consentsChannelEmail'),
                channelSMS: this.$root.find('#consentsChannelSMS'),
                channelPhone: this.$root.find('#consentsChannelPhone'),
            }
        }
    }

    mount() {
        this.checkboxes.global.channelAll.on('click', this.handleChannelAllClick.bind(this))
        this.checkboxes.global.channelNone.on('click', this.handleChannelNoneClick.bind(this))
        this.checkboxes.specific.channelClientPortal.on('click', this.handleSpecificChannelClick.bind(this))
        this.checkboxes.specific.channelEmail.on('click', this.handleSpecificChannelClick.bind(this))
        this.checkboxes.specific.channelSMS.on('click', this.handleSpecificChannelClick.bind(this))
        this.checkboxes.specific.channelPhone.on('click', this.handleSpecificChannelClick.bind(this))

        this.setup()
    }

    unmount() {}

    private setup() {
        const specific = Object.values(this.checkboxes.specific)

        if (this.checkboxes.global.channelNone.is(':checked')) {
            this.disable(this.checkboxes.global.channelAll, ...specific)
        } else if (this.checkboxes.global.channelAll.is(':checked')) {
            this.disable(this.checkboxes.global.channelNone)
        } else if (this.isAnyChecked(specific) && !this.isAllChecked(specific)) {
            this.disable(this.checkboxes.global.channelAll, this.checkboxes.global.channelNone)
        }
    }

    private handleChannelAllClick(ev: JQuery.ClickEvent) {
        const checkbox = $(ev.target)
        const isChecked = checkbox.is(':checked')

        if (isChecked) {
            this.uncheck(this.checkboxes.global.channelNone)
            this.disable(this.checkboxes.global.channelNone)
            this.check(...Object.values(this.checkboxes.specific))
        } else {
            // Uncheck all channels, don't touch channelNone
            this.enable(this.checkboxes.global.channelNone)
            this.uncheck(...Object.values(this.checkboxes.specific))
        }
    }

    private handleSpecificChannelClick(ev: JQuery.ClickEvent) {
        const checkboxes = Object.values(this.checkboxes.specific)
        if (this.isAllChecked(checkboxes)) {
            this.check(this.checkboxes.global.channelAll)
            this.enable(this.checkboxes.global.channelAll)
        } else if (this.isAnyChecked(checkboxes)) {
            this.uncheck(this.checkboxes.global.channelAll, this.checkboxes.global.channelNone)
            this.disable(this.checkboxes.global.channelAll, this.checkboxes.global.channelNone)
        } else { // None checked
            this.enable(this.checkboxes.global.channelAll, this.checkboxes.global.channelNone)
        }
    }

    private handleChannelNoneClick(ev: JQuery.ClickEvent) {
        const checkbox = $(ev.target)
        const isChecked = checkbox.is(':checked')

        if (isChecked) {
            // Uncheck and disable all others
            this.uncheck(this.checkboxes.global.channelAll, ...Object.values(this.checkboxes.specific))
            this.disable(this.checkboxes.global.channelAll, ...Object.values(this.checkboxes.specific))
        } else {
            // Enable all others
            this.enable(this.checkboxes.global.channelAll, ...Object.values(this.checkboxes.specific))
        }
    }

    private check(...checkbox: JQuery[]) {
        checkbox.forEach(c => this.checked(c, true))
    }

    private uncheck(...checkbox: JQuery[]) {
        checkbox.forEach(c => this.checked(c, false))
    }

    private disable(...checkbox: JQuery[]) {
        checkbox.forEach(c => this.disabled(c, true))
    }

    private enable(...checkbox: JQuery[]) {
        checkbox.forEach(c => this.disabled(c, false))
    }

    private isAllChecked(checkboxes: JQuery[]): boolean {
        return checkboxes.every(c => c.is(':checked'))
    }

    private isAnyChecked(checkboxes: JQuery[]): boolean {
        return checkboxes.some(c => c.is(':checked'))
    }

    private checked(checkbox: JQuery, checked: boolean) {
        checkbox.prop('checked', checked)
    }

    private disabled(checkbox: JQuery, disabled: boolean) {
        checkbox.prop('disabled', disabled)
    }
}
