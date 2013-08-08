/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 24.07.13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    "use strict";

    var timeout = 500;
    var closetimer = 0;
    var ddmenuitem = 0;
    var $j = jQuery.noConflict();

    var touchStarted = false
    var currX = 0
    var currY = 0
    var cachedX = 0
    var cachedY = 0;

    $j(document).ready(function () {
        $j('#mainMenu > li').bind('onclick', jsddm_open);
        $j('#mainMenu > li').bind('touchend', jsddm_timer);

      /*  $j('#mainMenu > li').bind('mouseover', jsddm_open);
        $j('#mainMenu > li').bind('mouseout', jsddm_timer);*/
    });

    function jsddm_open(e) {

        var cachedX = e.pageX;
        cachedY = e.pageY;
        touchStarted = true;

        console.log("cachedX:"+cachedX+" cachedY:"+cachedY)
        setTimeout(function (){
            currX = e.pageX;
            currY = e.pageY;
            if ((cachedX === currX) && !touchStarted && (cachedY === currY)) {
                // Here you get the Tap event
                $touchArea.text('Tap');
            }
        },200);

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

    document.onclick = jsddm_close;

}(jQuery));