<div id="acceptorCorrespondenceAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.correspondence.addres.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span><label><g:checkBox name="correspondenceAsMerchant"/><g:message code="panel.as.merchant"/></label></span>
                </li>
                <li>
                    <span><g:message code="panel.street" /></span>
                    <span>
                        <dict:streetSelect id="korespondencjaUlicaTytul" name="korespondencjaUlicaTytul" value="${data.korespondencjaUlicaTytul}" />
                        <g:textField name="korespondencjaUlica" style="width: 200px" value="${data.korespondencjaUlica}"/>
                    </span>
                    <span>
                        <span><g:message code="panel.house.number" /></span> <span><g:textField name="korespondencjaNrDomu" value="${data.korespondencjaNrDomu}" style="width: 50px"/></span>
                        <span><g:message code="panel.flat.number" /></span> <span><g:textField name="korespondencjaNrMieszkania" value="${data.korespondencjaNrMieszkania}" style="width: 50px"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.city" /></span>
                    <span><g:textField name="korespondencjaMiasto" value="${data.korespondencjaMiasto}" style="width: 280px;"/></span>
                    <span>
                        <span><g:message code="panel.postal.code" /></span> <span><g:textField class="postal-code" name="korespondencjaKodPocztowy" value="${data.korespondencjaKodPocztowy}" style="width: 50px"/></span>
                    </span>
                </li>
                <li>
                    <span><g:message code="panel.postal" /></span>
                    <span><g:textField name="korespondencjaPoczta" value="${data.korespondencjaPoczta}" style="width: 280px;"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>


<r:script>
    jQuery(document).ready(function() {

        jQuery("#correspondenceAsMerchant").click(function() {
            if(this.checked) {
                var selectedValue = jQuery('#akceptantUlicaTytul').val();
                jQuery("#korespondencjaUlicaTytul").filter(function() {
                    return jQuery(this).val() == selectedValue;
                }).prop('selected', true);

                jQuery('#korespondencjaUlicaTytul').val(jQuery('#akceptantUlicaTytul').val());
                jQuery('#korespondencjaUlica').val(jQuery('#akceptantUlica').val());
                jQuery('#korespondencjaNrDomu').val(jQuery('#akceptantNrDomu').val());
                jQuery('#korespondencjaNrMieszkania').val(jQuery('#akceptantNrMieszkania').val());
                jQuery('#korespondencjaMiasto').val(jQuery('#akceptantMiasto').val());
                jQuery('#korespondencjaKodPocztowy').val(jQuery('#akceptantKodPocztowy').val());
                jQuery('#korespondencjaPoczta').val(jQuery('#akceptantPoczta').val());
            }
        });
    });
</r:script>