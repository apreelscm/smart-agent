import {App} from "./eumowy-app.ts";

declare global {
    var EUMOWY: App;
}

/**
 * Install EUMOWY app into a global scope under EUMOWY name
 * and initialize it
 */
(function () {
    EUMOWY = new App()
    EUMOWY.init()
})()