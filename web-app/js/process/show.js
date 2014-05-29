jQuery(document).ready(function(){
    jQuery("#back").click(function(event) {
        event.preventDefault();
        history.back();
    });

    jQuery(".renewSubscriptions").click(function(event) {
        var confirmed = confirm(confirmRenewSubscriptionsMessage);

        if(confirmed) {
            jQuery("#renewingSubsriptionsInProgress").dialog({
                height: 100,
                width: 350,
                modal: true
            })
        } else {
            event.preventDefault();
        }
    });

    jQuery(".resendEmails").click(function(event) {
        jQuery("#resendingEmailsInProgress").dialog({
            height: 100,
            width: 300,
            modal: true
        })
    });

    jQuery("#dataUmowy").datepicker({ dateFormat: 'yy-mm-dd', maxDate: new Date() });
})