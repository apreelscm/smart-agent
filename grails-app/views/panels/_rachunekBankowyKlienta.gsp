<div id="bankAccountPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.bank.account.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 500px" class="centre">
            <ul class="table-list">
                <li>
                    <span class="align-right"><g:message code="panel.bank.account.number"/></span>
                    <span class="align-left"><g:textField name="numerRachunkuBankowego" class="bank-account" value="${data.numerRachunkuBankowego}" style="width: 250px"/></span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.bank.name"/></span>
                    <span class="align-left"><dict:bankSelect name="bank" style="width: 250px" value="${data.bank}"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>
