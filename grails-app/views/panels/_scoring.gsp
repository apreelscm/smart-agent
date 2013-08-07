<div id="scoringPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.scoring.title"/></div>
    </fieldset>

    <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
            <fieldset class="border">
            <legend><g:message code="panel.activity.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="activity" /> <g:message code="panel.activity.trade"/></p>
                <p><input type="radio" name="activity" /> <g:message code="panel.activity.services"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.ownership.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="ownership" /> <g:message code="panel.ownership.ownership"/></p>
                <p><input type="radio" name="ownership" /> <g:message code="panel.ownership.rent"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.activity.time.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="activityTime" /> <g:message code="panel.activity.time.more.than.five.years"/></p>
                <p><input type="radio" name="activityTime" /> <g:message code="panel.activity.time.between.one.and.five.years"/></p>
                <p><input type="radio" name="activityTime" /> <g:message code="panel.activity.time.less.than.one.year"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.concession.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.concession.needed"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
                <p><g:message code="panel.concession.kind"/> <g:textField name="scoringTODO"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.characteristic.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.salon"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.shop"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.stand"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.petrol.station"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.other"/> <g:textField name="scoringTODO" style="width: 300px"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.size.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.size.more.than.400"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.size.between.50.and.400"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.size.less.than.50"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.acceptance.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.acceptance.question"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.monitoring.title"/></legend>
            <div style="text-align: left">
                <p><g:message code="panel.monitoring.question"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.localization.title"/></legend>
            <div class="border" style="text-align: left; margin: 20px">
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.localization.cruising.route"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.localization.town"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.localization.periphery"/></p>
            </div>
            <div class="border" style="text-align: left; margin: 20px">
                <p><input type="radio" name="" id="" /> <g:message code="panel.shopping.center"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.commercial.pavilions"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.commercial.building"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.settlement"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.market"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.point.characteristic.other"/> <g:textField name="scoringTODO" style="width: 300px"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.place.size.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> <g:message code="panel.city.size.more.than.500"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.city.size.between.100.and.500"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.city.size.between.50.and.99"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.city.size.less.than.50"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.city.size.village"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.visit conclusion.title"/></legend>
            <div style="text-align: left">
                <p>Punkty były: </p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.open"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.close"/></p>
                <p><input type="checkbox" name=""/> <g:message code="panel.maintained"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.important.data.title"/></legend>
            <div style="text-align: left">
                <p><input type="checkbox" name="" id="" /> <g:message code="panel.luxury.goods"/></p>
                <p><input type="checkbox" name="" id="" /> <g:message code="panel.nightly.sell"/></p>
                <p><input type="checkbox" name="" id="" /> <g:message code="panel.tourism"/></p>
                <p><input type="checkbox" name="" id="" /> <g:message code="panel.instalment"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.frequency.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" /> <g:message code="panel.few.in.month"/></p>
                <p><input type="radio" name="" /> <g:message code="panel.few.in.week"/></p>
                <p><input type="radio" name="" /> <g:message code="panel.every.other.day"/></p>
                <p><input type="radio" name="" /> <g:message code="panel.every.day"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.count.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name=""/> <g:message code="panel.transaction.count.between.0.and.4"/></p>
                <p><input type="radio" name=""/> <g:message code="panel.transaction.count.between.5.and.10"/></p>
                <p><input type="radio" name=""/> <g:message code="panel.transaction.count.more.than.10"/></p>
            </div>
        </fieldset>

        <fieldset class="border">
            <legend><g:message code="panel.profitability.title"/></legend>
            <div style="text-align: left">
                <div style="display: inline-block; float: left; text-align: left">
                    <p><g:message code="panel.profitability.calc"/></p>
                </div>
                <div style="display: inline-block; float: right">
                    <div><g:textField name="scoringTODO" style="width: 100px"/> <g:message code="panel.polish.currency"/></div>
                </div>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend>TODO - TODO - zmiana tytulu!!</legend>
            <div style="display: inline-block; float: left; text-align: left">
                <p><g:message code="panel.monthly.circulation.overall"/></p>
                <p><g:message code="panel.monthly.circulation.cards"/></p>
                <p><g:message code="panel.average.monthly.circulation.per.point"/></p>
                <p><g:message code="panel.average.transaction.count"/></p>
            </div>
            <div style="display: inline-block; float: right">
                <div><g:textField name="scoringTODO" style="width: 100px"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO" style="width: 100px"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO" style="width: 100px"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO" style="width: 100px"/> <g:message code="panel.polish.currency"/></div>
            </div>
        </fieldset>
        </div>
</div>

<r:require module="jquery_ui"/>

<r:script>

    //TODO - zrobic aktywacje pol!!!!!


    jQuery(document).ready(function() {
        var aggDates = jQuery('#aggrementDates');
        aggDates.hide();
        jQuery('input[name="aggrementTime"]').change(function(){
            if (jQuery("#notSpecified").attr("checked")){
                aggDates.hide();
            } else {
                aggDates.show();
            }
        });


        var _aggrementStart = jQuery("#aggrementStart");
        var _aggrementEnd = jQuery("#aggrementEnd");

        _aggrementStart.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });
        _aggrementEnd.datepicker({ dateFormat: 'yy-dd-mm', minDate: new Date() });

        _aggrementStart.on("change", function(){
            _aggrementEnd.datepicker("option", "minDate",  _aggrementStart.val());
        });

        _aggrementEnd.on("change", function(){
            _aggrementStart.datepicker("option", "maxDate",  _aggrementEnd.val());
        });
    });
</r:script>