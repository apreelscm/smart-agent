function maskNewPointRefresh() {
	jQuery("#newPointPanel").find(".bank-account").mask('99 9999 9999 9999 9999 9999 9999');
	jQuery("#newPointPanel").find(".nip").mask('9999999999');
	jQuery("#newPointPanel").find(".regon").mask('999999999');
	jQuery("#newPointPanel").find(".postal-code").mask('99-999');
	jQuery("#newPointPanel").find(".phone").mask('(99) 999-99-99');
	jQuery("#newPointPanel").find(".mobile-phone").mask('999-999-999');
	jQuery("#newPointPanel").find(".fax").mask('(99) 999-99-99');
	jQuery("#newPointPanel").find(".ip").mask('999.999.999.999');
	jQuery("#newPointPanel").find(".ph-number").mask('99999');
}

jQuery(document).ready(function() {
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
