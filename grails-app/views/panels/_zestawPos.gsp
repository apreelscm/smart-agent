<fieldset class="subpanel-fieldset" style="clear: both;">
	<legend><g:message code="panel.newpos.posset.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="posset-sameForEveryPoint%ID%"><g:checkBox name="posset-sameForEveryPoint%ID%" /><g:message code="panel.sameforeverypoint" /></label>
		<table class="vertical-center" >
			<thead>
				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
			</thead>
			<tbody>
				<tr><td>typ <select name="posset-dialupType%ID%" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.dialup" /></td><td><g:textField name="dialupIlosc%ID%" id="dialupI%ID%" style="width: 50px"/> szt.</td><td><g:textField name="dialupPPIlosc%ID%" id="dialupPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="dialupCena%ID%" id="dialupPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="dialupPPCena%ID%" id="dialupPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
				<tr><td>typ <select name="posset-vpnType%ID%" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.vpn" /></td><td><g:textField name="vpnIlosc%ID%" id="vpnCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="vpnPPIlosc%ID%" id="vpnPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="vpnCena%ID%" id="vpnPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="vpnPPCena%ID%" id="vpnPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
				<tr><td>typ <select name="posset-sslType%ID%" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.ssl" /></td><td><g:textField name="sslIlosc%ID%" id="sslCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="sslPPIlosc%ID%" id="sslPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="sslCena%ID%" id="sslPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="sslPPCena%ID%" id="sslPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
				<tr><td>typ <select name="posset-wifiType%ID%" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.wifi" /></td><td><g:textField name="wifiIlosc%ID%" id="wifiCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="wifiPPIlosc%ID%" id="wifiPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="wifiCena%ID%" id="wifiPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="wifiPPCena%ID%" id="wifiPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
				<tr><td>typ <select name="posset-gprsType%ID%" style="width: 50px"></select></td><td style="text-align: right;"><g:message code="panel.gprs" /></td><td><g:textField name="gprsIlosc%ID%" id="gprsCount%ID%" style="width: 50px"/> szt.</td><td><g:textField name="gprsPPIlosc%ID%" id="gprsPPCount%ID%" style="width: 50px"/> PP. szt.</td><td><g:textField name="gprsCena%ID%" id="gprsPrice%ID%" style="width: 50px"/> zł.</td><td><g:textField name="gprsPPCena%ID%" id="gprsPPPrice%ID%" style="width: 50px"/> zł.</td></tr>
				<tr><td></td><td style="text-align: right;"><g:message code="panel.base" /></td><td><g:textField name="bazaIlosc%ID%" id="baseCount%ID%" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
			</tbody>
		</table>
	</div>
</fieldset>