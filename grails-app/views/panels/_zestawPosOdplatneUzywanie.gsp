<div id="posUsagePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.pos.usage.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
            <div style="text-align: left">
                <g:radioGroup name="odplatneUzywanie"
                              labels="['panel.usage.one.for.all.terminals','panel.usage.one.for.all.terminals.in.point', 'panel.usage.other.for.selected.terminals']"
                              values="['one_for_all_terminals', 'one_for_all_terminals_in_point', 'other_for_selected_terminals']"
                              value="${data.odplatneUzywanie}">
                    <p><label> ${it.radio} <g:message code="${it.label}"/></label></p>
                </g:radioGroup>
            </div>
        </div>
        <g:hiddenField name="isOdplatneUzywanieShown" value="tak"/>

        <div style="padding-top: 20px;">Liczba i cena aktywnych terminali u klienta</div>
        <div style="text-align: center; width: 950px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span>Liczba terminali</span>
                    <span>Cena terminali</span>
                    <span>Cena za pinpad</span>
                </li>
                <g:each status="i" var="hp" in="${data.hirePaymentsCurrent}">
                    <li>
                        <span><g:field name="hirePaymentsCurrent[${i}].termCount" type="text" class="integer-number" value="${hp.termCount}" style="width: 70px" readonly="true"/></span>
                        <span><g:field name="hirePaymentsCurrent[${i}].currentTermPayment" type="text" class="float-number" value="${hp.currentTermPayment}" style="width: 70px" readonly="true"/></span>
                        <span><g:field name="hirePaymentsCurrent[${i}].currentPpPayment" type="text" class="float-number" value="${hp.currentPpPayment}" style="width: 70px" readonly="true"/></span>
                    </li>
                </g:each>
            </ul>
        </div>

        <div id='one_for_all_terminals' style="text-align: center; width: 950px" class="centre">
            <table class="t">
                <thead>
                    <tr>
                        <td class="align-center">Term. Szt.</td>
                        <td class="align-center">PP. Szt.</td>
                        <td class="align-center">Odpłatne używanie term./mies.</td>
                        <td class="align-center">Odpłatne używanie PP/mies.</td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="align-center"><g:field name="odpUzyTermSzt" type="text" class="integer-number" value="${data.odpUzyTermSzt}" readonly="true"/></td>
                        <td class="align-center"><g:field name="odpUzyPpSzt" type="text" class="integer-number" value="${data.odpUzyPpSzt}" readonly="true"/></td>
                        <td class="align-center"><eumowy:textField name="odpUzyTermMies" type="text" class="float-number" value="${data.odpUzyTermMies}" validatable="${data}"/></td>
                        <td class="align-center"><g:field name="odpUzyPpMies" type="text" class="float-number" value="${data.odpUzyPpMies}" readonly="true"/></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div id='one_for_all_terminals_in_point' style="text-align: center; width: 950px" class="centre">
            <table class="t">
                <thead>
                <tr>
                    <td class="align-center">Pełna nazwa punktu</td>
                    <td class="align-center">Adres punktu</td>
                    <td class="align-center">Typ</td>
                    <td class="align-center">Term. Szt.</td>
                    <td class="align-center">PP. Szt.</td>
                    <td class="align-center">Opłata obowiązująca /mies.</td>
                    <td class="align-center">Opłata obowiązująca PP./mies.</td>
                    <td class="align-center">Nowa opłata /mies.</td>
                    <td class="align-center">Nowa opłata PP./mies.</td>
                    <td class="align-center"><g:message code="panel.label.choosen" /></td>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" var="hp" in="${data.hirePaymentsByPoint}">
                    <tr>
                        <td>${hp.name}
                            <g:hiddenField name="hirePaymentsByPoint[${i}].cbdId" value="${hp.cbdId}" />
                            <g:hiddenField name="hirePaymentsByPoint[${i}].name" value="${hp.name}" />
                        </td>
                        <td>${hp.address} <g:hiddenField name="hirePaymentsByPoint[${i}].address" value="${hp.address}" /></td>
                        <td class="align-center">${hp.type} <g:hiddenField name="hirePaymentsByPoint[${i}].type" value="${hp.type}" /></td>
                        <td class="align-center"><g:field name="hirePaymentsByPoint[${i}].termCount" type="text" class="float-number" value="${hp.termCount}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPoint[${i}].ppCount" type="text" class="float-number" value="${hp.ppCount}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPoint[${i}].currentTermPayment" type="text" class="float-number" value="${hp.currentTermPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPoint[${i}].currentPpPayment" type="text" class="float-number" value="${hp.currentPpPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><eumowy:textField name="hirePaymentsByPoint[${i}].newTermPayment" style="width: 40px" value="${hp.newTermPayment}" class="float-number" validatable="${hp}" validateField="newTermPayment"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPoint[${i}].newPpPayment" type="text" class="float-number" value="${hp.newPpPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:checkBox name="hirePaymentsByPoint[${i}].isChoosen" checked="${hp.isChoosen}"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
        <div id='other_for_selected_terminals' style="text-align: center; width: 950px" class="centre">
            <table class="t">
                <thead>
                    <tr>
                        <td class="align-center">Pełna nazwa punktu</td>
                        <td class="align-center">Adres punktu</td>
                        <td class="align-center">Numer terminala</td>
                        <td class="align-center">Typ</td>
                        <td class="align-center">Term. Szt.</td>
                        <td class="align-center">PP. Szt.</td>
                        <td class="align-center">Opłata obowiązująca /mies.</td>
                        <td class="align-center">Opłata obowiązująca PP./mies.</td>
                        <td class="align-center">Nowa opłata /mies.</td>
                        <td class="align-center">Nowa opłata PP./mies.</td>
                        <td class="align-center"><g:message code="panel.label.choosen" /></td>
                    </tr>
                </thead>
                <tbody>
                <g:each status="i" var="hp" in="${data.hirePaymentsByPos}">
                    <tr>
                        <td>${hp.name}
                            <g:hiddenField name="hirePaymentsByPos[${i}].tpsId" value="${hp.tpsId}" />
                            <g:hiddenField name="hirePaymentsByPos[${i}].cbdId" value="${hp.cbdId}" />
                            <g:hiddenField name="hirePaymentsByPos[${i}].name" value="${hp.name}" />
                        </td>
                        <td>${hp.address} <g:hiddenField name="hirePaymentsByPos[${i}].address" value="${hp.address}" /></td>
                        <td class="align-center">${hp.posNumber} <g:hiddenField name="hirePaymentsByPos[${i}].posNumber" value="${hp.posNumber}" /></td>
                        <td class="align-center">${hp.type} <g:hiddenField name="hirePaymentsByPos[${i}].type" value="${hp.type}" /></td>
                        <td class="align-center"><g:field name="hirePaymentsByPos[${i}].termCount" type="text" class="integer-number" value="${hp.termCount}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPos[${i}].ppCount" type="text" class="integer-number" value="${hp.ppCount}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPos[${i}].currentTermPayment" type="text" class="integer-number" value="${hp.currentTermPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPos[${i}].currentPpPayment" type="text" class="integer-number" value="${hp.currentPpPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><eumowy:textField name="hirePaymentsByPos[${i}].newTermPayment" style="width: 40px" value="${hp.newTermPayment}" class="float-number" validatable="${hp}" validateField="newTermPayment"/></td>
                        <td class="align-center"><g:field name="hirePaymentsByPos[${i}].newPpPayment" type="text" class="float-number" value="${hp.newPpPayment}" style="width: 40px" readonly="true"/></td>
                        <td class="align-center"><g:checkBox name="hirePaymentsByPos[${i}].isChoosen" checked="${hp.isChoosen}"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {

        var hirePaymentsByPointSize = ${data.hirePaymentsByPoint.size()};
        var hirePaymentsByPosSize = ${data.hirePaymentsByPos.size()};

        selectUsageOption(jQuery('input[name="odplatneUzywanie"]:checked').val());
        jQuery('input[name="odplatneUzywanie"]').change(function(e){
            selectUsageOption(e.target.value);
            cleanValues(e.target.value);
        });

        function selectUsageOption(selectedValue){
            var options = ['one_for_all_terminals', 'one_for_all_terminals_in_point', 'other_for_selected_terminals'];
            jQuery('#'+selectedValue).show();
            var xxx = options.filter(function(i) {
                return i != selectedValue
            });

            for (var i = 0; i < xxx.length; i++) {
                jQuery('#'+xxx[i]).hide();
            }
        }

        function cleanValues(selectedValue){
            if (selectedValue == 'one_for_all_terminals'){
                jQuery('#odpUzyTermMies').val('${data.odpUzyTermMies}');
                clean(hirePaymentsByPointSize, "hirePaymentsByPoint");
                clean(hirePaymentsByPosSize, "hirePaymentsByPos");
            } else if (selectedValue == 'one_for_all_terminals_in_point') {
                setFields(hirePaymentsByPointSize, "hirePaymentsByPoint", "${data.odpUzyTermCalc}")
                jQuery('#odpUzyTermMies').val("");
                clean(hirePaymentsByPosSize, "hirePaymentsByPos");
            } else if (selectedValue == 'other_for_selected_terminals') {
                setFields(hirePaymentsByPosSize, "hirePaymentsByPos", "${data.odpUzyTermCalc}")
                jQuery('#odpUzyTermMies').val("");
                clean(hirePaymentsByPointSize, "hirePaymentsByPoint");
            }
        }

        function clean(size, name){
            for (var i=0; i < size; i++){
                jQuery('#'+name+'\\['+ i +'\\]\\.newTermPayment').val("");
                jQuery('#'+name+'\\['+ i +'\\]\\.isChoosen').attr('checked', false);
            }
        }

        function setFields(size, name, value){
            for (var i=0; i < size; i++){
                jQuery('#'+name+'\\['+ i +'\\]\\.newTermPayment').val(value);
            }
        }

    });
</r:script>