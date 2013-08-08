/**
 * Created with IntelliJ IDEA.
 * User: mariusz.kaczkowski
 * Date: 24.07.13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    "use strict";

    var timeout = 200;
    var closetimer = 0;
    var ddmenuitem = 0;
    var $j = jQuery.noConflict();

    $j(document).ready(function () {
        $j('#mainMenu > li').bind('touchstart mouseover', jsddm_open);
        $j('#mainMenu > li').bind('mouseleave', jsddm_timer);
    });

    function jsddm_open(event) {
        event.stopPropagation();
        if(event.handled !== true) {
            ddmenuitem = $j(this).find('ul').eq(0).css('visibility', 'visible');
            event.handled = true
            document.addEventListener("touchstart",jsddm_close,false);
        } else {
            return false;
        }
    }

    function jsddm_close() {
        document.removeEventListener("click",jsddm_close,false);
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

}(jQuery));