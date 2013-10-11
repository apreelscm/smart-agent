<fieldset class="subpanel-fieldset">
   	<legend><g:message code="panel.newpoint.persontocontact.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].persontocontactAsForMerchant"><g:checkBox name="${panelType}[${id}].kontaktWPunkcieJakDlaMerchanta" id="${panelType}[${id}].persontocontactAsForMerchant" value="${pointData?.kontaktWPunkcieJakDlaMerchanta}"/><g:message code="panel.as.merchant" /></label>
		<ul class="table-list vertical-center">
			<li>
				<span><g:select name="${panelType}[${id}].kontaktWPunkcieTytul" id="${panelType}[${id}].contactAtPointTitle" from="['', 'Pan','Pani']" valueMessagePrefix="person.title" value="${pointData?.kontaktWPunkcieTytul}" validatable="${pointData}" required="true" /></span>
				<span><g:message code="panel.first.name" />:</span><span><eumowy:textField name="${panelType}[${id}].kontaktWPunkcieImie" id="${panelType}[${id}].contactAtPointFirstName" value="${pointData?.kontaktWPunkcieImie}" validatable="${pointData}" validateField="kontaktWPunkcieImie" style="width: 120px" maxlength="15" required="true"/></span>
				<span><g:message code="panel.last.name" />:</span><span><eumowy:textField name="${panelType}[${id}].kontaktWPunkcieNazwisko" id="${panelType}[${id}].contactAtPointLastName" value="${pointData?.kontaktWPunkcieNazwisko}" validatable="${pointData}" validateField="kontaktWPunkcieNazwisko" style="width: 120px" maxlength="18" required="true"/></span>
				<span><g:message code="panel.fax" />:</span><span><g:textField class="fax" name="${panelType}[${id}].kontaktWPunkcieFax" id="${panelType}[${id}].contactAtPointFax" value="${pointData?.kontaktWPunkcieFax}" style="width: 120px" maxlength="9"/></span>
			</li>
			<li>
				<span></span>
				<span><g:message code="panel.landline.phone.number" />:</span><span><eumowy:textField class="phone" name="${panelType}[${id}].kontaktWPunkcieTelStacjonarny" id="${panelType}[${id}].contactAtPointPhone" value="${pointData?.kontaktWPunkcieTelStacjonarny}" style="width: 120px" maxlength="9"/></span>
				<span><g:message code="panel.mobile.phone.number" />:</span><span><eumowy:textField class="mobile-phone" name="${panelType}[${id}].kontaktWPunkcieTelKomorkowy" id="${panelType}[${id}].contactAtPointMobilePhone" value="${pointData?.kontaktWPunkcieTelKomorkowy}" style="width: 120px" maxlength="9"/></span>
				<span style="white-space:nowrap"><g:message code="panel.email" />:</span><span><g:textField name="${panelType}[${id}].kontaktWPunkcieEmail" id="${panelType}[${id}].contactAtPointEmail" value="${pointData?.kontaktWPunkcieEmail}" style="width: 120px" maxlength="40" email="true"/></span>
			</li>
		</ul>
	</div>
</fieldset>