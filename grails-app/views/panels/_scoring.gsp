<div id="scoringPanel">
    <div class="belka-glowna"><g:message code="panel.scoring.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 400px">
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
                <p>Czy działaność firmy wymaga koncesji, licencji lub pozwolenia?</p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
                <p>Rodzaj zezwolenia (jeśli wymagany) <g:textField name="scoringTODO"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.characteristic.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> salon</p>
                <p><input type="radio" name="" id="" /> sklep</p>
                <p><input type="radio" name="" id="" /> stoisko</p>
                <p><input type="radio" name="" id="" /> stacja paliw</p>
                <p><input type="radio" name="" id="" /> inny <g:textField name="scoringTODO"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.size.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> Powyżej 400m^2</p>
                <p><input type="radio" name="" id="" /> 50-400m^2</p>
                <p><input type="radio" name="" id="" /> Poniżej 50m^2</p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.acceptance.title"/></legend>
            <div style="text-align: left">
                <p>Czy wcześniej prowadzono w firmie akceptację kart płatniczych?</p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.monitoring.title"/></legend>
            <div style="text-align: left">
                <p>Czy w punkcie jest zainstalowany monitoring?</p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.yes"/></p>
                <p><input type="radio" name="" id="" /> <g:message code="panel.no"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.localization.title"/></legend>
            <div class="border" style="text-align: left; margin: 20px">
                <p><input type="radio" name="" id="" /> trasa przelotowa</p>
                <p><input type="radio" name="" id="" /> centrum miasta</p>
                <p><input type="radio" name="" id="" /> peryferia miasta</p>
            </div>
            <div class="border" style="text-align: left; margin: 20px">
                <p><input type="radio" name="" id="" /> centrum handlowe</p>
                <p><input type="radio" name="" id="" /> pawilony handlowe</p>
                <p><input type="radio" name="" id="" /> budynek wolnostojący</p>
                <p><input type="radio" name="" id="" /> osiedle mieszkaniowe</p>
                <p><input type="radio" name="" id="" /> targowisko</p>
                <p><input type="radio" name="" id="" /> inny <g:textField name="scoringTODO"/></p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.point.place.size.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" id="" /> miasto ponad 500 tys. mieszkańców</p>
                <p><input type="radio" name="" id="" /> miasto 100-500 tys. mieszkańców</p>
                <p><input type="radio" name="" id="" /> miasto 50-99 tys. mieszkańców</p>
                <p><input type="radio" name="" id="" /> miasto poniżej 50 tys. mieszkańców</p>
                <p><input type="radio" name="" id="" /> wieś</p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.visit conclusion.title"/></legend>
            <div style="text-align: left">
                <p>Punkty były: </p>
                <p><input type="radio" name="" id="" /> czynne</p>
                <p><input type="radio" name="" id="" /> nieczynne</p>
                <p><input type="checkbox" name=""/> stan zadbany</p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.important.data.title"/></legend>
            <div style="text-align: left">
                <p><input type="checkbox" name="" id="" /> sprzedaż towarów luksusowych</p>
                <p><input type="checkbox" name="" id="" /> >50% w nocy</p>
                <p><input type="checkbox" name="" id="" /> ruch turystyczny lub przygraniczny</p>
                <p><input type="checkbox" name="" id="" /> usługi płatne z góry (zaliczki)</p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.frequency.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name="" /> kilka razy w miesiącu</p>
                <p><input type="radio" name="" /> kilka razy w tygodniu</p>
                <p><input type="radio" name="" /> co drugi dzień</p>
                <p><input type="radio" name="" /> codziennie</p>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend><g:message code="panel.transaction.count.title"/></legend>
            <div style="text-align: left">
                <p><input type="radio" name=""/> 0-4</p>
                <p><input type="radio" name=""/> 5-10</p>
                <p><input type="radio" name=""/> >10</p>
            </div>
        </fieldset>

        <fieldset class="border">
            <legend><g:message code="panel.profitability.title"/></legend>
            <div style="text-align: left">
                <div style="display: inline-block; float: left; text-align: left">
                    <p>Dochodowość/POS (kalkulator)</p>
                </div>
                <div style="display: inline-block; float: right">
                    <div><g:textField name="scoringTODO"/> <g:message code="panel.polish.currency"/></div>
                </div>
            </div>
        </fieldset>
        <fieldset class="border">
            <legend>Liczba dokonywanych dziennie transakcji TODO - zmiana tytulu!!</legend>
            <div style="display: inline-block; float: left; text-align: left">
                <p>Obrót miesięczny ogółem</p>
                <p>Obrót miesięczny na karty</p>
                <p>średni obrót miesięczny na punkt</p>
                <p>średnia wartość transakcji</p>
            </div>
            <div style="display: inline-block; float: right">
                <div><g:textField name="scoringTODO"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO"/> <g:message code="panel.polish.currency"/></div>
                <div><g:textField name="scoringTODO"/> <g:message code="panel.polish.currency"/></div>
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