<div id="posDiscountPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.pos.discount.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <g:hiddenField id="discountCount" name="discountCount" value="${data.allPoses.size()}"/>
			<table class="t">
				<thead style="text-align: center !important;">
					<tr>
						<td><g:message code="panel.number" /></td>
						<td><g:message code="panel.pos.discount.possetnumber" /></td>
						<td><g:message code="panel.pos.discount.datefrom" /></td>
						<td><g:message code="panel.pos.discount.dateto" /></td>
						<td><g:message code="panel.pos.discount.fee.amount" /></td>
						<td><g:message code="panel.pos.discount.choosen" /></td>
					</tr>
				</thead>
				<tbody>
					<g:each status="i" var="pos" in="${data.allPoses}" >
                        <tr>
                            <td>${pos.id}<input type="hidden" name="allPoses[${i}].id" value="${pos.id}" /><input type="hidden" name="allPoses[${i}].tpsId" value="${pos.tpsId}" /><input type="hidden" name="allPoses[${i}].cbdId" value="${pos.cbdId}" /></td>
                            <td>${pos.numerZestawuPos}<input type="hidden" name="allPoses[${i}].numerZestawuPos" value="${pos.numerZestawuPos}" /></td>
                            <td><eumowy:textField name="allPoses[${i}].dataOd" value="${g.formatDate( format:"yyyy-MM-dd", date:pos.dataOd)}" validatable="${pos.dataOd}" readonly="true"/></td>
                            <td><eumowy:textField name="allPoses[${i}].dataDo" value="${g.formatDate( format:"yyyy-MM-dd", date:pos.dataDo)}" validatable="${pos.dataDo}" readonly="true"/></td>
                            <td><eumowy:textField name="allPoses[${i}].wysokoscOplaty" value="${pos.wysokoscOplaty}" class="float-number"  validatable="${pos.wysokoscOplaty}"/></td>
                            <td><g:checkBox name="allPoses[${i}].czyWybrany" checked="${pos.czyWybrany}"/></td>
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
        var discCount = parseInt(jQuery("#discountCount").val());

        for (var i =0; i< discCount; i++){
            var start = jQuery('#allPoses\\['+ i +'\\]\\.dataOd');
            start.datepicker({
                dateFormat: 'yy-mm-dd',
                minDate: getFirstDayOfNextMonth(),
                beforeShowDay: function(date)
                {
                    if (date.getDate() == getFirstDayOfYearAndMonth(date.getFullYear(), date.getMonth()))
                    {
                        return [true, ''];
                    }
                    return [false, ''];
                }});

            new function (s) {
                s.on("change", function(event){
                    selectAllFields(new Date(event.target.value), discCount, 1);
                });
            }(start);

            new function (s) {
                console.log(s);
                s.on("change", function (){
                    var count = getCheckedCount(discCount);
                    if (count>=5){
                        disableCheckboxes(discCount);
                    } else {
                        enableCheckboxes(discCount);
                    }
                });
            } (jQuery('#allPoses\\['+ i +'\\]\\.czyWybrany'));

        }
    });

    function getLastDayOfYearAndMonth(year, month, monthsToAdd){
        var normalizeMonths = monthsToAdd -1;
        return new Date((new Date(year, month + 1 + normalizeMonths, 1)) - 1);
    }

    function getFirstDayOfYearAndMonth(year, month){
        return (new Date(year, month + 1, 1)).getDate();
    }

    function getFirstDayOfNextMonth(){
        var d = new Date();
        return new Date(d.getFullYear(), d.getMonth()+1, 1);
    }

    function selectAllFields(beginDate, count, monthsToAdd){
        var endDate = getLastDayOfYearAndMonth(beginDate.getFullYear(), beginDate.getMonth(), monthsToAdd);
        var dateStr = formatMyDate(endDate)

        for (var i=0; i< count; i++){
            jQuery('#allPoses\\['+ i +'\\]\\.dataOd').datepicker('setDate', beginDate);
            jQuery('#allPoses\\['+ i +'\\]\\.dataDo').val(dateStr);
        }
    }

    function formatMyDate(endDate){
        var month = endDate.getMonth()+1;
        var day = endDate.getDate();

        var monthStr = month<10?'0'+month:month;
        var dayStr = day<10?'0'+day:day;

        return endDate.getFullYear()+'-'+ monthStr+'-'+dayStr;
    }

    function getCheckedCount(discCount){
        var counter = 0;
        for (var i = 0; i<discCount; i++){
            if (jQuery('#allPoses\\['+ i +'\\]\\.czyWybrany').prop('checked')){
                counter++;
            };
        }
        return counter;
    }

    function disableCheckboxes(discCount){
        for (var i = 0; i<discCount; i++){
            var check = jQuery('#allPoses\\['+ i +'\\]\\.czyWybrany');
            if (!check.prop('checked')){
                check.attr("disabled", true);
            };
        }
    }

    function enableCheckboxes(discCount){
        for (var i = 0; i<discCount; i++){
            jQuery('#allPoses\\['+ i +'\\]\\.czyWybrany').removeAttr("disabled");
        }
    }

</r:script>