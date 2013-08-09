<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.terminaloptions.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<p><label for="terminaloptions-sameForEveryPoint%ID%"><g:checkBox name="terminaloptions-sameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label></p>
		
		<div style="float: left; padding-right: 2em;" >
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.paymentoptions" /></p>
			<ul class="table-list vertical-center">
				<li><label for="preauthorization%ID%"><g:checkBox name="preauthorization%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.preauthorization" /></label></li>
				<li><label for="noreturnfunction%ID%"><g:checkBox name="noreturnfunction%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.noreturnfunction" /></label></li>
				<li><label for="returnWithPassword%ID%"><g:checkBox name="returnWithPassword%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.returnwithpassword" /></label></li>
				<li><label for="setAnalysis%ID%"><g:checkBox name="setAnalysis%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.setanalysis" /></label></li>
				<li><label for="cashMachineSystemIntegration%ID%"><g:checkBox name="cashMachineSystemIntegration%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.cashmachinesystemintegration" /></label></li>
				<li><label for="returnIKO%ID%"><g:checkBox name="returnIKO%ID%" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.returniko" /></label></li>
			</ul>
		</div>
		<div style="float: left;  padding-right: 2em; height: 200px;" >
			<div>
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.cashierlogging" /></p>
			<ul class="table-list vertical-center">
				<li><label for="loggingBeforeEveryTransaction%ID%"><g:checkBox name="loggingBeforeEveryTransaction%ID%" /><g:message code="panel.newpoint.terminaloptions.cashierlogging.beforeeverytransaction" /></label></li>
				<li><label for="logginEveryChange%ID%"><g:checkBox name="logginEveryChange%ID%" /><g:message code="panel.newpoint.terminaloptions.cashierlogging.everychange" /></label></li>
			</ul>
			</div>
			<div>
				<p class="bold" ><g:message code="panel.newpoint.terminaloptions.tipssupport" /></p>
				<p><label for="tip1%ID%"><g:checkBox name="tip1%ID%" /><g:message code="panel.newpoint.terminaloptions.tipssupport.tip1" /></label></p>
			</div>
		</div>
		<div>
			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge" /></p>
 			<ul class="table-list vertical-center">
 				<li><span><label for="telePompka%ID%"><g:checkBox name="telePompka%ID%" /><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka" /></label></span> <span style="padding-left: 6em;"><label><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.terminalcount" /> <g:textField name="terminalCount%ID%" style="width: 50px;"/></label></span></li>
 				<li><label for="teleKodzik%ID%"><g:checkBox name="teleKodzik%ID%" /><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik" /></label></li>
 			</ul>
 			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.marketingproducts" /></p>
				<p><label for="giftCard%ID%"><g:checkBox name="giftCard%ID%" /><g:message code="panel.newpoint.terminaloptions.marketingproducts.giftcard" /></label></p>
				</div>
			</div>
			
		</div>
	</div>
</fieldset>