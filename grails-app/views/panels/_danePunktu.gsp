<div id="newPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.newpoint.pointdata.title" /></div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
            	<div style="float: right;">
            		<g:submitButton id="removePointButton" name="removePointButton" class="button submit" value="Usuń punkt" style="margin-right: 2em; margin-bottom: 1em;"/>
            	</div>
                <fieldset class="border" style="clear: both;">
                	<legend><g:message code="panel.newpoint.care.title"/></legend>
                	<ul class="table-list vertical-center">
                		<li>
	                		<span class="align-right"><g:message code="panel.newpoint.care.phgain"/></span><span><g:textField style="width: 45px;" name="phGain%ID%"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.bussinesscare" /></span><span><g:textField style="width: 45px;" name="businessCare%ID%"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare1%ID%"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare2%ID%"/></span>
	               			<span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="serviceCare3%ID%"/></span>
               			</li>
                	</ul>
                </fieldset>
                <fieldset class="border">
                	<legend><g:message code="panel.newpoint.pointdata.title" /></legend>
                	<div>
                		<ul class="table-list vertical-center">
                			<li><span class="align-right"><g:message code="panel.nip" /></span> <span><g:textField name="nip%ID%"/></span></li>
                			<li><span class="align-right"><g:message code="panel.mcccode" /></span> <span><g:textField name="mccCode%ID%"/></span> <span><label for="sameForEveryPoint"><g:checkBox name="sameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label></span></li>
                			<li><span class="align-right"><g:message code="panel.bussinesstypeinpractice" /></span> <span><g:textField name="bussinessTypeInPractice%ID%"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankaccountnumber" /></span> <span><g:textField name="bankAccountNumber%ID%"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankname" /></span> <span><select name="bankName%ID%"></select></span></li>
                		</ul>
                	</div>
                </fieldset>
                <fieldset class="border">
                	<legend><g:message code="panel.newpoint.dataforprinting" /></legend>
                	<div style="width: 700px" class="centre">
                		<p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                		<p><g:textField name="pointNameForPrintingFromPOSTerminal%ID%" id="pointNameForPrintingFromPOSTerminal%ID%"/></p>
                		<p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="dataforprinting-asAbove"><g:checkBox name="dataforprinting-asAbove%ID%" id="dataforprinting-asAbove%ID%" /><g:message code="panel.as.above" /></label></p>
                		<p><g:textField name="pointNameForSearchEngine%ID%" id="pointNameForSearchEngine%ID%"/></p>
                		<p><label for="dataforprinting-asForMerchant"><g:checkBox name="dataforprinting-asForMerchant%ID%" id="dataforprinting-asForMerchant%ID%"/><g:message code="panel.as.merchant" /></label></p>
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
                                   <g:textField name="dataforprinting-addressStreet%ID%" id="dataforprinting-addressStreet%ID%" style="width: 200px"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="dataforprinting-addressHomeNumber%ID%" style="width: 50px"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="dataforprinting-addressFlatNumber%ID%" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="dataforprinting-addressCity%ID%" style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="dataforprinting-addressPostalCode%ID%" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="dataforprinting-addressPostOffice%ID%" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
               			<p><g:message code="panel.line1" /> <g:textField name="otherDataForPrintingFromTerminal1%ID%" style="width: 90%;" /></p>
               			<p><g:message code="panel.line2" /> <g:textField name="otherDataForPrintingFromTerminal2%ID%" style="width: 90%;" /></p>
                	</div>
                </fieldset>
                <g:render template="../panels/adresDoKorespondencjiPunktu" />
                <g:render template="../panels/osobaDoKontaktuWPunkcie" />
                <fieldset class="border">
                	<legend><g:message code="panel.newpoint.posset.for.selected.point.title" /></legend>
                	<div>
                		<label for="possetforselectedpoint-sameForEveryPoint%ID%"><g:checkBox name="possetforselectedpoint-sameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label>
                		<table class="vertical-center" >
                			<thead>
                				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
                				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
               				</thead>
               				<tbody>
               					<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="dialupCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="dialupPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="dialupPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="dialupPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="vpnCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="vpnPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="vpnPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="vpnPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="sslCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="sslPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="sslPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="sslPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="wifiCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="wifiPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="wifiPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="wifiPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="gprsCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="gprsPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="gprsPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="gprsPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField name="baseCount%ID%" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
               				</tbody>
                		</table>
                	</div>
                </fieldset>
                <g:render template="../panels/informacjeTechniczne" />
                <g:render template="../panels/funkcjeTerminala" />
                <g:render template="../panels/dodatkoweWyposazenie" />
                <g:render template="../panels/adresacjaSeciowa" />
            </div>
         
    </fieldset>
</div>

<r:require module="jquery_ui"/>
<r:script>
	setupNewPointPanelHandlers("");
</r:script>