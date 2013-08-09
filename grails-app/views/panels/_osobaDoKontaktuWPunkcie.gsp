<fieldset class="subpanel-fieldset">
   	<legend><g:message code="panel.newpoint.persontocontact.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="persontocontact-asForMerchant%ID%"><g:checkBox name="persontocontact-asForMerchant%ID%" value=""/><g:message code="panel.as.merchant" /></label>
		<ul class="table-list vertical-center">
			<li>
				<span><g:message code="panel.first.name" />:</span><span><g:textField name="contactAtPointFirstName%ID%" style="width: 120px"/></span>
				<span><g:message code="panel.last.name" />:</span><span><g:textField name="contactAtPointLastName%ID%"  style="width: 120px"/></span>
				<span><g:message code="panel.fax" />:</span><span><g:textField name="contactAtPointFax%ID%" style="width: 120px"/></span>
			</li>
			<li>
				<span><g:message code="panel.landline.phone.number" />:</span><span><g:textField name="contactAtPointPhone%ID%"  style="width: 120px"/></span>
				<span><g:message code="panel.mobile.phone.number" />:</span><span><g:textField name="contactAtPointMobilePhone%ID%"  style="width: 120px"/></span>
				<span><g:message code="panel.email" />:</span><span><g:textField name="contactAtPointEmail%ID%"  style="width: 120px"/></span>
			</li>
		</ul>
	</div>
</fieldset>