(function() {
    'use strict';

    var isActualBeneficiary = jQuery("input[name='czyBeneficjentRzeczywisty']"),
        actualBeneficiaryData = jQuery("#actualBeneficiaryData"),
        beneficiaries = actualBeneficiaryData.find(".acceptor"),
        cantEstablishSection = jQuery("#cantEstablishBeneficiary"),
        copyFromRepresentativesButton = jQuery("button#copyFromRepresentatives"),
        beneficiariesDetails = actualBeneficiaryData.find("input[type=radio][name$='verification']"),
        addBeneficiaryButton = jQuery("#addAnotherBeneficiary"),
        representatives;


    jQuery(function() {
        actualBeneficiaryData.find(".date-field").datepicker({dateFormat: 'yy-mm-dd', maxDate: new Date()});
        actualBeneficiaryData.find(".percent-short").mask('09');
        disableHiddenBeneficiaryFields();
    });

    isActualBeneficiary.change(toggleBeneficiariesPanelsVisibility);
    copyFromRepresentativesButton.click(copyRepresentativesData);
    addBeneficiaryButton.click(showAnotherBeneficiary);
    beneficiariesDetails.change(clearOtherBeneficiaryDetail);

    function toggleBeneficiariesPanelsVisibility() {
        if(this.value == "true") {
            actualBeneficiaryData.removeClass("hidden");
            cantEstablishSection.addClass("hidden");

            clearFields(cantEstablishSection);
            enableFields(actualBeneficiaryData);

            copyFromRepresentativesButton.removeAttr('disabled');
        } else {
            actualBeneficiaryData.addClass("hidden");
            cantEstablishSection.removeClass("hidden");

            clearFields(actualBeneficiaryData);
            enableFields(cantEstablishSection);
        }
    }

    function copyRepresentativesData() {
        representatives = jQuery("#representativesContainer").find(".acceptor:not(.hidden)");

        representatives.each(function(representativeIndex, value) {
            jQuery(this).find("input, select").each(function() {
                setBeneficiaryFieldValue(representativeIndex, this);
            });
        });

        this.disabled = true;
    }

    function showAnotherBeneficiary() {
        var beneficiary = actualBeneficiaryData.find('div.acceptor.hidden').first();

        if(beneficiary.length === 0) {
            jQuery(this).attr('disabled', 'disabled');
            return false;
        }

        beneficiary.removeClass('hidden');
        enableFields(beneficiary);

        return false;
    }

    function clearOtherBeneficiaryDetail() {
        var $this = jQuery(this),
            acceptor = $this.parents("div.acceptor"),
            value = this.value;

        if(value == 'PESEL') {
            acceptor.find("input[type=text][name$='countryCode']").val('');
        } else {
            acceptor.find("input[type=text][name$='pesel']").val('');
        }
    }

    function setBeneficiaryFieldValue(beneficiaryIndex, representativeField) {
        if(representativeField.disabled) return false

        var fieldName, fieldType, beneficiary, beneficiaryField, value;

        fieldName = representativeField.name.split(".")[1];
        fieldType = representativeField.type;

        beneficiary = jQuery(beneficiaries[beneficiaryIndex]);
        beneficiaryField = beneficiary.find("[name$=" + fieldName + "]").not("input[type=hidden]");

        if(fieldType === "radio") {
            value = jQuery(representatives[beneficiaryIndex]).find("[name$=" + fieldName + "]:checked").val();
        } else {
            value = representativeField.value;
        }

        switch (fieldType) {
            case "text":
            case "select-one":
                beneficiaryField.val(value);
                break;
            case "checkbox":
                beneficiaryField[0].checked = representativeField.checked
                break;
            case "radio":
                value ?
                    beneficiary.find("[name$=" + fieldName + "][value=" + value + "]").attr("checked", "checked")
                    :
                    beneficiaryField.removeAttr("checked");
        }
    }

    function disableHiddenBeneficiaryFields() {
        disableFields(actualBeneficiaryData.find('div.acceptor.hidden'));
    }
})();