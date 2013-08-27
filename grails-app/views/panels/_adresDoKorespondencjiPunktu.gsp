<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="%ID%contactAddressAsForMerchant"><g:radio name="%ID%contactAddressAsForMerchantOrPrint" id="%ID%contactAddressAsForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
		<label for="%ID%contactAddressAsOnPrint"><g:radio name="%ID%contactAddressAsForMerchantOrPrint" id="%ID%contactAddressAsOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<select name="%ID%contactAddressStreetType">
                       <option value="ulica">ulica</option>
                       <option value="osiedle">osiedle</option>
                       <option value="aleja">aleja</option>
                       <option value="plac">plac</option>
                   </select>
                   <g:textField name="%ID%korespondencjaUlica" id="%ID%contactAddressAddressStreet" style="width: 200px"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="%ID%korespondencjaNrDomu" id="%ID%contactAddressAddressHomeNumber" style="width: 50px"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="%ID%korespondencjaNrMieszkania" id="%ID%contactAddressAddressFlatNumber" style="width: 50px"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><g:textField name="%ID%korespondencjaMiasto" id="%ID%contactAddressAddressCity" style="width: 280px;"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="%ID%korespondencjaKodPocztowy" id="%ID%contactAddressAddressPostalCode" style="width: 50px"/></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><g:textField name="%ID%korespondencjaPoczta" id="%ID%contactAddressAddressPostOffice" style="width: 280px;"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>