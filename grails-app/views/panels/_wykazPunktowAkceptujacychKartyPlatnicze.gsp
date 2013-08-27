<div id="paymentCardPointsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.payment.card.points.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 700px">
            <cbd:dccPointsAcceptedCards nip="${data.nip}" tytulPlatnosci="${data.punktyTytulPlatnosci}" systemKasowy="${data.punktySystemKasowy}" uta="${data.punktyUta}" accepted="${data.punktyWybrane}"/>
        </div>
    </fieldset>
</div>