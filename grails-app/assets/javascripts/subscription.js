//= require vendors/jquery
//= require vendors/json2.min
//= require_tree vendors/signature-pad

jQuery(document).ready(function() {
    jQuery('.sigPad').signaturePad({errorMessageDraw: '<g:message code="subscription.error" />'});
    jQuery('#dialog').dialog({
        modal: true,
        width: 750
    });
});