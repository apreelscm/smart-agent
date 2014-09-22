<fieldset id="cashbackVent">
    <header class="belka-glowna"><g:message code="panel.cashback.vent.title"/></header>

    <g:hiddenField name="isCashbackUpustEditable"  value="${data.isCashbackUpustEditable}"/>

    <div class="centre">
        <table class="centre" style="width: 390px">
            <tbody>
                <tr>
                    <td><g:message code="panel.cashback.vent.title"/></td>
                    <td><eumowy:currencyField type="text" name="cashbackUpust" value="${data.cashbackUpust}" validatable="${data}" readonly="${!data.isCashbackUpustEditable}"/></td>
                </tr>
            </tbody>
        </table>
    </div>
</fieldset>