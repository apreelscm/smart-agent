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
        readOnlyFields = $this.parents("div.acceptor").find('input, textarea, textField'),
        disabledFields = $this.parents("div.acceptor").find('select, checkbox, input[type="radio"]'),
        representativesDataChanged = jQuery("#representativesContainer input[type=radio][name$='isCBDDataChangedManually']"),
        nip = jQuery("#akceptantNip")[0].value,
        verificationDocumentsContainer = jQuery("#dokumentyWeryfikacyjne"),
        isPolitician = $this.parents("div.acceptor").find('div.isPolitician'),
        isCompanyData = $this.parents("div.acceptor").children('div.companyData'),
        index = $this.parents("div.acceptor").children('div.basicRepresentativeData').children('input[name="index"]')[0].value,
        prefix = $this.parents("div.acceptor").children('div.basicRepresentativeData').children('input[name="prefix"]')[0].value;

    var midName = `${prefix}[${index}].midCBD`;
    var representativeId = $this.parents("div.acceptor").children('div.basicRepresentativeData').children(`input[name="${midName}"]`)[0].value;

    var cityName = `${prefix}[${index}].city`;
    var citySelect = jQuery(`select[name="${cityName}"]`);
    var cityInput = jQuery(`input[name="${cityName}"]`);

    var documentTypeName = `${prefix}[${index}].documentType`;
    var documentTypeInput = jQuery(`select[name="${documentTypeName}"]`);

    var isAnyDataManual = jQuery("#acceptorsPanel input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked")?.length > 0;

    if (this.value === 'true') {
        disabledFields.removeAttr("disabled");
        readOnlyFields.removeAttr("readonly");
        verificationDocumentsContainer.removeClass("hidden");
        $this.parents("div.acceptor").children('div.basicRepresentativeData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        $this.parents("div.acceptor").children('div.sharedRepresentativeData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        if (isCompanyData.hasClass('hidden')) {
            $this.parents("div.acceptor").children('div.companyData')
                .find('input, textarea, textField, select, checkbox, input[type="radio"], *[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
            $this.parents("div.acceptor").children('div.personData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
            citySelect.show();
            cityInput.hide();
        }
        if ($this.parents("div.acceptor").children('div.personData').hasClass('hidden')) {
            $this.parents("div.acceptor").children('div.personData')
                .find('input, textarea, textField, select, checkbox, input[type="radio"], *[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
            $this.parents("div.acceptor").children('div.companyData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        }
    } else {
        jQuery("#confirm-submit-without-subscription-dialog").dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true,
            buttons:
                {
                    "Tak": function () {
                        cityInput.show();
                        setRepresentativesDataFromCBD(index, nip, representativeId);
                        readOnlyFields.attr("readonly", true);
                        disabledFields.attr('disabled', 'disabled');
                        representativesDataChanged.removeAttr("disabled");
                        $this.parents("div.acceptor").children('div.basicRepresentativeData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                        $this.parents("div.acceptor").children('div.sharedRepresentativeData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                        if (isCompanyData.hasClass('hidden')) {
                            $this.parents("div.acceptor").children('div.personData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                            $this.parents("div.acceptor").children('div.companyData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
                        } else {
                            $this.parents("div.acceptor").children('div.personData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
                            $this.parents("div.acceptor").children('div.companyData').find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                        }
                        if (!isAnyDataManual) {
                            verificationDocumentsContainer.addClass("hidden");
                        }
                        documentTypeInput.removeAttr('disabled');
                        enableFields(isPolitician);
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
        readOnlyFields = $this.parents("div.acceptor").find('input, textarea, textField'),
        disabledFields = $this.parents("div.acceptor").find('select, checkbox, input[type="radio"]'),
        hiddenFields = $this.parents("div.acceptor").find('*[cbdDataHiddenField="cbdDataHiddenField"]'),
        beneficiaryDataChanged = jQuery("#acceptorsAdditionalPanels input[type=radio][name$='isCBDDataChangedManually']"),
        nip = jQuery("#akceptantNip")[0].value,
        verificationDocumentsContainer = jQuery("#dokumentyWeryfikacyjne"),
        isPolitician = $this.parents("div.acceptor").find('div.isPolitician'),
        index = $this.parents("div.acceptor").children('input[name="index"]')[0].value,
        prefix = $this.parents("div.acceptor").children('input[name="prefix"]')[0].value;

    var midName = `${prefix}[${index}].midCBD`;
    var acceptantId = $this.parents("div.acceptor").children(`input[name="${midName}"]`)[0].value;

    var isAnyDataManual = jQuery("#acceptorsPanel input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked")?.length > 0;

    if (this.value === 'true') {
        disabledFields.removeAttr("disabled");
        readOnlyFields.removeAttr("readonly");
        hiddenFields.attr('disabled', 'disabled');
        verificationDocumentsContainer.removeClass("hidden");
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
                        hiddenFields.removeAttr('disabled');
                        if (!isAnyDataManual) {
                            verificationDocumentsContainer.addClass("hidden");
                        }
                        enableFields(isPolitician);
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
    const birthDate = jQuery(`#representatives\\[${index}\\]\\.birthDate`);
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
    const streetType = jQuery(`#representatives\\[${index}\\]\\.streetType`);
    const phoneType = jQuery(`#representatives\\[${index}\\]\\.phoneType`);
    const documentType = jQuery(`#representatives\\[${index}\\]\\.documentType`);
    const verification = jQuery(`#representatives\\[${index}\\]\\.verification`);

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
            birthCountry.val(data?.birthCountry);
            street.val(data?.street);
            flatNumber.val(data?.flatNumber);
            houseNumber.val(data?.houseNumber);
            postalCode.val(data?.postalCode);
            city.val(data?.city);
            postOffice.val(data?.postOffice);
            citizenship.val(data?.citizenship);
            phoneNumber.val(data?.mobilePhone);
            country.val(data?.country);
            email.val(data?.email);
            streetType.val(data?.streetTitle);
            phoneType.val(data?.phoneType?.name);
            documentType.val(data?.documentType);
            verification.val(data?.verification?.name);
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

    if ($this.attr("disabled") || $this.attr("readonly")) {
        return;
    }
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
