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
                			<li><span class="align-right"><g:message code="panel.nip" /></span> <span><g:textField name="points[${id}].nipPunktu" id="points[${id}].nip" value="${pointData?.nipPunktu}" maxlength="10"/></span></li>
                			<li><span class="align-right"><g:message code="panel.mcccode" /></span> <span><g:textField name="points[${id}].kodMCC" id="points[${id}].mccCode" value="${pointData?.kodMCC}" maxlength="4"/></span> <span><label for="points[${id}].sameForEveryPoint"><g:checkBox id="points[${id}].sameForEveryPoint" name="points[${id}].takSamoDlaWszystkichPunktow" value="${pointData?.takSamoDlaWszystkichPunktow}" /><g:message code="panel.sameforeverypoint" /></label></span></li>
                			<li><span class="align-right"><g:message code="panel.bussinesstypeinpractice" /></span> <span><g:textField name="points[${id}].rodzProwadzDzialalWPraktyce" id="points[${id}].bussinessTypeInPractice" value="${pointData?.rodzProwadzDzialalWPraktyce}" maxlength="60" /></span></li>
                			<li><span class="align-right"><g:message code="panel.bankaccountnumber" /></span> <span><g:textField name="points[${id}].numerRachunkuBankowego" id="points[${id}].bankAccountNumber" value="${pointData?.numerRachunkuBankowego}" maxlength="26"/></span></li>
                			<li><span class="align-right"><g:message code="panel.bankname" /></span> <span><select name="points[${id}].bank" id="points[${id}].bankName" value="${pointData?.bank}"></select></span></li>
                		</ul>
                	</div>
                </fieldset>
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.dataforprinting" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<p><g:message code="panel.newpoint.pointnameforprintingfromposterminal" /></p>
                		<p><g:textField name="points[${id}].nazwaDoWydrukuZTerminalaPos" id="points[${id}].pointNameForPrintingFromPOSTerminal" value="${pointData?.nazwaDoWydrukuZTerminalaPos}"/></p>
                		<p><g:message code="panel.newpoint.pointnameforsearchengine" /> <label for="points[${id}].dataforprintingAsAbove"><g:checkBox name="points[${id}].wydrukJakPowyzej" id="points[${id}].dataforprintingAsAbove" value="${pointData?.wydrukJakPowyzej}" /><g:message code="panel.as.above" /></label></p>
                		<p><g:textField name="points[${id}].nazwaDoWyszukiwarki" id="points[${id}].pointNameForSearchEngine" value="${pointData?.nazwaDoWyszukiwarki}"/></p>
                		<p><label for="points[${id}].dataforprintingAsForMerchant"><g:checkBox name="points[${id}].wydrukJakDlaMerchanta" id="points[${id}].dataforprintingAsForMerchant" value="${pointData?.wydrukJakDlaMerchanta}" /><g:message code="panel.as.merchant" /></label></p>
		                <ul class="table-list">
		                	<li>
		                		<span><g:message code="panel.street" /></span>
		                		<span>
		                			<dict:streetSelect name="points[${id}].wydrukUlicaTytul" id="points[${id}].dataforprintingAddressStreetType" value="${pointData?.wydrukUlicaTytul}"/>
                                	<g:textField name="points[${id}].wydrukUlica" id="points[${id}].dataforprintingAddressStreet" style="width: 200px" value="${pointData?.wydrukUlica}"/>
                                </span>
                                <span>
                                	<span><g:message code="panel.house.number" /></span> <span><g:textField name="points[${id}].wydrukNrDomu" id="points[${id}].dataforprintingAddressHomeNumber" style="width: 50px" value="${pointData?.wydrukNrDomu}"/></span>
                                	<span><g:message code="panel.flat.number" /></span> <span><g:textField name="points[${id}].wydrukNrLokalu" id="points[${id}].dataforprintingAddressFlatNumber" style="width: 50px" value="${pointData?.wydrukNrLokalu}"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span><g:message code="panel.city" /></span>
		                		<span><g:textField name="points[${id}].wydrukMiasto" id="points[${id}].dataforprintingAddressCity" value="${pointData?.wydrukMiasto}" style="width: 280px;"/></span>
		                		<span>
		                			<span><g:message code="panel.postal.code" /></span> <span><g:textField name="points[${id}].wydrukKodPocztowy" id="points[${id}].dataforprintingAddressPostalCode" value="${pointData?.wydrukKodPocztowy}" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span><g:message code="panel.postal" /></span>
		                		<span><g:textField name="points[${id}].wydrukPoczta" id="points[${id}].dataforprintingAddressPostOffice" value="${pointData?.wydrukPoczta}" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p><g:message code="panel.newpoint.otherdataforprintingfromterminal" /></p>
               			<p><g:message code="panel.line1" /> <g:textField name="points[${id}].wydrukLinia1" id="points[${id}].otherDataForPrintingFromTerminal1" value="${pointData?.wydrukLinia1}" style="width: 90%;" /></p>
               			<p><g:message code="panel.line2" /> <g:textField name="points[${id}].wydrukLinia2" id="points[${id}].otherDataForPrintingFromTerminal1" value="${pointData?.wydrukLinia2}" style="width: 90%;" /></p>
                	</div>
                </fieldset>
                <g:render template="../panels/adresDoKorespondencjiPunktu" />
                <g:render template="../panels/osobaDoKontaktuWPunkcie" />
                <fieldset class="subpanel-fieldset">
                	<legend><g:message code="panel.newpoint.posset.for.selected.point.title" /></legend>
                	<div class="subpanel-fieldset-centercontent" >
                		<label for="points[${id}].possetforselectedpointSameForEveryPoint"><g:checkBox id="points[${id}].possetforselectedpointSameForEveryPoint" name="points[${id}].zestawPosTakSamoDlaWszystkichPunktow" value="${pointData?.zestawPosTakSamoDlaWszystkichPunktow}" /><g:message code="panel.sameforeverypoint" /></label>
                		<table class="vertical-center" >
                			<thead>
                				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
                				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
               				</thead>
               				<tbody>
               					<tr><td>typ <g:select id="points[${id}].possetforselectedpointDialupType" name="points[${id}].dialupTyp" from="[]" valueMessagePrefix="" value="${pointData?.dialupTyp}"  style="width: 50px"/></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="points[${id}].dialupIlosc" id="points[${id}].dialupCount" value="${pointData?.dialupIlosc}" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].dialupPPIlosc" id="points[${id}].dialupPPCount" value="${pointData?.dialupPPIlosc}" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].dialupCena" id="points[${id}].dialupPrice" value="${pointData?.dialupCena}" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].dialupPPCena" id="points[${id}].dialupPPPrice" value="${pointData?.dialupPPCena}" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <g:select id="points[${id}].possetforselectedpointVpnType" name="points[${id}].vpnTyp" from="[]" valueMessagePrefix="" value="${pointData?.vpnTyp}"  style="width: 50px"/></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="points[${id}].vpnIlosc" id="points[${id}].vpnCount" value="${pointData?.vpnIlosc}" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].vpnPPIlosc" id="points[${id}].vpnPPCount" value="${pointData?.vpnPPIlosc}" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].vpnCena" id="points[${id}].vpnPrice" value="${pointData?.vpnCena}" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].vpnPPCena" id="points[${id}].vpnPPPrice" value="${pointData?.vpnPPCena}" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <g:select id="points[${id}].possetforselectedpointSslType" name="points[${id}].sslTyp" from="[]" valueMessagePrefix="" value="${pointData?.sslTyp}"  style="width: 50px"/></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="points[${id}].sslIlosc" id="points[${id}].sslCount" value="${pointData?.sslIlosc}" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].sslPPIlosc" id="points[${id}].sslPPCount" value="${pointData?.sslPPIlosc}" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].sslCena" id="points[${id}].sslPrice" value="${pointData?.sslCena}" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].sslPPCena" id="points[${id}].sslPPPrice" value="${pointData?.sslPPCena}" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <g:select id="points[${id}].possetforselectedpointWifiType" name="points[${id}].wifiTyp" from="[]" valueMessagePrefix="" value="${pointData?.wifiTyp}"  style="width: 50px"/></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="points[${id}].wifiIlosc" name="points[${id}].wifiCount" value="${pointData?.wifiIlosc}" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].wifiPPIlosc" id="points[${id}].wifiPPCount" value="${pointData?.wifiPPIlosc}" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].wifiCena" id="points[${id}].wifiPrice" value="${pointData?.wifiCena}" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].wifiPPCena" id="points[${id}].wifiPPPrice" value="${pointData?.wifiPPCena}" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <g:select id="points[${id}].possetforselectedpointGprsType" name="points[${id}].gprsTyp" from="[]" valueMessagePrefix="" value="${pointData?.gprsTyp}"  style="width: 50px"/></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="points[${id}].gprsIlosc" id="points[${id}].gprsCount" value="${pointData?.gprsIlosc}" style="width: 50px"/> szt.</td><td><g:textField name="points[${id}].gprsPPIlosc" id="points[${id}].gprsPPCount" value="${pointData?.gprsPPIlosc}" style="width: 50px"/> PP. szt.</td><td><g:textField name="points[${id}].gprsCena" id="points[${id}].gprsPrice" value="${pointData?.gprsCena}" style="width: 50px"/> zł.</td><td><g:textField name="points[${id}].gprsPPCena" id="points[${id}].gprsPPPrice" value="${pointData?.gprsPPCena}" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField id="points[${id}].baseCount" name="points[${id}].bazaIlosc" value="${pointData?.bazaIlosc}" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
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
%{--<r:script>
	setupNewPointPanelHandlers("");
</r:script>--}%
