<fieldset class="subpanel-fieldset">
   	<legend><g:message code="panel.newpoint.persontocontact.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="%ID%persontocontactAsForMerchant"><g:checkBox name="%ID%persontocontactAsForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
		<ul class="table-list vertical-center">
			<li>
				<span><g:message code="panel.first.name" />:</span><span><g:textField name="%ID%contactAtPointFirstName" style="width: 120px"/></span>
				<span><g:message code="panel.last.name" />:</span><span><g:textField name="%ID%contactAtPointLastName"  style="width: 120px"/></span>
				<span><g:message code="panel.fax" />:</span><span><g:textField name="%ID%contactAtPointFax" style="width: 120px"/></span>
			</li>
			<li>
				<span><g:message code="panel.landline.phone.number" />:</span><span><g:textField name="%ID%contactAtPointPhone"  style="width: 120px"/></span>
				<span><g:message code="panel.mobile.phone.number" />:</span><span><g:textField name="%ID%contactAtPointMobilePhone"  style="width: 120px"/></span>
				<span><g:message code="panel.email" />:</span><span><g:textField name="%ID%contactAtPointEmail"  style="width: 120px"/></span>
			</li>
		</ul>
	</div>
</fieldset>