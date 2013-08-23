<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.terminaloptions.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<p><label for="%ID%terminaloptionsSameForEveryPoint"><g:checkBox name="%ID%terminaloptionsSameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label></p>
		
		<div style="float: left; padding-right: 2em;" >
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.paymentoptions" /></p>
			<ul class="table-list vertical-center">
				<li><label for="%ID%preauthorization"><g:checkBox name="%ID%preauthorization" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.preauthorization" /></label></li>
				<li><label for="%ID%noreturnfunction"><g:checkBox name="%ID%noreturnfunction" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.noreturnfunction" /></label></li>
				<li><label for="%ID%returnWithPassword"><g:checkBox name="%ID%returnWithPassword" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.returnwithpassword" /></label></li>
				<li><label for="%ID%setAnalysis"><g:checkBox name="%ID%setAnalysis" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.setanalysis" /></label></li>
				<li><label for="%ID%cashMachineSystemIntegration"><g:checkBox name="%ID%cashMachineSystemIntegration" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.cashmachinesystemintegration" /></label></li>
				<li><label for="%ID%returnIKO"><g:checkBox name="%ID%returnIKO" /><g:message code="panel.newpoint.terminaloptions.paymentoptions.returniko" /></label></li>
			</ul>
		</div>
		<div style="float: left;  padding-right: 2em; height: 200px;" >
			<div>
			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.cashierlogging" /></p>
			<ul class="table-list vertical-center">
				<li><label for="%ID%loggingBeforeEveryTransaction"><g:checkBox name="%ID%loggingBeforeEveryTransaction" /><g:message code="panel.newpoint.terminaloptions.cashierlogging.beforeeverytransaction" /></label></li>
				<li><label for="%ID%logginEveryChange"><g:checkBox name="%ID%logginEveryChange" /><g:message code="panel.newpoint.terminaloptions.cashierlogging.everychange" /></label></li>
			</ul>
			</div>
			<div>
				<p class="bold" ><g:message code="panel.newpoint.terminaloptions.tipssupport" /></p>
				<p><label for="%ID%tip1"><g:checkBox name="%ID%tip1" /><g:message code="panel.newpoint.terminaloptions.tipssupport.tip1" /></label></p>
			</div>
		</div>
		<div>
			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge" /></p>
 			<ul class="table-list vertical-center">
 				<li><span><label for="%ID%telePompka"><g:checkBox name="%ID%telePompka" /><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka" /></label></span> <span style="padding-left: 6em;"><label><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.terminalcount" /> <g:textField name="%ID%terminalCount" style="width: 50px;"/></label></span></li>
 				<li><label for="%ID%teleKodzik"><g:checkBox name="%ID%teleKodzik" /><g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik" /></label></li>
 			</ul>
 			<div>
 			<p class="bold" ><g:message code="panel.newpoint.terminaloptions.marketingproducts" /></p>
				<p><label for="%ID%giftCard"><g:checkBox name="%ID%giftCard" /><g:message code="panel.newpoint.terminaloptions.marketingproducts.giftcard" /></label></p>
				</div>
			</div>
			
		</div>
	</div>
</fieldset>