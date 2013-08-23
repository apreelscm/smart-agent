<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.additionalequipment.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="%ID%additionalequipmentSameForEveryPoint%ID%"><g:checkBox name="%ID%additionalequipmentSameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label>
		<table class="vertical-center" >
			<tbody>
				<tr><td><g:message code="panel.newpoint.additionalequipment.pinpad" /></td><td>typ <select name="%ID%pinPadType%ID%" style="width: 50px"></select></td><td><g:textField name="%ID%pinPadCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="%ID%pinPadPrice%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.router" /></td><td>typ <select name="%ID%routerType%ID%" style="width: 50px"></select></td><td><g:textField name="%ID%routerCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="%ID%routerPrice%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.cardreader" /></td><td>typ <select name="%ID%cardReaderType%ID%" style="width: 50px"></select></td><td><g:textField name="%ID%cardReaderCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="%ID%cardReaderPrice%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.other" /> <g:textField name="%ID%otherAdditionalDevice" style="width: 150px;" /> <label for="%ID%otherAdditionalDeviceSsl"><g:checkBox name="%ID%otherAdditionalDeviceSsl" /><g:message code="panel.ssl" /></label> <label for="%ID%otherAdditionalDeviceGprs"><g:checkBox name="%ID%otherAdditionalDeviceGprs" /><g:message code="panel.gprs" /></label></td><td>typ <select name="%ID%otherAdditionalDeviceType" style="width: 50px"></select></td><td><g:textField name="%ID%otherAdditionalDeviceCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%otherAdditionalDevicePrice" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
			</tbody>
		</table>
	</div>
</fieldset>