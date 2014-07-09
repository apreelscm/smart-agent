<div id="posChangeTerminalPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.pos.exchange.title"/></div>
        <g:hiddenField name="isWymianaTermianlaShown" value="tak"/>
        <br/>
        <div id='other_for_selected_terminals' style="text-align: center; width: 950px" class="centre">
            <table class="t">
                <thead>
                    <tr>
                        <td class="align-center">Pełna nazwa punktu</td>
                        <td class="align-center">Adres punktu</td>
                        <td class="align-center">Numer terminala</td>
                        <td class="align-center">Model</td>
                        <td class="align-center">Opłata</td>
                        <td class="align-center">Nowy typ</td>
                        <td class="align-center">Nowy model</td>
                        <td class="align-center">Karta sim</td>
                        <td class="align-center"><g:message code="panel.label.choosen" /></td>
                    </tr>
                </thead>
                <tbody>
                <g:each status="i" var="pe" in="${data.posExchanges}">
                    <tr>
                        <td>${pe.name}
                        <g:hiddenField name="posExchanges[${i}].id" value="${pe.id}" />
                        <g:hiddenField name="posExchanges[${i}].tpsId" value="${pe.tpsId}" />
                        <g:hiddenField name="posExchanges[${i}].cbdId" value="${pe.cbdId}" />
                        <g:hiddenField name="posExchanges[${i}].name" value="${pe.name}" />
                        </td>
                        <td>${pe.address} <g:hiddenField name="posExchanges[${i}].address" value="${pe.address}" /></td>
                        <td class="align-center">${pe.posNumber} <g:hiddenField name="posExchanges[${i}].posNumber" value="${pe.posNumber}" /></td>
                        <td class="align-center">${pe.model} <g:hiddenField id="oldModel_${i}" name="posExchanges[${i}].model" value="${pe.model}" /></td>
                        <td class="align-center">${pe.currentPrice} <g:hiddenField id="posExchanges${i}.currentPrice" name="posExchanges[${i}].currentPrice" value="${pe.currentPrice}" /></td>
                        <td class="align-center"><g:select class="typeSelect" id="selectType_${i}" data-index="${i}" name="posExchanges[${i}].newType" from="['DIALUP','VPN','SSL','GPRS','PINPAD','WiFi']" valueMessagePrefix="panel" value="${pe.newType}" noSelection="['':'']"/></td>
                        <g:if test="${pe.newType}">
                            <td class="align-center"><dict:typeSelect class="selectModel" nip="${data.nip}" medium="${pe.newType}" data-index="${i}" id="selectModel_${i}" name="posExchanges[${i}].newModel" from="[]" valueMessagePrefix="" value="${pe.newModel}" style="width: 120px" /></td>
                        </g:if>
                        <g:else>
                            <td class="align-center"><select class="selectModel" data-index="${i}" id="selectModel_${i}" name="posExchanges[${i}].newModel" style="width: 120px"></select></td>
                        </g:else>
                        <td class="align-center"><dict:simCardSelect name="posExchanges[${i}].simType" id="selectSim_${i}" value="${pe.simType}" style="width: 70px;" disabled="true"/></td>
                        <td class="align-center"><g:checkBox class="acceptModel" data-index="${i}" id="acceptModel_${i}" name="posExchanges[${i}].isChoosen" checked="${pe.isChoosen}"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>


<script type="text/javascript">
    jQuery(function() {
        var count = ${data.posExchanges.size()}
        for (var i=0; i<count; i++){
            if (jQuery('#selectType_'+i).val() == 'GPRS'){
                jQuery('#selectSim_'+i).prop('disabled', false);
            }
        }
    });

    jQuery('.typeSelect').change(typeSelectChanged);
    jQuery('.acceptModel').change(acceptModelChanged);
    jQuery('.selectModel').change(selectModelChanged);

    function typeSelectChanged() {
        var modelSelect = jQuery('#selectModel_' + this.dataset.index),
                simSelect = jQuery('#selectSim_'+this.dataset.index);

        modelSelect.find('option').remove().end();

        jQuery('#acceptModel_'+this.dataset.index).attr('checked', false);

        if (jQuery(this).val() == 'GPRS'){
            simSelect.prop('disabled', false);
        } else {
            simSelect.prop('disabled', 'disabled');
            simSelect.val('');
        }

        jQuery.get("/eumowy/activity/getTerminalModels", {type: this.value}, function(data) {
            modelSelect.append("<option value='0'></option>")
            jQuery.each( data, function( index, value ) {
                modelSelect.append(prepareOptionForModelSelect(value))
            });
        });
    }

    function acceptModelChanged() {
        var calcId = '${data.calcId}';

        if (this.checked) {
            var oldModel = jQuery('#oldModel_'+this.dataset.index).val(),
                newModel = jQuery('#selectModel_'+this.dataset.index).val(),
                withoutCalc = (calcId == -1);

            if (withoutCalc && (isModelEqual('VX51', oldModel) || isModelEqual('VX52', oldModel)) && isModelEqual('IWL220C', newModel)) {
                alert('Dla wybranej wymiany modelowej wymagany jest Kalkulator');
                jQuery(this).attr('checked', false);
            }

            if (isModelEqual('VX670', oldModel) && isModelEqual('IWL220C', newModel)) {
                alert('Dla wybranej wymiany modelowej brak możliwości wymiany');
                jQuery(this).attr('checked', false);
            }
        }

        manageStateOfContinueButton();
    }

    function isModelEqual(expectedModelName, actualModelName) {
        return actualModelName.toUpperCase().indexOf(expectedModelName) != -1
    }

    function selectModelChanged() {
        var posNumber = jQuery("#posExchanges\\[" + this.dataset.index + "\\]\\.posNumber").val(),
                $selectModel = jQuery(this);

        if(isMobileRequireCalculator($selectModel, posNumber) || isPinpadRequireCalculator($selectModel, posNumber)) {
            alert('Dla wybranej wymiany modelowej wymagany jest Kalkulator');
        }

        manageStateOfContinueButton();
    }

    function manageStateOfContinueButton() {
        var continueButton = jQuery("#continueButton");

        if(hasModelRequiredForCalculator() || !hasAtLeastOneModelSelected()) {
            continueButton.attr('disabled', 'disabled');
        } else {
            continueButton.removeAttr('disabled');
        }
    }

    function hasAtLeastOneModelSelected() {
        var acceptModelCheckboxes = jQuery("[id^='acceptModel']"),
                hasOneCheckboxSelected = false;

        for(var i = 0; i < acceptModelCheckboxes.length; i++) {
            if (acceptModelCheckboxes[i].checked) {
                hasOneCheckboxSelected = true;
                break;
            }
        }

        return hasOneCheckboxSelected;
    }

    function hasModelRequiredForCalculator() {
        var hasModelRequiredForCalculator = false,
            $selectModel,
            posNumber;

        jQuery.each(jQuery(".selectModel"), function(index, value) {
            $selectModel = jQuery(value);
            posNumber = jQuery("#posExchanges\\[" + value.dataset.index + "\\]\\.posNumber").val();

            if(isMobileRequireCalculator($selectModel, posNumber) || isPinpadRequireCalculator($selectModel, posNumber)) {
                hasModelRequiredForCalculator = true;
                return false;
            }
        });

        return hasModelRequiredForCalculator;
    }

    function isMobileRequireCalculator($select, posNumber) {
        return isMobileSelected($select) && isCalculatorRequiredForMobile(posNumber);
    }

    function isPinpadRequireCalculator($select, posNumber) {
        return isPinpadSelected($select) && isCalculatorRequiredForPinpad(posNumber);
    }

    function isMobileSelected(select) {
        var selectedOption = select.find(":selected");

        return selectedOption.data('mobile');
    }

    function isPinpadSelected(select) {
        var selectedOption = select.find(":selected");

        return selectedOption.text().indexOf('PINPad') === 0;
    }

    function prepareOptionForModelSelect(data) {
        var isMobile = data["is_mobile"] == 1;

        return "<option value='" + data.value + "' data-mobile='" + isMobile + "'>" + data.value + "</option>";
    }

    function isCalculatorRequiredForMobile(terminalNumber) {
        var terminalsFirstNumbers = ['288', '258', '26', '256', '280', '3'];

        return isCalculatorRequired(terminalsFirstNumbers, terminalNumber);
    }

    function isCalculatorRequiredForPinpad(terminalNumber) {
        var terminalsFirstNumbers = ['288', '258', '26', '256', '280', '27', '287'];

        return isCalculatorRequired(terminalsFirstNumbers, terminalNumber);
    }

    function isCalculatorRequired(numbers, terminalNumber) {
        var calculatorRequired = false,
            numberLength;

        jQuery.each(numbers, function(index, value) {
            numberLength = value.length;
            if(value === terminalNumber.substring(0, numberLength)) {
                calculatorRequired = true;
                return false;
            }
        });

        return calculatorRequired;
    }
</script>
