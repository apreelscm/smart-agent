
function maskNewPointRefresh() {
    maskFields(jQuery("#addNewPointPanelPlaceholder"));
}

function maskNewPosRefresh() {
    maskFields(jQuery("#addNewPosPanelPlaceholder"));
}

function maskFields(element){
    element.find(".bank-account").mask('00 0000 0000 0000 0000 0000 0000');
    element.find(".nip").mask('0000000000');
    element.find(".regon").mask('000000000');
    element.find(".postal-code").mask('00-000');
    element.find(".phone").mask('(00) 000-00-00');
    element.find(".mobile-phone").mask('000-000-000');
    element.find(".fax").mask('(00) 000-00-00');
    element.find(".ip").mask('099.099.099.099');
    element.find(".ph-number").mask('00000');
    element.find(".integer-number").mask('099999');
    element.find(".float-number").mask('099999X99', {translation:  {'X': {pattern: /[.]/, optional: true}}});
    element.find(".percent-number").mask('09X999', {translation:  {'X': {pattern: /[.]/}}})
    element.find(".flat-price").mask('AAAAA');
}

jQuery(document).ready(function() {
    jQuery(".bank-account").mask('00 0000 0000 0000 0000 0000 0000');
    jQuery(".nip").mask('0000000000');
    jQuery(".regon").mask('000000000');
    jQuery(".postal-code").mask('00-000');
    jQuery(".phone").mask('(00) 000-00-00');
    jQuery(".mobile-phone").mask('000-000-000');
    jQuery(".fax").mask('(00) 000-00-00');
    jQuery(".ip").mask('099.099.099.099');
    jQuery(".ph-number").mask('00000');
    jQuery(".integer-number").mask('099999');
    jQuery(".float-number").mask('099999X99', {translation:  {'X': {pattern: /[.]/, optional: true}}});
    jQuery(".percent-number").mask('09X999', {translation:  {'X': {pattern: /[.]/}}});
    jQuery(".flat-price").mask('XXXXX', {translation: {'X': {pattern: /[a-zA-Z0-9.-]/}}});
});
