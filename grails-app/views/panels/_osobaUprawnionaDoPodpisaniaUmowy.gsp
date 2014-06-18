<%@ page import="com.eservice.eumowy.enums.AcceptorLocation" %>

<div id="acceptorsPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre" style="padding-top: 20px; width: 915px">
            <g:hiddenField name="isFromBisnode" value="${data.isFromBisnode}"/>
            
            <g:if test="${data.isFromBisnode && representativesBisnode?.size() > 0}">
                <div>
                    <g:checkBox name="isRepresentativesChangedManually" value="${data.isRepresentativesChangedManually}" readonly="readonly"/>
                    <g:message code="representatives.change"/>
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
                <eumowy:enumRadioGroup values="${AcceptorLocation.values()}" name="akceptantLokalizacja" value="${data.akceptantLokalizacja}"
                                       radioWrapperClass="acceptorLocationRadioWrapper" required="true"/>

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

<div id="acceptorsAdditionalPanels" class="${data.isAkceptantAbroad() ?: "hidden"}">
    <g:render template="/panels/beneficjenciRzeczywisci"/>
    <g:render template="/panels/dokumentyWeryfikacyjne"/>
</div>

<g:javascript src="panels/osobaUprawnionaDoPodpisaniaUmowy.js"/>

<g:if test="${data.isFromBisnode}">
    <script type="text/javascript">
        var representatives = {};

        <g:each in="${representativesBisnode}" var="representative" status="i">
        representatives[${i}] = {title: '${representative.title}', fistName: '${representative.firstName}', lastName: '${representative.lastName}', position: '${representative.position}'};
        </g:each>

        jQuery(".imieField, .nazwiskoField").change(function() {
            var $this = jQuery(this),
                    selectedOptionNo = $this[0].selectedIndex,
                    parentDiv = $this.parent('div'),
                    firstNameSelect = parentDiv.find('.imieField'),
                    lastNameSelect = parentDiv.find('.nazwiskoField'),
                    titleInput = parentDiv.find('.tytulField'),
                    positionInput = parentDiv.find('.positionField');

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
    </script>
</g:if>