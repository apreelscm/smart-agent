var actualBeneficiaryData = jQuery("#actualBeneficiaryData"),
    beneficiaries = actualBeneficiaryData.find(".acceptor"),
    representatives;

jQuery("input[name='czyBeneficjentRzeczywisty']").change(function() {
    if(this.value == "true") {
        actualBeneficiaryData.removeClass("hidden");
        enableFields(actualBeneficiaryData);
    } else {
        actualBeneficiaryData.addClass("hidden");
        clearFields(actualBeneficiaryData);
        disableFields(actualBeneficiaryData);
    }
});

jQuery("#copyFromRepresentatives").click(function() {
    representatives = jQuery("#representativesContainer").find(".acceptor");

    representatives.each(function(representativeIndex, value) {
        jQuery(this).find("input, select").each(function() {
            setBeneficiaryFieldValue(representativeIndex, this);
        });
    });

    this.disabled = true;
});

function setBeneficiaryFieldValue(beneficiaryIndex, representativeField) {
    var fieldName, fieldType, beneficiary, bebeficiaryField, value;

    fieldName = representativeField.name.split(".")[1];
    fieldType = representativeField.type;

    beneficiary = jQuery(beneficiaries[beneficiaryIndex]);
    bebeficiaryField = beneficiary.find("[name$=" + fieldName + "]");

    if(fieldType === "radio") {
        value = jQuery(representatives[beneficiaryIndex]).find("[name$=" + fieldName + "]:checked").val();
    } else {
        value = representativeField.value;
    }

    switch (fieldType) {
        case "text":
        case "select-one":
            bebeficiaryField.val(value);
            break;
        case "checkbox":
            bebeficiaryField[0].checked = representativeField.checked
            break;
        case "radio":
            value ?
                beneficiary.find("[name$=" + fieldName + "][value=" + value + "]").attr("checked", "checked")
                :
                bebeficiaryField.removeAttr("checked");
    }
}