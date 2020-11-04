//= require vendors/jquery.mask

function maskNewPointRefresh() {
    maskFields(jQuery("#addNewPointPanelPlaceholder"));
}

function maskNewPosRefresh() {
    maskFields(jQuery("#addNewPosPanelPlaceholder"));
}

var LANDLINE_PHONE_FORMAT = '(00) 000-00-00';
var MOBILE_PHONE_FORMAT = '000-000-000';
var PERCENT_SHORT_FORMAT = '09';

function maskFields(element){
    element.find(".bank-account").mask('00 0000 0000 0000 0000 0000 0000');
    element.find(".nip").mask('0000000000');
    element.find(".regon").mask('000000000');
    element.find(".postal-code").mask('00-000');
    element.find(".phone").mask(LANDLINE_PHONE_FORMAT);
    element.find(".mobile-phone").mask(MOBILE_PHONE_FORMAT);
    element.find(".fax").mask('(00) 000-00-00');
    element.find(".ip").mask('099.099.099.099');
    element.find(".ph-number").mask('S9999');
    element.find(".integer-number").mask('099999');
    element.find(".float-number").mask('099999X99', {translation:  {'X': {pattern: /[.]/, optional: true}}});
    element.find(".percent-number").mask('09X999', {translation:  {'X': {pattern: /[.]/}}});
    element.find(".percent-short").mask(PERCENT_SHORT_FORMAT);
    element.find(".flat-price").mask('AAAAA');
    element.find(".krs-number").mask('09999999999999999999');
}

jQuery(document).ready(function() {
    jQuery(".bank-account").mask('00 0000 0000 0000 0000 0000 0000');
    jQuery(".nip").mask('0000000000');
    jQuery(".regon").mask('000000000');
    jQuery(".postal-code").mask('00-000');
    jQuery(".phone").mask(LANDLINE_PHONE_FORMAT);
    jQuery(".mobile-phone").mask(MOBILE_PHONE_FORMAT);
    jQuery(".fax").mask('(00) 000-00-00');
    jQuery(".ip").mask('099.099.099.099');
    jQuery(".ph-number").mask('S9999');
    jQuery(".integer-number").mask('099999');
    jQuery(".float-number").mask('099999X99', {translation:  {'X': {pattern: /[.]/, optional: true}}});
    jQuery(".percent-number").mask('09X999', {translation:  {'X': {pattern: /[.]/}}});
    jQuery(".flat-price").mask('XXXXX', {translation: {'X': {pattern: /[a-zA-Z0-9.-]/}}});
    jQuery(".date-field").mask('0000-00-00');
    jQuery(".pesel-field").mask('00000000000');
    jQuery(".isin-field").mask('XXAAAAAAAAA0', {translation: {'X': {pattern: /[A-Z]/}}});
});
