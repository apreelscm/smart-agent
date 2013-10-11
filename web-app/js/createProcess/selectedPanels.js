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