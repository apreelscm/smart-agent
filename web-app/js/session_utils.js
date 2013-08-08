
(function ($) {


    function Clock(bodyId, clockId, redirectPage, time) {

        var body = document.getElementById(bodyId);
        body.addEventListener("mousedown", function() {
            onEvent(body)
        }, false);

        var timer = setTimeout(function() {
            tick()
        }, 1000);
        var clock = document.getElementById(clockId);

        var seconds = time;

        function onEvent(body) {
            seconds = time;
        }

        function tick() {
            timer = setTimeout(function() {
                tick()
            }, 1000);
            seconds--;
            clock.innerHTML = formatTime();
            if (seconds == 0)
                window.location.href = redirectPage;
        }

        function formatTime() {
            var m = Math.floor(seconds / 60)
            var s = seconds % 60;
            if (m < 10)
                m = "0" + m;
            if (s < 10)
                s = "0" + s;
            return m + ":" + s;
        }
    }


// expose Clock as a jQuery plugin
    $.fn.clock = function (options) {
        var instance = new Clock();
        instance.activate(this, options);
        return this;
    };

    var $j = jQuery.noConflict();

    $j(document).ready(function () {
        new Clock("mainBody", "clock", "/eumowy/logout", 600);
        window.setInterval(function () {
            jQuery.post("${createLink(uri:'/ping')}")
                .fail(function() {
                    var message = "Brak połączenia z serwerem";
                    alert(message);
                });
        }, 51000);
    });


}(jQuery));
