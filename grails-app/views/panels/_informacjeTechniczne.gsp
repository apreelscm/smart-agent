<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.technicalinformation.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].technicalinformationSameForEveryPoint"><g:checkBox name="${panelType}[${id}].informacjeTechniczneTakSamoDlaWszystkichPunktow" id="${panelType}[${id}].technicalinformationSameForEveryPoint" value="${pointData?.informacjeTechniczneTakSamoDlaWszystkichPunktow}"/><g:message code="panel.sameforeverypoint" /></label>
		<p><g:message code="panel.newpoint.technicalinformation.dayclose" /></p>
		<p><g:message code="panel.from" /> <eumowy:textField name="${panelType}[${id}].zamkniecieDniaOd" style="width: 140px;" id="${panelType}[${id}].dayCloseFrom" value="${pointData?.zamkniecieDniaOd}" required="true"/> <g:message code="panel.to" /> <eumowy:textField name="${panelType}[${id}].zamkniecieDniaDo" style="width: 140px;" id="${panelType}[${id}].dayCloseTo" value="${pointData?.zamkniecieDniaDo}" required="true"/> <g:message code="panel.newpoint.technicalinformation.plannedinstallationdate" /> <g:textField name="${panelType}[${id}].planowanaDataInstalacji" style="width: 140px;" id="${panelType}[${id}].plannedInstallationDate" value="${formatDate(format:'yyyy-MM-dd',date:pointData?.planowanaDataInstalacji)}"/></p>
		<p><g:message code="panel.additionalnotes" /></p>
		<p><g:textField name="${panelType}[${id}].uwagiDodatkowe" id="${panelType}[${id}].additionalNotes" value="${pointData?.uwagiDodatkowe}" maxlength="2000"/></p>
	</div>
</fieldset>