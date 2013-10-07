function maskNewPointRefresh() {
    maskFields(jQuery("#addNewPointPanelPlaceholder"));
}

function maskNewPosRefresh() {
    maskFields(jQuery("#addNewPosPanelPlaceholder"))
}

function maskFields(element){
    element.find(".bank-account").mask('99 9999 9999 9999 9999 9999 9999');
    element.find(".nip").mask('9999999999');
    element.find(".regon").mask('999999999');
    element.find(".postal-code").mask('99-999');
    element.find(".phone").mask('(99) 999-99-99');
    element.find(".mobile-phone").mask('999-999-999');
    element.find(".fax").mask('(99) 999-99-99');
    element.find(".ip").mask('0ZZ.0ZZ.0ZZ.0ZZ', {translation:  {'Z': {pattern: /[0-9]/, optional: true}}});
    element.find(".ph-number").mask('99999');
}

newPosPanel
jQuery(document).ready(function() {
    jQuery(".bank-account").live('keyup', function(event){
        var element = jQuery(this),
            value = element.val().toString();
        if(event.keyCode === 32){
            element.val(value.slice(0, -1));
        }
    })
    jQuery(".bank-account").mask('99 9999 9999 9999 9999 9999 9999');
    jQuery(".nip").mask('9999999999');
    jQuery(".regon").mask('999999999');
    jQuery(".postal-code").mask('99-999');
    jQuery(".phone").mask('(99) 999-99-99');
    jQuery(".mobile-phone").mask('999-999-999');
    jQuery(".fax").mask('(99) 999-99-99');
    jQuery(".ip").mask('0ZZ.0ZZ.0ZZ.0ZZ', {translation:  {'Z': {pattern: /[0-9]/, optional: true}}});
    jQuery(".ph-number").mask('99999');
});
