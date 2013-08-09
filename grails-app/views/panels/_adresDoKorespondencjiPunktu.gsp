<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.contactaddress" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="contactAddress-asForMerchant%ID%"><g:radio name="contactAddress-asForMerchantOrPrint%ID%" id="contactAddress-asForMerchant%ID%" value=""/><g:message code="panel.as.merchant" /></label>
		<label for="contactAddress-asOnPrint%ID%"><g:radio name="contactAddress-asForMerchantOrPrint%ID%" id="contactAddress-asOnPrint%ID%" value=""/><g:message code="panel.as.on.print" /></label>
		<ul class="table-list">
		  	<li>
		  		<span><g:message code="panel.street" /></span>
		  		<span>
		  			<select name="contactAddress-streetType%ID%">
                       <option value="ulica">ulica</option>
                       <option value="osiedle">osiedle</option>
                       <option value="aleja">aleja</option>
                       <option value="plac">plac</option>
                   </select>
                   <g:textField name="contactAddress-addressStreet%ID%" style="width: 200px"/>
                </span>
                <span>
                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="contactAddress-addressHomeNumber%ID%" style="width: 50px"/></span>
                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="contactAddress-addressFlatNumber%ID%" style="width: 50px"/></span>
                </span>
            </li>
		  	<li>
		  		<span><g:message code="panel.city" /></span>
		  		<span><g:textField name="contactAddress-addressCity%ID%" style="width: 280px;"/></span>
		  		<span>
		  			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="contactAddress-addressPostalCode%ID%" style="width: 50px"/></span>
		  		</span>
		  	</li>
		  	<li>
		  		<span><g:message code="panel.postal" /></span>
		  		<span><g:textField name="contactAddress-addressPostOffice%ID%" style="width: 280px;"/></span>
		  	</li>
	  	</ul>
	</div>
</fieldset>