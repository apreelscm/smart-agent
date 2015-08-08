jQuery(function () {
    'use strict';

    var acceptorOfficialNameField = jQuery("div#acceptorDataPanel input[name='akceptantNazwaOficjalna']"),
        isAcceptorDataChanged = jQuery("div#acceptorDataPanel input[name='isAcceptorDataChanged']");

    if (isAcceptorDataChanged.length) {
        acceptorOfficialNameField.change(function() {
            isAcceptorDataChanged.val(true);
        });
    }
});