<fieldset>
    <header class="belka-glowna"><g:message code="panel.actual.beneficiaries.title"/></header>
        <input type="radio" name="czyBeneficjentRzeczywisty" value="false" required="required">
        <span class="bold"><g:message code="beneficiary.cant.establish.label"/></span>

        <section id="cantEstablishBeneficiary">

            <div>
                <input type="checkbox" name="akceptantJestSpolka">
                <label><g:message code="acceptor.listed.company.label"/></label>
            </div>
            
            <div>
                <label for="nazwaGieldy"><g:message code="stock.exchange.name.label"/></label>
                <g:textField name="nazwaGieldy" maxlength="50"/>

                <label for="isinAkceptanta"><g:message code="acceptor.isin.code.label"/></label>
                <g:textField name="isinAkceptanta" maxlength="12" class="isin-field"/>
            </div>
            
            <div>
                <input type="checkbox" name="akceptantJestPodmiotem"/>
                <label><g:message code="acceptor.subject.label"/></label>
            </div>
            
            <div>
                <input type="checkbox" name="akceptantJestOrganem"/>
                <label><g:message code="acceptor.organ.label"/></label>
            </div>
            
            <div>
                <input type="checkbox" name="akceptantNieMaBeneficjenta"/>
                <label><g:message code="acceptor.no.physical.beneficiary.label"/></label>
                <label class="display-block"><g:message code="acceptor.no.physical.beneficiary.a.label"/></label>
                <label class="display-block"><g:message code="acceptor.no.physical.beneficiary.b.label"/></label>
                <label class="display-block" style="max-width: inherit"><g:message code="acceptor.no.physical.beneficiary.c.label"/></label>
            </div>
        </section>

        <input type="radio" name="czyBeneficjentRzeczywisty" value="true" required="required">
        <span class="bold"><g:message code="beneficiary.data.label"/></span>

        <section id="actualBeneficiaryData">
            %{--<g:each in="${1..3}">--}%
                %{--<g:render template="/common/acceptor" model="['representative': 'beneficjent' + it, 'dropdowns': false]"/>--}%
            %{--</g:each>--}%
        </section>
</fieldset>

<script type="text/javascript">

</script>