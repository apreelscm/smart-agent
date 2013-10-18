function refreshTelepomkaAndTelekodzikPercentValues(){
    var showTK = false,
        showTP = false,
        checkedDoladowaniaInPanels = jQuery('div.newPointPanel').find('input.doladowanie:checked'),
        checkedDoladowaniaInFormaDoladowania = jQuery('div#formaDoladowania').find('input.doladowanie:checked'),
        teleKodzikiInPPPaymentPanel = jQuery("div#ppPaymentPanel").find("input.telekodzikValue"),
        telepompkiInPPPPaymentPanel = jQuery("div#ppPaymentPanel").find("input.telepompkaValue");

    checkedDoladowaniaInPanels.each(function(){
        var type = this.getAttribute('data-doladowanie');
        if(type === "telekodzik"){
            showTK = true
        } else {
            showTP = true
        }
    });

    checkedDoladowaniaInFormaDoladowania.each(function(){
        var type = this.getAttribute('data-doladowanie');
        if(type === "telekodzik"){
            showTK = true
        } else {
            showTP = true
        }
    });

    if(showTK){
        teleKodzikiInPPPaymentPanel.each(function(){
            this.value = this.getAttribute('data-value');
        })
    } else {
        teleKodzikiInPPPaymentPanel.each(function(){
            this.value = '';
        })
    }

    if (showTP) {
        telepompkiInPPPPaymentPanel.each(function() {
            this.value = this.getAttribute('data-value');
        })
    } else {
        telepompkiInPPPPaymentPanel.each(function() {
            this.value = '';
        })
    }
}

(function ($) {
    $(function() {

        refreshCityField(jQuery('#akceptantKodPocztowy').val(),  jQuery("#akceptantMiasto"))

        $("#akceptantKodPocztowy").on("keyup", function(e) {
            refreshCityField(jQuery(e.target).val(),  jQuery("#akceptantMiasto"))
        });
    });

    function refreshCityField(code, select){
        var selectValue = select.val()

        select.empty();
        if (code && code.length == 6){
            $.get("/eumowy/activity/getCity", {code: code.replace(/\s+/g, '')}, function(data) {
                var cities = eval('(' + data + ')');

                if(cities instanceof Array){
                    $.each(cities, function(value) {
                        select.append('<option value="'+cities[value]+'">'+cities[value]+'</option>')
                    });
                }
                select.val(selectValue)
            });
        }else{
            select.val('')
        }
    }
}(jQuery));