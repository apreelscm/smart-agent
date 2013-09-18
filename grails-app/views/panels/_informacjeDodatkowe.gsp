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
                        <span class="align-right"><g:textField name="dzialalnoscFormaInna" value="${data.dzialalnoscFormaInna}" readonly="${data.dzialalnoscForma != ''}" style="width: 140px;"/></span>
                    </li>
                    <li>
                        <span class="align-left"><g:message code="panel.document"/></span>
                        <span>
                            <g:select name="dzialalnoscDokumentSel"
                                      from="['', 'KRS', 'Wpis do ewidencji']"
                                      keys="['', 'krs', 'ewidencja']"
                                      value="${data.dzialalnoscDokument}"
                                      style="width: 200px;"
                                      disabled="true"/>
                            <g:hiddenField name="dzialalnoscDokument" value="${data.dzialalnoscDokument}"/>
                        </span>
                        <span><g:message code="panel.other"/></span>
                        <span class="align-right"><g:textField name="dzialalnoscDokumentInny" value="${data.dzialalnoscDokumentInny}" readonly="${data.dzialalnoscDokument != ''}" style="width: 140px;"/></span>
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
                    clearAndReadonlyOtherFields(false, false);
                    break;
                case 'spolka_akcyjna':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'spolka_zoo':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'spolka_cywilna':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'osoba_fizyczna':
                    selectDoc('ewidencja');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'spolka_komandytowa':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
            }

            function selectDoc(value){
                jQuery('[name=dzialalnoscDokumentSel] option').filter(function() {
                    return (jQuery(this).val() == value);
                }).prop('selected', true);
                jQuery('#dzialalnoscDokument').val(value)
            }

            function clearAndReadonlyOtherFields(clear, disable){
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
    });
</r:script>