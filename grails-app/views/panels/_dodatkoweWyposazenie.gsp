<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.additionalequipment.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<label for="${panelType}[${id}].additionalequipmentSameForEveryPoint"><g:checkBox id="${panelType}[${id}].additionalequipmentSameForEveryPoint" name="${panelType}[${id}].dodatkoweWyposazenieTakSamoDlaWszystkichPunktow" value="${pointData?.dodatkoweWyposazenieTakSamoDlaWszystkichPunktow}" /><g:message code="panel.sameforeverypoint" /></label>
		<table class="vertical-center" >
			<tbody>
				<tr class="baseRow display-none">
                    <td><g:message code="panel.base" /></td>
                    <td colspan="2"></td>
                    <td><eumowy:textField name="${panelType}[${id}].bazaIlosc" id="${panelType}[${id}].bazaCount" class="integer-number" style="width: 50px" value="${pointData?.bazaIlosc}" /> szt.</td>
                </tr>
				<tr>
                    <td><g:message code="panel.newpoint.additionalequipment.router" /></td>
                    <td colspan="2"></td>
                    <td><eumowy:textField name="${panelType}[${id}].routerIlosc" id="${panelType}[${id}].routerCount" class="integer-number" style="width: 50px" value="${pointData?.routerIlosc}"/> szt.</td>
                </tr>
				<tr>
                    <td><g:message code="panel.newpoint.additionalequipment.cardreader" /></td>
                    <td colspan="2"></td>
                    <td><eumowy:textField name="${panelType}[${id}].czytnikKartIlosc" id="${panelType}[${id}].cardReaderCount" class="integer-number" style="width: 50px" value="${pointData?.czytnikKartIlosc}"/> szt.</td>
                </tr>
                <tr>
                    <td><g:message code="panel.sim.card" /></td>
                    <td></td>
                    <td><dict:simCardSelect name="${panelType}[${id}].kartaSimTyp" id="${panelType}[${id}].simCardType" value="${pointData?.kartaSimTyp}" class="kartaSimTyp" style="width: 163px;"/></td>
                    <td><eumowy:textField name="${panelType}[${id}].kartaSimIlosc" id="${panelType}[${id}].simCardCount" class="integer-number kartaSimIlosc" style="width: 50px" value="${pointData?.kartaSimIlosc}"/> szt.</td>
                </tr>
				<tr>
                    <td><g:message code="panel.newpoint.additionalequipment.other" /></td>
                    <td></td>
                    <td><g:textField name="${panelType}[${id}].inneWyposazenie" id="${panelType}[${id}].otherAdditionalDevice" value="${pointData?.inneWyposazenie}" maxlength="255" style="width: 150px;" /></td>
                    <td><eumowy:textField name="${panelType}[${id}].inneWyposazenieIlosc" id="${panelType}[${id}].otherAdditionalDeviceCount" class="integer-number" style="width: 50px" value="${pointData?.inneWyposazenieIlosc}"/> szt.</td>
                </tr>
			</tbody>
		</table>
	</div>
</fieldset>