<div id="scoringPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.scoring.title"/></div>
    </fieldset>

    <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
            <fieldset class="border">
            <legend><g:message code="panel.activity.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringDzialalnosc" value="handel"/> <g:message code="panel.activity.trade"/></label></p>
                <p><label><input type="radio" name="scoringDzialalnosc" value="usługi"/> <g:message code="panel.activity.services"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.ownership.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringWlasnosc" value="własność"/> <g:message code="panel.ownership.ownership"/></label></p>
                <p><label><input type="radio" name="scoringWlasnosc" value="wynajem/dzierżawa"/> <g:message code="panel.ownership.rent"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.activity.time.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringDzialalnoscCzas" value="Powyżej 5 lat"/> <g:message code="panel.activity.time.more.than.five.years"/></label></p>
                <p><label><input type="radio" name="scoringDzialalnoscCzas" value="1-5 lat"/> <g:message code="panel.activity.time.between.one.and.five.years"/></label></p>
                <p><label><input type="radio" name="scoringDzialalnoscCzas" value="Poniżej roku"/> <g:message code="panel.activity.time.less.than.one.year"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.concession.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.concession.needed"/></p>
                <p><label><input type="radio" name="scoringKoncesja" value="tak"/> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoringKoncesja" value="nie"/> <g:message code="panel.no"/></label></p>
                <p><g:message code="panel.concession.kind"/> <g:textField name="rodzajZezwolenia" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.characteristic.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringCharakterystyka" value="salon"/> <g:message code="panel.point.characteristic.salon"/></label></p>
                <p><label><input type="radio" name="scoringCharakterystyka" value="sklep"/> <g:message code="panel.point.characteristic.shop"/></label></p>
                <p><label><input type="radio" name="scoringCharakterystyka" value="stoisko"/> <g:message code="panel.point.characteristic.stand"/></label></p>
                <p><label><input type="radio" name="scoringCharakterystyka" value="stacja paliw"/> <g:message code="panel.point.characteristic.petrol.station"/></label></p>
                <p><label><input type="radio" name="scoringCharakterystyka" value="inny" /> <g:message code="panel.point.characteristic.other"/> </label><g:textField name="scoringCharakterystykaInna" style="width: 300px" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.size.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringWielkoscPunktu" value="Powyżej 400m^2"/> <g:message code="panel.point.size.more.than.400"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscPunktu" value="50-400m^2"/> <g:message code="panel.point.size.between.50.and.400"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscPunktu" value="Poniżej 50m^2"/> <g:message code="panel.point.size.less.than.50"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.acceptance.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.acceptance.question"/></p>
                <p><label><input type="radio" name="scoringAkceptacja" value="tak" /> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoringAkceptacja" value="nie" /> <g:message code="panel.no"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.monitoring.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.monitoring.question"/></p>
                <p><label><input type="radio" name="scoringMonitoring" value="tak" /> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoringMonitoring" value="nie" /> <g:message code="panel.no"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.localization.title"/></legend>
            <div class="border" style="text-align: left; margin: 20px">
                <p><label><input type="radio" name="scoringLokalizacjaPunktu" value="trasa przelotowa"/> <g:message code="panel.point.localization.cruising.route"/></label></p>
                <p><label><input type="radio" name="scoringLokalizacjaPunktu" value="centrum miasta"/> <g:message code="panel.point.localization.town"/></label></p>
                <p><label><input type="radio" name="scoringLokalizacjaPunktu" value="peryferia miasta"/> <g:message code="panel.point.localization.periphery"/></label></p>
            </div>
            <div class="border" style="text-align: left; margin: 20px">
                <p><label><input type="radio" name="scoringTypPunktu" value="centrum handlowe"/> <g:message code="panel.shopping.center"/></label></p>
                <p><label><input type="radio" name="scoringTypPunktu" value="pawilony handlowe"/> <g:message code="panel.commercial.pavilions"/></label></p>
                <p><label><input type="radio" name="scoringTypPunktu" value="budynek wolnostojący"/> <g:message code="panel.commercial.building"/></label></p>
                <p><label><input type="radio" name="scoringTypPunktu" value="osiedle mieszkaniowe"/> <g:message code="panel.settlement"/></label></p>
                <p><label><input type="radio" name="scoringTypPunktu" value="targowisko"/> <g:message code="panel.market"/></label></p>
                <p><label><input type="radio" name="scoringTypPunktu" value="inny"/> <g:message code="panel.point.characteristic.other"/></label> <g:textField name="scoringTypPunktuInny" style="width: 300px" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.place.size.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringWielkoscMiejscowosci" value="miasto ponad 500 tys. mieszkańców"/> <g:message code="panel.city.size.more.than.500"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscMiejscowosci" value="miasto 100-500 tys. mieszkańców"/> <g:message code="panel.city.size.between.100.and.500"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscMiejscowosci" value="miasto 50-99 tys. mieszkańców"/> <g:message code="panel.city.size.between.50.and.99"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscMiejscowosci" value="miasto poniżej 50 tys. mieszkańców"/> <g:message code="panel.city.size.less.than.50"/></label></p>
                <p><label><input type="radio" name="scoringWielkoscMiejscowosci" value="wieś"/> <g:message code="panel.city.size.village"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.visit conclusion.title"/></legend>
            <div style="text-align: left">
                <p>Punkty były: </p>
                <p><label><input type="radio" name="scoringOtwartyZamkniety" value="czynne"/> <g:message code="panel.open"/></label></p>
                <p><label><input type="radio" name="scoringOtwartyZamkniety" value="nieczynne"/> <g:message code="panel.close"/></label></p>
                <p><label><input type="checkbox" name="scoringStanZadbany" value="stan zadbany"/> <g:message code="panel.maintained"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.important.data.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="checkbox" name="scoringWazneDane" value="sprzedaż towarów luksusowych" /> <g:message code="panel.luxury.goods"/></label></p>
                <p><label><input type="checkbox" name="scoringWazneDane" value=">50% w nocy" /> <g:message code="panel.nightly.sell"/></label></p>
                <p><label><input type="checkbox" name="scoringWazneDane" value="ruch turystyczny lub przygraniczny" /> <g:message code="panel.tourism"/></label></p>
                <p><label><input type="checkbox" name="scoringWazneDane" value="usługi płatne z góry (zaliczki)" /> <g:message code="panel.instalment"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.frequency.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringCzestoscTransakcji" value="kilka razy w miesiącu" /> <g:message code="panel.few.in.month"/></label></p>
                <p><label><input type="radio" name="scoringCzestoscTransakcji" value="kilka razy w tygodniu" /> <g:message code="panel.few.in.week"/></label></p>
                <p><label><input type="radio" name="scoringCzestoscTransakcji" value="co drugi dzień" /> <g:message code="panel.every.other.day"/></label></p>
                <p><label><input type="radio" name="scoringCzestoscTransakcji" value="codziennie" /> <g:message code="panel.every.day"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.count.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoringIloscTransakcji" value="0-4"/> <g:message code="panel.transaction.count.between.0.and.4"/></label></p>
                <p><label><input type="radio" name="scoringIloscTransakcji" value="5-10"/> <g:message code="panel.transaction.count.between.5.and.10"/></label></p>
                <p><label><input type="radio" name="scoringIloscTransakcji" value=">10"/> <g:message code="panel.transaction.count.more.than.10"/></label></p>
            </div>
        </fieldset>

        <fieldset class="border">
            <legend><g:message code="panel.profitability.title"/></legend>
            <ul class="table-list">
                <li>
                    <span><g:message code="panel.profitability.calc"/></span>
                    <span><g:textField name="scoringDochodowosc" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
            </ul>
        </fieldset>
        <fieldset class="border">
            <legend>TODO - TODO - zmiana tytulu!!</legend>
            <ul class="table-list">
                <li>
                    <span><label><input type="radio" name="scoringDeklaracjaFinansowa" value="actual" /> <g:message code="panel.actual"/></label></span>
                    <span><label><input type="radio" name="scoringDeklaracjaFinansowa" value="declared" /> <g:message code="panel.declared"/></label></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.monthly.circulation.overall"/></span>
                    <span><g:textField name="scoringDeklaracjaFinansowaObrotOgolem" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.monthly.circulation.cards"/></span>
                    <span><g:textField name="scoringDeklaracjaFinansowaObrotNaKarty" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.average.monthly.circulation.per.point"/></span>
                    <span><g:textField name="scoringDeklaracjaFinansowaSredniObrot" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.average.transaction.count"/></span>
                    <span><g:textField name="scoringDeklaracjaFinansowaSredniaTransakcja" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
            </ul>
        </fieldset>
        </div>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        var concessionName = jQuery('#scoringKoncesjaNazwa');
        jQuery('input[name="scoringKoncesja"]').change(function(e){
            if (e.target.value === 'yes'){
                concessionName.removeAttr("disabled");
            } else {
                concessionName.attr("disabled", true);
                concessionName.val("")
            }
        });


        var scoringCharacteristic = jQuery('#scoringCharakterystykaInna');
        jQuery('input[name="scoringCharakterystyka"]').change(function(e){
            if (e.target.value === 'other'){
                scoringCharacteristic.removeAttr("disabled");
            } else {
                scoringCharacteristic.attr("disabled", true);
                scoringCharacteristic.val("")
            }
        });

        var scoringPointType = jQuery('#scoringTypPunktuInny');
        jQuery('input[name="scoringTypPunktu"]').change(function(e){
            if (e.target.value === 'other'){
                scoringPointType.removeAttr("disabled");
            } else {
                scoringPointType.attr("disabled", true);
                scoringPointType.val("")
            }
        });

    });
</r:script>