<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.technicalinformation.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="%ID%technicalinformationSameForEveryPoint"><g:checkBox name="%ID%technicalinformationSameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label>
		<p><g:message code="panel.newpoint.technicalinformation.dayclose" /></p>
		<p><g:message code="panel.from" /> <g:textField name="%ID%zamkniecieDniaOd" style="width: 140px;" id="%ID%dayCloseFrom"/> <g:message code="panel.to" /> <g:textField name="%ID%zamkniecieDniaDo" style="width: 140px;" id="%ID%dayCloseTo"/> <g:message code="panel.newpoint.technicalinformation.plannedinstallationdate" /> <g:textField name="%ID%planowanaDataInstalacji" style="width: 140px;" id="%ID%plannedInstallationDate"/></p>
		<p><g:message code="panel.additionalnotes" /></p>
		<p><g:textField name="%ID%uwagiDodatkowe" id="%ID%additionalNotes"/></p>
	</div>
</fieldset>