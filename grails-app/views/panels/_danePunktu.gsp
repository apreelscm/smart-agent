<div id="newPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.newpoint.pointdata.title" /></div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
                <div class="subpanel">
                	<h4><g:message code="panel.newpoint.care.title"/> </h4>
                	<ul class="table-list vertical-center">
                		<li>
	                		<span class="align-right"><g:message code="panel.newpoint.care.phgain"/></span><span><g:textField style="width: 45px;" name="phGain"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.bussinesscare" /></span><span><g:textField style="width: 45px;" name="businessCare"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare1"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare2"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare3"/></span>
               			</li>
                	</ul>
                </div>
                <div class="subpanel">
                	<h4><g:message code="panel.newpoint.pointdata.title" /></h4>
                	<div>
                		<ul class="table-list vertical-center">
                			<li><span class="align-right"><g:message code="panel.nip" /></span> <span><g:textField name="nip"/></span></li>
                			<li><span class="align-right"><g:message code="panel.mcccode" /></span> <span><g:textField name="mccCode"/></span> <span><label for="sameForEveryPoint"><g:checkBox name="sameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label></span></li>
                			<li><span class="align-right"><g:message code="panel.bussinesstypeinpractice" /></span> <span><g:textField name="bussinessTypeInPractice"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankaccountnumber" /></span> <span><g:textField name="bankAccountNumber"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankname" /></span> <span><select name="bankName"></select></span></li>
                		</ul>
                	</div>
                </div>
                <div class="subpanel">
                	<h4><g:message code="panel.newpoint.dataforprinting" /></h4>
                	<div style="width: 700px" class="centre">
                		<p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                		<p><g:textField name="pointNameForPrintingFromPOSTerminal"/></p>
                		<p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="dataforprinting-asAbove"><g:checkBox name="dataforprinting-asAbove" /><g:message code="panel.as.above" /></label></p>
                		<p><g:textField name="pointNameForSearchEngine"/></p>
                		<p><label for="dataforprinting-asForMerchant"><g:checkBox name="dataforprinting-asForMerchant" /><g:message code="panel.as.merchant" /></label></p>
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
                                   <g:textField name="dataforprinting-addressStreet" style="width: 200px"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="dataforprinting-addressHomeNumber" style="width: 50px"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="dataforprinting-addressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="dataforprinting-addressCity" style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="dataforprinting-addressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="dataforprinting-addressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
               			<p><g:message code="panel.line1" /> <g:textField name="otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
               			<p><g:message code="panel.line2" /> <g:textField name="otherDataForPrintingFromTerminal2" style="width: 90%;" /></p>
                	</div>
                </div>
                <g:render template="../panels/adresDoKorespondencjiPunktu" />
                <g:render template="../panels/osobaDoKontaktuWPunkcie" />
                <div class="subpanel">
                	<h4><g:message code="panel.newpoint.posset.for.selected.point.title" /></h4>
                	<div>
                		<label for="possetforselectedpoint-sameForEveryPoint"><g:checkBox name="possetforselectedpoint-sameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label>
                		<table class="vertical-center" >
                			<thead>
                				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
                				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
               				</thead>
               				<tbody>
               					<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="dialupCount" style="width: 50px"/> szt.</td><td><g:textField name="dialupPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="dialupPrice" style="width: 50px"/> zł.</td><td><g:textField name="dialupPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="vpnCount" style="width: 50px"/> szt.</td><td><g:textField name="vpnPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="vpnPrice" style="width: 50px"/> zł.</td><td><g:textField name="vpnPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="sslCount" style="width: 50px"/> szt.</td><td><g:textField name="sslPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="sslPrice" style="width: 50px"/> zł.</td><td><g:textField name="sslPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="wifiCount" style="width: 50px"/> szt.</td><td><g:textField name="wifiPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="wifiPrice" style="width: 50px"/> zł.</td><td><g:textField name="wifiPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="gprsCount" style="width: 50px"/> szt.</td><td><g:textField name="gprsPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="gprsPrice" style="width: 50px"/> zł.</td><td><g:textField name="gprsPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField name="baseCount" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
               				</tbody>
                		</table>
                	</div>
                </div>
                <g:render template="../panels/informacjeTechniczne" />
                <g:render template="../panels/funkcjeTerminala" />
                <g:render template="../panels/dodatkoweWyposazenie" />
                <g:render template="../panels/adresacjaSeciowa" />
            </div>
         
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        jQuery("#plannedInstallationDate").datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#dayCloseFrom").datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#dayCloseTo").datepicker({ dateFormat: 'yy-mm-dd' });
    });
</r:script>