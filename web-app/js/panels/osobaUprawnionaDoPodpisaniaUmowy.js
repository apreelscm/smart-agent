var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
    $representativesDropdows = jQuery("#acceptorsPanel #representativesDropdowns"),
    $representativesTextfields = jQuery("#acceptorsPanel #representativesTextfields"),
    $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
    $representativeLocation = jQuery("#acceptorsPanel input[name='akceptantLokalizacja']"),
    $representativePESELKraj = jQuery("#representativesContainer input[name$='lokalizacjaPesel']"),
    $acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels");

attachDatepickers();

disableFields($representativesDropdows);
disableFields($representativesTextfields);

setReprezentantImieAndNazwiskoRequired();

$representativesChangedManually.change(setRepresentativesView);
$representativeLocation.change(setAdditionalInformationState);
$representativePESELKraj.live("change", setAcceptorState);

function setRepresentativesView() {
    var isChecked = $representativesChangedManually.is(":checked");

    if (isChecked) {
        $representativesContainer.html($representativesTextfields.html());
    } else {
        $representativesContainer.html($representativesDropdows.html());
    }

    enableFields($representativesContainer)
}

function setReprezentantImieAndNazwiskoRequired() {
    $("#reprezentant1Imie").attr('required', 'required');
    $("#reprezentant1Nazwisko").attr('required', 'required');
}

function setAdditionalInformationState() {
    var isAbroad = $representativeLocation.filter(":checked").val() === "ABROAD",
        acceptorCountryWrapper = $representativesContainer.find(".acceptorCountry"),
        acceptorAbroadWrapper = $representativesContainer.find(".acceptorAbroad");

    if(isAbroad) {
        $acceptorsAdditionalPanels.removeClass('hidden');

        acceptorCountryWrapper.addClass("hidden");
        acceptorAbroadWrapper.removeClass("hidden");

        clearFields(acceptorCountryWrapper);
        attachDatepickers();
    } else {
        $acceptorsAdditionalPanels.addClass('hidden');

        $representativesContainer.find(".acceptorCountry").removeClass("hidden");
        $representativesContainer.find(".acceptorAbroad").addClass("hidden");

        clearFields($acceptorsAdditionalPanels);
        clearFields(acceptorAbroadWrapper);
    }
}

function setAcceptorState() {
    var $this = jQuery(this),
        acceptor = $this.parents("div.acceptor"),
        passportDocumentTypeRadio = acceptor.find("input[type='radio'][name$='typDokumentu'][value='PASSPORT']"),
        birthDateField = acceptor.find("input[type='text'][name$='dataUrodzenia']"),
        politicalField = acceptor.find("input[type='checkbox'][name$='czyStanowiskoPolityczne']"),
        selectedOption = this.value;

    if(selectedOption === "COUNTRY") {
        passportDocumentTypeRadio.attr("checked", "checked");
        birthDateField.attr("required", "required");
        politicalField.attr("required", "required");
    } else {
        birthDateField.removeAttr("required");
        politicalField.removeAttr("required");
    }
}

function attachDatepickers() {
    $representativesContainer.find(".date-field").datepicker({dateFormat: 'yy-mm-dd', maxDate: new Date()});
}