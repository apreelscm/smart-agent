<%@ page import="com.eservice.eumowy.enums.AcceptorLocation" %>

<div id="acceptorsPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre" style="padding-top: 20px; width: 915px">
            <g:hiddenField name="isFromBisnode" value="${data.isFromBisnode}"/>
            
            <g:if test="${data.isFromBisnode && representativesBisnode?.size() > 0}">
                <div>
                    <g:checkBox name="isRepresentativesChangedManually" value="${data.isRepresentativesChangedManually}" readonly="readonly"/> <g:message code="representatives.change"/>
                </div>

                <div style="margin-bottom: 20px">
                    <label for="poleOpisowe" style="margin-top: 20px">Pole opisowe</label>
                    <g:textArea name="poleOpisowe" maxlength ="1000" rows="3" cols="70" style="height: auto; width: auto"/>
                </div>

                <eumowy:enumRadioGroup values="${AcceptorLocation.values()}" name="akceptantLokalizacja" value="${data.akceptantLokalizacja}"
                                       radioWrapperClass="acceptorLocationRadioWrapper" required="true"/>

                <div id="representativesContainer">
                    <g:render template="../panels/reprezentaciDropdowns"/>
                </div>

                <div id="representativesDropdowns" class="hidden">
                    <g:render template="../panels/reprezentaciDropdowns"/>
                </div>

                <div id="representativesTextfields" class="hidden">
                    <g:render template="../panels/reprezentaciTextfields"/>
                </div>
            </g:if>
            <g:else>
                <div id="representativesContainer">
                    <g:render template="../panels/reprezentaciTextfields"/>
                </div>
            </g:else>

            <div style="margin-top: 30px; margin-bottom: 0">
                <label for="emailDoWysylkiDokumentu"><g:message code="email.receiver.address.label"/>:</label>
                <g:textField id="emailDoWysylkiDokumentu" class="" name="emailDoWysylkiDokumentu" value="${data.emailDoWysylkiDokumentu}" validatable="${data}" style="width: 150px" email="true"/>
            </div>
        </div>
    </fieldset>
</div>

<div id="acceptorsAdditionalPanels" class="hidden">
    <g:render template="/panels/beneficjenciRzeczywisci"/>
    <g:render template="/panels/dokumentyWeryfikacyjne"/>
</div>

<script type="text/javascript">
    var $representativesContainer = jQuery("#acceptorsPanel #representativesContainer"),
        $representativesDropdows = jQuery("#acceptorsPanel #representativesDropdowns"),
        $representativesTextfields = jQuery("#acceptorsPanel #representativesTextfields"),
        $representativesChangedManually = jQuery("#acceptorsPanel #isRepresentativesChangedManually"),
        $representativeLocation = jQuery("#acceptorsPanel input[name='akceptantLokalizacja']"),
        $representativePESELKraj = jQuery("#representativesContainer input[name$='lokalizacjaPesel']"),
        $acceptorsAdditionalPanels = jQuery("#acceptorsAdditionalPanels");

    disableHiddenInputs();
    setRepresentativesView();
    setReprezentantImieAndNazwiskoRequired();

    $representativesChangedManually.change(setRepresentativesView);
    $representativeLocation.change(setAdditionalInformationState);
    $representativePESELKraj.live("change", setAcceptorState);

    function disableHiddenInputs() {
        jQuery("#representativesDropdowns input, #representativesDropdowns select, #representativesTextfields input, #representativesTextfields select").attr('disabled', 'disabled');
    }

    function setRepresentativesView() {
        var isChecked = $representativesChangedManually.is(":checked");

        if (isChecked) {
            $representativesContainer.html($representativesTextfields.html());
        } else {
            $representativesContainer.html($representativesDropdows.html());
        }

        $representativesContainer.find("input, select").removeAttr('disabled');
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
        } else { //selectedOption === "PESEL"
            birthDateField.removeAttr("required");
            politicalField.removeAttr("required");
        }
    }

    function clearFields(fieldsContainer) {
        fieldsContainer.find("input[type='checkbox']").removeAttr("checked");
        fieldsContainer.find("input[type='radio']").removeAttr("checked");
        fieldsContainer.find("input[type='text']").val('');
    }
</script>