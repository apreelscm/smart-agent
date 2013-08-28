<fieldset class="subpanel-fieldset">
   	<legend><g:message code="panel.newpoint.persontocontact.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="%ID%persontocontactAsForMerchant"><g:checkBox name="%ID%persontocontactAsForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
		<ul class="table-list vertical-center">
			<li>
				<span><g:message code="panel.first.name" />:</span><span><g:textField name="%ID%kontaktWPunkcieImie" name="%ID%contactAtPointFirstName" style="width: 120px"/></span>
				<span><g:message code="panel.last.name" />:</span><span><g:textField name="%ID%kontaktWPunkcieNazwisko" id="%ID%contactAtPointLastName"  style="width: 120px"/></span>
				<span><g:message code="panel.fax" />:</span><span><g:textField name="%ID%kontaktWPunkcieFax" id="%ID%contactAtPointFax" style="width: 120px"/></span>
			</li>
			<li>
				<span><g:message code="panel.landline.phone.number" />:</span><span><g:textField name="%ID%kontaktWPunkcieTelStacjonarny" id="%ID%contactAtPointPhone"  style="width: 120px"/></span>
				<span><g:message code="panel.mobile.phone.number" />:</span><span><g:textField name="%ID%kontaktWPunkcieTelKomorkowy" id="%ID%contactAtPointMobilePhone"  style="width: 120px"/></span>
				<span><g:message code="panel.email" />:</span><span><g:textField name="%ID%kontaktWPunkcieEmail" id="%ID%contactAtPointEmail"  style="width: 120px"/></span>
			</li>
		</ul>
	</div>
</fieldset>