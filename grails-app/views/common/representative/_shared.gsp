<div class="sharedRepresentativeData">
    <div class="hasSignedContract ${hasErrors(bean: representative, field: 'hasSignedContract', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="hasSignedContract">
            <p class="error-message"><g:message code="representative.option.required"/></p>
        </g:hasErrors>

        <span><g:message code="person.signed.contract"/></span>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract"
                 value="true"
                 checked="${representative?.hasSignedContract == true}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="yes"/></label>

        <g:radio name="${prefix}[${seqNo}].hasSignedContract"
                 value="false"
                 checked="${representative?.hasSignedContract == false}"/>
        <g:hiddenField name="${prefix}[${seqNo}].hasSignedContract"
                       cbdDataHiddenField="cbdDataHiddenField"
                       value="${representative?.hasSignedContract}"/>
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="no"/></label>

        <script type="text/javascript">
            var val = jQuery('input[name="${prefix}[${seqNo}].hasSignedContract"]:checked').val(),
                $phoneContainer = jQuery('.phone-container-${prefix}-${seqNo}'),
                $emailContainer = jQuery('.email-container-${prefix}-${seqNo}'),
                $hasSignedContract = jQuery(".hasSignedContract"),
                $acceptorsPanel = jQuery("#acceptorsPanel"),
                isAnyDataManual = $acceptorsPanel.find("input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked").length > 0,
                hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract = jQuery("#hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract").val() === 'true',
                additionalData = jQuery('input[name="${prefix}[${seqNo}].additionalData"]').is(':checked');

            if (hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract || isAnyDataManual) {
                $hasSignedContract.removeClass("hidden");
                $hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
                $hasSignedContract.find('input').removeAttr('disabled');
            } else {
                $hasSignedContract.addClass("hidden")
                $hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                $hasSignedContract.find('input').attr('disabled', 'disabled');
            }

            if (val === undefined || val === 'false') {
                $phoneContainer.hide();
                $phoneContainer.find('input[type=radio].telephone-type:checked').attr('checked', false);
                $phoneContainer.find('input.phone-number').val(null);
                $emailContainer.hide();
                $emailContainer.find('input').val(null);
            } else {
                enableFields($phoneContainer);
                enableFields($emailContainer);

                $phoneContainer.show();
                $emailContainer.show();
            }

            jQuery('input[name="${prefix}[${seqNo}].hasSignedContract"]').change(function(e) {
                var $this = jQuery(this),
                    val = $this.val(),
                    $acceptor = $this.parents("div.acceptor"),
                    $personData = $acceptor.children('div.personData'),
                    $companyData = $acceptor.children('div.companyData'),
                    $phoneContainer,
                    $emailContainer;

                if ($personData.is(':visible')) {
                    $phoneContainer = $personData.find('.phone-container');
                    $emailContainer = $personData.find('.email-container');
                } else {
                    $phoneContainer = $companyData.find('.phone-container');
                    $emailContainer = $companyData.find('.email-container');
                }

                if (val === 'false') {
                    $phoneContainer.hide();
                    $phoneContainer.find('input[type=radio].telephone-type:checked').attr('checked', false);
                    $phoneContainer.find('input.phone-number').val(null);

                    $emailContainer.hide();
                    $emailContainer.find('input').val(null);
                } else {
                    enableFields($phoneContainer);
                    enableFields($emailContainer);

                    $phoneContainer.show();
                    $emailContainer.show();
                }
            });
        </script>
    </div>
</div>
