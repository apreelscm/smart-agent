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
						<td>${pos.id}</td>
						<td>${pos.numerZestawuPos}</td>
						<td>${pos.dataOd}</td>
						<td>${pos.dataDo}</td>
						<td>${pos.wysokoscOplaty}</td>
						<td><g:checkBox name="allPoses[${i}].czyWybrany"/></td>
					</tr>
					</g:each>
				</tbody>
			</table>
        </div>
    </fieldset>
</div>