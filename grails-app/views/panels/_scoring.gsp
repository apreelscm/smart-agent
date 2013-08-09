<div id="scoringPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.scoring.title"/></div>
    </fieldset>

    <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
            <fieldset class="border">
            <legend><g:message code="panel.activity.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-activity" value="a"/> <g:message code="panel.activity.trade"/></label></p>
                <p><label><input type="radio" name="scoring-activity" value="b"/> <g:message code="panel.activity.services"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.ownership.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-ownership" /> <g:message code="panel.ownership.ownership"/></label></p>
                <p><label><input type="radio" name="scoring-ownership" /> <g:message code="panel.ownership.rent"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.activity.time.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-activity-time" /> <g:message code="panel.activity.time.more.than.five.years"/></label></p>
                <p><label><input type="radio" name="scoring-activity-time" /> <g:message code="panel.activity.time.between.one.and.five.years"/></label></p>
                <p><label><input type="radio" name="scoring-activity-time" /> <g:message code="panel.activity.time.less.than.one.year"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.concession.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.concession.needed"/></p>
                <p><label><input type="radio" name="scoring-concession" value="yes"/> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoring-concession" value="no"/> <g:message code="panel.no"/></label></p>
                <p><g:message code="panel.concession.kind"/> <g:textField name="scoring-concession-name" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.characteristic.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-characteristic" /> <g:message code="panel.point.characteristic.salon"/></label></p>
                <p><label><input type="radio" name="scoring-characteristic" /> <g:message code="panel.point.characteristic.shop"/></label></p>
                <p><label><input type="radio" name="scoring-characteristic" /> <g:message code="panel.point.characteristic.stand"/></label></p>
                <p><label><input type="radio" name="scoring-characteristic" /> <g:message code="panel.point.characteristic.petrol.station"/></label></p>
                <p><label><input type="radio" name="scoring-characteristic" value="other" /> <g:message code="panel.point.characteristic.other"/> </label><g:textField name="scoring-characteristic-other" style="width: 300px" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.size.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.point.size.more.than.400"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.point.size.between.50.and.400"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.point.size.less.than.50"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.acceptance.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.acceptance.question"/></p>
                <p><label><input type="radio" name="scoring-acceptance" value="yes" /> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoring-acceptance" value="no" /> <g:message code="panel.no"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.monitoring.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.monitoring.question"/></p>
                <p><label><input type="radio" name="scoring-monitoring" value="yes" /> <g:message code="panel.yes"/></label></p>
                <p><label><input type="radio" name="scoring-monitoring" value="no" /> <g:message code="panel.no"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.localization.title"/></legend>
            <div class="border" style="text-align: left; margin: 20px">
                <p><label><input type="radio" name="scoring-point-localization" /> <g:message code="panel.point.localization.cruising.route"/></label></p>
                <p><label><input type="radio" name="scoring-point-localization" /> <g:message code="panel.point.localization.town"/></label></p>
                <p><label><input type="radio" name="scoring-point-localization" /> <g:message code="panel.point.localization.periphery"/></label></p>
            </div>
            <div class="border" style="text-align: left; margin: 20px">
                <p><label><input type="radio" name="scoring-point-type" /> <g:message code="panel.shopping.center"/></label></p>
                <p><label><input type="radio" name="scoring-point-type" /> <g:message code="panel.commercial.pavilions"/></label></p>
                <p><label><input type="radio" name="scoring-point-type" /> <g:message code="panel.commercial.building"/></label></p>
                <p><label><input type="radio" name="scoring-point-type" /> <g:message code="panel.settlement"/></label></p>
                <p><label><input type="radio" name="scoring-point-type" /> <g:message code="panel.market"/></label></p>
                <p><label><input type="radio" name="scoring-point-type" value="other"/> <g:message code="panel.point.characteristic.other"/></label> <g:textField name="scoring-point-type-other" style="width: 300px" disabled=""/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.place.size.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.city.size.more.than.500"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.city.size.between.100.and.500"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.city.size.between.50.and.99"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.city.size.less.than.50"/></label></p>
                <p><label><input type="radio" name="scoring-point-size" /> <g:message code="panel.city.size.village"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.visit conclusion.title"/></legend>
            <div style="text-align: left">
                <p>Punkty były: </p>
                <p><label><input type="radio" name="scoring-visit-conclusion" value="open"/> <g:message code="panel.open"/></label></p>
                <p><label><input type="radio" name="scoring-visit-conclusion" value="close"/> <g:message code="panel.close"/></label></p>
                <p><label><input type="checkbox" name="scoring-visit-conclusion-maintained"/> <g:message code="panel.maintained"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.important.data.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="checkbox" name="scoring-important-data" value="" /> <g:message code="panel.luxury.goods"/></label></p>
                <p><label><input type="checkbox" name="scoring-important-data" value="" /> <g:message code="panel.nightly.sell"/></label></p>
                <p><label><input type="checkbox" name="scoring-important-data" value="" /> <g:message code="panel.tourism"/></label></p>
                <p><label><input type="checkbox" name="scoring-important-data" value="" /> <g:message code="panel.instalment"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.frequency.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-transaction-frequency" value="" /> <g:message code="panel.few.in.month"/></label></p>
                <p><label><input type="radio" name="scoring-transaction-frequency" value="" /> <g:message code="panel.few.in.week"/></label></p>
                <p><label><input type="radio" name="scoring-transaction-frequency" value="" /> <g:message code="panel.every.other.day"/></label></p>
                <p><label><input type="radio" name="scoring-transaction-frequency" value="" /> <g:message code="panel.every.day"/></label></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.count.title"/></legend>
            <div style="text-align: left">
                <p><label><input type="radio" name="scoring-transaction-count"/> <g:message code="panel.transaction.count.between.0.and.4"/></label></p>
                <p><label><input type="radio" name="scoring-transaction-count"/> <g:message code="panel.transaction.count.between.5.and.10"/></label></p>
                <p><label><input type="radio" name="scoring-transaction-count"/> <g:message code="panel.transaction.count.more.than.10"/></label></p>
            </div>
        </fieldset>

        <fieldset class="border">
            <legend><g:message code="panel.profitability.title"/></legend>
            <ul class="table-list">
                <li>
                    <span><g:message code="panel.profitability.calc"/></span>
                    <span><g:textField name="scoring-profitability" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
            </ul>
        </fieldset>
        <fieldset class="border">
            <legend>TODO - TODO - zmiana tytulu!!</legend>
            <ul class="table-list">
                <li>
                    <span><label><input type="radio" name="scoring-financial-declaration" value="actual" /> <g:message code="panel.actual"/></label></span>
                    <span><label><input type="radio" name="scoring-financial-declaration" value="declared" /> <g:message code="panel.declared"/></label></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.monthly.circulation.overall"/></span>
                    <span><g:textField name="scoring-financial-declaration-m-circ-overall" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.monthly.circulation.cards"/></span>
                    <span><g:textField name="scoring-financial-declaration-m-circ-cards" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.average.monthly.circulation.per.point"/></span>
                    <span><g:textField name="scoring-financial-declaration-a-m-circ" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.average.transaction.count"/></span>
                    <span><g:textField name="scoring-financial-declaration-a-trans-count" style="width: 100px"/> <g:message code="panel.polish.currency"/></span>
                </li>
            </ul>


        </fieldset>
        </div>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {
        var concessionName = jQuery('#scoring-concession-name');
        jQuery('input[name="scoring-concession"]').change(function(e){
            if (e.target.value === 'yes'){
                concessionName.removeAttr("disabled");
            } else {
                concessionName.attr("disabled", true);
                concessionName.val("")
            }
        });


        var scoringCharacteristic = jQuery('#scoring-characteristic-other');
        jQuery('input[name="scoring-characteristic"]').change(function(e){
            if (e.target.value === 'other'){
                scoringCharacteristic.removeAttr("disabled");
            } else {
                scoringCharacteristic.attr("disabled", true);
                scoringCharacteristic.val("")
            }
        });

        var scoringPointType = jQuery('#scoring-point-type-other');
        jQuery('input[name="scoring-point-type"]').change(function(e){
            if (e.target.value === 'other'){
                scoringPointType.removeAttr("disabled");
            } else {
                scoringPointType.attr("disabled", true);
                scoringPointType.val("")
            }
        });

    });
</r:script>