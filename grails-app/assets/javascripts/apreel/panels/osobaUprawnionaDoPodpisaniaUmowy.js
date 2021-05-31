(function () {
    'use strict';

    var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
        $bisnodeRepresentatives = jQuery("#acceptorsPanel #bisnodeRepresentatives"),
        $customRepresentatives = jQuery("#acceptorsPanel #customRepresentatives"),
        $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
        $representativeTelephoneType = jQuery("#representativesContainer input[type=radio].telephone-type"),
        $representativeDocumentType = jQuery("#representativesContainer input[type=radio][name$='documentType']"),
        $representativeIsPolitician = jQuery("#representativesContainer input[type=radio][name$='isPolitician']"),
        $representativeVerification = jQuery("#representativesContainer input[type=radio][name$='verification']"),
        $representativePosition = jQuery("#representativesContainer select[name$='position']"),
        $additionalInfoSelect = jQuery("div#additionalInformationPanel select[name='dzialalnoscForma']"),
        $addAnotherAcceptorButton = jQuery("button#addAnotherAcceptor"),
        $companyData = jQuery("div#acceptorsPanel div.companyData"),
        $personData = jQuery("div#acceptorsPanel div.personData");

    attachDatepickers();
    attachTooltips();

    disableFields($bisnodeRepresentatives);
    disableFields($customRepresentatives);
    disableHiddenRepresentativesFields();

    $representativesChangedManually.change(setRepresentativesView);
    $additionalInfoSelect.change(legalFormChanged);
    $representativePosition.change(onPositionChange);
    $representativeDocumentType.change(onDocumentTypeChange);
    $representativeIsPolitician.change(onIsPoliticianChange);
    $representativeVerification.change(clearVerificationDetail);
    $representativeTelephoneType.change(phoneTypeChanged);
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
        var $acceptor = jQuery(this).parents("div.acceptor");
        manageVisibilityOfDocumentInfo($acceptor);
        menageVisibilityOfCitizenship($additionalInfoSelect.val(), jQuery(this).val(), $acceptor.find('.citizenShipDiv'),'');
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

    function legalFormChanged() {
        if (this.value === "") {
            $personData.addClass('hidden');
            $companyData.addClass('hidden');

            clearFields($personData);
            clearFields($companyData);

            return false;
        }

        var isPerson = isLegalFormPerson(this.value);

        if (isPerson) {
            $personData.removeClass('hidden');
            $companyData.addClass('hidden');

            disableFields($companyData);
            clearFields($companyData);
            enableFields($personData);
            $personData.find('input[type="radio"][id$="telephoneType-disabled"]').each(function(idx) {
                var $elem = jQuery(this);
                var nameAttr = $elem.attr("name");
                if (nameAttr.endsWith("-disabled")) {
                    $elem.attr("name", nameAttr.substr(0, nameAttr.length - 9));
                }
            });

            $companyData.find('input[type="radio"][id$="telephoneType"]').each(function(idx) {
                var $elem = jQuery(this);
                var nameAttr = $elem.attr("name");
                if (!nameAttr.endsWith("-disabled")) {
                    $elem.attr("name", nameAttr + "-disabled");
                }
            });
            jQuery("input#akceptantNieMaBeneficjenta").prop('checked', true);
        } else {
            $personData.addClass('hidden');
            $companyData.removeClass('hidden');

            disableFields($personData);
            clearFields($personData);
            enableFields($companyData);

            $companyData.find('input[type="radio"][id$="telephoneType-disabled"]').each(function(idx) {
                var $elem = jQuery(this);
                var nameAttr = $elem.attr("name");
                if (nameAttr.endsWith("-disabled")) {
                    $elem.attr("name", nameAttr.substr(0, nameAttr.length - 9));
                }
            });

            $personData.find('input[type="radio"][id$="telephoneType"]').each(function(idx) {
                var $elem = jQuery(this);
                var nameAttr = $elem.attr("name");
                if (!nameAttr.endsWith("-disabled")) {
                    $elem.attr("name", nameAttr + "-disabled");
                }
            });
        }

        $representativesContainer.find('div.acceptor').each(function (_, value) {
            manageVisibilityOfDocumentInfo(jQuery(value));
        });
    }

    function manageVisibilityOfDocumentInfo($acceptor) {
        var documentInfo = $acceptor.find('div.acceptorDocumentInfoWrapper'),
            position = $acceptor.find("select[name$='position']").val();

        clearFields(documentInfo);

        var isPerson = isLegalFormPerson($additionalInfoSelect.val());

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
            var config = {
                altField: "input#" + input.id,
                dateFormat: 'yy-mm-dd',
                changeYear: true
            };
            var $input = jQuery(input);
            var minDate = input.className.indexOf('date-future') !== -1 ? new Date() : null;
            var maxDate = input.className.indexOf('date-past') !== -1 ? new Date() : null;

            if (minDate) config.minDate = minDate;
            if (maxDate) config.maxDate = maxDate;

            $input.datepicker(config);
        });
    }

    function attachTooltips() {
        $representativesContainer.find(".tooltip").tooltip({
            'tooltipClass': 'representative-tooltip'
        });
    }

    function disableHiddenRepresentativesFields() {
        disableFields($representativesContainer.find('div.acceptor.hidden'));
        disableFields($representativesContainer.find('div.companyData.hidden'));
        disableFields($representativesContainer.find('div.personData.hidden'));

        $representativesContainer.find('div.companyData.hidden input[type="radio"][id$="telephoneType"]').each(function(idx) {
            var $elem = jQuery(this);
            var nameAttr = $elem.attr("name");
            if (!nameAttr.endsWith("-disabled")) {
                $elem.attr("name", nameAttr + "-disabled");
            }
        });

        $representativesContainer.find('div.personData.hidden input[type="radio"][id$="telephoneType"]').each(function(idx) {
            var $elem = jQuery(this);
            var nameAttr = $elem.attr("name");
            if (!nameAttr.endsWith("-disabled")) {
                $elem.attr("name", nameAttr + "-disabled");
            }
        });
    }

    function phoneTypeChanged(){
        var phoneNumberSelector = jQuery(this).closest("div").find("input.phone-number");
        if (this.value === 'LANDLINE') {
            phoneNumberSelector.removeClass('mobile-phone');
            phoneNumberSelector.addClass('phone');
            phoneNumberSelector.mask(LANDLINE_PHONE_FORMAT);
        } else {
            phoneNumberSelector.removeClass('phone');
            phoneNumberSelector.addClass('mobile-phone');
            phoneNumberSelector.mask(MOBILE_PHONE_FORMAT);
        }
    }

})();