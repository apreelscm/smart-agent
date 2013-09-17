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
            <table class="t">
	            <thead>
		            <tr>
				        <td><g:message code="panel.dcc.table.full.name" /></td>
				        <td><g:message code="panel.dcc.table.street" /></td>
				        <td><g:message code="panel.dcc.table.city" /></td>
				        <td><g:message code="panel.dcc.table.house.number" /></td>
				        <td><g:message code="panel.dcc.table.code" /></td>
				        <td><g:message code="panel.dcc.table.poz.count" /></td>
			        	<td>&nbsp;</td>
			        </tr>
	        	</thead>
        		<tbody>
        			<g:each status="i" var="point" in="${data.allPoints}">
	        		<tr>
		                <td>${point.nazwa}<input type="hidden" name="allPoints[${i}].id" value="${point.nazwa}" /><input type="hidden" name="allPoints[${i}].id" value="${point.id}" /><input type="hidden" name="allPoints[${i}].cbdId" value="${point.cbdId}" /></td>
		                <td>${point.ulica}<input type="hidden" name="allPoints[${i}].ulica" value="${point.ulica}" /></td>
		                <td>${point.miejscowosc}<input type="hidden" name="allPoints[${i}].miejscowosc" value="${point.miejscowosc}" /></td>
		                <td>${point.nrBudynku}<input type="hidden" name="allPoints[${i}].nrBudynku" value="${point.nrBudynku}" /></td>
		                <td>${point.kodPocztowy}<input type="hidden" name="allPoints[${i}].kodPocztowy" value="${point.kodPocztowy}" /></td>
		                <td>${point.liczbaPos}<input type="hidden" name="allPoints[${i}].liczbaPos" value="${point.liczbaPos}" /></td>
		                <td><g:checkBox name="allPoints[${i}].czyWybranyZakresUruchomienia" checked="${point.czyWybranyZakresUruchomienia}" /></td>
	                </tr>
	                </g:each>
        		</tbody>
       		</table>
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