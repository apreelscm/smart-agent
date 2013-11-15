<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.technicalinformation.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].technicalinformationSameForEveryPoint">
            <g:checkBox name="${panelType}[${id}].informacjeTechniczneTakSamoDlaWszystkichPunktow" id="${panelType}[${id}].technicalinformationSameForEveryPoint" value="${pointData?.informacjeTechniczneTakSamoDlaWszystkichPunktow}"/><g:message code="panel.sameforeverypoint" />
        </label>
		<p>
            <g:message code="panel.newpoint.technicalinformation.dayclose" />
        </p>
		<p>
            <g:message code="panel.from" />
            <g:select name='${panelType}[${id}].timeFromHours' from="${00..24}" class="select-without-arrow time-picker timeFromHours" required="true" noSelection="${['':'']}"/>:
            <g:select name='${panelType}[${id}].timeFromMinutes' from="${00..59}" class="select-without-arrow time-picker timeFromMinutes" required="true" noSelection="${['':'']}"/>
            <g:hiddenField name="${panelType}[${id}].zamkniecieDniaOd" id="${panelType}[${id}].dayCloseFrom" value="${pointData?.zamkniecieDniaOd}"/>

            <g:message code="panel.to" />
            <g:select name='${panelType}[${id}].timeToHours' from="${00..24}" class="select-without-arrow time-picker timeToHours" required="true" noSelection="${['':'']}"/>:
            <g:select name='${panelType}[${id}].timeToMinutes' from="${00..59}" class="select-without-arrow time-picker timeToMinutes" required="true" noSelection="${['':'']}"/>
            <g:hiddenField name="${panelType}[${id}].zamkniecieDniaDo" id="${panelType}[${id}].dayCloseTo" value="${pointData?.zamkniecieDniaDo}"/>

            <g:message code="panel.newpoint.technicalinformation.plannedinstallationdate" />
            <g:textField name="${panelType}[${id}].planowanaDataInstalacji" style="width: 140px;" id="${panelType}[${id}].plannedInstallationDate" value="${formatDate(format:'yyyy-MM-dd',date:pointData?.planowanaDataInstalacji)}"/>
        </p>
		<p><g:message code="panel.additionalnotes" /></p>
		<p><g:textField name="${panelType}[${id}].uwagiDodatkowe" id="${panelType}[${id}].additionalNotes" value="${pointData?.uwagiDodatkowe}" maxlength="2000"/></p>
	</div>
</fieldset>