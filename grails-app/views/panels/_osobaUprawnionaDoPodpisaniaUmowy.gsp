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

<script type="text/javascript">
    var $representativesContainer = jQuery("#representativesContainer"),
        $representativesDropdows = jQuery("#representativesDropdowns"),
        $representativesTextfields = jQuery("#representativesTextfields");

    disableHiddenInputs();
    setRepresentativesView();

    jQuery("#isRepresentativesChangedManually").change(setRepresentativesView);

    function disableHiddenInputs() {
        jQuery("#representativesDropdowns input, #representativesDropdowns select, #representativesTextfields input, #representativesTextfields select").attr('disabled', 'disabled');
    }

    function setRepresentativesView() {
        var isChecked = jQuery("#isRepresentativesChangedManually").is(":checked");

        if (isChecked) {
            $representativesContainer.html($representativesTextfields.html());
        } else {
            $representativesContainer.html($representativesDropdows.html());
        }

        $representativesContainer.find("input, select").removeAttr('disabled');
    }
</script>