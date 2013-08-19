<div id="printDataPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.print.data.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <div class="align-left">
                <p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                <p><g:textField name="printPointName"/></p>
                <p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="printAsAbove"><g:checkBox name="printAsAbove" id="printAsAbove" /><g:message code="panel.as.above" /></label></p>
                <p><g:textField name="printSearchName"/></p>
                <p><label for="printAsMerchant"><g:checkBox name="printAsMerchant"/><g:message code="panel.as.merchant" /></label></p>
                <ul class="table-list">
                    <li>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect id="printAddressStreetTitle" name="printAddressStreetTitle" />
                            <g:textField name="printAddressStreet" id="printAddressStreet" style="width: 200px"/>
                        </span>
                        <span>
                            <span><g:message code="panel.house.number" /></span> <span><g:textField name="printAddressHomeNumber" style="width: 50px"/></span>
                            <span><g:message code="panel.flat.number" /></span> <span><g:textField name="printAddressFlatNumber" style="width: 50px"/></span>
                        </span>
                    </li>
                    <li>
                        <span><g:message code="panel.city" /></span>
                        <span><g:textField name="printAddressCity" style="width: 280px;"/></span>
                        <span>
                            <span><g:message code="panel.postal.code" /></span> <span><g:textField name="printAddressPostalCode" style="width: 50px"/></span>
                        </span>
                    </li>
                    <li>
                        <span><g:message code="panel.postal" /></span>
                        <span><g:textField name="printAddressPostal" style="width: 280px;"/></span>
                    </li>
                </ul>
                <p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
                <p><g:message code="panel.line1" /> <g:textField name="otherDataForPrintingFromTerminal1%ID%" style="width: 90%;" /></p>
                <p><g:message code="panel.line2" /> <g:textField name="otherDataForPrintingFromTerminal2%ID%" style="width: 90%;" /></p>
            </div>
        </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {

        jQuery("#printAsAbove").click(function() {
            if(this.checked) {
                jQuery('#printSearchName').val(jQuery('#printPointName').val());
            }
        });

        jQuery("#printAsMerchant").click(function(e) {
            if(this.checked) {
                var selectedValue = jQuery('#addressStreetTitle').val();
                jQuery("#printAddressStreetTitle").filter(function() {
                    return jQuery(this).val() == selectedValue;
                }).prop('selected', true);

                jQuery('#printAddressStreetTitle').val(jQuery('#addressStreetTitle').val());
                jQuery('#printAddressStreet').val(jQuery('#addressStreet').val());
                jQuery('#printAddressHomeNumber').val(jQuery('#addressHomeNumber').val());
                jQuery('#printAddressFlatNumber').val(jQuery('#addressFlatNumber').val());
                jQuery('#printAddressCity').val(jQuery('#addressCity').val());
                jQuery('#printAddressPostalCode').val(jQuery('#addressPostalCode').val());
                jQuery('#printAddressPostal').val(jQuery('#addressPostal').val());
            }
        });
    });
</r:script>