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
    fieldsContainer.find("input, select").removeAttr('readonly');
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

function onRepresentativeCBDDataChange() {
    var $this = jQuery(this),
        readOnlyFields = $this.parents("div.acceptor").find('input, textarea, select, textField'),
        disabledFields = $this.parents("div.acceptor").find('textarea, select, text, textField, checkbox, input'),
        representativesDataChanged = jQuery("#representativesContainer input[type=radio][name$='isCBDDataChangedManually']"),
        representativeId = $this.parents("div.acceptor").children('div.basicRepresentativeData').children('input[name=mid]')[0].value,
        nip = jQuery("#akceptantNip")[0].value,
        index = $this.parents("div.acceptor").children('div.basicRepresentativeData').children('input[name=index]')[0].value;

    if (this.value === 'true') {
        disabledFields.removeAttr("disabled");
        readOnlyFields.removeAttr("readonly");
    } else {
        jQuery("#confirm-submit-without-subscription-dialog").dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true,
            buttons:
                {
                    "Tak": function () {
                        readOnlyFields.attr("readonly", true);
                        disabledFields.attr('disabled', 'disabled');
                        representativesDataChanged.removeAttr("disabled");
                        setRepresentativesDataFromCBD(index, nip, representativeId);
                        jQuery(this).dialog("close");
                    },
                    "Nie": function () {
                        jQuery(`#representatives\\[${index}\\]\\.isCBDDataChangedManually`)[0].click()
                        jQuery(this).dialog("close");
                    }
                }
        });
    }
}

function onAcceptantCBDDataChange() {
    var $this = jQuery(this),
        readOnlyFields = $this.parents("div.acceptor").find('input, textarea, select, textField'),
        disabledFields = $this.parents("div.acceptor").find('textarea, select, text, textField, checkbox, input'),
        beneficiaryDataChanged = jQuery("#acceptorsAdditionalPanels input[type=radio][name$='isCBDDataChangedManually']"),
        acceptantId = $this.parents("div.acceptor").children('input[name=mid]')[0].value,
        nip = jQuery("#akceptantNip")[0].value,
        index = $this.parents("div.acceptor").children('input[name=index]')[0].value;

    if (this.value === 'true') {
        disabledFields.removeAttr("disabled");
        readOnlyFields.removeAttr("readonly");
    } else {
        jQuery("#confirm-submit-without-subscription-dialog").dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true,
            buttons:
                {
                    "Tak": function () {
                        readOnlyFields.attr("readonly", true);
                        disabledFields.attr('disabled', 'disabled');
                        beneficiaryDataChanged.removeAttr("disabled");
                        setAcceptantDataFromCBD(index, nip, acceptantId);
                        jQuery(this).dialog("close");
                    },
                    "Nie": function () {
                        jQuery(`#beneficiaries\\[${index}\\]\\.isCBDDataChangedManually`)[0].click()
                        jQuery(this).dialog("close");
                    }
                }
        });
    }
}

function setRepresentativesDataFromCBD(index, nip, representativeCbdMidId) {
    const name = jQuery(`#representatives\\[${index}\\]\\.name`);
    const surname = jQuery(`#representatives\\[${index}\\]\\.surname`);
    const position = jQuery(`#representatives\\[${index}\\]\\.position`);
    const salutation = jQuery(`#representatives\\[${index}\\]\\.salutation`);
    const documentNumber = jQuery(`#representatives\\[${index}\\]\\.documentNumber`);
    const documentExpirationDate = jQuery(`#representatives\\[${index}\\]\\.personDocumentExpirationDate`);
    const documentIssueDate = jQuery(`#representatives\\[${index}\\]\\.personDocumentIssueDate`);
    const birthDate = jQuery(`#representatives\\[${index}\\]\\.personBirthDate`);
    const pesel = jQuery(`#representatives\\[${index}\\]\\.pesel`);
    const birthCountry = jQuery(`#representatives\\[${index}\\]\\.birthCountry`);
    const street = jQuery(`#representatives\\[${index}\\]\\.street`);
    const houseNumber = jQuery(`#representatives\\[${index}\\]\\.houseNumber`);
    const flatNumber = jQuery(`#representatives\\[${index}\\]\\.flatNumber`);
    const postalCode = jQuery(`#representatives\\[${index}\\]\\.postalCode`);
    const city = jQuery(`#representatives\\[${index}\\]\\.city`);
    const postOffice = jQuery(`#representatives\\[${index}\\]\\.postOffice`);
    const country = jQuery(`#representatives\\[${index}\\]\\.country`);
    const citizenship = jQuery(`#representatives\\[${index}\\]\\.citizenship`);
    const phoneNumber = jQuery(`#representatives\\[${index}\\]\\.phoneNumber`);
    const email = jQuery(`#representatives\\[${index}\\]\\.email`);

    jQuery.get("/eumowy/activity/getCbdReprezentantData", {nip, representativeCbdMidId}, function (data) {
        if (data != null) {
            name.val(data?.name);
            surname.val(data?.surname);
            position.val(data?.position);
            salutation.val(data?.salutation);
            documentNumber.val(data?.documentNumber);
            documentExpirationDate.val(convertStringToDate(data?.documentExpirationDate));
            documentIssueDate.val(convertStringToDate(data?.documentIssueDate));
            birthDate.val(convertStringToDate(data?.birthDate));
            pesel.val(data?.pesel);
            birthCountry.val(data?.birthCountryCBD?.toString());
            street.val(data?.street);
            flatNumber.val(data?.flatNumber);
            houseNumber.val(data?.houseNumber);
            postalCode.val(data?.postalCode);
            city.val(data?.cityCBD?.toString());
            postOffice.val(data?.postOfficeCBD?.toString());
            citizenship.val(data?.citizenshipCBD?.toString());
            phoneNumber.val(data?.mobilePhone);
            country.val(data?.countryCBD?.toString());
            email.val(data?.email);
        }
    });
}

function convertStringToDate(dateString) {
    if (dateString != null && dateString.length > 0) {
        const date = new Date(dateString);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        if (month < 10) {
            month = `0${month}`
        }
        if (day < 10) {
            day = `0${day}`
        }
        return `${year}-${month}-${day}`
    } else {
        return null
    }
}

function setAcceptantDataFromCBD(index, nip, acceptantCbdMidId) {
    const name = jQuery(`#beneficiaries\\[${index}\\]\\.name`);
    const surname = jQuery(`#beneficiaries\\[${index}\\]\\.surname`);
    const position = jQuery(`#beneficiaries\\[${index}\\]\\.position`);
    const salutation = jQuery(`#beneficiaries\\[${index}\\]\\.salutation`);
    const birthDate = jQuery(`#beneficiaries\\[${index}\\]\\.personBirthDate`);
    const pesel = jQuery(`#beneficiaries\\[${index}\\]\\.pesel`);
    const citizenship = jQuery(`#beneficiaries\\[${index}\\]\\.citizenship`);

    jQuery.get("/eumowy/activity/getCbdAcceptantData", {nip, acceptantCbdMidId}, function (data) {
        if (data != null) {
            name.val(data?.name);
            surname.val(data?.surname);
            position.val(data?.position);
            salutation.val(data?.salutation);
            birthDate.val(convertStringToDate(data?.birthDate));
            pesel.val(data?.pesel);
            citizenship.val(data?.citizenship?.toString());
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
