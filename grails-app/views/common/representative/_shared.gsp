<div class="sharedRepresentativeData">
    <div id="hasSignedContract-${prefix}-${seqNo}" class="hasSignedContract ${hasErrors(bean: representative, field: 'hasSignedContract', 'errorSpan')}">
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
        <label for="${prefix}[${seqNo}].hasSignedContract"><g:message code="no"/></label>

        <g:hiddenField name="${prefix}[${seqNo}].hasSignedContract"
                       cbdDataHiddenField="cbdDataHiddenField"
                       disabled="${representative == null || representative?.isCBDDataChangedManually}"
                       value="${representative?.hasSignedContract}"/>

        <script type="text/javascript">
            // This script gets repeated for each panel, we need to make sure global variables that target specific panel
            // have unique names, otherwise variable values might be overwritten with data from last panel
            function isCompany() {
                return jQuery('#panel-${prefix}-${seqNo}').children('div.companyData').is(':visible') === true;
            }
            function getPhoneContainer() {
                return isCompany()
                    ? jQuery('.phone-container-${prefix}-${seqNo}.phone-container-company')
                    : jQuery('.phone-container-${prefix}-${seqNo}');
            }
            function getEmailContainer() {
                return isCompany()
                    ? jQuery('.email-container-${prefix}-${seqNo}.email-container-company')
                    : jQuery('.email-container-${prefix}-${seqNo}');
            }
            var ${prefix}Form${seqNo} = {
                isSignedContractChecked: jQuery('input[name="${prefix}[${seqNo}].hasSignedContract"]:checked').val(),
                isCompany: isCompany(),
                $hasSignedContract: jQuery('#hasSignedContract-${prefix}-${seqNo}'),
                $phoneContainer: getPhoneContainer(),
                $emailContainer: getEmailContainer(),
            };
            var $acceptorsPanel = jQuery("#acceptorsPanel"),
                isAnyDataManual = $acceptorsPanel.find("input[type=radio][name$='isCBDDataChangedManually'][value=true]:checked").length > 0,
                hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract = jQuery("#hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract").val() === 'true',
                additionalData = jQuery('input[name="${prefix}[${seqNo}].additionalData"]').is(':checked');

            if (!hasActivitiesThatRequiresAtLeastOneRepresentativeToSignContract && !isAnyDataManual) {
                ${prefix}Form${seqNo}.$hasSignedContract.addClass("hidden")
                ${prefix}Form${seqNo}.$hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').removeAttr("disabled");
                ${prefix}Form${seqNo}.$hasSignedContract.find('input:not([type="hidden"])').attr('disabled', 'disabled');
            } else {
                ${prefix}Form${seqNo}.$hasSignedContract.removeClass("hidden");
                ${prefix}Form${seqNo}.$hasSignedContract.find('*[cbdDataHiddenField="cbdDataHiddenField"]').attr("disabled");
                ${prefix}Form${seqNo}.$hasSignedContract.find('input:not([type="hidden"])').removeAttr('disabled');
            }

            if (${prefix}Form${seqNo}.isSignedContractChecked === undefined || ${prefix}Form${seqNo}.isSignedContractChecked === 'false') {
                ${prefix}Form${seqNo}.$phoneContainer.hide();
                ${prefix}Form${seqNo}.$phoneContainer.find('input[type=radio].telephone-type:checked').attr('checked', false);
                ${prefix}Form${seqNo}.$phoneContainer.find('input.phone-number').val(null);
                ${prefix}Form${seqNo}.$emailContainer.hide();
                ${prefix}Form${seqNo}.$emailContainer.find('input').val(null);
            } else {
                enableFields(${prefix}Form${seqNo}.$phoneContainer);
                enableFields(${prefix}Form${seqNo}.$emailContainer);

                ${prefix}Form${seqNo}.$phoneContainer.show();
                ${prefix}Form${seqNo}.$emailContainer.show();
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
