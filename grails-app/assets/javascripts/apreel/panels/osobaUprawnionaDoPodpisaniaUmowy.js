(function() {
    'use strict';

    var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
        $representativesDropdows = jQuery("#acceptorsPanel #representativesDropdowns"),
        $representativesTextfields = jQuery("#acceptorsPanel #representativesTextfields"),
        $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
        $representativeTypLokalizacji = jQuery("#representativesContainer input[type=radio][name$='locationType']"),
        $representativeDetail = jQuery("#representativesContainer input[type=radio][name$='verification']"),
        $countrySelect = $representativesContainer.find("select[name$='country']"),
        $acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels"),
        $additionalInfoSelect = jQuery("div#additionalInformationPanel select[name='dzialalnoscForma']"),
        $addAnotherAcceptorButton = jQuery("button#addAnotherAcceptor"),
        $companyData = jQuery("div#acceptorsPanel div.companyData"),
        $personData = jQuery("div#acceptorsPanel div.personData");

    attachDatepickers();

    disableFields($representativesDropdows);
    disableFields($representativesTextfields);
    disableHiddenRepresentativesFields();

    setAdditionalInformationPanelsVisibility();

    $representativesChangedManually.change(setRepresentativesView);
    $additionalInfoSelect.change(legalFormChanged);
    $representativeTypLokalizacji.change(setCitizenship);
    $representativeTypLokalizacji.change(setAdditionalInformationPanelsVisibility);
    $representativeDetail.change(clearOtherDetail);
    $countrySelect.change(togglePoliticalDetails);
    $addAnotherAcceptorButton.click(showNextAcceptor);

    function setRepresentativesView() {
        var isChecked = $representativesChangedManually.is(":checked");

        if (isChecked) {
            $representativesContainer.html($representativesTextfields.html());
        } else {
            $representativesContainer.html($representativesDropdows.html());
        }

        enableFields($representativesContainer)
    }

    function setAdditionalInformationPanelsVisibility() {
        var isAbroad = false;
        $representativeTypLokalizacji.filter(":checked").each(function(i, input) {
            if (input.value === "ABROAD") {
                isAbroad = true;
            }
        });

        if(isAbroad) {
            enableFields($acceptorsAdditionalPanels);
            $acceptorsAdditionalPanels.removeClass('hidden');
        } else {
            disableFields($acceptorsAdditionalPanels);
            $acceptorsAdditionalPanels.addClass('hidden');
        }
    }

    function clearOtherDetail() {
        var $this = jQuery(this),
            acceptor = $this.parents("div.acceptor"),
            value = this.value;

        switch(value) {
            case 'PESEL':
                acceptor.find("input[type=text][name$='birthDate']").val('');
                acceptor.find("input[type=text][name$='countryCode']").val('');
                break;
            case 'COUNTRY_CODE':
                acceptor.find("input[type=text][name$='birthDate']").val('');
                acceptor.find("input[type=text][name$='pesel']").val('');
                break;
            case 'BIRTH_DATE':
                acceptor.find("input[type=text][name$='pesel']").val('');
                acceptor.find("input[type=text][name$='countryCode']").val('');
        }
    }

    function togglePoliticalDetails() {
        var acceptor = jQuery(this).parents("div.acceptor"),
            politicianDeclaration = acceptor.find("div.isPolitician"),
            radios = politicianDeclaration.find("input"),
            selectedOption = this.value;

        if(selectedOption === "Polska" || selectedOption === "") {
            politicianDeclaration.addClass('hidden');
            radios.removeAttr('required');
            radios.removeAttr('checked');
        } else {
            politicianDeclaration.removeClass('hidden');
            radios.attr('required', 'required');
        }
    }

    function legalFormChanged() {
        if(this.value === "") {
            $personData.addClass('hidden');
            $companyData.addClass('hidden');

            clearFields($personData);
            clearFields($companyData);

            setAdditionalInformationPanelsVisibility();

            return false;
        }

        var isCompany = ["STOCK_COMPANY", "ZOO_COMPANY", "LIMITED_COMPANY", "OPEN_COMPANY"].indexOf(this.value) > -1;

        if(isCompany) {
            $personData.addClass('hidden');
            $companyData.removeClass('hidden');

            disableFields($personData);
            clearFields($personData);
            enableFields($companyData);
        } else {
            $personData.removeClass('hidden');
            $companyData.addClass('hidden');

            disableFields($companyData);
            clearFields($companyData);
            enableFields($personData);
            jQuery("input#akceptantNieMaBeneficjenta").prop('checked', true);
        }

        setAdditionalInformationPanelsVisibility();
    }

    function setCitizenship() {
        var $this = jQuery(this),
            acceptor = $this.parents("div.acceptor"),
            citizenship = acceptor.find("input[name$='citizenship']"),
            selectedOption = this.value;

        if(selectedOption === "ABROAD") {
            citizenship.val('').removeAttr('readonly');
        } else {
            citizenship.val('polskie').attr('readonly', 'readonly');
        }
    }

    function showNextAcceptor() {
        var acceptor = $representativesContainer.find('div.acceptor.hidden').first(),
            companyData = acceptor.find('.companyData'),
            personData = acceptor.find('.personData');

        if(acceptor.length === 0) {
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
        $representativesContainer.find(".date-field").datepicker({dateFormat: 'yy-mm-dd', maxDate: new Date()});
    }

    function disableHiddenRepresentativesFields() {
        disableFields($representativesContainer.find('div.acceptor.hidden'));
        disableFields($representativesContainer.find('div.companyData.hidden'));
        disableFields($representativesContainer.find('div.personData.hidden'));
    }
})();