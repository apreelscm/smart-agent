<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].contactAddressAsForMerchant"><g:radio name="${panelType}[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="${panelType}[${id}].contactAddressAsForMerchant" value="" /><g:message code="panel.as.merchant" /></label>
		<label for="${panelType}[${id}].contactAddressAsOnPrint"><g:radio name="${panelType}[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="${panelType}[${id}].contactAddressAsOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<dict:streetSelect name="${panelType}[${id}].korespondencjaUlicaTytul" id="${panelType}[${id}].contactAddressAddressStreetType" value="${pointData?.korespondencjaUlicaTytul}"/>
                	<g:textField name="${panelType}[${id}].korespondencjaUlica" id="${panelType}[${id}].contactAddressAddressStreet" value="${pointData?.korespondencjaUlica}" style="width: 200px" maxlength="19"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="${panelType}[${id}].korespondencjaNrDomu" id="${panelType}[${id}].contactAddressAddressHomeNumber" value="${pointData?.korespondencjaNrDomu}" style="width: 50px" maxlength="4"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="${panelType}[${id}].korespondencjaNrLokalu" id="${panelType}[${id}].contactAddressAddressFlatNumber" value="${pointData?.korespondencjaNrLokalu}" style="width: 50px" maxlength="4"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><g:textField name="${panelType}[${id}].korespondencjaMiasto" id="${panelType}[${id}].contactAddressAddressCity" value="${pointData?.korespondencjaMiasto}" style="width: 280px;" maxlength="19"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="${panelType}[${id}].korespondencjaKodPocztowy" id="${panelType}[${id}].contactAddressAddressPostalCode" value="${pointData?.korespondencjaKodPocztowy}" style="width: 50px" maxlength="5" /></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><g:textField name="${panelType}[${id}].korespondencjaPoczta" id="${panelType}[${id}].contactAddressAddressPostOffice" value="${pointData?.korespondencjaPoczta}" style="width: 280px;" maxlength="19"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>