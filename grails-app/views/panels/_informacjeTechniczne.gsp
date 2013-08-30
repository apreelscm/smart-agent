<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.technicalinformation.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="points[${id}].technicalinformationSameForEveryPoint"><g:checkBox name="points[${id}].informacjeTechniczneTakSamoDlaWszystkichPunktow" id="points[${id}].technicalinformationSameForEveryPoint" value="${pointData?.informacjeTechniczneTakSamoDlaWszystkichPunktow}"/><g:message code="panel.sameforeverypoint" /></label>
		<p><g:message code="panel.newpoint.technicalinformation.dayclose" /></p>
		<p><g:message code="panel.from" /> <g:textField name="points[${id}].zamkniecieDniaOd" style="width: 140px;" id="points[${id}].dayCloseFrom" value="${pointData?.zamkniecieDniaOd}"/> <g:message code="panel.to" /> <g:textField name="points[${id}].zamkniecieDniaDo" style="width: 140px;" id="points[${id}].dayCloseTo" value="${pointData?.zamkniecieDniaDo}"/> <g:message code="panel.newpoint.technicalinformation.plannedinstallationdate" /> <g:textField name="points[${id}].planowanaDataInstalacji" style="width: 140px;" id="points[${id}].plannedInstallationDate" value="${pointData?.planowanaDataInstalacji}"/></p>
		<p><g:message code="panel.additionalnotes" /></p>
		<p><g:textField name="points[${id}].uwagiDodatkowe" id="points[${id}].additionalNotes" value="${pointData?.uwagiDodatkowe}"/></p>
	</div>
</fieldset>