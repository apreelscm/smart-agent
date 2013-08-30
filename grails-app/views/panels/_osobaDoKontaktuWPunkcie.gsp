<fieldset class="subpanel-fieldset">
   	<legend><g:message code="panel.newpoint.persontocontact.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="points[${id}].persontocontactAsForMerchant"><g:checkBox name="points[${id}].persontocontactAsForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
		<ul class="table-list vertical-center">
			<li>
				<span><g:message code="panel.first.name" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieImie" id="points[${id}].contactAtPointFirstName" style="width: 120px" maxlength="15"/></span>
				<span><g:message code="panel.last.name" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieNazwisko" id="points[${id}].contactAtPointLastName"  style="width: 120px" maxlength="18"/></span>
				<span><g:message code="panel.fax" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieFax" id="points[${id}].contactAtPointFax" style="width: 120px" maxlength="9"/></span>
			</li>
			<li>
				<span><g:message code="panel.landline.phone.number" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieTelStacjonarny" id="points[${id}].contactAtPointPhone"  style="width: 120px" maxlength="9"/></span>
				<span><g:message code="panel.mobile.phone.number" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieTelKomorkowy" id="points[${id}].contactAtPointMobilePhone"  style="width: 120px" maxlength="9"/></span>
				<span><g:message code="panel.email" />:</span><span><g:textField name="points[${id}].kontaktWPunkcieEmail" id="points[${id}].contactAtPointEmail"  style="width: 120px" maxlength="40"/></span>
			</li>
		</ul>
	</div>
</fieldset>