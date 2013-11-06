<fieldset class="subpanel-fieldset">
    <legend><g:message code="panel.newpoint.terminaloptions.title" /></legend>
    <div class="subpanel-fieldset-centercontent" >
        <p><label for="${panelType}[${id}].terminaloptionsSameForEveryPoint"><g:checkBox name="${panelType}[${id}].funkcjeTerminalaTakSamoDlaWszystkichPunktow" id="${panelType}[${id}].terminaloptionsSameForEveryPoint" value="${pointData?.funkcjeTerminalaTakSamoDlaWszystkichPunktow}"/><g:message code="panel.sameforeverypoint" /></label></p>

        <div style="float: left; padding-right: 2em;" >
            <p class="bold" ><g:message code="panel.newpoint.terminaloptions.paymentoptions" /></p>
            <ul class="table-list vertical-center">
                <li><label for="${panelType}[${id}].preauthorization"><g:checkBox name="${panelType}[${id}].preautoryzacja" id="${panelType}[${id}].preauthorization" value="${pointData?.preautoryzacja}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.preauthorization" /></label></li>
                <li><label for="${panelType}[${id}].noreturnfunction"><g:checkBox name="${panelType}[${id}].brakFunkcjiZwrotu" id="${panelType}[${id}].noreturnfunction" value="${pointData?.brakFunkcjiZwrotu}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.noreturnfunction" /></label></li>
                <li><label for="${panelType}[${id}].returnWithPassword"><g:checkBox name="${panelType}[${id}].zwrotNaHaslo" id="${panelType}[${id}].returnWithPassword" value="${pointData?.zwrotNaHaslo}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.returnwithpassword" /></label></li>
                <li><label for="${panelType}[${id}].setAnalysis"><g:checkBox name="${panelType}[${id}].analizaZbioru" id="${panelType}[${id}].setAnalysis" value="${pointData?.analizaZbioru}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.setanalysis" /></label></li>
                <li><label for="${panelType}[${id}].cashMachineSystemIntegration"><g:checkBox name="${panelType}[${id}].integracjaZSysKas" id="${panelType}[${id}].cashMachineSystemIntegration" value="${pointData?.integracjaZSysKas}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.cashmachinesystemintegration" /></label></li>
                <li><label for="${panelType}[${id}].returnIKO"><g:checkBox name="${panelType}[${id}].zwrotyIKO" id="${panelType}[${id}].returnIKO" value="${pointData?.zwrotyIKO}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.returniko" /></label></li>
            </ul>
        </div>
        <div style="float: left;  padding-right: 2em; height: 200px;" >
            <div>
                <p class="bold" ><g:message code="panel.newpoint.terminaloptions.cashierlogging" /></p>
                <ul class="table-list vertical-center">
                    <li><label for="${panelType}[${id}].loggingBeforeEveryTransaction"><g:checkBox name="${panelType}[${id}].logowaniePrzedKazdaTransakcja" id="${panelType}[${id}].loggingBeforeEveryTransaction" value="${pointData?.logowaniePrzedKazdaTransakcja}"/><g:message code="panel.newpoint.terminaloptions.cashierlogging.beforeeverytransaction" /></label></li>
                    <li><label for="${panelType}[${id}].logginEveryChange"><g:checkBox name="${panelType}[${id}].logowanieZmianowe" id="${panelType}[${id}].logginEveryChange" value="${pointData?.logowanieZmianowe}"/><g:message code="panel.newpoint.terminaloptions.cashierlogging.everychange" /></label></li>
                </ul>
            </div>
            <div>
                <p class="bold" ><g:message code="panel.newpoint.terminaloptions.tipssupport" /></p>
                <p><label for="${panelType}[${id}].tip1"><g:checkBox name="${panelType}[${id}].napiwek1" id="${panelType}[${id}].tip1" value="${pointData?.napiwek1}"/><g:message code="panel.newpoint.terminaloptions.tipssupport.tip1" /></label></p>
            </div>
        </div>
        <div>
            <div>
                <p class="bold" ><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge" /></p>
                <ul class="table-list vertical-center">
                    <li>
                        <span>

                            <g:set var="hasNewUmowaAndPrepaid" value="${data.hasNewUmowaAndPrepaid}"/>
                            <g:set var="isRozszerzenie" value="${data.isRozszerzenie}"/>
                            <g:set var="tpFromCalcEnabled" value="${data.isDoladowania_tp}"/>
                            <g:set var="tkFromCalcEnabled" value="${data.isDoladowania_tk}"/>
                            <g:set var="hasPrepaid" value="${data.hasPrepaid}"/>
                            <g:set var="tpEnabled" value="${hasNewUmowaAndPrepaid || tpFromCalcEnabled || (tpFromCalcEnabled && isRozszerzenie && hasPrepaid)}"/>
                            <g:set var="tkEnabled" value="${hasNewUmowaAndPrepaid || tkFromCalcEnabled || (tkFromCalcEnabled && isRozszerzenie && hasPrepaid)}"/>

                            <div>
                                <label for="${panelType}[${id}].teleKodzik" class="doladowanieLabel"><g:checkBox name="${panelType}[${id}].teleKodzik" value="${pointData?.teleKodzik}"  disabled="${!tkEnabled}" class="doladowanie" data-doladowanie="telekodzik"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik" /></label>
                            </div>
                            <div>
                                <label for="${panelType}[${id}].telePompka" class="doladowanieLabel"><g:checkBox name="${panelType}[${id}].telePompka" value="${pointData?.telePompka}"  disabled="${!tpEnabled}" class="doladowanie" data-doladowanie="telepompka"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka" /></label>
                            </div>
                        </span>
                        <span style="padding-left: 6em;display: inline-block">
                            <div><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.terminalcount" /></div>
                            <eumowy:textField name="${panelType}[${id}].terminalIlosc"
                            					class="integer-number"
                                                id="${panelType}[${id}].terminalCount"
                                                value="${pointData?.terminalIlosc}"
                                                validatable="${pointData}"
                                                validateField="terminalIlosc" disabled="${!tkEnabled && !tpEnabled}"
                                                style="width: 50px;"  />

                        </span>
                    </li>
                </ul>
                <div>
                    <p class="bold" ><g:message code="panel.newpoint.terminaloptions.marketingproducts" /></p>
                    <g:hiddenField name="czyGift" value="${data.czyGift}"/>
                    <p><label for="${panelType}[${id}].giftCard"><g:checkBox disabled="${!(data.czyGift && data.isRozszerzenie)}" name="${panelType}[${id}].kartaPodarunkowa" id="${panelType}[${id}].giftCard" value="${pointData?.kartaPodarunkowa}"/><g:message code="panel.newpoint.terminaloptions.marketingproducts.giftcard" /></label></p>
                </div>
            </div>

        </div>
    </div>
</fieldset>