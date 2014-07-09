var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
    $representativesDropdows = jQuery("#acceptorsPanel #representativesDropdowns"),
    $representativesTextfields = jQuery("#acceptorsPanel #representativesTextfields"),
    $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
    $acceptorLocation = jQuery("#acceptorsPanel input[name='akceptantLokalizacja']"),
    $representativePESELKraj = jQuery("#representativesContainer input[name$='lokalizacjaPesel']"),
    $representativeTypLokalizacji = jQuery("#representativesContainer input[type=radio][name$='typLokalizacji']"),
    $acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels");

attachDatepickers();

disableFields($representativesDropdows);
disableFields($representativesTextfields);

setReprezentantImieAndNazwiskoRequired();

$representativesChangedManually.change(setRepresentativesView);
$acceptorLocation.change(setAdditionalInformationState);
$representativeTypLokalizacji.change(typLokalizacjiChanged);
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
    $("#representatives\\[0\\]\\.imie").attr('required', 'required');
    $("#representatives\\[0\\]\\.nazwisko").attr('required', 'required');
}

function setAdditionalInformationState() {
    var isAbroad = $acceptorLocation.filter(":checked").val() === "ABROAD",
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

function typLokalizacjiChanged() {
    var $this = jQuery(this),
        acceptor = $this.parents("div.acceptor"),
        peselField = acceptor.find("input[type='text'][name$='lokalizacjaPesel']"),
        countryField = acceptor.find("input[type='text'][name$='lokalizacjaKraj']"),
        selectedOption = this.value;

    if(selectedOption === "ABROAD") {
        peselField.val('');
        peselField.attr('disabled', 'disabled');
        countryField.removeAttr('disabled');
    } else {
        countryField.val('');
        countryField.attr('disabled', 'disabled');
        peselField.removeAttr('disabled');
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