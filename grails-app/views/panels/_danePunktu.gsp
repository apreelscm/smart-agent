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
                			<li><span class="align-right"><g:message code="panel.nip" /></span> <span><g:textField name="%ID%nipPunktu" id="%ID%nip"/></span></li>
                			<li><span class="align-right"><g:message code="panel.mcccode" /></span> <span><g:textField name="%ID%kodMCC" id="%ID%mccCode"/></span> <span><label for="%ID%sameForEveryPoint"><g:checkBox id="%ID%sameForEveryPoint" /><g:message code="panel.sameforeverypoint" /></label></span></li>
                			<li><span class="align-right"><g:message code="panel.bussinesstypeinpractice" /></span> <span><g:textField name="%ID%rodzProwadzDzialalWPraktyce" id="%ID%bussinessTypeInPractice"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankaccountnumber" /></span> <span><g:textField name="%ID%kontoBankNumer" id="%ID%bankAccountNumber"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankname" /></span> <span><select name="%ID%nazwaBanku" id="%ID%bankName"></select></span></li>
                		</ul>
                	</div>
                </fieldset>
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.dataforprinting" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                		<p><g:textField name="%ID%nazwaDoWydrukuZTerminalaPos" id="%ID%pointNameForPrintingFromPOSTerminal"/></p>
                		<p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="%ID%dataforprintingAsAbove"><g:checkBox name="%ID%dataforprinting-asAbove" id="%ID%dataforprintingAsAbove" /><g:message code="panel.as.above" /></label></p>
                		<p><g:textField name="%ID%nazwaDoWyszukiwarki" id="%ID%pointNameForSearchEngine"/></p>
                		<p><label for="%ID%dataforprintingAsForMerchant"><g:checkBox name="%ID%dataforprintingAsForMerchant" id="%ID%dataforprintingAsForMerchant"/><g:message code="panel.as.merchant" /></label></p>
		                <ul class="table-list">
		                	<li>
		                		<span><g:message code="panel.street" /></span>
		                		<span>
		                			<select name="%ID%dataforprintingStreetType">
                                       <option value="ulica">ulica</option>
                                       <option value="osiedle">osiedle</option>
                                       <option value="aleja">aleja</option>
                                       <option value="plac">plac</option>
                                   </select>
                                   <g:textField name="%ID%wydrukUlica" id="%ID%dataforprintingAddressStreet" style="width: 200px"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="%ID%wydrukNrDomu" id="%ID%dataforprintingAddressHomeNumber" style="width: 50px"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="%ID%wydrukNrMieszkania" id="%ID%dataforprintingAddressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="%ID%wydrukMiasto" id="%ID%dataforprintingAddressCity"  style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="%ID%wydrukKodPocztowy" id="%ID%dataforprintingAddressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="%ID%wydrukPoczta" id="%ID%dataforprintingAddressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
               			<p><g:message code="panel.line1" /> <g:textField name="%ID%wydrukLinia1" id="%ID%otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
               			<p><g:message code="panel.line2" /> <g:textField name="%ID%wydrukLinia2" id="%ID%otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
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
               					<tr><td>typ <select name="%ID%possetforselectedpointDialupType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="%ID%dialupIlosc" id="%ID%dialupCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%dialupPPIlosc" id="%ID%dialupPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="%ID%dialupCena" id="%ID%dialupPrice" style="width: 50px"/> zł.</td><td><g:textField name="%ID%dialupPPCena" id="%ID%dialupPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="%ID%possetforselectedpointVpnType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="%ID%vpnIlosc" id="%ID%vpnCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%vpnPPIlosc" id="%ID%vpnPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="%ID%vpnCena" id="%ID%vpnPrice" style="width: 50px"/> zł.</td><td><g:textField name="%ID%vpnPPCena" id="%ID%vpnPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="%ID%possetforselectedpointSslType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="%ID%sslIlosc" id="%ID%sslCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%sslPPIlosc" id="%ID%sslPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="%ID%sslCena" id="%ID%sslPrice" style="width: 50px"/> zł.</td><td><g:textField name="%ID%sslPPCena" id="%ID%sslPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="%ID%possetforselectedpointWifiType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="%ID%wifiIlosc" name="%ID%wifiCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%wifiPPIlosc" id="%ID%wifiPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="%ID%wifiCena" id="%ID%wifiPrice" style="width: 50px"/> zł.</td><td><g:textField name="%ID%wifiPPCena" id="%ID%wifiPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select name="%ID%possetforselectedpointGprsType" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="%ID%gprsIlosc" id="%ID%gprsCount" style="width: 50px"/> szt.</td><td><g:textField name="%ID%gprsPPIlosc" id="%ID%gprsPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="%ID%gprsCena" id="%ID%gprsPrice" style="width: 50px"/> zł.</td><td><g:textField name="%ID%gprsPPCena" id="%ID%gprsPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField name="%ID%baseCount" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
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