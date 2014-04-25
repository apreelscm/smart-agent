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
                            <td class="align-center"><dict:typeSelect nip="${data.nip}" medium="${pe.newType}" data-index="${i}" id="selectModel_${i}" name="posExchanges[${i}].newModel" from="[]" valueMessagePrefix="" value="${pe.newModel}" style="width: 120px" /></td>
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


<r:require module="jquery_ui"/>

<script type="text/javascript">
    //aktywacja selectow sima dla GPRS
    jQuery(function() {
        var count = ${data.posExchanges.size()}
        for (var i=0; i<count; i++){
            if (jQuery('#selectType_'+i).val() == 'GPRS'){
                jQuery('#selectSim_'+i).prop('disabled', false);
            }
        }
    });

    jQuery('.typeSelect').on('change', function() {
        var modelSelect = jQuery('#selectModel_'+this.dataset.index);
        //usuniecie obecnych opcji
        modelSelect.find('option').remove().end();
        //odznaczenie checkboxa
        jQuery('#acceptModel_'+this.dataset.index).attr('checked', false);

        //aktywacja sima
        var simSelect = jQuery('#selectSim_'+this.dataset.index);
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
    });

    jQuery('.acceptModel').change(function(){
        var calcId = ${data.calcId};
        if (this.checked){
            var oldModel = jQuery('#oldModel_'+this.dataset.index).val(),
                newModel = jQuery('#selectModel_'+this.dataset.index).val();

            if (calcId ==-1 && (oldModel.toUpperCase().indexOf('VX51') != -1 || oldModel.toUpperCase().indexOf('VX52') != -1) && newModel.toUpperCase().indexOf('IWL220C') != -1){
                alert('Dla wybranej wymiany modelowej wymagany jest Kalkulator');
                jQuery(this).attr('checked', false);
            }

            if (oldModel.toUpperCase().indexOf('VX670') != -1 && newModel.toUpperCase().indexOf('IWL220C') != -1){
                alert('Dla wybranej wymiany modelowej brak możliwości wymiany');
                jQuery(this).attr('checked', false);
            }
        }
    });

    jQuery('.selectModel').change(function() {
        var select = jQuery(this),
            selectedOption = select.find(":selected"),
            isSelectedOptionMobile = selectedOption.data('mobile'),
            isPinpadSelected = selectedOption.text().indexOf('PINPad') === 0,
            posNumber = jQuery("#posExchanges\\[" + this.dataset.index + "\\]\\.posNumber").val();

        if(isSelectedOptionMobile && isCalculatorRequiredForMobile(posNumber)) {
            alert('Dla wybranej wymiany modelowej wymagany jest Kalkulator');
            return;
        }

        if(isPinpadSelected && isCalculatorRequiredForPinpad(posNumber)) {
            alert('Dla wybranej wymiany modelowej wymagany jest Kalkulator');
        }

    });

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
