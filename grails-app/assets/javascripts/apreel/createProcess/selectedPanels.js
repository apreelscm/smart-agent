//= require_tree vendors/jquery-ui
//= require vendors/filestyle.min
//= require setup.newpoint.panel
//= require apreel/mask
//= require apreel/validation_utils

var panelInternalCount = {
    _value: 0,
    get value() { return this._value; },
    set value(obj) { this._value = obj; /*Listener code can go here*/
        evaluateSredniObrot()
    }
};

function evaluateSredniObrot(){
    var divideBy = parseInt($j("#liczbaPtkCbd").val()) + panelInternalCount.value
    var sredniObrot = divideBy ? parseFloat($j("#progrnozaMiesieczna").val()) / divideBy : 0
    $j("#scoringDeklaracjaFinansowaSredniObrot").val(Math.round(sredniObrot * 100)/100)
}


function refreshTelepomkaAndTelekodzikPercentValues(){
    var isActiveTK = false,
        isActiveTP = false,
        teleKodzikiInPPPaymentPanel = jQuery("div#ppPaymentPanel").find("div.telekodzikValue"),
        telepompkiInPPPPaymentPanel = jQuery("div#ppPaymentPanel").find("div.telepompkaValue");

    manageTKAndTPCheckedProperty();

    var checkedDoladowaniaInPanels = jQuery('div.newPointPanel').find('input.doladowanie:checked'),
        checkedDoladowaniaInFormaDoladowania = jQuery('div#formaDoladowania').find('input.doladowanie:checked');

    checkedDoladowaniaInPanels.each(function(){
        var type = this.getAttribute('data-doladowanie');
        if(type === "telekodzik"){
            isActiveTK = true
        } else {
            isActiveTP = true
        }
    });

    checkedDoladowaniaInFormaDoladowania.each(function(){
        var type = this.getAttribute('data-doladowanie');
        if(type === "telekodzik"){
            isActiveTK = true
        } else {
            isActiveTP = true
        }
    });

    jQuery("#hasAtLeastOneDoladowanie").val(isActiveTK || isActiveTP);

    if(isActiveTK && isActiveTP){
        teleKodzikiInPPPaymentPanel.parent().removeClass('visibility-hidden');
        telepompkiInPPPPaymentPanel.parent().removeClass('visibility-hidden');
        return;
    }

    if(isActiveTP){
        telepompkiInPPPPaymentPanel.parent().removeClass('visibility-hidden');
    } else {
        telepompkiInPPPPaymentPanel.parent().addClass('visibility-hidden');
    }

    if(isActiveTK){
        teleKodzikiInPPPaymentPanel.parent().removeClass('visibility-hidden');
    } else {
        teleKodzikiInPPPaymentPanel.parent().addClass('visibility-hidden');
    }
}

function manageTKAndTPCheckedProperty(){
    var isMainTKChecked = jQuery('input.mainDoladowanieTK').prop('checked'),
        isMainTPChecked = jQuery('input.mainDoladowanieTP').prop('checked'),
        telekodzikiInPointPanels = jQuery('div.newPointPanel').find("[data-doladowanie='telekodzik']"),//TODO: refactor
        telekodzikiInPosPanels = jQuery('div.newPosPanel').find("[data-doladowanie='telekodzik']"),
        telepompkiInPointPanels = jQuery('div.newPointPanel').find("[data-doladowanie='telepompka']"),
        telepompkiInPosPanels = jQuery('div.newPosPanel').find("[data-doladowanie='telepompka']");

    if(isMainTKChecked === undefined || isMainTPChecked === undefined){  //checkboxy nie istnieja
        return;
    }

    if(isMainTKChecked && isMainTPChecked){
        telekodzikiInPointPanels.removeAttr('disabled');
        telekodzikiInPosPanels.removeAttr('disabled');
        telepompkiInPointPanels.removeAttr('disabled');
        telepompkiInPosPanels.removeAttr('disabled');
    } else {
        if(isMainTKChecked){
            telepompkiInPointPanels.attr('disabled', 'disabled');
            telepompkiInPointPanels.removeAttr('checked');
            telepompkiInPosPanels.attr('disabled', 'disabled');
            telepompkiInPosPanels.removeAttr('checked');
        } else {
            telepompkiInPosPanels.removeAttr('disabled');
            telepompkiInPointPanels.removeAttr('disabled');
        }

        if(isMainTPChecked){
            telekodzikiInPointPanels.attr('disabled', 'disabled');
            telekodzikiInPointPanels.removeAttr('checked');
            telekodzikiInPosPanels.attr('disabled', 'disabled');
            telekodzikiInPosPanels.removeAttr('checked');
        } else {
            telekodzikiInPointPanels.removeAttr('disabled');
            telekodzikiInPosPanels.removeAttr('disabled');
        }
    }
}

var $j = jQuery.noConflict();

(function ($) {
    $(function() {
        refreshCityField(jQuery('#akceptantKodPocztowy').val(),  jQuery("#akceptantMiasto"))
        refreshCityField(jQuery('#akceptantKontaktKodPocztowy').val(),  jQuery("#akceptantKontaktMiasto"))
        refreshCityField(jQuery('#wydrukKodPocztowy').val(),  jQuery("#wydrukMiasto"))
        //TODO - nie ma pol o takim id !!! To sa pola z punktu!!
        refreshOpiekaSerwisowa(jQuery('#wydrukKodPocztowy'), jQuery("#opiekaSerwisowaI"), jQuery("#opiekaSerwisowaII"), jQuery("#opiekaSerwisowaIII"))

        $("#akceptantKodPocztowy").on("keyup", function(e) {
            refreshCityField(jQuery(e.target).val(),  jQuery("#akceptantMiasto"))
        });

        $("#akceptantKontaktKodPocztowy").on("keyup", function(e) {
            refreshCityField(jQuery(e.target).val(),  jQuery("#akceptantKontaktMiasto"))
        });

        $("#wydrukKodPocztowy").on("keyup", function(e) {
            refreshCityField(jQuery(e.target).val(),  jQuery("#wydrukMiasto"))
            //TODO - nie ma pol o takim id !!! To sa pola z punktu!!
            refreshOpiekaSerwisowa(jQuery(e.target).val(), jQuery("#opiekaSerwisowaI"), jQuery("#opiekaSerwisowaII"), jQuery("#opiekaSerwisowaIII"))
        });
    });
}(jQuery));


function refreshCityField(code, select, spinner){
    var selectValue = select.val()

    select.empty();
    if (code && code.length == 6){
        if(spinner){
            spinner.removeClass('visibility-hidden');
        }
        $j.get("/eumowy/activity/getCity", {code: code.replace(/\s+/g, '')}, function(cities) {
            if (cities.length > 0) {
                $j.each(cities, function (city) {
                    select.append('<option value="' + cities[city] + '">' + cities[city] + '</option>')
                });
            } else {
                showNoCitiesDialog(this);
            }

            select.val(selectValue)
            select.removeClass("error")

            if (spinner) {
                spinner.addClass('visibility-hidden');
            }
        });
    }else{
        select.val('')
    }
}

function refreshOpiekaSerwisowa(code, opiekaOne, opiekaTwo, installer){
    if (code && code.length == 6){
        $j.get("/eumowy/activity/getOpiekaSerwisowa", {code: code.replace(/\s+/g, '')}, function(data) {
            opiekaOne.val(data.opiekaOneCode);
            opiekaTwo.val(data.opiekaTwoCode);
            installer.val(data.opiekaOneCode);
        });
    }else{
        opiekaOne.val('')
        opiekaTwo.val('')
        installer.val('')
    }
}

function setFieldPropertiesInDodatkoweWyposazenie(element, value, setForPosPanel){
    var panel;
    if(setForPosPanel){
        panel = jQuery(element).closest("div.newPosPanel");
    } else {
        panel = jQuery(element).closest("div.newPointPanel");
    }
    if(value == "Verifone Vx670 GPRS" || value == "INGENICO IWL220C"){
        panel.find("tr.baseRow").show();
        setRequiredForSimCard(true, panel)
    } else {
        var id = jQuery(element).attr('id');
        var bazaXPath = id.substring(0,id.indexOf('.')) + ".bazaIlosc";
        jQuery("[name='"+bazaXPath+"']").val("");
        panel.find("tr.baseRow").hide();
        if (value !== ""){
            setRequiredForSimCard(true, panel)
        }
        else {
            setRequiredForSimCard(false, panel)
        }
    }

    function setRequiredForSimCard(isRequired, panel){
        if(isRequired){
            panel.find("select.kartaSimTyp").attr("required", true);
            panel.find("input.kartaSimIlosc").attr("required", true);
        } else {
            panel.find("select.kartaSimTyp").removeAttr("required", true).removeClass("error");
            panel.find("input.kartaSimIlosc").removeAttr("required", true).removeClass("error");
        }
    }
}

jQuery(function() {
    jQuery("#representatives\\[0\\]\\.imie").attr('required', 'required');
    jQuery("#representatives\\[0\\]\\.nazwisko").attr('required', 'required');
});

