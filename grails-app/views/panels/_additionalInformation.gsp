<div id="additionalInformationPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.additional.information.title"/> </div>
            <div style="text-align: center; padding-top: 20px" class="centre">
                <div>
                    <div style="display: inline-block; text-align: right">
                           <div>
                               <div style="display:inline-block; "><g:message code="panel.legal.form"/></div>
                               <select name="legalForm" id="legalForm" style="width: 200px;">
                                   <option value="lfEmpty"></option>
                                   <option value="lfSa">Spółka akcyjna</option>
                                   <option value="lfSzoo">Spółka z o.o.</option>
                                   <option value="lfSc">Spółka cywilna</option>
                                   <option value="lfOf">Osoba fizyczna</option>
                                   <option value="lfSk">Spółka komandytowa</option>
                               </select>
                           </div>
                           <div>
                               <div style="display:inline-block; "><g:message code="panel.document"/></div>
                               <select name="aIDocument" id="aIDocument" style="width: 200px;" disabled="disabled">
                                   <option value="docEmpty"></option>
                                   <option value="docKRS">KRS</option>
                                   <option value="docEwidencja">Wpis do ewidencji</option>
                               </select>
                           </div>
                    </div>
                    <div style="display: inline-block; padding-left: 20px">
                            <div>
                                <div style="display:inline-block; "><g:message code="panel.other"/></div>
                                <div style="display:inline-block; "><g:textField name="legalFormOther" style="width: 70px;"/></div>
                            </div>
                            <div>
                                <div style="display:inline-block; "><g:message code="panel.other"/></div>
                                <div style="display:inline-block; "><g:textField name="docOther" style="width: 70px;"/></div>
                            </div>
                    </div>
                </div>
            </div>
    </fieldset>
</div>

<r:script>
    jQuery(document).ready(function() {
        jQuery('#legalForm').change(function(){

            var result = jQuery("#legalForm option:selected").val();
            switch(result){
                case 'lfEmpty':
                    selectDoc('docEmpty');
                    clearAndDisableOtherFields(false, false);
                    break;
                case 'lfSa':
                    selectDoc('docKRS');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'lfSzoo':
                    selectDoc('docKRS');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'lfSc':
                    selectDoc('docKRS');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'lfOf':
                    selectDoc('docEwidencja');
                    clearAndDisableOtherFields(true, true);
                    break;
                case 'lfSk':
                    selectDoc('docKRS');
                    clearAndDisableOtherFields();
                    break;
            }

            function selectDoc(value){
                jQuery('[name=aIDocument] option').filter(function() {
                    return (jQuery(this).val() == value);
                }).prop('selected', true);
            }

            function clearAndDisableOtherFields(clear, disable){
                var f = jQuery("#legalFormOther");
                var d = jQuery("#docOther");
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