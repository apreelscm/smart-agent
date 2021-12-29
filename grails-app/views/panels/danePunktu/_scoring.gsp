<fieldset class="subpanel-fieldset">
    <legend><g:message code="panel.scoring.title"/></legend>

<div class="subpanel-fieldset-centercontent-scoring">

    <table class="vertical-center" id="zestawPOSTable">
        <tr>
            <td class="isAcceptedCardTransactions"><g:message code="scoring.isAcceptedCardTransactions.label"/>
            </td>
            <td>
                <g:radio name="${panelType}[${id}].isAcceptedCardTransactions" value="true"
                         checked="${pointData?.isAcceptedCardTransactions == true}"/>
                <label for="${panelType}[${id}].isAcceptedCardTransactions"><g:message code="yes"/></label>

                <g:radio name="${panelType}[${id}].isAcceptedCardTransactions" value="false"
                         checked="${pointData?.isAcceptedCardTransactions == false}"/>
                <label for="${panelType}[${id}].isAcceptedCardTransactions"><g:message code="no"/></label>
            </td>
        </tr>

        <tr>
            <td class="isPrivateApartment"><g:message code="scoring.isPrivateApartment.label"/>
            </td>
            <td>
                <g:radio name="${panelType}[${id}].isPrivateApartment" value="true"
                         checked="${pointData?.isPrivateApartment == true}"/>
                <label for="${panelType}[${id}].isPrivateApartment"><g:message code="yes"/></label>

                <g:radio name="${panelType}[${id}].isPrivateApartment" value="false"
                         checked="${pointData?.isPrivateApartment == false}"/>
                <label for="${panelType}[${id}].isPrivateApartment"><g:message code="no"/></label>
            </td>
        </tr>

        <tr>
            <td class="isAcceptedPrepayments"><g:message code="scoring.isAcceptedPrepayments.label"/>
            </td>
            <td>
                <g:radio name="${panelType}[${id}].isAcceptedPrepayments" value="true"
                         checked="${pointData?.isAcceptedPrepayments == true}"/>
                <label for="${panelType}[${id}].isAcceptedPrepayments"><g:message code="yes"/></label>

                <g:radio name="${panelType}[${id}].isAcceptedPrepayments" value="false"
                         checked="${pointData?.isAcceptedPrepayments == false}"/>
                <label for="${panelType}[${id}].isAcceptedPrepayments"><g:message code="no"/></label>
            </td>
        </tr>


        <tr id="percentageOfPrepaymentsTr"
            class="hidden">
            <td>
                <g:message code="scoring.percentageOfPrepayments.label"/>
            </td>
            <td><eumowy:textField name="${panelType}[${id}].percentageOfPrepayments"
                                  value="${pointData?.percentageOfPrepayments}"
                                  style="text-align: right; width: 150px"
                                  id="${panelType}[${id}].percentageOfPrepayments"
                                  class="integer-number"/></td>
        </tr>
        <tr id="averageDeliveryTime"
            class="hidden">
            <td><g:message code="scoring.averageDeliveryTime.label"/>
            </td>
            <td><g:textField name="${panelType}[${id}].averageDeliveryTime"
                             class="integer-number"
                             id="${panelType}[${id}].averageDeliveryTime"
                             required="required"
                             style="text-align: right; width: 150px"
                             value="${pointData?.averageDeliveryTime}"/><g:message code="panel.days"/></td>
        </tr>
        <tr id="maximumDeliveryTime"
            class="hidden">
            <td><g:message code="scoring.maximumDeliveryTime.label"/>
            </td>
            <td><g:textField name="${panelType}[${id}].maximumDeliveryTime"
                             id="${panelType}[${id}].maximumDeliveryTime"
                             required="required"
                             style="text-align: right; width: 150px"
                             value="${pointData?.maximumDeliveryTime}"
                             class="integer-number"/><g:message code="panel.days"/></td>
        </tr>
        <tr>
            <td><g:message code="scoring.monthlyCashTurnover.label"/>
            </td>
            <td><eumowy:currencyField name="${panelType}[${id}].monthlyCashTurnover"
                                      class="decimal-number"
                                      id="${panelType}[${id}].monthlyCashTurnover"
                                      required="required"
                                      value="${pointData?.monthlyCashTurnover}"/></td>
        </tr>
        <tr>
            <td><g:message
                    code="scoring.monthlyTurnoverInInstitution.label"/>
            </td>
            <td><eumowy:currencyField name="${panelType}[${id}].monthlyTurnoverInInstitution"
                                      class="decimal-number"
                                      id="${panelType}[${id}].monthlyTurnoverInInstitution"
                                      required="required"
                                      value="${pointData?.monthlyTurnoverInInstitution}"/></td>
        </tr>
        <tr>
            <td><g:message code="scoring.averageBill.label"/>
            </td>
            <td><eumowy:currencyField name="${panelType}[${id}].averageBill"
                                      class="decimal-number"
                                      id="${panelType}[${id}].averageBill"
                                      style="width: 150px"
                                      required="required"
                                      value="${pointData?.averageBill}"/></td>
        </tr>
        <tr>
            <td><g:message code="scoring.highestCashTransaction.label"/>
            </td>
            <td><eumowy:currencyField name="${panelType}[${id}].highestCashTransaction"
                                      class="decimal-number"
                                      id="${panelType}[${id}].highestCashTransaction"
                                      style="width: 150px"
                                      required="required"
                                      value="${pointData?.highestCashTransaction}"/></td>
        </tr>

        <tr>
            <td><g:message code="scoring.numberOfDailyTransactions.label"/>
            </td>
            <td><eumowy:select name="${panelType}[${id}].numberOfDailyTransactions"
                               from="['', '04', '5-10', 'powyżej 10']"
                               id="${panelType}[${id}].highestCashTransaction"
                               style="text-align: right; width: 180px"
                               required="required"
                               value="${pointData?.numberOfDailyTransactions}"/></td>
        </tr>
    </table>
</div>
</div>
</fieldset>
