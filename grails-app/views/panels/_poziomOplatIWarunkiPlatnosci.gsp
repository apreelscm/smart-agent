<%@ page import="com.eservice.eumowy.Constants" %>
<fieldset id="feesAndPaymentsTerms">
    <header class="belka-glowna"><g:message code="fees.level.and.payment.terms.title"/></header>

    <div class="centre">
        <p><g:message code="fees.level.and.payment.terms.info"/></p>

        <table class="centre">
            <tbody>
                <tr>
                    <td><g:message code="to.label"/></td>
                    <td><eumowy:currencyField name="oplatyIPlatnosciDo" validatable="${data}" value="${data.oplatyIPlatnosciDo}" readonly="readonly"/></td>
                    <td><g:message code="above.label"/></td>
                    <td><eumowy:currencyField name="oplatyIPlatnosciPowyzej" validatable="${data}" value="${data.oplatyIPlatnosciPowyzej}" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td><g:message code="payment.value.label"/></td>
                    <td><eumowy:percentageField name="oplataPrDo" validatable="${data}" value="${data.oplataPrDo}" readonly="readonly"/></td>
                    <td></td>
                    <td><eumowy:percentageField name="oplataPrPowyzej" validatable="${data}" value="${data.oplataPrPowyzej}" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td><g:message code="diners.club.label"/></td>
                    <td>3,20%</td>
                    <td></td>
                    <td>3,20%</td>
                </tr>
            </tbody>
        </table>

        <g:hiddenField name="dinersClubDo" value="3.2"/>
        <g:hiddenField name="dinersClubPowyzej" value="3.2"/>
    </div>
</fieldset>