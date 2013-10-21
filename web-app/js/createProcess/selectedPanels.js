function refreshTelepomkaAndTelekodzikPercentValues(){
    console.log('click');
    var isActiveTK = false,
        isActiveTP = false,
        checkedDoladowaniaInPanels = jQuery('div.newPointPanel').find('input.doladowanie:checked'),
        checkedDoladowaniaInFormaDoladowania = jQuery('div#formaDoladowania').find('input.doladowanie:checked'),
        teleKodzikiInPPPaymentPanel = jQuery("div#ppPaymentPanel").find("div.telekodzikValue"),
        telepompkiInPPPPaymentPanel = jQuery("div#ppPaymentPanel").find("div.telepompkaValue"),
        hasActiveAtLeastOneDoladowanie = jQuery("input#hasActiveAtLeastOneDoladowanie");

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
        teleKodzikiInPPPaymentPanel.parent().addClass('visibility-hidden');
    } else {
        teleKodzikiInPPPaymentPanel.parent().removeClass('visibility-hidden');
    }

    if(isActiveTK){
        telepompkiInPPPPaymentPanel.parent().addClass('visibility-hidden');
    } else {
        telepompkiInPPPPaymentPanel.parent().removeClass('visibility-hidden');
    }

}