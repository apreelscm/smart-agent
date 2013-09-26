<div id="bankAccountPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.bank.account.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 500px" class="centre">
            <ul class="table-list">
                <li>
                    <span class="align-right"><g:message code="panel.bank.account.number"/></span>
                    <span class="align-left"><eumowy:textField name="numerRachunkuBankowegoKlienta" class="bank-account" value="${data.numerRachunkuBankowegoKlienta}" validatable="${data}" style="width: 250px" required="true"/></span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.bank.name"/></span>
                    <span class="align-left"><eumowy:textField name="bankKlienta" value="${data.bankKlienta}" validatable="${data}" style="width: 250px" required="true"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#numerRachunkuBankowegoKlienta").on("keyup", function(e) {
            var accountNr = jQuery(e.target).val();
            if ( accountNr != undefined && accountNr != null){

                var bankNameInput = jQuery("#bankKlienta");

                if (validateAccountNumber(accountNr)){
                    jQuery.get("/eumowy/activity/getBankName", {accountNo: accountNr.replace(/\s+/g, '')}, function(data) {
                        if (data != undefined && data != null && data != "") {
                            var obj = JSON.parse(data);
                            bankNameInput.val(obj.name);
                        }
                    });
                } else {
                    bankNameInput.val('').attr('placeholder', 'Numer konta jest nieprawidłowy');
                }
            }
        });
    });
</r:script>