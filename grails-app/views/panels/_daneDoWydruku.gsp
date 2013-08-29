<div id="printDataPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.print.data.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <div class="align-left">
                <p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                <p><g:textField name="nazwaDoWydrukuZTerminalaPos" value="${data.nazwaDoWydrukuZTerminalaPos}"/></p>
                <p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="wydrukJakWyzej"><g:checkBox name="wydrukJakWyzej" id="wydrukJakWyzej" /><g:message code="panel.as.above" /></label></p>
                <p><g:textField name="wydrukNazwaDoWyszukwarki" value="${data.wydrukNazwaDoWyszukwarki}"/></p>
                <p><label for="wydrukJakMerchant"><g:checkBox name="wydrukJakMerchant"/><g:message code="panel.as.merchant" /></label></p>
                <ul class="table-list">
                    <li>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect name="wydrukUlicaTytul" />
                            <g:textField name="wydrukUlica" value="${data.wydrukUlica}" style="width: 200px"/>
                        </span>
                        <span>
                            <span><g:message code="panel.house.number" /></span> <span><g:textField name="wydrukNrDomu" value="${data.wydrukNrDomu}" style="width: 50px"/></span>
                            <span><g:message code="panel.flat.number" /></span> <span><g:textField name="wydrukNrMieszkania" value="${data.wydrukNrMieszkania}" style="width: 50px"/></span>
                        </span>
                    </li>
                    <li>
                        <span><g:message code="panel.city" /></span>
                        <span><g:textField name="wydrukMiasto" value="${data.wydrukMiasto}" style="width: 280px;"/></span>
                        <span>
                            <span><g:message code="panel.postal.code" /></span> <span><g:textField class="postal-code" name="wydrukKodPocztowy" value="${data.wydrukKodPocztowy}" style="width: 50px"/></span>
                        </span>
                    </li>
                    <li>
                        <span><g:message code="panel.postal" /></span>
                        <span><g:textField name="wydrukPoczta" value="${data.wydrukPoczta}" style="width: 280px;"/></span>
                    </li>
                </ul>
                <p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
                <p><g:message code="panel.line1" /> <g:textField name="wydrukLinia1" value="${data.wydrukLinia1}" style="width: 90%;" /></p>
                <p><g:message code="panel.line2" /> <g:textField name="wydrukLinia2" value="${data.wydrukLinia2}" style="width: 90%;" /></p>
            </div>
        </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {

        jQuery("#wydrukJakWyzej").click(function() {
            if(this.checked) {
                jQuery('#wydrukNazwaDoWyszukwarki').val(jQuery('#nazwaDoWydrukuZTerminalaPos').val());
            }
        });

        jQuery("#wydrukJakMerchant").click(function(e) {
            if(this.checked) {
                var selectedValue = jQuery('#akceptantUlicaTytul').val();
                jQuery("#wydrukUlicaTytul").filter(function() {
                    return jQuery(this).val() == selectedValue;
                }).prop('selected', true);

                jQuery('#printAddressStreetTitle').val(jQuery('#akceptantUlicaTytul').val());
                jQuery('#wydrukUlica').val(jQuery('#akceptantUlica').val());
                jQuery('#wydrukNrDomu').val(jQuery('#akceptantNrDomu').val());
                jQuery('#wydrukNrMieszkania').val(jQuery('#akceptantNrMieszkania').val());
                jQuery('#wydrukMiasto').val(jQuery('#akceptantMiasto').val());
                jQuery('#wydrukKodPocztowy').val(jQuery('#akceptantKodPocztowy').val());
                jQuery('#wydrukPoczta').val(jQuery('#akceptantPoczta').val());
            }
        });
    });
</r:script>