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
    enableFields(isDirectPep);

    if (this.value === 'true') {
        isDirectPep.removeClass('hidden');
    } else {
        isDirectPep.addClass('hidden');
    }
}

function additionalDataChanged() {
    var $this = jQuery(this),
        checked = $this.is(':checked'),
        $acceptor = $this.parents("div.acceptor"),
        $documentType = $acceptor.find('select[id$="personDocumentType"]'),
        $documentNumber = $acceptor.find('input[id$="documentNumber"]'),
        $personDocumentExpirationDate = $acceptor.find('input[id$="personDocumentExpirationDate"]'),
        $personDocumentIssueDate = $acceptor.find('input[id$="personDocumentIssueDate"]'),
        $verification = $acceptor.find('input[type="radio"][id$="verification"]:visible'),
        $pesel = $acceptor.find('input[id$="pesel"]'),
        $birthDate = $acceptor.find('input[id$="birthDate"]'),
        $birthCountry = $acceptor.find('select[id$="birthCountry"]'),
        $citizenship = $acceptor.find('select[id$="citizenship"]'),
        isVerificationChecked = $verification.is(':checked');

    if (checked) {
        if ($documentType.is(':visible')) {
            $documentType.removeAttr('disabled');
            $documentNumber.removeAttr('readonly');
            $personDocumentExpirationDate.removeAttr('readonly');
            $personDocumentIssueDate.removeAttr('readonly');
        }

        $birthCountry.removeAttr('disabled');

        if ($citizenship.is(':visible')) {
            $citizenship.removeAttr('disabled');
        }

        if (!isVerificationChecked) {
            $verification.removeAttr('disabled');
            $pesel.removeAttr('readonly');
            $birthDate.removeAttr('readonly');
        }
    } else {
        if ($documentType.is(':visible')) {
            $documentType.attr('disabled', 'disabled');
            $documentType.val(null);

            $documentNumber.attr('readonly', true);
            $documentNumber.val(null);

            $personDocumentExpirationDate.attr('readonly', true);
            $personDocumentExpirationDate.val(null);

            $personDocumentIssueDate.attr('readonly', true);
            $personDocumentIssueDate.val(null);
        }

        $birthCountry.attr('disabled', 'disabled');
        $birthCountry.val(null);

        if ($citizenship.is(':visible')) {
            $citizenship.attr('disabled', 'disabled');
            $citizenship.val(null);
        }

        var isVerificationEnabled = $verification.attr('disabled') !== 'disabled';

        if (isVerificationEnabled) {
            $verification.attr('disabled', 'disabled');
            $verification.attr('checked', false);

            $pesel.attr('readonly', true);
            $pesel.val(null);

            $birthDate.attr('readonly', true);
            $birthDate.val(null);
        }
    }
}

function onRepresentativeCBDDataChange(showDialog) {
    var $this = jQuery(this),
        $acceptor = $this.parents("div.acceptor"),
        $readOnlyFields = $acceptor.find('input, textarea, textField'),
        $disabledFields = $acceptor.find('select, checkbox, input[type="radio"]'),
        $representativesDataChanged = jQuery("#representativesContainer input[type=radio][name$='isCBDDataChangedManually']"),
        $verificationDocumentsContainer = jQuery("#dokumentyWeryfikacyjne"),
        $isPolitician = $acceptor.find('div.isPolitician'),
        $isPep = $acceptor.find('div.isDirectPep'),
        $acceptorHasSignedContract = $acceptor.find('div.hasSignedContract'),
        $phoneContainer = $acceptor.find('.phone-container'),
        $emailContainer = $acceptor.find('.email-container'),
        $personData = $acceptor.children('div.personData'),
        $companyData = $acceptor.children('div.companyData'),
        $basicRepresentativeData = $acceptor.children('div.basicRepresentativeData'),
        $sharedRepresentativeData = $acceptor.children('div.sharedRepresentativeData'),
        $additionalData = $acceptor.find("input[type=checkbox][name$='additionalData']"),
        $hasSignedContract = jQuery(".hasSignedContract"),
        index = $basicRepresentativeData.children('input[name="index"]')[0].value,
        prefix = $basicRepresentativeData.children('input[name="prefix"]')[0].value,
        nip = jQuery("#akceptantNip")[0].value,
        isFromBisnode = jQuery("#isFromBisnode").val(),
        regon = jQuery("#akceptantRegon").val(),
        hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract = jQuery("#hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract").val() === 'true';

    var midName = prefix + '[' + index + '].midCBD';
    var representativeId = $basicRepresentativeData.children('input[name="' + midName + '"]')[0].value;

    var $acceptorsPanel = jQuery("#acceptorsPanel"),
        isAnyDataManual = $acceptorsPanel.find("input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked").length > 0;

    function changeFieldsAvailabilityForActualCbdData() {
        if (isFromBisnode === 'true') {
            setRepresentativesDataFromBisnode(index, nip, regon);
        } else {
            setRepresentativesDataFromCBD(index, nip, representativeId);
        }

        $readOnlyFields.attr("readonly", true);
        $disabledFields.attr('disabled', 'disabled');
        $representativesDataChanged.removeAttr("disabled");
        $additionalData.removeAttr('disabled');
        $additionalData.removeAttr("checked");
        $basicRepresentativeData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
        $sharedRepresentativeData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");

        if ($companyData.hasClass('hidden')) {
            $personData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
            $companyData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
        } else {
            $personData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
            $companyData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
        }

        if (!isAnyDataManual) {
            $verificationDocumentsContainer.addClass("hidden");
        }

        if (!hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract && !isAnyDataManual) {
            $hasSignedContract.addClass("hidden")
            $hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
            $hasSignedContract.find('input').attr('disabled', 'disabled');
        }

        if (isAnyDataManual) {
            $hasSignedContract.removeClass("hidden");
            $hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
            $hasSignedContract.find('input').removeAttr('disabled');
        }

        enableFields($isPolitician);
        enableFields($acceptorHasSignedContract);

        var isPolitician = $isPolitician.find('input').val();
        var hasSignedContract = $acceptorHasSignedContract.find('input').val();

        if (isPolitician) {
            enableFields($isPep);
        }

        if (hasSignedContract) {
            enableFields($phoneContainer);
            enableFields($emailContainer);
        }
    }

    if (this.value === 'true') {
        $additionalData.attr('disabled', 'disabled');
        $additionalData.removeAttr("checked");
        $disabledFields.removeAttr("disabled");
        $readOnlyFields.removeAttr("readonly");
        $verificationDocumentsContainer.removeClass("hidden");
        $basicRepresentativeData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        $sharedRepresentativeData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');

        if ($companyData.hasClass('hidden')) {
            $companyData
                .find('input, textarea, textField, select, checkbox, input[type="radio"], *[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
            $personData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        }

        if ($personData.hasClass('hidden')) {
            $personData
                .find('input, textarea, textField, select, checkbox, input[type="radio"], *[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
            $companyData.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr('disabled', 'disabled');
        }

        $hasSignedContract.removeClass("hidden");
        $hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
        $hasSignedContract.find('input').removeAttr('disabled');
    } else {
        if (showDialog === false) {
            changeFieldsAvailabilityForActualCbdData();
        } else {
            jQuery("#confirm-submit-without-subscription-dialog").dialog({
                resizable: true,
                height: 200,
                width: 450,
                modal: true,
                buttons:
                    {
                        "Tak": function () {
                            changeFieldsAvailabilityForActualCbdData();
                            jQuery(this).dialog("close");
                        },
                        "Nie": function () {
                            jQuery('#representatives\\[' + index + '\\]\\.isCBDDataChangedManually')[0].click();
                            jQuery(this).dialog("close");
                        }
                    }
            });
        }
    }
}

function onAcceptantCBDDataChange() {
    var $this = jQuery(this),
        $acceptor = $this.parents("div.acceptor"),
        readOnlyFields = $acceptor.find('input, textarea, textField'),
        disabledFields = $acceptor.find('select, checkbox, input[type="radio"]'),
        hiddenFields = $acceptor.find('*[cbdDataHiddenField="cbdDataHiddenField"]'),
        beneficiaryDataChanged = jQuery("#acceptorsAdditionalPanels input[type=radio][name$='isCBDDataChangedManually']"),
        nip = jQuery("#akceptantNip")[0].value,
        verificationDocumentsContainer = jQuery("#dokumentyWeryfikacyjne"),
        isPolitician = $acceptor.find('div.isPolitician'),
        index = $acceptor.children('input[name="index"]')[0].value,
        prefix = $acceptor.children('input[name="prefix"]')[0].value;

    var midName = prefix + '[' + index + '].midCBD';
    var acceptantId = $acceptor.children('input[name="' + midName + '"]')[0].value;

    var $isCBDDataChangedManually = jQuery("#acceptorsPanel input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked");
    var isAnyDataManual = $isCBDDataChangedManually.length;

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
                        jQuery('#beneficiaries\\[' + index + '\\]\\.isCBDDataChangedManually')[0].click();
                        jQuery(this).dialog("close");
                    }
                }
        });
    }
}

function setRepresentativesDataFromBisnode(index, nip, regon) {
    var name = jQuery('#representatives\\[' + index + '\\]\\.name');
    var surname = jQuery('#representatives\\[' + index + '\\]\\.surname');
    var position = jQuery('#representatives\\[' + index + '\\]\\.position');
    var salutation = jQuery('#representatives\\[' + index + '\\]\\.salutation');
    var documentNumber = jQuery('#representatives\\[' + index + '\\]\\.documentNumber');
    var documentExpirationDate = jQuery('#representatives\\[' + index + '\\]\\.personDocumentExpirationDate');
    var documentIssueDate = jQuery('#representatives\\[' + index + '\\]\\.personDocumentIssueDate');
    var birthDate = jQuery('#representatives\\[' + index + '\\]\\.birthDate');
    var pesel = jQuery('#representatives\\[' + index + '\\]\\.pesel');
    var birthCountry = jQuery('#representatives\\[' + index + '\\]\\.birthCountry');
    var citizenship = jQuery('#representatives\\[' + index + '\\]\\.citizenship');
    var documentType = jQuery('#representatives\\[' + index + '\\]\\.personDocumentType');
    var verification = jQuery('#representatives\\[' + index + '\\]\\.verification');

    jQuery.get("/eumowy/activity/getBisnodeReprezentantData", {index: index, nip: nip, regon: regon}, function (data) {
        if (data != null) {
            name.val(data.name);
            surname.val(data.surname);
            position.val(data.position);
            salutation.val(data.salutation);
            pesel.val(data.pesel);
            birthCountry.val(data.birthCountry);
            verification.val(data.verification ? data.verification.name : null);

            documentType.val(null);
            documentNumber.val(null);
            documentExpirationDate.val(null);
            documentIssueDate.val(null);
            birthDate.val(null);
            citizenship.val(null);
        }
    });
}

function setRepresentativesDataFromCBD(index, nip, representativeCbdMidId) {
    var name = jQuery('#representatives\\[' + index + '\\]\\.name');
    var surname = jQuery('#representatives\\[' + index + '\\]\\.surname');
    var position = jQuery('#representatives\\[' + index + '\\]\\.position');
    var salutation = jQuery('#representatives\\[' + index + '\\]\\.salutation');
    var documentNumber = jQuery('#representatives\\[' + index + '\\]\\.documentNumber');
    var documentExpirationDate = jQuery('#representatives\\[' + index + '\\]\\.personDocumentExpirationDate');
    var documentIssueDate = jQuery('#representatives\\[' + index + '\\]\\.personDocumentIssueDate');
    var birthDate = jQuery('#representatives\\[' + index + '\\]\\.birthDate');
    var pesel = jQuery('#representatives\\[' + index + '\\]\\.pesel');
    var birthCountry = jQuery('#representatives\\[' + index + '\\]\\.birthCountry');
    var citizenship = jQuery('#representatives\\[' + index + '\\]\\.citizenship');
    var phoneNumber = jQuery('#representatives\\[' + index + '\\]\\.phoneNumber');
    var email = jQuery('#representatives\\[' + index + '\\]\\.email');
    var phoneType = jQuery('#representatives\\[' + index + '\\]\\.phoneType');
    var documentType = jQuery('#representatives\\[' + index + '\\]\\.documentType');
    var verification = jQuery('#representatives\\[' + index + '\\]\\.verification');

    jQuery.get("/eumowy/activity/getCbdReprezentantData", {index: index, nip: nip, representativeCbdMidId: representativeCbdMidId}, function (data) {
        if (data != null) {
            name.val(data.name);
            surname.val(data.surname);
            position.val(data.position);
            salutation.val(data.salutation);
            documentNumber.val(data.documentNumber);
            documentExpirationDate.val(convertStringToDate(data.documentExpirationDate));
            documentIssueDate.val(convertStringToDate(data.documentIssueDate));
            birthDate.val(convertStringToDate(data.birthDate));
            pesel.val(data.pesel);
            birthCountry.val(data.birthCountry);
            citizenship.val(data.citizenship);
            phoneNumber.val(data.mobilePhone);
            email.val(data.email);
            phoneType.val(data.phoneType ? data.phoneType.name : null);
            documentType.val(data.documentType);
            verification.val(data.verification ? data.verification.name : null);
        }
    });
}

function convertStringToDate(dateString) {
    if (dateString != null && dateString.length) {
        var date = new Date(dateString);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        if (month < 10) {
            month = '0' + month;
        }
        if (day < 10) {
            day = '0' + day;
        }
        return year + '-' + month + '-' + day;
    } else {
        return null
    }
}

function setAcceptantDataFromCBD(index, nip, acceptantCbdMidId) {
    var name = jQuery('#beneficiaries\\[' + index + '\\]\\.name');
    var surname = jQuery('#beneficiaries\\[' + index + '\\]\\.surname');
    var position = jQuery('#beneficiaries\\[' + index + '\\]\\.position');
    var salutation = jQuery('#beneficiaries\\[' + index + '\\]\\.salutation');
    var birthDate = jQuery('#beneficiaries\\[' + index + '\\]\\.personBirthDate');
    var pesel = jQuery('#beneficiaries\\[' + index + '\\]\\.pesel');
    var citizenship = jQuery('#beneficiaries\\[' + index + '\\]\\.citizenship');

    jQuery.get("/eumowy/activity/getCbdAcceptantData", {index: index, nip: nip, acceptantCbdMidId: acceptantCbdMidId}, function (data) {
        if (data != null) {
            name.val(data.name);
            surname.val(data.surname);
            position.val(data.position);
            salutation.val(data.salutation);
            birthDate.val(convertStringToDate(data.birthDate));
            pesel.val(data.pesel);
            citizenship.val(data.citizenship ? data.citizenship.toString() : null);
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
