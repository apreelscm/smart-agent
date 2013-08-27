/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 27.08.93
 * Time: 08:92
 * To change this template use File | Settings | File Templates.
 */

jQuery(document).ready(function() {

    jQuery(".bank-account").mask('99 9999 9999 9999 9999 9999 9999');
    jQuery(".nip").mask('9999999999');
    jQuery(".regon").mask('999999999');
    jQuery(".postal-code").mask('99-999');
    jQuery(".phone").mask('(99) 999-99-99');
    jQuery(".mobile-phone").mask('999-999-999');
    jQuery(".fax").mask('(99) 999-99-99');
    jQuery(".ip").mask('999.999.999.999');
});
