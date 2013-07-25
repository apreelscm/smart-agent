/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 24.07.13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
var timeout = 500;
var closetimer = 0;
var ddmenuitem = 0;
var $j = jQuery.noConflict();

function jsddm_open() {
    jsddm_canceltimer();
    jsddm_close();
    ddmenuitem = $j(this).find('ul').eq(0).css('visibility', 'visible');
}

function jsddm_close() {
    if (ddmenuitem) ddmenuitem.css('visibility', 'hidden');
}

function jsddm_timer() {
    closetimer = window.setTimeout(jsddm_close, timeout);
}

function jsddm_canceltimer() {
    if (closetimer) {
        window.clearTimeout(closetimer);
        closetimer = null;
    }
}

$j(document).ready(function () {
    $j('#mainMenu > li').bind('mouseover', jsddm_open);
    $j('#mainMenu > li').bind('mouseout', jsddm_timer);
});

document.onclick = jsddm_close;