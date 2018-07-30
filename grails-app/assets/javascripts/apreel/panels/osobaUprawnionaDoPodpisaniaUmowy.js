(function () {
    'use strict';

    var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
        $bisnodeRepresentatives = jQuery("#acceptorsPanel #bisnodeRepresentatives"),
        $customRepresentatives = jQuery("#acceptorsPanel #customRepresentatives"),
        $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
        $representativeDocumentType = jQuery("#representativesContainer input[type=radio][name$='documentType']"),
        $representativeVerification = jQuery("#representativesContainer input[type=radio][name$='verification']"),
        $representativePosition = jQuery("#representativesContainer select[name$='position']"),
        $additionalInfoSelect = jQuery("div#additionalInformationPanel select[name='dzialalnoscForma']"),
        $addAnotherAcceptorButton = jQuery("button#addAnotherAcceptor"),
        $companyData = jQuery("div#acceptorsPanel div.companyData"),
        $personData = jQuery("div#acceptorsPanel div.personData");

    attachDatepickers();

    disableFields($bisnodeRepresentatives);
    disableFields($customRepresentatives);
    disableHiddenRepresentativesFields();

    $representativesChangedManually.change(setRepresentativesView);
    $additionalInfoSelect.change(legalFormChanged);
    $representativePosition.change(onPositionChange);
    $representativeDocumentType.change(onDocumentTypeChange);
    $representativeVerification.change(clearOtherDetail);
    $addAnotherAcceptorButton.click(showNextAcceptor);

    function setRepresentativesView() {
        var isChecked = $representativesChangedManually.is(":checked"),
            basicData = $representativesContainer.find('div.basicRepresentativeData'),
            template = isChecked ? $customRepresentatives.html() : $bisnodeRepresentatives.html();

        basicData.each(function (index, value) {
            var acceptorBasicData = jQuery(value),
                isAcceptorActive = !acceptorBasicData.parent().hasClass('hidden');

            if (isAcceptorActive) {
                var id = acceptorBasicData.find('input[name$=id]').val(),
                    name = acceptorBasicData.find('input').first().attr('name').split('.')[0],
                    newTemplate = jQuery(template.replace(/representative\[null]/g, name));

                newTemplate[0].value = id;

                acceptorBasicData.html(newTemplate);
                enableFields(acceptorBasicData);
                attachBisnodeNameChangeEvent();
            }
        });
    }

    function onPositionChange() {
        manageVisibilityOfDocumentInfo(jQuery(this).parents("div.acceptor"));
    }

    function onDocumentTypeChange() {
        var $this = jQuery(this),
            acceptorIdDates = $this.parents("div.acceptor").find('div.acceptorIdDatesWrapper');

        clearFields(acceptorIdDates);

        switch (this.value) {
            case 'IDENTITY_CARD':
                acceptorIdDates.removeClass('hidden');
                break;
            case 'PASSPORT':
                acceptorIdDates.addClass('hidden');
                break;
        }
    }

    function clearOtherDetail() {
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

    function legalFormChanged() {
        if (this.value === "") {
            $personData.addClass('hidden');
            $companyData.addClass('hidden');

            clearFields($personData);
            clearFields($companyData);

            return false;
        }

        var isPerson = ["PARTNERSHIP_COMPANY", "PERSON"].indexOf(this.value) > -1;

        if (isPerson) {
            $personData.removeClass('hidden');
            $companyData.addClass('hidden');

            disableFields($companyData);
            clearFields($companyData);
            enableFields($personData);
            jQuery("input#akceptantNieMaBeneficjenta").prop('checked', true);
        } else {
            $personData.addClass('hidden');
            $companyData.removeClass('hidden');

            disableFields($personData);
            clearFields($personData);
            enableFields($companyData);
        }

        $representativesContainer.find('div.acceptor').each(function (_, value) {
            manageVisibilityOfDocumentInfo(jQuery(value));
        });
    }

    function manageVisibilityOfDocumentInfo($acceptor) {
        var documentInfo = $acceptor.find('div.acceptorDocumentInfoWrapper'),
            position = $acceptor.find("select[name$='position']").val();

        clearFields(documentInfo);

        var isPerson = ["PARTNERSHIP_COMPANY", "PERSON"].indexOf($additionalInfoSelect.val()) > -1;

        if (position === 'Pełnomocnik' || isPerson) {
            documentInfo.removeClass('hidden');
        } else {
            documentInfo.addClass('hidden');
        }
    }

    function showNextAcceptor() {
        var acceptor = $representativesContainer.find('div.acceptor.hidden').first(),
            companyData = acceptor.find('.companyData'),
            personData = acceptor.find('.personData');

        if (acceptor.length === 0) {
            jQuery(this).attr('disabled', 'disabled');
            return false;
        }

        acceptor.removeClass('hidden');
        enableFields(acceptor);

        if (companyData.hasClass('hidden')) {
            disableFields(companyData);
        } else if (personData.hasClass('hidden')) {
            disableFields(personData);
        }
    }

    function attachDatepickers() {
        $representativesContainer.find(".date-field").each(function (index, input) {
            jQuery(input).datepicker({ altField: "input[name='" + input.name + "']", dateFormat: 'yy-mm-dd', maxDate: new Date()});
        });
    }

    function disableHiddenRepresentativesFields() {
        disableFields($representativesContainer.find('div.acceptor.hidden'));
        disableFields($representativesContainer.find('div.companyData.hidden'));
        disableFields($representativesContainer.find('div.personData.hidden'));
    }
})();