<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="points[${id}].contactAddressAsForMerchant"><g:radio name="points[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="points[${id}].contactAddressAsForMerchant" value="" /><g:message code="panel.as.merchant" /></label>
		<label for="points[${id}].contactAddressAsOnPrint"><g:radio name="points[${id}].korespondencjaJakDlaMerchantaLubWydruku" id="points[${id}].contactAddressAsOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<dict:streetSelect name="points[${id}].korespondencjaUlicaTytul" id="points[${id}].contactAddressAddressStreetType" value="${pointData?.korespondencjaUlicaTytul}"/>
                	<g:textField name="points[${id}].korespondencjaUlica" id="points[${id}].contactAddressAddressStreet" value="${pointData?.korespondencjaUlica}" style="width: 200px"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="points[${id}].korespondencjaNrDomu" id="points[${id}].contactAddressAddressHomeNumber" value="${pointData?.korespondencjaNrDomu}" style="width: 50px"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="points[${id}].korespondencjaNrLokalu" id="points[${id}].contactAddressAddressFlatNumber" value="${pointData?.korespondencjaNrLokalu}" style="width: 50px"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><g:textField name="points[${id}].korespondencjaMiasto" id="points[${id}].contactAddressAddressCity" value="${pointData?.korespondencjaMiasto}" style="width: 280px;"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="points[${id}].korespondencjaKodPocztowy" id="points[${id}].contactAddressAddressPostalCode" value="${pointData?.korespondencjaKodPocztowy}" style="width: 50px"/></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><g:textField name="points[${id}].korespondencjaPoczta" id="points[${id}].contactAddressAddressPostOffice" value="${pointData?.korespondencjaPoczta}" style="width: 280px;"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>