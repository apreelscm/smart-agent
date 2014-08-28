<div id="cashbackVent">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.cashback.vent.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 400px" class="centre">
            <table class="table-list centre">
                <tr class="${data.wydrukGrafikiCena ?: 'display-none'}">
                    <td><g:message code="panel.cashback.vent.title"/></td>
                    <td>
                        <eumowy:currencyField type="text" name="upustCashback" value="${data.upustCashback}" readonly="true"/>
                    </td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>