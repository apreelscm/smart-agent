(function() {
    jQuery('#dzialalnoscForma').change(function(){

        var result = jQuery("#dzialalnoscForma option:selected").val();
        switch(result) {
            case 'PARTNERSHIP_COMPANY':
                selectDoc('umowa_spolki_cywilnej');
                clearAndReadonlyOtherFields(true, true);
                break;
            case 'PERSON':
                selectDoc('ewidencja');
                clearAndReadonlyOtherFields(true, true);
                break;
            default:
                selectDoc('krs');
                clearAndReadonlyOtherFields(true, true);
        }

        function selectDoc(value) {
            jQuery('[name=dzialalnoscDokumentSel] option').filter(function() {
                return (jQuery(this).val() == value);
            }).prop('selected', true);
            jQuery('#dzialalnoscDokument').val(value)
        }

        function clearAndReadonlyOtherFields(clear, disable) {
            var f = jQuery("#dzialalnoscFormaInna");
            var d = jQuery("#dzialalnoscDokumentInny");
            if (clear){
                f.removeAttr("value");
                d.removeAttr("value");
            }

            if (disable) {
                f.attr("readonly", true);
                d.attr("readonly", true);
            } else {
                f.removeAttr("readonly");
                d.removeAttr("readonly");
            }
        }
    });
})();