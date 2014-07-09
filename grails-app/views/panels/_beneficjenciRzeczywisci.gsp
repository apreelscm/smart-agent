<asset:javascript src="apreel/panels/beneficjenciRzeczywisci.js"/>

<fieldset id="actualBeneficiaries">
    <header class="belka-glowna"><g:message code="panel.actual.beneficiaries.title"/></header>

    <g:radio name="czyBeneficjentRzeczywisty" value="false" required="required" checked="${data.czyBeneficjentRzeczywisty}"/>
    <span class="bold"><g:message code="beneficiary.cant.establish.label"/></span>

    <section id="cantEstablishBeneficiary" class="${data.czyBeneficjentRzeczywisty ? "hidden" : ""}">
        <div>
            <g:checkBox name="akceptantJestSpolka" checked="${data.akceptantJestSpolka}"/>
            <label><g:message code="acceptor.listed.company.label"/></label>
        </div>

        <div>
            <label for="nazwaGieldy"><g:message code="stock.exchange.name.label"/></label>
            <g:textField name="nazwaGieldy" value="${data.nazwaGieldy}" maxlength="50"/>

            <label for="isinAkceptanta"><g:message code="acceptor.isin.code.label"/></label>
            <g:textField name="isinAkceptanta" value="${data.isinAkceptanta}" maxlength="12" class="isin-field"/>
        </div>

        <div>
            <g:checkBox name="akceptantJestPodmiotem" checked="${data.akceptantJestPodmiotem}"/>
            <label><g:message code="acceptor.subject.label"/></label>
        </div>

        <div>
            <g:checkBox name="akceptantJestOrganem" checked="${data.akceptantJestOrganem}"/>
            <label><g:message code="acceptor.organ.label"/></label>
        </div>

        <div>
            <g:checkBox name="akceptantNieMaBeneficjenta" value="${data.akceptantNieMaBeneficjenta}"/>
            <label><g:message code="acceptor.no.physical.beneficiary.label"/></label>
            <label class="display-block"><g:message code="acceptor.no.physical.beneficiary.a.label"/></label>
            <label class="display-block"><g:message code="acceptor.no.physical.beneficiary.b.label"/></label>
            <label class="display-block" style="max-width: inherit"><g:message code="acceptor.no.physical.beneficiary.c.label"/></label>
        </div>
    </section>

    <div style="margin: 10px 0">
        <g:radio name="czyBeneficjentRzeczywisty" value="true" required="required" checked="${data.czyBeneficjentRzeczywisty}"/>
        <span class="bold"><g:message code="beneficiary.data.label"/></span>
    </div>

    <section id="actualBeneficiaryData" class="${data.czyBeneficjentRzeczywisty ?: "hidden"}">
        <button type="button" id="copyFromRepresentatives" class="button submit"><g:message code="beneficiary.copy.from.representatives.label"/></button>

        <g:each in="${0..3}">
            <div class="acceptor">
                <g:render template="/common/representative/basicData" model="[prefix: 'beneficiaries', seqNo: it,
                        representative: data.beneficiaries[it]]"/>

                <g:render template="/common/representative/acceptorAbroad" model="[prefix: 'beneficiaries', seqNo: it,
                        representative: data.beneficiaries[it]]"/>

                <div style="margin-top: 25px">
                    <g:render template="/common/representative/relationWithAcceptor" model="[seqNo: it,
                            representative: data.beneficiaries[it]]"/>
                </div>
            </div>
        </g:each>
    </section>
</fieldset>

