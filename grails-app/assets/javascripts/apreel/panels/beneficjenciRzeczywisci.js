(function () {
    'use strict';

    var actualBeneficiaryData = jQuery("#actualBeneficiaryData"),
        beneficiaries = actualBeneficiaryData.find(".acceptor"),
        copyFromRepresentativesButton = jQuery("button#copyFromRepresentatives"),
        addBeneficiaryButton = jQuery("#addAnotherBeneficiary"),
        representatives;


    jQuery(function () {
        actualBeneficiaryData.find(".date-field").datepicker({dateFormat: 'yy-mm-dd', maxDate: new Date()});
        actualBeneficiaryData.find(".percent-short").mask(PERCENT_SHORT_FORMAT);
        actualBeneficiaryData.find("input[type=radio][name$='isPolitician']").change(onIsPoliticianChange);
        disableHiddenBeneficiaryFields();
    });

    addBeneficiaryButton.click(showAnotherBeneficiary);
    copyFromRepresentativesButton.click(copyRepresentativesData);

    function copyRepresentativesData() {
        representatives = jQuery("#representativesContainer").find(".acceptor:not(.hidden)");

        representatives.each(function (representativeIndex, value) {
            jQuery(this).find("input, select").each(function () {
                setBeneficiaryFieldValue(representativeIndex, this);
            });
        });
    }

    function showAnotherBeneficiary() {
        var beneficiary = actualBeneficiaryData.find('div.acceptor.hidden').first();

        if (beneficiary.length === 0) {
            jQuery(this).attr('disabled', 'disabled');
            return false;
        }

        beneficiary.removeClass('hidden');
        enableFields(beneficiary);

        return false;
    }

    function setBeneficiaryFieldValue(beneficiaryIndex, representativeField) {
        if (representativeField.disabled) return false;

        var fieldName = representativeField.name.split(".")[1],
            fieldType = representativeField.type,
            beneficiary = jQuery(beneficiaries[beneficiaryIndex]),
            beneficiaryField = beneficiary.find("[name$=" + fieldName + "]").not("input[type=hidden]"),
            value = representativeField.value;

        if (beneficiaryField.length === 0 || !!beneficiaryField.attr('disabled')) {
            return false;
        }

        if (fieldType === "radio") {
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
