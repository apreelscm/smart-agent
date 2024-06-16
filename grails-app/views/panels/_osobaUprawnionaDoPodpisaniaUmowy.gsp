<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation" %>

<div id="acceptorsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre">
            <g:hiddenField name="isFromBisnode" value="${data.isFromBisnode}"/>
            <g:hiddenField name="hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract"
                           value="${data.hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract}"/>

            <div id="representativesContainer">
                <g:each in="${0..3}">
                    <div id="panel-representatives-${it}" class="acceptor ${it != 0 && (it >= data.representatives.size()) ? 'hidden' : ''}">
                        <div class="basicRepresentativeData">
                            <g:render template="/common/representative/basicData" model="[prefix: 'representatives', seqNo: it,
                                                                                          representative: data.representatives[it]]"/>

                        </div>

                        <g:render template="/common/representative/company" model="[prefix: 'representatives', seqNo: it,
                                                                                    additionalClass: (data.isPersonForm() == true) ? 'hidden' : '',
                                                                                    representative: data.representatives[it]]"/>

                        <g:render template="/common/representative/personOrPartnership" model="[prefix: 'representatives', seqNo: it,
                                                                                                additionalClass: (data.isPersonForm() == true) ? '' : 'hidden',
                                                                                                representative: data.representatives[it]]"/>

                        <g:render template="/common/representative/shared" model="[prefix: 'representatives', seqNo: it,
                                                                                   representative: data.representatives[it]]"/>
                    </div>
                </g:each>

                <div class="text-center" style="margin-bottom: 15px">
                    <button type="button" id="addAnotherAcceptor" class="button submit"><g:message code="add.acceptor.button"/></button>
                </div>
            </div>
        </div>
    </fieldset>
</div>

<div id="acceptorsAdditionalPanels">
    <g:render template="/panels/beneficjenciRzeczywisci"/>
    <g:render template="/panels/dokumentyWeryfikacyjne"/>
</div>

<asset:javascript src="apreel/panels/beneficjenciRzeczywisci.js"/>
<asset:javascript src="apreel/panels/osobaUprawnionaDoPodpisaniaUmowy.js"/>
