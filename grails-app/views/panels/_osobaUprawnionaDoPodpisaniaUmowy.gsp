<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation" %>

<div id="acceptorsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre">
            <g:hiddenField name="isFromBisnode" value="${data.isFromBisnode}"/>
            
            <g:if test="${data.isFromBisnode && representativesBisnode?.size() > 0}">
                <g:set var="firstNames" value="${[""] + representativesBisnode?.collect {it.firstName}}"/>
                <g:set var="lastNames" value="${[""] + representativesBisnode?.collect {it.lastName}}"/>

                <div>
                    <g:checkBox name="isRepresentativesChangedManually" value="${data.isRepresentativesChangedManually}" readonly="readonly"/>
                    <g:message code="representatives.change"/>
                </div>

                <div style="margin-bottom: 20px">
                    <label for="poleOpisowe" style="margin-top: 20px"><g:message code="description.field.label"/></label>
                    <g:textArea name="poleOpisowe" maxlength ="1000" rows="3" cols="70"/>
                </div>

                <div id="representativesContainer">
                    <g:render template="../panels/reprezentanci" model="[hasDropdowns: !data.isRepresentativesChangedManually]"/>
                </div>

                %{--Below field will be switched with javascript when user check 'Zmiana danych reprezentacji'--}%
                <div id="bisnodeRepresentatives" class="hidden">
                    <g:render template="/common/representative/basicData" model="[prefix: 'representative', dropdowns: true]"/>
                </div>

                <div id="customRepresentatives" class="hidden">
                    <g:render template="/common/representative/basicData" model="[prefix: 'representative', dropdowns: false]"/>
                </div>
            </g:if>
            <g:else>
                <div id="representativesContainer">
                    <g:render template="../panels/reprezentanci" model="[hasDropdowns: false]"/>
                </div>
            </g:else>
        </div>
    </fieldset>

    <div id="email-documents" class="text-center">
        <label for="emailDoWysylkiDokumentu"><g:message code="email.receiver.address.label"/>:</label>
        <g:textField id="emailDoWysylkiDokumentu" class="" name="emailDoWysylkiDokumentu" value="${data.emailDoWysylkiDokumentu}" validatable="${data}" style="width: 150px" email="true"/>
    </div>
</div>

<g:if test="${czyNowaUmowa}">
    <div id="acceptorsAdditionalPanels">
        <g:render template="/panels/beneficjenciRzeczywisci"/>
        <g:render template="/panels/dokumentyWeryfikacyjne"/>
    </div>

    <asset:javascript src="apreel/panels/beneficjenciRzeczywisci.js"/>
    <asset:javascript src="apreel/panels/osobaUprawnionaDoPodpisaniaUmowy.js"/>
</g:if>

<g:if test="${data.isFromBisnode}">
    <script type="text/javascript">
        var representatives = {};

        <g:each in="${representativesBisnode}" var="representative" status="i">
            representatives[${i}] = {title: '${representative.title}', fistName: '${representative.firstName}', lastName: '${representative.lastName}', position: '${representative.position}'};
        </g:each>

        attachBisnodeNameChangeEvent();

        function attachBisnodeNameChangeEvent() {
            jQuery(".nameField, .surnameField").change(function() {
                var $this = jQuery(this),
                        selectedOptionNo = $this[0].selectedIndex,
                        parentDiv = $this.parent('div'),
                        firstNameSelect = parentDiv.find('.nameField'),
                        lastNameSelect = parentDiv.find('.surnameField'),
                        titleInput = parentDiv.find('.salutationField'),
                        positionInput = parentDiv.find('.positionField');

                if (!firstNameSelect[0] || !lastNameSelect[0]) {
                    return false;
                }

                firstNameSelect[0].selectedIndex = selectedOptionNo;
                lastNameSelect[0].selectedIndex = selectedOptionNo;

                if(selectedOptionNo === 0) {
                    positionInput.val('');
                    titleInput.val('')
                } else {
                    positionInput.val(representatives[selectedOptionNo - 1].position);
                    titleInput.val(representatives[selectedOptionNo - 1].title);
                }
            });
        }
    </script>
</g:if>