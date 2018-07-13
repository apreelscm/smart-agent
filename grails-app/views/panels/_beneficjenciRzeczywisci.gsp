<fieldset id="actualBeneficiaries">
    <header class="belka-glowna"><g:message code="panel.actual.beneficiaries.title"/></header>

    <button type="button" id="copyFromRepresentatives" class="button submit"><g:message code="beneficiary.copy.from.representatives.label"/></button>

    <section id="actualBeneficiaryData">

        <g:each in="${0..3}">
            <div class="acceptor ${it != 0 && (it >= data.beneficiaries.size()) ? 'hidden' : ''}">
                <g:render template="/common/representative/beneficiary" model="[prefix: 'beneficiaries', seqNo: it,
                        representative: data.beneficiaries[it]]"/>
            </div>
        </g:each>

        <div class="text-center" style="margin-bottom: 15px">
            <button type="button" id="addAnotherBeneficiary" class="button submit"><g:message code="add.beneficiary.button"/></button>
        </div>
    </section>
</fieldset>

