var actualBeneficiaryData = jQuery("#actualBeneficiaryData"),
    beneficiaries = actualBeneficiaryData.find(".acceptor"),
    cantEstablishSection = jQuery("#cantEstablishBeneficiary"),
    representatives;

jQuery("input[name='czyBeneficjentRzeczywisty']").change(function() {
    if(this.value == "true") {
        actualBeneficiaryData.removeClass("hidden");
        cantEstablishSection.addClass("hidden");

        clearFields(cantEstablishSection)
        disableFields(cantEstablishSection);
        enableFields(actualBeneficiaryData);
    } else {
        actualBeneficiaryData.addClass("hidden");
        cantEstablishSection.removeClass("hidden");

        clearFields(actualBeneficiaryData);
        disableFields(actualBeneficiaryData);
        enableFields(cantEstablishSection);
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