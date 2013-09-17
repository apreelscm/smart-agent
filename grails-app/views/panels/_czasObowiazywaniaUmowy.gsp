<div id="aggrementTimePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.time.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <g:radioGroup name="umowaCzas"
                              labels="['panel.aggrement.time.not.defined','panel.aggrement.time.defined']"
                              values="['nieoznaczony', 'oznaczony']"
                              value="${data.umowaCzas}">
                    <li><span class="align-left"><label> ${it.radio} <g:message code="${it.label}"/></label></span></li>
                </g:radioGroup>
            </ul>
            <ul>
                <li id="aggrementDates">
                    <!-- czas: ${data.umowaCzas} od: ${data.umowaOznOd} do: ${data.umowaOznDo} -->
                    <span>
                        <span><g:message code="panel.from"/></span>
                        <span><eumowy:textField name="umowaOznOd" value="${data.umowaOznOd}" readonly="true" style="width: 120px;"/></span>
                        <span><g:message code="panel.to"/></span>
                        <span><eumowy:textField name="umowaOznDo" value="${data.umowaOznDo}" readonly="true" style="width: 120px;"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
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
        _aggrementEnd.datepicker({ dateFormat: 'yy-mm-dd', minDate: new Date() });

        _aggrementStart.on("change", function(){
            _aggrementEnd.datepicker("option", "minDate",  _aggrementStart.val());
        });

        _aggrementEnd.on("change", function(){
            _aggrementStart.datepicker("option", "maxDate",  _aggrementEnd.val());
        });
    });
</r:script>