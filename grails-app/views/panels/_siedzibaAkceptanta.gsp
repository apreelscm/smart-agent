<div id="acceptorAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.address.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span><g:message code="panel.street" /></span>
                        <span>
                            <dict:streetSelect name="akceptantUlicaTytul" value="${data.akceptantUlicaTytul}" readonly="${data.isFromCbd('akceptantUlicaTytul')}"/>
                            <g:hiddenField name="akceptantUlicaTytulCbd" value="${data.akceptantUlicaTytulCbd}"/>
                            <eumowy:textField name="akceptantUlica" value="${data.akceptantUlica}" validatable="${data}" readonly="${data.isFromCbd('akceptantUlica')}" style="width: 200px" maxlength="19" required="true"/>
                            <g:hiddenField name="akceptantUlicaCbd" value="${data.akceptantUlicaCbd}"/>
                        </span>
                        <span>
                            <span>
                                <g:message code="panel.house.number" /></span> <span><eumowy:textField name="akceptantNrDomu"  value="${data.akceptantNrDomu}" validatable="${data}" readonly="${data.isFromCbd('akceptantNrDomu')}" style="width: 50px" maxlength="4" required="true"/>
                                <g:hiddenField name="akceptantNrDomuCbd" value="${data.akceptantNrDomuCbd}"/>
                            </span>
                            <span>
                                <g:message code="panel.flat.number" /></span> <span><eumowy:textField name="akceptantNrMieszkania" value="${data.akceptantNrMieszkania}" validatable="${data}" readonly="${data.isFromCbd('akceptantNrMieszkania')}" style="width: 50px" maxlength="4" required="true"/>
                                <g:hiddenField name="akceptantNrMieszkaniaCbd" value="${data.akceptantNrMieszkaniaCbd}"/>
                            </span>
                        </span>
                    </span>
                </li>
                <li>
                    <span>
                    <span><g:message code="panel.city" /></span>
                    <span>
                        <eumowy:textField name="akceptantMiasto" value="${data.akceptantMiasto}" validatable="${data}" readonly="${data.isFromCbd('akceptantMiasto')}" style="width: 280px;" maxlength="19" required="true"/>
                        <g:hiddenField name="akceptantMiastoCbd" value="${data.akceptantMiastoCbd}"/>
                    </span>
                    <span>
                        <span>
                            <g:message code="panel.postal.code" /></span> <span><eumowy:textField class="postal-code" name="akceptantKodPocztowy" value="${data.akceptantKodPocztowy}" validatable="${data}" readonly="${data.isFromCbd('akceptantKodPocztowy')}" style="width: 50px" maxlength="5" required="true"/>
                            <g:hiddenField name="akceptantKodPocztowyCbd" value="${data.akceptantKodPocztowyCbd}"/>
                        </span>
                    </span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.postal" /></span>
                        <span>
                            <eumowy:textField name="akceptantPoczta" value="${data.akceptantPoczta}" validatable="${data}" readonly="${data.isFromCbd('akceptantPoczta')}" style="width: 280px;" maxlength="19" required="true"/>
                            <g:hiddenField name="akceptantPocztaCbd" value="${data.akceptantPocztaCbd}"/>
                        </span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:message code="panel.landline.phone.number"/></span>
                        <span>
                            <eumowy:textField class="phone" name="akceptantTelStacjonarny" value="${data.akceptantTelStacjonarny}" validatable="${data}" readonly="${data.isFromCbd('akceptantTelStacjonarny')}" style="width: 100px;" maxlength="9"/>
                            <g:hiddenField name="akceptantTelStacjonarnyCbd" value="${data.akceptantTelStacjonarnyCbd}"/>
                        </span>
                        <span><g:message code="panel.fax"/></span>
                        <span>
                            <g:textField class="fax" name="akceptantFax" value="${data.akceptantFax}" readonly="${data.isFromCbd('akceptantFax')}" style="width: 100px" maxlength="9"/>
                            <g:hiddenField name="akceptantFaxCbd" value="${data.akceptantFaxCbd}"/>
                        </span>
                        <span><g:message code="panel.mobile.phone.number"/></span>
                        <span>
                            <eumowy:textField class="mobile-phone" name="akceptantTelKomorkowy" value="${data.akceptantTelKomorkowy}" validatable="${data}" readonly="${data.isFromCbd('akceptantTelKomorkowy')}" style="width: 100px" maxlength="9"/>
                            <g:hiddenField name="akceptantTelKomorkowyCbd" value="${data.akceptantTelKomorkowyCbd}"/>
                        </span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>