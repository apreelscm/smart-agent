<div id="newPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna">Dane punktu</div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
            
                <div class="subpanel">
                	<h4>Opieka</h4>
                	<ul class="table-list vertical-center">
                		<li>
	                		<span class="align-right">PH pozysk</span><span><g:textField style="width: 45px;" name="phGain"/></span>
	               			<span class="align-right">Opieka biznesowa</span><span><g:textField style="width: 45px;" name="businessCare"/></span>
	               			<span class="align-right">Opieka serwisowa</span><span><g:textField style="width: 45px;" name="serviceCare1"/></span>
	               			<span class="align-right">Opieka serwisowa</span><span><g:textField style="width: 45px;" name="serviceCare2"/></span>
	               			<span class="align-right">Opieka serwisowa</span><span><g:textField style="width: 45px;" name="serviceCare3"/></span>
               			</li>
                	</ul>
                </div>
                <div class="subpanel">
                	<h4>Dane punktu</h4>
                	<div>
                		<ul class="table-list vertical-center">
                			<li><span class="align-right">NIP</span> <span><g:textField name="nip"/></span></li>
                			<li><span class="align-right">Kod MCC</span> <span><g:textField name="mccCode"/></span> <span><label><g:checkBox name="sameForEveryPoint" />Tak samo dla wszystkich punktów</label></span></li>
                			<li><span class="align-right">Rodz. prowadz. działal. w praktyce</span> <span><g:textField name="annexPrepaidDate"/></span></li>
                			<li><span class="align-right">Numer rachunku bankowego</span> <span><g:textField name="bankAccountNumber"/></span></li>
                			<li><span class="align-right">Nazwa banku</span> <span><select name="bankName"></select></span></li>
                		</ul>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Dane do wydruku</h4>
                	<div style="width: 700px" class="centre">
                		<p>Nazwa punktu do wydruku z terminala POS:</p>
                		<p><g:textField name="pointNameForPrintingFromPOSTerminal"/></p>
                		<p>Nazwa do wyszukiwarki: <label><g:checkBox name="asAbove" />Jak powyżej</label></p>
                		<p><g:textField name="pointNameForSearchEngine"/></p>
                		<p><label><g:checkBox name="asForMerchant" />Jak dla Merchanta</label></p>
		                <ul class="table-list">
		                	<li>
		                		<span>Ulica</span>
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
                                	<span>Nr domu</span> <span><g:textField name="addressHomeNumber" style="width: 50px"/></span>
                                	<span>Nr lokalu</span> <span><g:textField name="addressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span>Miasto</span>
		                		<span><g:textField name="addressCity" style="width: 280px;"/></span>
		                		<span>
		                			<span>Kod pocztowy</span> <span><g:textField name="addressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span>Poczta</span>
		                		<span><g:textField name="addressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                		<p>Inne dane na wydruku z terminala POS (tekst grzecznościowy)</p>
               			<p>Linia 1 <g:textField name="otherDataForPrintingFromTerminal1" style="width: 90%;" /></p>
               			<p>Linia 2 <g:textField name="otherDataForPrintingFromTerminal2" style="width: 90%;" /></p>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Adres do korespondencji punktu</h4>
                	<div>
                		<label><g:radio name="asForMerchant" value=""/>Jak dla Merchanta</label>
                		<label><g:radio name="asOnPrint" value=""/>Jak na wydruku</label>
                		<ul class="table-list">
		                	<li>
		                		<span>Ulica</span>
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
                                	<span>Nr domu</span> <span><g:textField name="addressHomeNumber" style="width: 50px"/></span>
                                	<span>Nr lokalu</span> <span><g:textField name="addressFlatNumber" style="width: 50px"/></span>
                                </span>
                            </li>
		                	<li>
		                		<span>Miasto</span>
		                		<span><g:textField name="addressCity" style="width: 280px;"/></span>
		                		<span>
		                			<span>Kod pocztowy</span> <span><g:textField name="addressPostalCode" style="width: 50px"/></span>
		                		</span>
		                	</li>
		                	<li>
		                		<span>Poczta</span>
		                		<span><g:textField name="addressPostOffice" style="width: 280px;"/></span>
		                	</li>
		                </ul>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Osoba do kontaktu w punkcie</h4>
                	<div>
                		<label><g:checkBox name="asForMerchant" value=""/>Jak dla Merchanta</label>
                		<ul class="table-list vertical-center">
                			<li>
                				<span>Imię:</span><span><g:textField name="contactAtPointFirstName" style="width: 120px"/></span>
                				<span>Nazwisko:</span><span><g:textField name="contactAtPointLastName"  style="width: 120px"/></span>
                				<span>Faks:</span><span><g:textField name="contactAtPointFax" style="width: 120px"/></span>
                			</li>
                			<li>
                				<span>Tel stacjonarny:</span><span><g:textField name="contactAtPointPhone"  style="width: 120px"/></span>
                				<span>Tel komórkowy:</span><span><g:textField name="contactAtPointMobilePhone"  style="width: 120px"/></span>
                				<span>e-mail:</span><span><g:textField name="contactAtPointEmail"  style="width: 120px"/></span>
                			</li>
                		</ul>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Zestaw POS dla wybranego Punktu</h4>
                	<div>
                		<label><g:checkBox name="sameForEveryPoint" />Taki sam dla wszystkich punktów</label>
                		<table class="vertical-center" >
                			<thead>
                				<tr><td></td><td></td><td></td><td></td><td colspan="2"  style="text-align: center;">Cena</td></tr>
                				<tr><td></td><td></td><td></td><td></td><td>Term./mies</td><td>PP./mies.</td></tr>
               				</thead>
               				<tbody>
               					<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;">DIAL UP</td><td><g:textField name="dialupCount" style="width: 50px"/> szt.</td><td><g:textField name="dialupPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="dialupPrice" style="width: 50px"/> zł.</td><td><g:textField name="dialupPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;">VPN</td><td><g:textField name="vpnCount" style="width: 50px"/> szt.</td><td><g:textField name="vpnPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="vpnPrice" style="width: 50px"/> zł.</td><td><g:textField name="vpnPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;">SSL</td><td><g:textField name="sslCount" style="width: 50px"/> szt.</td><td><g:textField name="sslPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="sslPrice" style="width: 50px"/> zł.</td><td><g:textField name="sslPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;">WiFi</td><td><g:textField name="wifiCount" style="width: 50px"/> szt.</td><td><g:textField name="wifiPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="wifiPrice" style="width: 50px"/> zł.</td><td><g:textField name="wifiPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td>typ <select style="width: 50px"></select></td><td style="text-align: right;">GPRS</td><td><g:textField name="gprsCount" style="width: 50px"/> szt.</td><td><g:textField name="gprsPPCount" style="width: 50px"/> PP. szt.</td><td><g:textField name="gprsPrice" style="width: 50px"/> zł.</td><td><g:textField name="gprsPPPrice" style="width: 50px"/> zł.</td></tr>
                				<tr><td></td><td style="text-align: right;">BAZA</td><td><g:textField name="baseCount" style="width: 50px"/> szt.</td><td></td><td></td><td></td></tr>
               				</tbody>
                		</table>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Informacje techniczne</h4>
                	<div>
                		<label><g:checkBox name="sameForEveryPoint" />Taki sam dla wszystkich punktów</label>
                		<p>Zamknięcie dnia</p>
                		<p>od <g:textField name="dayCloseFrom" style="width: 140px;" id="dayCloseFrom"/> do <g:textField name="dayCloseTo" style="width: 140px;" id="dayCloseTo"/> Planowana data instalacji <g:textField name="plannedInstallationDate" style="width: 140px;" id="plannedInstallationDate"/></p>
                		<p>Uwagi dodatkowe</p>
                		<p><g:textField name="additionalNotes"/></p>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Funkcje terminala</h4>
                	<div>
                		<p><label><g:checkBox name="sameForEveryPoint" />Taki sam dla wszystkich punktów</label></p>
                		
                		<div style="float: left; padding-right: 2em;" >
                			<p class="bold" >Funkcje płatnicze</p>
                			<ul class="table-list vertical-center">
                				<li><label><g:checkBox name="preauthorization" />Preautoryzacja</label></li>
                				<li><label><g:checkBox name="noreturnfunction" />Brak funkcji zwrotu</label></li>
                				<li><label><g:checkBox name="returnWithPassword" />Zwrot na hasło</label></li>
                				<li><label><g:checkBox name="setAnalysis" />Analiza zbioru</label></li>
                				<li><label><g:checkBox name="cashMachineSystemIntegration" />Integracja z sys. kas.</label></li>
                				<li><label><g:checkBox name="returnIKO" />Zwroty IKO</label></li>
                			</ul>
                		</div>
                		<div style="float: left;  padding-right: 2em; height: 200px;" >
                			<div>
                			<p class="bold" >Logowanie kasjerów</p>
                			<ul class="table-list vertical-center">
                				<li><label><g:checkBox name="loggingBeforeEveryTransaction" />Przed każ. transakcją</label></li>
                				<li><label><g:checkBox name="logginEveryChange" />Zmianowe</label></li>
                			</ul>
                			</div>
                			<div>
                				<p class="bold" >Obsługa napiwków</p>
                				<p><label><g:checkBox name="tips" />Napiwek 1</label></p>
                			</div>
                		</div>
                		<div>
                			<div>
	                			<p class="bold" >Doładowania telefoniczne</p>
	                			<ul class="table-list vertical-center">
	                				<li><span><label><g:checkBox name="telePompka" />TelePOMPKA</label></span> <span style="padding-left: 6em;"><label>Liczba terminali <g:textField name="ad" style="width: 50px;"/></label></span></li>
	                				<li><label><g:checkBox name="teleKodzik" />TeleKODZIK</label></li>
	                			</ul>
	                			<div>
	                			<p class="bold" >Produkty Marketingowe</p>
                				<p><label><g:checkBox name="giftCard" />Karta podarunkowa</label></p>
                				</div>
                			</div>
                			
                		</div>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Dodatkowe wyposażenia</h4>
                	<div>
                		<label><g:checkBox name="sameForEveryPoint" />Taki sam dla wszystkich punktów</label>
                		<table class="vertical-center" >
                			<tbody>
                				<tr><td>Pin-pad</td><td>typ <select style="width: 50px"></select></td><td><g:textField name="pinPadCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail" style="width: 50px"/> cena [zł]</td></tr>
                				<tr><td>Router</td><td>typ <select style="width: 50px"></select></td><td><g:textField name="routerCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail" style="width: 50px"/> cena [zł]</td></tr>
                				<tr><td>Czytnik kart bezstykowych</td><td>typ <select style="width: 50px"></select></td><td><g:textField name="cardReaderCount" style="width: 50px"/> szt.</td><td><g:textField name="contactEmail" style="width: 50px"/> cena [zł]</td></tr>
                				<tr><td>Inne <g:textField name="otherAdditionalDevice" style="width: 150px;" /> <label><g:checkBox name="otherAdditionalDeviceSsl" />SSL</label> <label><g:checkBox name="otherAdditionalDeviceGprs" />GPRS</label></td><td>typ <select name="otherAdditionalDeviceType" style="width: 50px"></select></td><td><g:textField name="otherAdditionalDeviceCount" style="width: 50px"/> szt.</td><td><g:textField name="otherAdditionalDevicePrice" style="width: 50px"/> cena [zł]</td></tr>
                			</tbody>
                		</table>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Adresacja statyczna POS WiFi/VPN</h4>
                	<div>
                		<ul class="table-list vertical-center" >
                			<li><span class="align-right" >Maska</span><span><g:textField name="staticDeviceMask" /></span></li>
                			<li><span class="align-right" >Bramka</span><span><g:textField name="staticDeviceGateway" /></span></li>
                			<li><span class="align-right" >Adres IP</span><span><g:textField name="staticDeviceIp" /></span></li>
                			<li><span class="align-right" >Kontakt (nr tel) do informatyka</span><span><g:textField name="staticDeviceSupportContact" /></span></li>
                			<li><span class="align-right" ><select style="width: 50px"></select> Imię</span><span><g:textField name="staticDeviceSupportContactName" /></span></li>
                			<li><span class="align-right" >Nazwisko</span><span><g:textField name="staticDeviceSupportContactSurname" /></span></li>
                		</ul>
                	</div>
                </div>
                <div class="subpanel">
                	<h4>Adresacja dynamiczna POS WiFi</h4>
                	<div>
                		<ul class="table-list vertical-center" >
                			<li><span class="align-right" >Kontakt (nr tel) do informatyka</span><span><g:textField name="dynamicDeviceSupportContact" /></span></li>
                			<li><span class="align-right" ><select style="width: 50px"></select> Imię</span><span><g:textField name="dynamicDeviceSupportName" /></span></li>
                			<li><span class="align-right" >Nazwisko</span><span><g:textField name="dynamicDeviceSupportSurname" /></span></li>
                		</ul>
                	</div>
                </div>
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