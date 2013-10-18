<r:script>
    jQuery(document).ready(function() {
        var fields = new Array("akceptantUlica", "akceptantNrDomu", "akceptantMiasto", "akceptantKodPocztowy", "akceptantPoczta"),
            clientCbdId = ${client?.cbdId ? client?.cbdId : 'null'};

        if(clientCbdId !== null && clientCbdId !== ""){
            for(var i in fields){
                jQuery("#" + fields[i]).removeAttr('required');
            }
        }
    });
</r:script>
<div id="acceptorAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.address.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect name="akceptantUlicaTytul" value="${data.akceptantUlicaTytul}" readonly="${data.checkIfFromCbd('akceptantUlicaTytul')}" default="UL"/>
                            <g:hiddenField name="akceptantUlicaTytulCbd" value="${data.akceptantUlicaTytulCbd}"/>
                            <eumowy:textField name="akceptantUlica" value="${data.akceptantUlica}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantUlica')}" style="width: 200px" required="true"/>
                            <g:hiddenField name="akceptantUlicaCbd" value="${data.akceptantUlicaCbd}"/>
                        </span>
                        <span>
                            <span>
                                <g:message code="panel.house.number" /></span> <span><eumowy:textField name="akceptantNrDomu"  value="${data.akceptantNrDomu}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantNrDomu')}" style="width: 50px" maxlength="4" required="true"/>
                                <g:hiddenField name="akceptantNrDomuCbd" value="${data.akceptantNrDomuCbd}"/>
                            </span>
                            <span>
                                <g:message code="panel.flat.number" /></span> <span><eumowy:textField name="akceptantNrMieszkania" value="${data.akceptantNrMieszkania}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantNrMieszkania')}" style="width: 50px" maxlength="4"/>
                                <g:hiddenField name="akceptantNrMieszkaniaCbd" value="${data.akceptantNrMieszkaniaCbd}"/>
                            </span>
                        </span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.city" /></span>
                        <span>
                            <g:set var="isCityDisabled" value="${data.isFromCbd('akceptantMiasto')}"/>

                            <g:hiddenField name="akceptantMiastoCbd" value="${data.akceptantMiastoCbd}"/>
                            <eumowy:textField id="akceptantMiasto" name="akceptantMiasto" value="${data.akceptantMiasto}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantMiasto')}" style="width: 280px;" required="true"/>

                            %{--CR - to uncomment--}%
                         %{--   <g:if test="${isCityDisabled}">
                                <eumowy:textField name="akceptantMiasto" value="${data.akceptantMiasto}" validatable="${data}" readonly="true" style="width: 280px;" required="true"/>
                            </g:if>
                            <g:else>
                                <g:select id="akceptantMiasto" name="akceptantMiasto" value="${data.akceptantMiasto}" from=""
                                          style="width: 280px;"  required="required"/>
                            </g:else>--}%
                        </span>
                        <span>
                            <span>
                                <g:message code="panel.postal.code" /></span> <span><eumowy:textField id="akceptantKodPocztowy" class="postal-code" name="akceptantKodPocztowy" value="${data.akceptantKodPocztowy}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantKodPocztowy')}" style="width: 50px" maxlength="5" required="true"/>
                        <g:hiddenField name="akceptantKodPocztowyCbd" value="${data.akceptantKodPocztowyCbd}"/>
                        </span>
                        </span>
                    </span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.postal" /></span>
                        <span>
                            <eumowy:textField name="akceptantPoczta" value="${data.akceptantPoczta}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantPoczta')}" style="width: 280px;" required="true"/>
                            <g:hiddenField name="akceptantPocztaCbd" value="${data.akceptantPocztaCbd}"/>
                        </span>
                    </span>
                </li>
                <li>
                    <span>

                        <g:hiddenField name="hasAkceptantTel" value="true"/>
                        <g:hiddenField name="akceptantTelStacjonarnyCbd" value="${data.akceptantTelStacjonarnyCbd}"/>
                        <g:hiddenField name="akceptantTelKomorkowyCbd" value="${data.akceptantTelKomorkowyCbd}"/>
                        <g:hiddenField name="akceptantFaxCbd" value="${data.akceptantFaxCbd}"/>

                        <span class="${hasErrors(bean:data,field:'hasAkceptantTel','errorSpan')}">
                            <div style="display: inline"><g:message code="panel.landline.phone.number"/> <eumowy:textField class="phone" name="akceptantTelStacjonarny" value="${data.akceptantTelStacjonarny}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantTelStacjonarny')}" style="width: 100px;" maxlength="9"/></div>
                            <div style="display: inline; margin-left: 5px"><g:message code="panel.mobile.phone.number"/> <eumowy:textField class="mobile-phone" name="akceptantTelKomorkowy" value="${data.akceptantTelKomorkowy}" validatable="${data}" readonly="${data.checkIfFromCbd('akceptantTelKomorkowy')}" style="width: 100px" maxlength="9"/></div>
                        </span>

                        <span>
                            <g:message code="panel.fax"/> <g:textField class="fax" name="akceptantFax" value="${data.akceptantFax}" readonly="${data.checkIfFromCbd('akceptantFax')}" style="width: 100px" maxlength="9"/>
                        </span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>