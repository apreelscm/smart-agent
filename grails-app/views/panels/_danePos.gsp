<div id="newPosPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.newpos.posdata.title" /></div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
            	<div style="float: right;">
            		<g:submitButton id="removePosButton" name="removePosButton" class="button submit" value="Usuń Pos" style="margin-right: 2em; margin-bottom: 1em;"/>
            	</div>
            	<fieldset class="border" style="clear: both;">
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