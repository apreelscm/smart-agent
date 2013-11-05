<div id="printDataPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.print.data.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <div class="align-left">
                <p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                <p><eumowy:textField name="nazwaDoWydrukuZTerminalaPos" value="${data.nazwaDoWydrukuZTerminalaPos}" validatable="${data}" maxlength ="40" required="true" class="nazwaField"/></p>
                <p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="wydrukJakWyzej"><g:checkBox name="wydrukJakWyzej" id="wydrukJakWyzej" /><g:message code="panel.as.above" /></label></p>
                <p><eumowy:textField name="wydrukNazwaDoWyszukwarki" value="${data.wydrukNazwaDoWyszukwarki}" validatable="${data}" maxlength ="40" required="true" class="nazwaField"/></p>
                <p><label for="wydrukJakMerchant"><g:checkBox name="wydrukJakMerchant"/><g:message code="panel.as.merchant" /></label></p>
                <ul class="table-list">
                    <li>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect name="wydrukUlicaTytul" default="UL"/>
                            <eumowy:textField name="wydrukUlica" value="${data.wydrukUlica}" style="width: 200px" required="true"/>
                        </span>
                        <span>
                            <span><g:message code="panel.house.number" /></span> <span><eumowy:textField name="wydrukNrDomu" value="${data.wydrukNrDomu}" validatable="${data}" style="width: 50px" maxlength ="4" required="true"/></span>
                            <span><g:message code="panel.flat.number" /></span> <span><eumowy:textField name="wydrukNrMieszkania" value="${data.wydrukNrMieszkania}" validatable="${data}" style="width: 50px" maxlength ="4"/></span>
                        </span>
                    </li>
                    <li>
                        <span>
                            <span><g:message code="panel.postal.code" /></span> <span><eumowy:textField id="wydrukKodPocztowy" class="postal-code" name="wydrukKodPocztowy" value="${data.wydrukKodPocztowy}" validatable="${data}" style="width: 50px" maxlength ="5" required="true"/></span>
                        </span>
                        <span><g:message code="panel.city" /></span>
                        <span>
                            %{--<eumowy:textField name="wydrukMiasto" value="${data.wydrukMiasto}" validatable="${data}" style="width: 280px;" required="true"/>--}%
                            <g:select id="wydrukMiasto" name="wydrukMiasto" value="${data.wydrukMiasto}"
                                      from="[data.wydrukMiasto]" style="width: 280px;" required="required"/>
                        </span>

                    </li>
                    <li>
                        <span><g:message code="panel.postal" /></span>
                        <span><eumowy:textField name="wydrukPoczta" value="${data.wydrukPoczta}" validatable="${data}" style="width: 280px;" required="true"/></span>
                    </li>
                </ul>
                <p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
                <p><g:message code="panel.line1" /> <g:textField name="wydrukLinia1" value="${data.wydrukLinia1}" style="width: 90%;" maxlength ="35" /></p>
                <p><g:message code="panel.line2" /> <g:textField name="wydrukLinia2" value="${data.wydrukLinia2}" style="width: 90%;" maxlength ="35" /></p>
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