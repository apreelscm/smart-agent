<div id="newPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.newpoint.pointdata.title" /></div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
            	<div style="float: right;">
            		<g:submitButton id="removePointButton" name="removePointButton" class="button submit" value="Usuń punkt" style="margin-right: 2em; margin-bottom: 1em;"/>
            	</div>
            	<div style="clear: both;"></div>
                <g:render template="../panels/opieka" />
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.pointdata.title" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<ul class="table-list vertical-center">
                			<li><span class="align-right"><g:message code="panel.nip" /></span> <span><g:textField name="points[${id}].nipPunktu" id="points[${id}].nip"/></span></li>
                			<li><span class="align-right"><g:message code="panel.mcccode" /></span> <span><g:textField name="points[${id}].kodMCC" id="points[${id}].mccCode"/></span> <span><label for="points[${id}].sameForEveryPoint"><g:checkBox id="points[${id}].sameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label></span></li>
                			<li><span class="align-right"><g:message code="panel.bussinesstypeinpractice" /></span> <span><g:textField name="points[${id}].rodzProwadzDzialalWPraktyce" id="points[${id}].bussinessTypeInPractice"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankaccountnumber" /></span> <span><g:textField name="points[${id}].numerRachunkuBankowego" id="points[${id}].bankAccountNumber"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankname" /></span> <span><select name="points[${id}].bank" id="points[${id}].bankName"></select></span></li>
                		</ul>
                	</div>
                </fieldset>
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.dataforprinting" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                		<p><g:textField name="points[${id}].nazwaDoWydrukuZTerminalaPos" id="points[${id}].pointNameForPrintingFromPOSTerminal"/></p>
                		<p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="points[${id}].dataforprintingAsAbove"><g:checkBox name="points[${id}].dataforprinting-asAbove" id="points[${id}].dataforprintingAsAbove" /><g:message code="panel.as.above" /></label></p>
                		<p><g:textField name="points[${id}].nazwaDoWyszukiwarki" id="points[${id}].pointNameForSearchEngine"/></p>
                		<p><label for="points[${id}].dataforprintingAsForMerchant"><g:checkBox name="points[${id}].dataforprintingAsForMerchant" id="points[${id}].dataforprintingAsForMerchant"/><g:message code="panel.as.merchant" /></label></p>
		                <ul class="table-list">
		                	<li>
		                		<span><g:message code="panel.street" /></span>
		                		<span>
		                			<select name="points[${id}].dataforprintingStreetType">
                                       <option value="ulica">ulica</option>
                                       <option value="osiedle">osiedle</option>
                                       <option value="aleja">aleja</option>
                                       <option value="plac">plac</option>
                                   </select>
                                   <g:textField name="points[${id}].wydrukUlica" id="points[${id}].dataforprintingAddressStreet" style="width: 200px"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="points[${id}].wydrukNrDomu" id="points[${id}].dataforprintingAddressHomeNumber" style="width: 50px"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="points[${id}].wydrukNrMieszkania" id="points[${id}].dataforprintingAddressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="points[${id}].wydrukMiasto" id="points[${id}].dataforprintingAddressCity"  style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="points[${id}].wydrukKodPocztowy" id="points[${id}].dataforprintingAddressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="points[${id}].wydrukPoczta" id="points[${id}].dataforprintingAddressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
               			<p><g:message code="panel.line1" /> <g:textField name="points[${id}].wydrukLinia1" id="points[${id}].otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
               			<p><g:message code="panel.line2" /> <g:textField name="points[${id}].wydrukLinia2" id="points[${id}].otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
                	</div>
                </fieldset>
                <g:render template="../panels/adresDoKorespondencjiPunktu" />
                <g:render template="../panels/osobaDoKontaktuWPunkcie" />
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.posset.for.selected.point.title" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<label for="possetforselectedpointSameForEveryPoint"><g:checkBox name="possetforselectedpointSameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label>
                		<table class="vertical-center" >
                			<thead>
                				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
                				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
               				</thead>
               				<tbody>
               					<tr><td>typ <select name="points[${id}].possetforselectedpointDialupType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="points[${id}].dialupIlosc" id="points[${id}].dialupCount" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].dialupPPIlosc" id="points[${id}].dialupPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].dialupCena" id="points[${id}].dialupPrice" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].dialupPPCena" id="points[${id}].dialupPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="points[${id}].possetforselectedpointVpnType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="points[${id}].vpnIlosc" id="points[${id}].vpnCount" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].vpnPPIlosc" id="points[${id}].vpnPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].vpnCena" id="points[${id}].vpnPrice" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].vpnPPCena" id="points[${id}].vpnPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="points[${id}].possetforselectedpointSslType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="points[${id}].sslIlosc" id="points[${id}].sslCount" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].sslPPIlosc" id="points[${id}].sslPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].sslCena" id="points[${id}].sslPrice" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].sslPPCena" id="points[${id}].sslPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="points[${id}].possetforselectedpointWifiType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="points[${id}].wifiIlosc" name="points[${id}].wifiCount" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].wifiPPIlosc" id="points[${id}].wifiPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].wifiCena" id="points[${id}].wifiPrice" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].wifiPPCena" id="points[${id}].wifiPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="points[${id}].possetforselectedpointGprsType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="points[${id}].gprsIlosc" id="points[${id}].gprsCount" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].gprsPPIlosc" id="points[${id}].gprsPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].gprsCena" id="points[${id}].gprsPrice" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].gprsPPCena" id="points[${id}].gprsPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField name="points[${id}].baseCount" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
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