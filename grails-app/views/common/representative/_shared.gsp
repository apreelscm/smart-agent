<div class="sharedRepresentativeData">
    <div class="hasSignedContract ${hasErrors(bean: representative, field: 'hasSignedContract', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="hasSignedContract">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <span><g:message code="person.signed.contract"/></span>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract"
                 value="true"
                 disabled="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                 checked="${representative?.hasSignedContract == true}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract"
                 value="false"
                 disabled="${!czyNowaUmowa && !representative?.isCBDDataChangedManually}"
                 checked="${representative?.hasSignedContract == false}"/>
        <g:hiddenField name="${prefix}[${seqNo}].hasSignedContract"
                       disabled="${czyNowaUmowa || representative?.isCBDDataChangedManually}"
                       cbdDataHiddenField="cbdDataHiddenField"
                       value="${representative?.hasSignedContract}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="no"/></label>

        <script type="text/javascript">

            var val = jQuery('input[name="${prefix}[${seqNo}].hasSignedContract"]:checked').val();
            if (val === undefined || val === 'false') {
                jQuery('.phone-container-${prefix}-${seqNo}').hide();
                jQuery('.phone-container-${prefix}-${seqNo} input[type=radio].telephone-type:checked').attr('checked', false);
                jQuery('.phone-container-${prefix}-${seqNo} input.phone-number').val(null);
                jQuery('.email-container-${prefix}-${seqNo}').hide();
                jQuery('.email-container-${prefix}-${seqNo} input').val(null);
            } else {
                jQuery('.phone-container-${prefix}-${seqNo}').show();
                jQuery('.email-container-${prefix}-${seqNo}').show();
            }

            jQuery('input[name="${prefix}[${seqNo}].hasSignedContract"]').change(function(e) {
                var val = jQuery(this).val();
                if (val === 'false') {
                    jQuery('.phone-container-${prefix}-${seqNo}').hide();
                    jQuery('.phone-container-${prefix}-${seqNo} input[type=radio].telephone-type:checked').attr('checked', false);
                    jQuery('.phone-container-${prefix}-${seqNo} input.phone-number').val(null);
                    jQuery('.email-container-${prefix}-${seqNo}').hide();
                    jQuery('.email-container-${prefix}-${seqNo} input').val(null);
                } else {
                    jQuery('.phone-container-${prefix}-${seqNo}').show();
                    jQuery('.email-container-${prefix}-${seqNo}').show();
                }
            });
        </script>
    </div>
</div>
