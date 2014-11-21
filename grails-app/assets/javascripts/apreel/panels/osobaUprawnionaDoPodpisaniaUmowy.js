var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
    $representativesDropdows = jQuery("#acceptorsPanel #representativesDropdowns"),
    $representativesTextfields = jQuery("#acceptorsPanel #representativesTextfields"),
    $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
    $acceptorLocation = jQuery("#acceptorsPanel input[name='akceptantLokalizacja']"),
    $representativePESELKraj = jQuery("#representativesContainer input[name$='lokalizacjaPesel']"),
    $representativeTypLokalizacji = jQuery("#representativesContainer input[type=radio][name$='typLokalizacji']"),
    $acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels"),
    $additionalInfoSelect = jQuery("div#additionalInformationPanel select[name='dzialalnoscForma']"),
    $companyData = jQuery("div#acceptorsPanel div#companyData"),
    $personData = jQuery("div#acceptorsPanel div#personData");

attachDatepickers();

disableFields($representativesDropdows);
disableFields($representativesTextfields);

setReprezentantImieAndNazwiskoRequired();

$representativesChangedManually.change(setRepresentativesView);
$acceptorLocation.change(setAdditionalInformationState);
$additionalInfoSelect.change(legalFormChanged);
$representativeTypLokalizacji.change(typLokalizacjiChanged);
$representativePESELKraj.on("change", setAcceptorState);

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
    jQuery("#representatives\\[0\\]\\.imie").attr('required', 'required');
    jQuery("#representatives\\[0\\]\\.nazwisko").attr('required', 'required');
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

function legalFormChanged() {
    if(this.value === "") {
        $personData.addClass('hidden');
        $companyData.addClass('hidden');
        return false;
    }

    var isCompany = ["STOCK_COMPANY", "ZOO_COMPANY", "LIMITED_COMPANY", "OPEN_COMPANY"].indexOf(this.value) > -1;

    if(isCompany) {
        $personData.addClass('hidden');
        $companyData.removeClass('hidden');

        disableFields($personData);
        enableFields($companyData);
    } else {
        $personData.removeClass('hidden');
        $companyData.addClass('hidden');

        disableFields($companyData);
        enableFields($personData);
    }
}

function typLokalizacjiChanged() {
    var $this = jQuery(this),
        acceptor = $this.parents("div.acceptor"),
        politicianDeclaration = acceptor.find("div.isPolitician"),
        citizenship = acceptor.find("input[name$='obywatelstwo']"),
        radios = politicianDeclaration.find("input"),
        selectedOption = this.value;

    if(selectedOption === "ABROAD") {
        politicianDeclaration.removeClass('hidden');
        radios.attr('required', 'required');
        citizenship.val('').removeAttr('readonly');
    } else {
        politicianDeclaration.addClass('hidden');
        radios.removeAttr('required');
        radios.removeAttr('checked');
        citizenship.val('polskie').attr('readonly', 'readonly');
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