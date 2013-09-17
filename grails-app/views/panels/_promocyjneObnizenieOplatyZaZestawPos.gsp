<div id="posDiscountPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.pos.discount.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
			<table class="t">
				<thead style="text-align: center !important;">
					<tr>
						<td><g:message code="panel.number" /></td>
						<td><g:message code="panel.pos.discount.possetnumber" /></td>
						<td><g:message code="panel.pos.discount.datefrom" /></td>
						<td><g:message code="panel.pos.discount.dateto" /></td>
						<td><g:message code="panel.pos.discount.fee.amount" /></td>
						<td><g:message code="panel.pos.discount.choosen" /></td>
					</tr>
				</thead>
				<tbody>
					<g:each status="i" var="pos" in="${data.allPoses}" >
					<tr>
						<td>${pos.id}<input type="hidden" name="allPoses[${i}].id" value="${pos.id}" /><input type="hidden" name="allPoses[${i}].tpsId" value="${pos.tpsId}" /></td>
						<td>${pos.numerZestawuPos}<input type="hidden" name="allPoses[${i}].numerZestawuPos" value="${pos.numerZestawuPos}" /></td>
						<td>${pos.dataOd}<input type="hidden" name="allPoses[${i}].dataOd" value="${pos.dataOd}" /></td>
						<td>${pos.dataDo}<input type="hidden" name="allPoses[${i}].dataDo" value="${pos.dataDo}" /></td>
						<td>${pos.wysokoscOplaty}<input type="hidden" name="allPoses[${i}].wysokoscOplaty" value="${pos.wysokoscOplaty}" /></td>
						<td><g:checkBox name="allPoses[${i}].czyWybrany"/></td>
					</tr>
					</g:each>
				</tbody>
			</table>
        </div>
    </fieldset>
</div>