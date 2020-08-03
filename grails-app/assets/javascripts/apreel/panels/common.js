function clearFields(fieldsContainer) {
    fieldsContainer.find("input[type='checkbox']").removeAttr("checked");
    fieldsContainer.find("input[type='radio']").removeAttr("checked");
    fieldsContainer.find("input[type='text']").val('');
    fieldsContainer.find("select").val('');
}

function disableFields(fieldsContainer) {
    fieldsContainer.find("input, select").attr('disabled', 'disabled');
}

function enableFields(fieldsContainer) {
    fieldsContainer.find("input, select").removeAttr('disabled');
}

function onIsPoliticianChange() {
    var $this = jQuery(this),
        isDirectPep = $this.parents("div.acceptor").find('div.isDirectPep');

    clearFields(isDirectPep);

    if (this.value === 'true') {
        isDirectPep.removeClass('hidden');
    } else {
        isDirectPep.addClass('hidden');
    }
}

function clearVerificationDetail() {
    var $this = jQuery(this),
        acceptor = $this.parents("div.acceptor"),
        value = this.value;

    switch (value) {
        case 'PESEL':
            acceptor.find("input[type=text][name$='birthDate']").val('');
            acceptor.find("select[name$='birthCountry']").val('');
            break;
        case 'BIRTH_DATE':
            acceptor.find("input[type=text][name$='pesel']").val('');
    }
}