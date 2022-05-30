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

function onCBDDataChange() {
    var $this = jQuery(this),
        readOnlyFields = $this.parents("div.acceptor").find('input, textarea, select, textField'),
        disabledFields = $this.parents("div.acceptor").find('textarea, select, text, textField, checkbox, input'),
        representativesDataChanged = jQuery("#representativesContainer input[type=radio][name$='isCBDDataChangedManually']"),
        beneficiaryDataChanged = jQuery("#acceptorsAdditionalPanels input[type=radio][name$='isCBDDataChangedManually']");


    if (this.value === 'true') {
        disabledFields.removeAttr("disabled");
        readOnlyFields.removeAttr("readonly");
        isRepresentativesCBDDataChanged()
    } else {
        readOnlyFields.attr("readonly", true);
        disabledFields.attr('disabled', 'disabled');
        representativesDataChanged.removeAttr("disabled");
        beneficiaryDataChanged.removeAttr("disabled");
        renderPopup();
        isRepresentativesCBDDataChanged()
    }
}

function setRepresentativesDataFromCBD() {
    const name = jQuery('#representatives\\[0\\]\\.name'); //todo dodać index zamiast 0
    const surname = jQuery('#representatives\\[0\\]\\.surname');  //todo dodać index zamiast 0
    const position = jQuery('#representatives\\[0\\]\\.position');
    const saluation = jQuery('#representatives\\[0\\]\\.salutation');
    const documentNumber = jQuery('#representatives\\[0\\]\\.documentNumber');
    const documentExpirationDate = jQuery('#representatives\\[0\\]\\.personDocumentExpirationDate');
    const documentIssueDate = jQuery('#representatives\\[0\\]\\.personDocumentIssueDate');
    const birthDate = jQuery('#representatives\\[0\\]\\.personBirthDate');
    const pesel = jQuery('#representatives\\[0\\]\\.pesel');
    const birthCountry = jQuery('#representatives\\[0\\]\\.birthCountry');
    const street = jQuery('#representatives\\[0\\]\\.street');
    const houseNumber = jQuery('#representatives\\[0\\]\\.houseNumber');
    const flatNumber = jQuery('#representatives\\[0\\]\\.flatNumber');
    const postalCode = jQuery('#representatives\\[0\\]\\.postalCode');
    const city = jQuery('#representatives\\[0\\]\\.city');
    const postOffice = jQuery('#representatives\\[0\\]\\.postOffice');
    const country = jQuery('#representatives\\[0\\]\\.country');
    const citizenship = jQuery('#representatives\\[0\\]\\.citizenship');
    const phoneNumber = jQuery('#representatives\\[0\\]\\.phoneNumber');
    const email = jQuery('#representatives\\[0\\]\\.email');

    $j.get("/eumowy/activity/getReprezentantData", {id: 49497}, function (data) {   //todo trzeba zmienić id na pobrane z reprezentanta
        console.log(data)
        name.val(data?.nameCBD);
        surname.val(data?.surnameCBD);
        position.val(data?.positionCBD);
        saluation.val(data?.salutationCBD);
        name.val(data?.nameCBD);
        documentNumber.val(data?.documentNumberCBD);
        documentExpirationDate.val(data?.documentExpirationDateCBD);
        documentIssueDate.val(data?.documentIssueDateCBD);
        birthDate.val(data?.birthDateCBD);
        pesel.val(data?.peselCBD);
        birthCountry.val(data?.birthCountryCBD?.toString());
        street.val(data?.streetCBD);
        flatNumber.val(data?.flatNumberCBD);
        houseNumber.val(data?.houseNumberCBD);
        postalCode.val(data?.postalCodeCBD);
        city.val(data?.cityCBD?.toString());
        postOffice.val(data?.postOfficeCBD?.toString());
        citizenship.val(data?.citizenshipCBD?.toString());
        phoneNumber.val(data?.mobilePhoneCBD);
        country.val(data?.countryCBD?.toString());
        email.val(data?.emailCBD);
    });
}

function isRepresentativesCBDDataChanged() {
    var values = [];
    let representativesDataChanged = jQuery("#representativesContainer input[type=radio][name$='isCBDDataChangedManually']:checked");
    let beneficiaryDataChanged = jQuery("#acceptorsAdditionalPanels input[type=radio][name$='isCBDDataChangedManually']:checked");
    let acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels > div")

    representativesDataChanged.each(function (index, item) {
        values.push(item.value);
    });
    beneficiaryDataChanged.each(function (index, item) {
        values.push(item.value);
    });

    if (values.includes('true')) {
        acceptorsAdditionalPanels.removeClass('hidden')
    } else {
        acceptorsAdditionalPanels.addClass('hidden');
    }
}

function renderPopup() {
    result = false;
    jQuery("#confirm-submit-without-subscription-dialog").dialog({
        resizable: true,
        height: 200,
        width: 450,
        modal: true,
        buttons:
            {
                "Tak": function () {
                    jQuery(this).dialog("close");
                    setRepresentativesDataFromCBD();
                    result = true;
                },
                "Nie": function () {
                    jQuery(this).dialog("close");
                }
            }
    });
}

function isProcurator(position) {
    return position === "Pełnomocnik"
}

function isLegalFormPerson(legalForm) {
    return ["PARTNERSHIP_COMPANY", "PERSON"].indexOf(legalForm) > -1
}

function isLegalFormCompany(legalForm) {
    return !isLegalFormPerson(legalForm);
}

function menageVisibilityOfCitizenship(legalForm, position, citizenShipFieldSet, citizenship) {
    if (isLegalFormCompany(legalForm)) {
        if (isProcurator(position)) {
            jQuery(citizenShipFieldSet).removeClass('hidden');
            jQuery(citizenShipFieldSet).find('.citizenship').val(citizenship);
        } else {
            jQuery(citizenShipFieldSet).addClass('hidden');
            jQuery(citizenShipFieldSet).find('.citizenship').val('');
        }
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

function onScoringChange() {
    var $this = jQuery(this),
        isAcceptedPrep = $this.parents("div.subpanel-fieldset-centercontent-scoring")
            .find('#percentageOfPrepaymentsTr'),
        maximumDeliveryTime1 = $this.parents("div.subpanel-fieldset-centercontent-scoring")
            .find('#maximumDeliveryTime'),
        averageDeliveryTime = $this.parents("div.subpanel-fieldset-centercontent-scoring")
            .find('#averageDeliveryTime');

    clearFields(isAcceptedPrep);
    clearFields(maximumDeliveryTime1);

    if (this.value === 'false' || this.value == null) {
        isAcceptedPrep.addClass('hidden');
        maximumDeliveryTime1.addClass('hidden');
        averageDeliveryTime.addClass('hidden');
    } else {
        isAcceptedPrep.removeClass('hidden');
        maximumDeliveryTime1.removeClass('hidden');
        averageDeliveryTime.removeClass('hidden');
    }
}
