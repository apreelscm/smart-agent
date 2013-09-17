<div id="paymentCardPointsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.payment.card.points.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px;">
            %{--<cbd:dccPointsAcceptedCards nip="${data.nip}" tytulPlatnosci="${data.punktyTytulPlatnosci}" systemKasowy="${data.punktySystemKasowy}" uta="${data.punktyUta}" accepted="${data.punktyWybrane}"/>--}%
            <table class="t">
            	<thead>
            		<tr>
            			<td><g:message code="panel.label.number" /></td>
				        <td><g:message code="panel.label.full.name" /></td>
				        <td><g:message code="panel.label.street" /></td>
				        <td><g:message code="panel.label.city" /></td>
				        <td><g:message code="panel.label.house.number" /></td>
				        <td class="min-width-20px"><g:message code="panel.label.code" /></td>
				        <td class="min-width-10px"><g:message code="panel.label.payment.title" /></td>
				        <td class="min-width-10px"><g:message code="panel.label.sys.kas" /></td>
				        <td class="min-width-10px"><g:message code="panel.label.uta" /></td>
				        <td><g:message code="panel.label.choosen" /></td>
            		</tr>
           		</thead>
           		<tbody>
           			<g:each status="i" var="point" in="${data.allPoints}">
           				<tr>
							<td>${point.id}<input type="hidden" name="allPoints[${i}].id" value="${point.id}" /><input type="hidden" name="allPoints[${i}].cbdId" value="${point.cbdId}" /></td>
							<td>${point.nazwa}<input type="hidden" name="allPoints[${i}].nazwa" value="${point.nazwa}" /></td>
							<td>${point.ulica}<input type="hidden" name="allPoints[${i}].ulica" value="${point.ulica}" /></td>
							<td>${point.miejscowosc}<input type="hidden" name="allPoints[${i}].miejscowosc" value="${point.miejscowosc}" /></td>
							<td>${point.nrBudynku}<input type="hidden" name="allPoints[${i}].nrBudynku" value="${point.nrBudynku}" /></td>
							<td>${point.kodPocztowy}<input type="hidden" name="allPoints[${i}].kodPocztowy" value="${point.kodPocztowy}" /></td>
							<td><g:checkBox name="allPoints[${i}].tytulPlatnosci" checked="${point.tytulPlatnosci}" /></td>
							<td><g:checkBox name="allPoints[${i}].systemKasowy" checked="${point.systemKasowy}" /></td>
							<td><g:checkBox name="allPoints[${i}].uta" checked="${point.uta}" /></td>
							<td><g:checkBox name="allPoints[${i}].czyWybranyAkceptacjaKart" checked="${point.czyWybranyAkceptacjaKart}" /></td>
						</tr>
           			</g:each>
           		</tbody>
       		</table>
        </div>
    </fieldset>
</div>