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
                            <g:set var="hasNewUmowaAndPrepaid" value="${processInstance?.activities?.any{it.code.equals('nowaUmowa')} && processInstance?.activities?.any{it.code.equals('dodaniePrepaid')}}"/>
                            <div>
                                <label for="${panelType}[${id}].telePompka"><g:checkBox name="${panelType}[${id}].telePompka" value="${pointData?.telePompka}"  disabled="${!hasNewUmowaAndPrepaid}"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka" /></label>
                            </div>
                            <div>
                                <label for="${panelType}[${id}].teleKodzik"><g:checkBox name="${panelType}[${id}].teleKodzik" value="${pointData?.teleKodzik}"  disabled="${!hasNewUmowaAndPrepaid}"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik" /></label>
                            </div>
                        </span>
                        <span style="padding-left: 6em;display: inline-block">
                            <div><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.terminalcount" /></div>
                            <!--<eumowy:numberField name="${panelType}[${id}].terminalIlosc" id="${panelType}[${id}].terminalCount" validatable="${data}"
                                                class="${hasErrors(bean:data,field:'liczbaTerminali','error')}"
                                                value="${pointData?.terminalIlosc}" style="width: 50px;"
                                                errorMessage="${message(code:'default.tooMuch.liczbaTerminali')}"/>-->
                            %{--<eumowy:numberField name="${panelType}[${id}].terminalIlosc" id="${panelType}[${id}].terminalCount" value="${pointData?.terminalIlosc}" style="width: 50px;" />--}%
                            <eumowy:textField name="${panelType}[${id}].terminalIlosc" id="${panelType}[${id}].terminalCount" class="integer-number" value="${pointData?.terminalIlosc}" style="width: 50px;" />
                        </span>
                    </li>
                </ul>
                <div>
                    <p class="bold" ><g:message code="panel.newpoint.terminaloptions.marketingproducts" /></p>
                    <p><label for="${panelType}[${id}].giftCard"><g:checkBox name="${panelType}[${id}].kartaPodarunkowa" id="${panelType}[${id}].giftCard" value="${pointData?.kartaPodarunkowa}"/><g:message code="panel.newpoint.terminaloptions.marketingproducts.giftcard" /></label></p>
                </div>
            </div>

        </div>
    </div>
</fieldset>