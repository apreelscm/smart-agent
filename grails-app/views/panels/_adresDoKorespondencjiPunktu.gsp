<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="points[${id}].contactAddressAsForMerchant"><g:radio name="points[${id}].contactAddressAsForMerchantOrPrint" id="points[${id}].contactAddressAsForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
		<label for="points[${id}].contactAddressAsOnPrint"><g:radio name="points[${id}].contactAddressAsForMerchantOrPrint" id="points[${id}].contactAddressAsOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<select name="points[${id}].contactAddressStreetType">
                       <option value="ulica">ulica</option>
                       <option value="osiedle">osiedle</option>
                       <option value="aleja">aleja</option>
                       <option value="plac">plac</option>
                   </select>
                   <g:textField name="points[${id}].korespondencjaUlica" id="points[${id}].contactAddressAddressStreet" style="width: 200px" maxlength="19"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="points[${id}].korespondencjaNrDomu" id="points[${id}].contactAddressAddressHomeNumber" style="width: 50px" maxlength="4"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="points[${id}].korespondencjaNrLokalu" id="points[${id}].contactAddressAddressFlatNumber" style="width: 50px" maxlength="4"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><g:textField name="points[${id}].korespondencjaMiasto" id="points[${id}].contactAddressAddressCity" style="width: 280px;" maxlength="19"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="points[${id}].korespondencjaKodPocztowy" id="points[${id}].contactAddressAddressPostalCode" style="width: 50px" maxlength="5"/></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><g:textField name="points[${id}].korespondencjaPoczta" id="points[${id}].contactAddressAddressPostOffice" style="width: 280px;" maxlength="19"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>