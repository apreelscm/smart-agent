<div id="additionalInformationPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.additional.information.title"/> </div>
            <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
                <ul class="table-list centre">
                    <li>
                        <span class="align-left"><g:message code="panel.legal.form"/></span>
                        <span>
                            <g:select name="dzialalnoscForma"
                                      from="['', 'Spółka akcyjna', 'Spółka z o.o.', 'Spółka cywilna', 'Osoba fizyczna', 'Spółka komandytowa']"
                                      keys="['', 'spolka_akcyjna', 'spolka_zoo', 'spolka_cywilna', 'osoba_fizyczna', 'spolka_komandytowa']"
                                      value="${data.dzialalnoscForma}"
                                      style="width: 200px;"/>
                        </span>
                        <span><g:message code="panel.other"/></span>
                        <span class="align-right"><g:textField name="dzialalnoscFormaInna" value="${data.dzialalnoscFormaInna}" style="width: 70px;"/></span>
                    </li>
                    <li>
                        <span class="align-left"><g:message code="panel.document"/></span>
                        <span>
                            <g:select name="dzialalnoscDokument"
                                      from="['', 'KRS', 'Wpis do ewidencji']"
                                      keys="['', 'krs', 'ewidencja']"
                                      value="${data.dzialalnoscDokument}"
                                      style="width: 200px;"
                                      disabled="disabled"/>
                        </span>
                        <span><g:message code="panel.other"/></span>
                        <span class="align-right"><g:textField name="dzialalnoscDokumentInny" value="${data.dzialalnoscDokumentInny}" style="width: 70px;"/></span>
                    </li>
                </ul>
            </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {
        jQuery('#dzialalnoscForma').change(function(){

            var result = jQuery("#dzialalnoscForma option:selected").val();
            switch(result){
                case '':
                    selectDoc('');
                    clearAndDisableOtherFields(false, false);
                    break;
                case 'spolka_akcyjna':
                    selectDoc('krs');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'spolka_zoo':
                    selectDoc('krs');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'spolka_cywilna':
                    selectDoc('krs');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'osoba_fizyczna':
                    selectDoc('ewidencja');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'spolka_komandytowa':
                    selectDoc('krs');
                    clearAndDisableOtherFields();
                    break;
            }

            function selectDoc(value){
                jQuery('[name=dzialalnoscDokument] option').filter(function() {
                    return (jQuery(this).val() == value);
                }).prop('selected', true);
            }

            function clearAndDisableOtherFields(clear, disable){
                var f = jQuery("#dzialalnoscFormaInna");
                var d = jQuery("#dzialalnoscDokumentInny");
                if (clear){
                    f.val("");
                    d.val("");
                }

                if (disable) {
                    f.attr("disabled", true);
                    d.attr("disabled", true);
                } else {
                    f.removeAttr("disabled");
                    d.removeAttr("disabled");
                }
            }
        });
    });
</r:script>