(function ($) {
    $(function() {
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
                $(this).fadeOut();
            });

        $('.errorNotification').click(function(){
            var message = $(this).data("message")
            alert(message);
        })

    });
}(jQuery));

function showLoadingDialog(){
    jQuery("#loadingDialog").dialog({
        resizable: true,
        height: 100,
        width: 250,
        modal: true
    });
}

function showDeletingAttachmentDialog(current){
    jQuery(current).removeAttr('href');
    jQuery("#deletingAttachment").dialog({
        resizable: true,
        height: 100,
        width: 250,
        modal: true
    })
}

function hideDeletingAttachmentDialog(){
    jQuery("#deletingAttachment").dialog("close");
}