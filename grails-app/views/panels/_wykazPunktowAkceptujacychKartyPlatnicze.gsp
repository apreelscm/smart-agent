<div id="paymentCardPointsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.payment.card.points.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 700px">
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
           			<g:each in="${data.allPoints}">
           				<tr>
							<td>${it.id}</td>
							<td>${it.nazwa}</td>
							<td>${it.ulica}</td>
							<td>${it.miejscowosc}</td>
							<td>${it.nrBudynku}</td>
							<td>${it.kodPocztowy}</td>
							<td><g:checkBox name="punktyTytulPlatnosci" value="${point.id}" checked="${it.tytulPlatnosci}" /></td>
							<td><g:checkBox name="punktySystemKasowy" value="${point.id}" checked="${it.systemKasowy}" /></td>
							<td><g:checkBox name="punktyUta" value="${point.id}" checked="${it.uta}" /></td>
							<td><g:checkBox name="punktyWybrane" value="${point.id}" checked="${it.czyWybrany}" /></td>
						</tr>
           			</g:each>
           		</tbody>
       		</table>
        </div>
    </fieldset>
</div>