if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});


        $('.errorNotification').click(function(){
            var message = $(this).data("message")
            alert(message);
        })

	})(jQuery);
}