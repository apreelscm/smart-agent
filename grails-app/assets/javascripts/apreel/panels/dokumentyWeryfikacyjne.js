jQuery("#verificationDocuments input[type=checkbox][id=beneficjentWeryfikacjaKRS]").change(function() {
    var beneficjentKRS = jQuery("#verificationDocuments #beneficjentKRS");

    if(this.checked) {
        beneficjentKRS.removeAttr('disabled');
    } else {
        beneficjentKRS.attr('disabled', 'disabled');
        beneficjentKRS.val('');
    }
});

jQuery("#verificationDocuments").find(".krs-number").mask('09999999999999999999');