<a id="tips">Useful Tips Section</a>
<div id="paymentCardPointsPanel">
    <form method="#tips" action="post">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.payment.card.points.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 700px">
            <cbd:dccPointsAcceptedCards nip="${data.nip}" tytulPlatnosci="[1,2]" systemKasowy="[2,3]" uta="[1,2,3]" accepted="[1,3]"/>
        </div>
    </fieldset>
        <input type="submit"/>
    </form>
</div>