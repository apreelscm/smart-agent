<asset:javascript src="apreel/panels/dokumentyWeryfikacyjne.js"/>
<div id="dokumentyWeryfikacyjne" class="dokumentyWeryfikacyjne ${!czyDaneReprezentatowLubBeneficjentowZmienione ? 'hidden' : ''}">
    <fieldset>

    <header class="belka-glowna"><g:message code="panel.verifications.documents.title"/></header>

    <section id="verificationDocuments">
        <div>
            <g:checkBox name="beneficjentWeryfikacjaKRS"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaKRS}"/>
            <label><g:message code="beneficiary.verification.krs.label"/></label>

            <eumowy:textField name="beneficjentKRS" maxlength="20"
                              value="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaKRS ? data.beneficjentKRS : ""}" validatable="${data}"
                              class="krs-number"/>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaDokumentTozsamosci"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaDokumentTozsamosci}"/>
            <label><g:message code="beneficiary.verification.ids.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaGielda"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaGielda}"/>
            <label><g:message code="beneficiary.verification.stack.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaSpolka"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaSpolka}"/>
            <label><g:message code="beneficiary.verification.company.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaKsiega"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaKsiega}"/>
            <label><g:message code="beneficiary.verification.stock.paper.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaSchemat"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaSchemat}"/>
            <label><g:message code="beneficiary.verification.schema.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficjentWeryfikacjaRejestr"
                        checked="${czyDaneReprezentatowLubBeneficjentowZmienione && data.beneficjentWeryfikacjaRejestr}"/>
            <label><g:message code="beneficiary.verification.register.label"/></label>
        </div>
    </section>
</fieldset>
</div>
