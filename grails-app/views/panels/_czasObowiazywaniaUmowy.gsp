<asset:javascript src="vendors/moment/moment.min.js"/>

<div id="aggrementTimePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.time.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <div class="${hasErrors(bean:data,field:'hasUmowaCzas','errorContainer')}">
                    <g:hiddenField name="hasUmowaCzas" value="true"/>
                    <g:hiddenField name="czyAneks" value="${data.czyAneks}"/>
                    <g:hiddenField name="liczbaMiesiecyLojalnosciowych" value="${data.liczbaMiesiecyLojalnosciowych}"/>

                    <li>
                        <span class="align-left">
                            <label>
                                <g:radio name="umowaCzas" value="nieoznaczony" checked="${!data.czyAneks}" disabled="${data.czyAneks}"/>
                                <g:message code="panel.aggrement.time.not.defined"/>
                            </label>
                        </span>
                    </li>
                    <li>
                        <span class="align-left">
                            <label>
                                <g:radio name="umowaCzas" value="oznaczony" checked="${data.czyAneks}" disabled="${!data.czyAneks}"/>
                                <g:message code="panel.aggrement.time.defined"/>
                            </label>
                        </span>
                    </li>
                </div>
            </ul>
            <ul>
                <li id="aggrementDates">
                    <span>
                        <span><g:message code="panel.from"/></span>
                        <span><eumowy:textField name="umowaOznOd" value="${data.umowaOznOd}" validatable="${data}" style="width: 120px;"/></span>
                        <span><g:message code="panel.to"/></span>
                        <span><eumowy:textField name="umowaOznDo" value="${data.umowaOznDo}" validatable="${data}" readonly="readonly" style="width: 120px;"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<script type="text/javascript">
    jQuery(document).ready(function() {
        var aggDates = jQuery('#aggrementDates');
        var umowaOznOd = jQuery('#umowaOznOd');
        var umowaOznDo = jQuery('#umowaOznDo');

        if (jQuery('input[name="umowaCzas"]:checked').val() != 'oznaczony'){
            aggDates.hide();
            umowaOznOd.attr("disabled", true);
            umowaOznOd.val("");
            umowaOznDo.attr("disabled", true);
            umowaOznDo.val("");
        };

        jQuery('input[name="umowaCzas"]').change(function(e){
            if (e.target.value === 'nieoznaczony'){
                aggDates.hide();
                umowaOznOd.attr("disabled", true);
                umowaOznOd.val("");
                umowaOznDo.attr("disabled", true);
                umowaOznDo.val("");
            } else {
                aggDates.show();
                umowaOznOd.removeAttr("disabled");
                umowaOznDo.removeAttr("disabled");
            }
        });

        var _aggrementStart = jQuery("#umowaOznOd");
        var _aggrementEnd = jQuery("#umowaOznDo");

        _aggrementStart.datepicker({ dateFormat: 'yy-mm-dd', minDate: new Date() });

        _aggrementStart.on("change", function(){
            var startDate = _aggrementStart.datepicker("getDate"),
                    monthsToAdd = '${data.liczbaMiesiecyLojalnosciowych}',
                    endDate = moment(startDate).add(monthsToAdd, 'M');

            if (moment(startDate).date() === 1) {
                endDate = endDate.subtract(1, 'days');
            } else {
                endDate = endDate.endOf('month');
            }

            _aggrementEnd.val(endDate.format("YYYY-MM-DD"));
        });
    });
</script>