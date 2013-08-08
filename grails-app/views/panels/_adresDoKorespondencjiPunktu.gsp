<div class="subpanel">
                	<h4><g:message code="panel.newpoint.contactaddress" /></h4>
                	<div>
                		<label><g:radio name="asForMerchant" value=""/><g:message code="panel.as.merchant" /></label>
                		<label><g:radio name="asOnPrint" value=""/><g:message code="panel.as.on.print" /></label>
                		<ul class="table-list">
		                	<li>
		                		<span><g:message code="panel.street" /></span>
		                		<span>
		                			<select>
                                       <option value="ulica">ulica</option>
                                       <option value="osiedle">osiedle</option>
                                       <option value="aleja">aleja</option>
                                       <option value="plac">plac</option>
                                   </select>
                                   <g:textField name="addressStreet" style="width: 200px"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="addressHomeNumber" style="width: 50px"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="addressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="addressCity" style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="addressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="addressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                	</div>
                </div>