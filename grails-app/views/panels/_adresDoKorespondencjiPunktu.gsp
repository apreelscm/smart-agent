<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].contactAddressAsForMerchant"><g:radio name="${panelType}[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="${panelType}[${id}].contactAddressAsForMerchant" value="" /><g:message code="panel.as.merchant" /></label>
		<label for="${panelType}[${id}].contactAddressAsOnPrint"><g:radio name="${panelType}[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="${panelType}[${id}].contactAddressAsOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<dict:streetSelect name="${panelType}[${id}].korespondencjaUlicaTytul" id="${panelType}[${id}].contactAddressAddressStreetType" value="${pointData?.korespondencjaUlicaTytul}" default="UL"/>
                	<eumowy:textField name="${panelType}[${id}].korespondencjaUlica" id="${panelType}[${id}].contactAddressAddressStreet" value="${pointData?.korespondencjaUlica}" validatable="${pointData}" validateField="korespondencjaUlica" style="width: 200px" required="true"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><eumowy:textField name="${panelType}[${id}].korespondencjaNrDomu" id="${panelType}[${id}].contactAddressAddressHomeNumber" value="${pointData?.korespondencjaNrDomu}" validatable="${pointData}" validateField="korespondencjaNrDomu" style="width: 50px" maxlength="4" required="true"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><eumowy:textField name="${panelType}[${id}].korespondencjaNrLokalu" id="${panelType}[${id}].contactAddressAddressFlatNumber" value="${pointData?.korespondencjaNrLokalu}" style="width: 50px" maxlength="4"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><eumowy:textField name="${panelType}[${id}].korespondencjaMiasto" id="${panelType}[${id}].contactAddressAddressCity" value="${pointData?.korespondencjaMiasto}" validatable="${pointData}" validateField="korespondencjaMiasto" style="width: 280px;" required="true"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><eumowy:textField class="postal-code" name="${panelType}[${id}].korespondencjaKodPocztowy" id="${panelType}[${id}].contactAddressAddressPostalCode" value="${pointData?.korespondencjaKodPocztowy}" validatable="${pointData}" validateField="korespondencjaKodPocztowy" style="width: 50px" maxlength="5" required="true"/></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><eumowy:textField name="${panelType}[${id}].korespondencjaPoczta" id="${panelType}[${id}].contactAddressAddressPostOffice" value="${pointData?.korespondencjaPoczta}" validatable="${pointData}" validateField="korespondencjaPoczta" style="width: 280px;" required="true"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>