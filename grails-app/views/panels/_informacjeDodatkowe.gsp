<%@ page import="com.eservice.eumowy.enums.options.LegalForm" %>

<div id="additionalInformationPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.additional.information.title"/> </div>
            <div class="centre ${hasErrors(bean: data, field: 'dzialalnoscForma', 'errorSpan')}" style="text-align: center; padding-top: 20px; width: 750px">
                <g:hasErrors bean="${data}" field="dzialalnoscForma">
                    <g:eachError bean="${data}" field="dzialalnoscForma">
                        <p class="error-message"><g:message error="${it}"/></p>
                    </g:eachError>
                </g:hasErrors>

                <ul class="table-list centre">
                    <li>
                        <span class="align-left"><g:message code="panel.legal.form"/></span>
                        <span>
                            <g:select name="dzialalnoscForma" noSelection="${['': '']}"
                                      from="${LegalForm.values()}" valueMessagePrefix="legal.form"
                                      value="${data.dzialalnoscForma}"
                                      style="width: 200px;"/>
                        </span>
                        <span><g:message code="panel.other"/></span>
                        <span class="align-right"><g:textField name="dzialalnoscFormaInna" value="${data.dzialalnoscFormaInna}" readonly="${data.dzialalnoscForma != 'inne' && data.dzialalnoscForma != ''}" style="width: 140px;"/></span>
                    </li>
                    <li>
                        <span class="align-left"><g:message code="panel.document"/></span>
                        <span>
                            <g:select name="dzialalnoscDokumentSel"
                                      from="['', 'KRS', 'Wpis do ewidencji', 'Umowa spółki cywilnej']"
                                      keys="['inne', 'krs', 'ewidencja', 'umowa_spolki_cywilnej']"
                                      value="${data.dzialalnoscDokument}"
                                      style="width: 200px;"
                                      disabled="true"/>
                            <g:hiddenField name="dzialalnoscDokument" value="${data.dzialalnoscDokument}"/>
                        </span>
                        <span><g:message code="panel.other"/></span>
                        <span class="align-right"><g:textField name="dzialalnoscDokumentInny" value="${data.dzialalnoscDokumentInny}" readonly="${data.dzialalnoscDokument != 'inne' && data.dzialalnoscDokument != ''}" style="width: 140px;"/></span>
                    </li>
                </ul>
            </div>
    </fieldset>
</div>

<script type="text/javascript">
    jQuery(document).ready(function() {
        jQuery('#dzialalnoscForma').change(function(){

            var result = jQuery("#dzialalnoscForma option:selected").val();
            switch(result){
                case 'STOCK_COMPANY':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'ZOO_COMPANY':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'PARTNERSHIP_COMPANY':
                    selectDoc('umowa_spolki_cywilnej');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'PERSON':
                    selectDoc('ewidencja');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'LIMITED_COMPANY':
                    selectDoc('krs');
                    clearAndReadonlyOtherFields(true, true);
                    break;
                case 'OPEN_COMPANY':
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
</script>
