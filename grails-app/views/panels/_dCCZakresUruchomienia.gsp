<div id="dccRangePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.dcc.range.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
            <div style="text-align: left">
                <g:radioGroup name="dccZakresUruchomienia"
                              labels="['panel.dcc.range.current.and.new','panel.dcc.range.current', 'panel.dcc.range.direct']"
                              values="['obecne_i_nowe', 'obecne', 'wskazane']"
                              value="${data.dccZakresUruchomienia}">
                    <p><label> ${it.radio} <g:message code="${it.label}"/></label></p>
                </g:radioGroup>
            </div>
        </div>
        <div id="dccRange" class="centre" style="text-align: center; padding-top: 20px; width: 800px; max-width: 950px">
            <cbd:dccRange nip="${data.nip}" accepted="[1,3]"/>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>
    jQuery(document).ready(function() {

        var dccRange = jQuery('#dccRange');

        if (jQuery('input[name="dccZakresUruchomienia"]:checked').val() != 'wskazane'){
            enableCheckbox(false);
            dccRange.hide();
        } else {
            console.log(jQuery('input[name="dccZakresUruchomienia"]:checked').val());
        }

        jQuery('input[name="dccZakresUruchomienia"]').change(function(e){
            if (e.target.value == 'obecne_i_nowe'){
                enableCheckbox(false);
                dccRange.hide();
            } else if (e.target.value == 'obecne'){
                enableCheckbox(false);
                dccRange.hide();
            } else {
                enableCheckbox(true);
                dccRange.show();
            }
        });

        function enableCheckbox(checked) {
            var checkboxes = jQuery('input[name="dccRangePOS"]');
            if (checked) {
                checkboxes.removeAttr("disabled");
            } else {
                checkboxes.attr("disabled", true);
                checkboxes.attr("checked", false);
            }
        }
    });
</r:script>