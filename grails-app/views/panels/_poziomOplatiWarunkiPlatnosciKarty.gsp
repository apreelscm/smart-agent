<div id="cardPaymentPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.card.payment.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 800px" class="centre">
            <table class="vertical-center">
                <thead>
                    <tr><td></td><td class="bold" style=" text-align: center"><g:message code="panel.percent.fee.label"/></td><td  class="bold" style=" text-align: center"><g:message code="panel.flat.rate.label"/></td></tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="bold">1. <g:message code="panel.visa.ue"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">1.1 <g:message code="panel.consumer.credit.charge"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaEUKKOPr" value="${data.visaEUKKOPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaEUKKOSt" value="${data.visaEUKKOSt}" /></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">1.2 <g:message code="panel.consumer.debit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}" name="visaEUKDPr" value="${data.visaEUKDPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaEUKDSt" value="${data.visaEUKDSt}" /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">1.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaEUKBPr" value="${data.visaEUKBPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaEUKBSt" value="${data.visaEUKBSt}" /> </td>
                    </tr>
                    <tr>
                        <td class="bold">2. <g:message code="panel.visa.not.ue"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">2.1 <g:message code="panel.consumer.credit.charge"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaOutEUKKOPr" value="${data.visaOutEUKKOPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaOutEUKKOSt" value="${data.visaOutEUKKOSt}" /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">2.2 <g:message code="panel.consumer.debit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaOutEUKDPr" value="${data.visaOutEUKDPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaOutEUKDSt" value="${data.visaOutEUKDSt}" /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">2.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaOutEUKBPr" value="${data.visaOutEUKBPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaOutEUKBSt" value="${data.visaOutEUKBSt}" /> </td>
                    </tr>
                    <tr>
                        <td class="bold">3. <g:message code="panel.visa.poland"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">3.1.1 <g:message code="panel.consumer.credit.charge.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPolskaKKO1Pr" value="${data.visaPolskaKKO1Pr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPolskaKKO1St" readonly="true" value="${data.visaPolskaKKO1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">3.1.2 <g:message code="panel.consumer.credit.charge.from" args="['20,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPolskaKKO2Pr" value="${data.visaPolskaKKO2Pr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPolskaKKO2St" readonly="true" value="${data.visaPolskaKKO2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">3.2.1 <g:message code="panel.consumer.debit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPolskaKD1Pr" value="${data.visaPolskaKD1Pr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPolskaKD1St" readonly="true" value="${data.visaPolskaKD1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">3.2.2 <g:message code="panel.consumer.debit.from" args="['20,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPolskaKD2Pr" value="${data.visaPolskaKD2Pr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPolskaKD2St" readonly="true" value="${data.visaPolskaKD2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">3.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPolskaKBPr" value="${data.visaPolskaKBPr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="visaPolskaKBSt" value="${data.visaPolskaKBSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="bold">4. <g:message code="panel.mastercard.ue"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">4.1 <g:message code="panel.consumer.credit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardEUKKPr" value="${data.mastercardEUKKPr}" /> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardEUKKSt" value="${data.mastercardEUKKSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">4.2 <g:message code="panel.consumer.debit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardEUKDPr" value="${data.mastercardEUKDPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardEUKDSt" value="${data.mastercardEUKDSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">4.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardEUKBLPr" value="${data.mastercardEUKBLPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardEUKBLSt" value="${data.mastercardEUKBLSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">4.4 <g:message code="panel.maestro"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardEUMPr" value="${data.mastercardEUMPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardEUMSt" value="${data.mastercardEUMSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="bold">5. <g:message code="panel.mastercard.not.ue"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">5.1 <g:message code="panel.consumer.credit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardOutEUKKPr" value="${data.mastercardOutEUKKPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardOutEUKKSt" value="${data.mastercardOutEUKKSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">5.2 <g:message code="panel.consumer.debit"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardOutEUKDPr" value="${data.mastercardOutEUKDPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardOutEUKDSt" value="${data.mastercardOutEUKDSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">5.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardOutEUKBPr" value="${data.mastercardOutEUKBPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardOutEUKBSt" value="${data.mastercardOutEUKBSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">5.4 <g:message code="panel.maestro"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardOutEUMPr" value="${data.mastercardOutEUMPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="mastercardOutEUMSt" value="${data.mastercardOutEUMSt}"  /> </td>
                    </tr>
                    <tr>
                        <td class="bold">6. <g:message code="panel.mastercard.poland"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.1.1 <g:message code="panel.consumer.credit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKK1Pr" value="${data.mastercardPolskaKK1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKK1St" readonly="true" value="${data.mastercardPolskaKK1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.1.2 <g:message code="panel.consumer.credit.from.to" args="['20,01', '40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKK2Pr" value="${data.mastercardPolskaKK2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKK2St" readonly="true" value="${data.mastercardPolskaKK2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.1.3 <g:message code="panel.consumer.credit.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKK3Pr" value="${data.mastercardPolskaKK3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKK3St" readonly="true" value="${data.mastercardPolskaKK3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.2.1 <g:message code="panel.consumer.debit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKD1Pr" value="${data.mastercardPolskaKD1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKD1St" readonly="true" value="${data.mastercardPolskaKD1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.2.2 <g:message code="panel.consumer.debit.from.to" args="['20,01','40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKD2Pr" value="${data.mastercardPolskaKD2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKD2St" readonly="true" value="${data.mastercardPolskaKD2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.2.3 <g:message code="panel.consumer.debit.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKD3Pr" value="${data.mastercardPolskaKD3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKD3St" readonly="true" value="${data.mastercardPolskaKD3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaKBPr" value="${data.mastercardPolskaKBPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaKBSt" readonly="false" value="${data.mastercardPolskaKBSt}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.4.1 <g:message code="panel.maestro.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaM1Pr" value="${data.mastercardPolskaM1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaM1St" readonly="true" value="${data.mastercardPolskaM1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.4.2 <g:message code="panel.maestro.from.to" args="['20,01', '40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaM2Pr" value="${data.mastercardPolskaM2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaM2St" readonly="true" value="${data.mastercardPolskaM2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">6.4.3 <g:message code="panel.maestro.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPolskaM3Pr" value="${data.mastercardPolskaM3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPolskaM3St" readonly="true" value="${data.mastercardPolskaM3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="bold">7. <g:message code="panel.visa.pkopb"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">7.1.1 <g:message code="panel.consumer.credit.charge.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPKOBPKKO1Pr" value="${data.visaPKOBPKKO1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPKOBPKKO1St" readonly="true" value="${data.visaPKOBPKKO1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">7.1.2 <g:message code="panel.consumer.credit.charge.from" args="['20,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPKOBPKKO2Pr" value="${data.visaPKOBPKKO2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPKOBPKKO2St" readonly="true" value="${data.visaPKOBPKKO2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">7.2.1 <g:message code="panel.consumer.debit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPKOBPKD1Pr" value="${data.visaPKOBPKD1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPKOBPKD1St" readonly="true" value="${data.visaPKOBPKD1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">7.2.2 <g:message code="panel.consumer.debit.from" args="['20,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPKOBPKD2Pr" value="${data.visaPKOBPKD2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPKOBPKD2St" readonly="true" value="${data.visaPKOBPKD2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">7.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="visaPKOBPKB3Pr" value="${data.visaPKOBPKB3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="visaPKOBPKB3St" readonly="false" value="${data.visaPKOBPKB3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="bold">8. <g:message code="panel.mastercard.pkobp"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.1.1 <g:message code="panel.consumer.credit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKK1Pr" value="${data.mastercardPKOBPKK1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKK1St" readonly="true" value="${data.mastercardPKOBPKK1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.1.2 <g:message code="panel.consumer.credit.from.to" args="['20,01', '40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKK2Pr" value="${data.mastercardPKOBPKK2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKK2St" readonly="true" value="${data.mastercardPKOBPKK2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.1.3 <g:message code="panel.consumer.credit.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKK3Pr" value="${data.mastercardPKOBPKK3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKK3St" readonly="true" value="${data.mastercardPKOBPKK3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.2.1 <g:message code="panel.consumer.debit.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKD1Pr" value="${data.mastercardPKOBPKD1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKD1St" readonly="true" value="${data.mastercardPKOBPKD1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.2.2 <g:message code="panel.consumer.debit.from.to" args="['20,01', '40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKD2LPr" value="${data.mastercardPKOBPKD2LPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKD2LSt" readonly="true" value="${data.mastercardPKOBPKD2LSt}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.2.3 <g:message code="panel.consumer.debit.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKD3Pr" value="${data.mastercardPKOBPKD3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKD3St" readonly="true" value="${data.mastercardPKOBPKD3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.3 <g:message code="panel.commercial.buisness"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPKBPr" value="${data.mastercardPKOBPKBPr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPKBSt" readonly="false" value="${data.mastercardPKOBPKBSt}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.4.1 <g:message code="panel.maestro.to" args="['20,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPM1Pr" value="${data.mastercardPKOBPM1Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPM1St" readonly="true" value="${data.mastercardPKOBPM1St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.4.2 <g:message code="panel.maestro.from.to" args="['20,01','40,00']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPM2Pr" value="${data.mastercardPKOBPM2Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPM2St" readonly="true" value="${data.mastercardPKOBPM2St}"/> </td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">8.4.3 <g:message code="panel.maestro.from" args="['40,01']"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="mastercardPKOBPM3Pr" value="${data.mastercardPKOBPM3Pr}"/> </td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="mastercardPKOBPM3St" readonly="true" value="${data.mastercardPKOBPM3St}"/> </td>
                    </tr>
                    <tr>
                        <td class="bold">9. <g:message code="panel.dinersclub.upercase"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number"  name="dinersClubPr" value="${data.dinersClubPr}" validatable="${data}"/> </td>
                        <td></td></tr>
                    <tr>
                        <td class="bold">10. <g:message code="panel.mobile"/></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">10a <g:message code="panel.iko"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" name="ikoPr" readonly="true" value="${data.ikoPr}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">10b <g:message code="panel.blik"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" name="blikPr" readonly="true" value="${data.blikPr}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">11 <g:message code="panel.payment.for.jcb"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" name="jcbPr" readonly="true" value="${data.jcbPr}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">12 <g:message code="panel.payment.for.upi"/></td>
                        <td class="percent-number-col"><eumowy:percentageField class="percent-number" name="upiPr" readonly="true" value="${data.upiPr}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">13 <g:message code="panel.payment.for.autorisation"/></td>
                        <td></td>
                        <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}" name="oplataAutoryzacyjnaSt" readonly="true" value="${data.oplataAutoryzacyjnaSt}"/></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">14 <g:message code="panel.payment.for.cardsOutOfEU"/></td>
                        <td><eumowy:textField name="cardsOutOfEU" readonly="true" value="${data.cardsOutOfEU}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">15 <g:message code="panel.payment.for.cardsInEUNotInPL"/></td>
                        <td><eumowy:textField name="cardsInEUNotInPL" readonly="true" value="${data.cardsInEUNotInPL}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">16 <g:message code="panel.payment.for.cardsInPL"/></td>
                        <td><eumowy:textField name="cardsInPL" readonly="true" value="${data.cardsInPL}" validatable="${data}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cardPaymentPanel-padding">17 <g:message code="panel.payment.tokenized"/></td>
                        <td colspan="2"></td>
                    </tr>
                <tr>
                    <td class="cardPaymentPanel-padding-2">17.1 <g:message code="panel.payment.tokenized.for.visaCardsInPLAndEU"/></td>
                    <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="tokenizedVisaCardsInPLAndEUPr" value="${data.tokenizedVisaCardsInPLAndEUPr}"/> </td>
                    <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="tokenizedVisaCardsInPLAndEUSt" value="${data.tokenizedVisaCardsInPLAndEUSt}"/> </td>
                </tr>
                <tr>
                    <td class="cardPaymentPanel-padding-2">17.2 <g:message code="panel.payment.tokenized.for.visaCardsOutOfEU"/></td>
                    <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="tokenizedVisaCardsOutOfEUPr" value="${data.tokenizedVisaCardsOutOfEUPr}"/> </td>
                    <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="tokenizedVisaCardsOutOfEUSt" value="${data.tokenizedVisaCardsOutOfEUSt}"/> </td>
                </tr>
                <tr>
                    <td class="cardPaymentPanel-padding-2">17.3 <g:message code="panel.payment.tokenized.for.mastercardCardsInPLAndEU"/></td>
                    <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="tokenizedMastercardCardsInPLAndEUPr" value="${data.tokenizedMastercardCardsInPLAndEUPr}"/> </td>
                    <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="tokenizedMastercardCardsInPLAndEUSt" value="${data.tokenizedMastercardCardsInPLAndEUSt}"/> </td>
                </tr>
                <tr>
                    <td class="cardPaymentPanel-padding-2">17.4 <g:message code="panel.payment.tokenized.for.mastercardCardsOutOfEU"/></td>
                    <td class="percent-number-col"><eumowy:percentageField class="percent-number" validatable="${data}"  name="tokenizedMastercardCardsOutOfEUPr" value="${data.tokenizedMastercardCardsOutOfEUPr}"/> </td>
                    <td class="flat-price-col"><eumowy:flatPriceField class="flat-price" validatable="${data}"  name="tokenizedMastercardCardsOutOfEUSt" value="${data.tokenizedMastercardCardsOutOfEUSt}"/> </td>
                </tr>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>