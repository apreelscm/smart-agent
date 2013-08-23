<div id="rechargeTypePanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.recharge.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 600px" class="centre">
            <div style="display: inline"><label><g:checkBox name="doladowania_tp" value="${data.doladowania_tp}" /> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka"/></label></div>
            <div style="display: inline"><label><g:checkBox name="doladowania_tk" value="${data.doladowania_tk}" /> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik"/></label></div>

            <div class="align-center" style="padding-top: 10px"><g:message code="panel.declared.selling.electronic.recharch"/></div>
            <table style="padding-top: 10px">
                <tr class="vertical-center">
                    <td>&nbsp;</td>
                    <td class="align-right"><g:textField name="srednia_sprzedaz_doladowan" value="${data.srednia_sprzedaz_doladowan}" style="width: 100px"/></td>
                    <td class="align-left"><g:message code="panel.for.each.pos"/></td>
                </tr>
                <tr class="vertical-center">
                    <td class="align-right">słownie</td>
                    <td class="align-right"><g:textField name="srednia_sprzedaz_doladowan_slownie" value="${data.srednia_sprzedaz_doladowan_slownie}" style="width: 300px"/></td>
                    <td class="align-left"><g:message code="panel.for.each.pos"/></td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>