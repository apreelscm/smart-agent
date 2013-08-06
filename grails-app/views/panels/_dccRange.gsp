<div id="dccRangePanel">
    <fieldset>
        <div class="belka-glowna">DCC - Zakres uruchomienia</div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
            <div style="text-align: left">
                <p><input type="radio" name="dccRange" id="currentAndNew" /> na wszystkich Zestawach POZ aktualnie zainstalowanych u Akceptanta i wszystkich nowych</p>
                <p><input type="radio" name="dccRange" id="current" /> na wszystkich Zestawach POZ aktualnie zainstalowanych u Akceptanta</p>
                <p><input type="radio" name="dccRange" id="direct" /> w urządzeniach akceptujących karty w punktach handlowo usługowych w lokalizacjach:</p>
            </div>
        </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 800px; max-width: 950px">
            <table class="t">
                <thead>
                <tr>
                    <td>Pełna Nazwa Punktu</td>
                    <td>Ulica</td>
                    <td>Miasto</td>
                    <td>Numer<br>domu</td>
                    <td>Kod</td>
                    <td>Liczba<br>POZ</td>
                    <td>&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Skleb wielobranżowy "Jacek i Agatka"</td>
                    <td>Zielona</td>
                    <td>Warszawa</td>
                    <td>34</td>
                    <td>09-876</td>
                    <td>2</td>
                    <td><input type="checkbox" name="dccRangePOS" value="1" id="r_1"/> </td>
                </tr>
                <tr>
                    <td>Kwiaciarnia "U Zosi"</td>
                    <td>Zielona</td>
                    <td>Siedlce</td>
                    <td>34</td>
                    <td>09-876</td>
                    <td>2</td>
                    <td><input type="checkbox" name="dccRangePOS" value="2" id="r_2"/> </td>
                </tr>
                <tr>
                    <td>PHU "Kluczyk"</td>
                    <td>Jasia i Małgosi</td>
                    <td>Szczecin</td>
                    <td>34</td>
                    <td>11-112</td>
                    <td>2</td>
                    <td><input type="checkbox" name="dccRangePOS" value="3" id="r_3"/> </td>
                </tr>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>

<r:require module="jquery_ui"/>

<r:script>

    jQuery(document).ready(function() {
        jQuery('input[name="dccRange"]').change(function(){
            if (jQuery("#currentAndNew").attr("checked")){
                enableCheckbox(false);
            } else if (jQuery("#current").attr("checked")){
                enableCheckbox(false);
            } else {
                enableCheckbox(true);
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