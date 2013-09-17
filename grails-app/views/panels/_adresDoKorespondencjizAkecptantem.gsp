<div id="acceptorCorrespondenceAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.correspondence.addres.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span><label for="correspondenceAsMerchant"><g:checkBox name="correspondenceAsMerchant"/><g:message code="panel.as.merchant"/></label></span>
                </li>
                <li>
                    <span><g:message code="panel.street" /></span>
                    <span>
                        <dict:streetSelect id="akceptantKontaktUlicaTytul" name="akceptantKontaktUlicaTytul" value="${data.akceptantKontaktUlicaTytul}" />
                        <g:textField name="akceptantKontaktUlica" style="width: 200px" value="${data.akceptantKontaktUlica}" maxlength ="19"/>
                    </span>
                    <span>
                        <span><g:message code="panel.house.number" /></span> <span><g:textField name="akceptantKontaktNrDomu" value="${data.akceptantKontaktNrDomu}" style="width: 50px" maxlength ="4"/></span>
                        <span><g:message code="panel.flat.number" /></span> <span><g:textField name="akceptantKontaktNrMieszkania" value="${data.akceptantKontaktNrMieszkania}" style="width: 50px" maxlength ="4"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.city" /></span>
                    <span><g:textField name="akceptantKontaktMiasto" value="${data.akceptantKontaktMiasto}" style="width: 280px;"/></span>
                    <span>
                        <span><g:message code="panel.postal.code" /></span> <span><g:textField class="postal-code" name="akceptantKontaktKodPocztowy" value="${data.akceptantKontaktKodPocztowy}" style="width: 50px" maxlength ="5"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.postal" /></span>
                    <span><g:textField name="akceptantKontaktPoczta" value="${data.akceptantKontaktPoczta}" style="width: 280px;" maxlength ="19"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>


<r:script>
    jQuery(document).ready(function() {

        jQuery("#correspondenceAsMerchant").click(function() {
            if(this.checked) {
				jQuery('#akceptantKontaktUlicaTytul').val(jQuery('#akceptantUlicaTytul').val());
                jQuery('#akceptantKontaktUlica').val(jQuery('#akceptantUlica').val());
                jQuery('#akceptantKontaktNrDomu').val(jQuery('#akceptantNrDomu').val());
                jQuery('#akceptantKontaktNrMieszkania').val(jQuery('#akceptantNrMieszkania').val());
                jQuery('#akceptantKontaktMiasto').val(jQuery('#akceptantMiasto').val());
                jQuery('#akceptantKontaktKodPocztowy').val(jQuery('#akceptantKodPocztowy').val());
                jQuery('#akceptantKontaktPoczta').val(jQuery('#akceptantPoczta').val());
            }
            else {
                jQuery('#akceptantKontaktUlicaTytul').val('');
                jQuery('#akceptantKontaktUlica').val('');
                jQuery('#akceptantKontaktNrDomu').val('');
                jQuery('#akceptantKontaktNrMieszkania').val('');
                jQuery('#akceptantKontaktMiasto').val('');
                jQuery('#akceptantKontaktKodPocztowy').val('');
                jQuery('#akceptantKontaktPoczta').val('');
            }
        });
    });
</r:script>