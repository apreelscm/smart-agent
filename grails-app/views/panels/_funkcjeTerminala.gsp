<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.terminaloptions.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<p><label for="points[${id}].terminaloptionsSameForEveryPoint"><g:checkBox name="points[${id}].funkcjeTerminalaTakSamoDlaWszystkichPunktow" id="points[${id}].terminaloptionsSameForEveryPoint" value="${pointData?.funkcjeTerminalaTakSamoDlaWszystkichPunktow}"/><g:message code="panel.sameforeverypoint" /></label></p>
		
		<div style="float: left; padding-right: 2em;" >
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.paymentoptions" /></p>
			<ul class="table-list vertical-center">
				<li><label for="points[${id}].preauthorization"><g:checkBox name="points[${id}].preautoryzacja" id="points[${id}].preauthorization" value="${pointData?.preautoryzacja}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.preauthorization" /></label></li>
				<li><label for="points[${id}].noreturnfunction"><g:checkBox name="points[${id}].brakFunkcjiZwrotu" id="points[${id}].noreturnfunction" value="${pointData?.brakFunkcjiZwrotu}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.noreturnfunction" /></label></li>
				<li><label for="points[${id}].returnWithPassword"><g:checkBox name="points[${id}].zwrotNaHaslo" id="points[${id}].returnWithPassword" value="${pointData?.zwrotNaHaslo}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.returnwithpassword" /></label></li>
				<li><label for="points[${id}].setAnalysis"><g:checkBox name="points[${id}].analizaZbioru" id="points[${id}].setAnalysis" value="${pointData?.analizaZbioru}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.setanalysis" /></label></li>
				<li><label for="points[${id}].cashMachineSystemIntegration"><g:checkBox name="points[${id}].integracjaZSysKas" id="points[${id}].cashMachineSystemIntegration" value="${pointData?.integracjaZSysKas}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.cashmachinesystemintegration" /></label></li>
				<li><label for="points[${id}].returnIKO"><g:checkBox name="points[${id}].zwrotyIKO" id="points[${id}].returnIKO" value="${pointData?.zwrotyIKO}"/><g:message code="panel.newpoint.terminaloptions.paymentoptions.returniko" /></label></li>
			</ul>
		</div>
		<div style="float: left;  padding-right: 2em; height: 200px;" >
			<div>
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.cashierlogging" /></p>
			<ul class="table-list vertical-center">
				<li><label for="points[${id}].loggingBeforeEveryTransaction"><g:checkBox name="points[${id}].logowaniePrzedKazdaTransakcja" id="points[${id}].loggingBeforeEveryTransaction" value="${pointData?.logowaniePrzedKazdaTransakcja}"/><g:message code="panel.newpoint.terminaloptions.cashierlogging.beforeeverytransaction" /></label></li>
				<li><label for="points[${id}].logginEveryChange"><g:checkBox name="points[${id}].logowanieZmianowe" id="points[${id}].logginEveryChange" value="${pointData?.logowanieZmianowe}"/><g:message code="panel.newpoint.terminaloptions.cashierlogging.everychange" /></label></li>
			</ul>
			</div>
			<div>
				<p class="bold" ><g:message code="panel.newpoint.terminaloptions.tipssupport" /></p>
				<p><label for="points[${id}].tip1"><g:checkBox name="points[${id}].napiwek1" id="points[${id}].tip1" value="${pointData?.napiwek1}"/><g:message code="panel.newpoint.terminaloptions.tipssupport.tip1" /></label></p>
			</div>
		</div>
		<div>
			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge" /></p>
 			<ul class="table-list vertical-center">
 				<li><span><label for="points[${id}].telePompka"><g:checkBox name="points[${id}].telePompka" value="${pointData?.telePompka}"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka" /></label></span> <span style="padding-left: 6em;"><label><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.terminalcount" /> <g:textField name="points[${id}].terminalIlosc" id="points[${id}].terminalCount" value="${pointData?.terminalIlosc}" style="width: 50px;"/></label></span></li>
 				<li><label for="points[${id}].teleKodzik"><g:checkBox name="points[${id}].teleKodzik" value="${pointData?.teleKodzik}"/><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik" /></label></li>
 			</ul>
 			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.marketingproducts" /></p>
				<p><label for="points[${id}].giftCard"><g:checkBox name="points[${id}].kartaPodarunkowa" id="points[${id}].giftCard" value="${pointData?.kartaPodarunkowa}"/><g:message code="panel.newpoint.terminaloptions.marketingproducts.giftcard" /></label></p>
				</div>
			</div>
			
		</div>
	</div>
</fieldset>