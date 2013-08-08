<div class="subpanel">
	<h4><g:message code="panel.newpoint.additionalequipment.title" /></h4>
	<div>
		<label for="additionalequipment-sameForEveryPoint%ID%"><g:checkBox name="additionalequipment-sameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label>
		<table class="vertical-center" >
			<tbody>
				<tr><td><g:message code="panel.newpoint.additionalequipment.pinpad" /></td><td>typ <select style="width: 50px"></select></td><td><g:textField name="pinPadCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.router" /></td><td>typ <select style="width: 50px"></select></td><td><g:textField name="routerCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.cardreader" /></td><td>typ <select style="width: 50px"></select></td><td><g:textField name="cardReaderCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
				<tr><td><g:message code="panel.newpoint.additionalequipment.other" /> <g:textField name="otherAdditionalDevice%ID%" style="width: 150px;" /> <label for="otherAdditionalDeviceSsl%ID%"><g:checkBox name="otherAdditionalDeviceSsl%ID%" /><g:message code="panel.ssl" /></label> <label for="otherAdditionalDeviceGprs%ID%"><g:checkBox name="otherAdditionalDeviceGprs%ID%" /><g:message code="panel.gprs" /></label></td><td>typ <select name="otherAdditionalDeviceType%ID%" style="width: 50px"></select></td><td><g:textField name="otherAdditionalDeviceCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="otherAdditionalDevicePrice%ID%" style="width: 50px"/> <g:message code="panel.price" /></td></tr>
			</tbody>
		</table>
	</div>
</div>