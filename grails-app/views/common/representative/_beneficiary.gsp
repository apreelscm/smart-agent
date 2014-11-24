<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<div>
    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'typLokalizacji', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="typLokalizacji">
            <g:eachError bean="${representative}" field="typLokalizacji">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].detail" value="PESEL"
                     checked="${representative?.detail}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}"
                              maxlength="11" class="pesel-field display-inline-block" disabled="${representative?.isRepresentativeLocationAbroad()}"
                              validatable="${representative}" validateField="lokalizacjaPesel"/>
        </div>

        <div class="acceptorRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].detail" value="COUNTRY_CODE"
                     checked="${representative?.detail}"/>
            <div class="label"><g:message code="country.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].kodKraju" value="${representative?.kodKraju}"
                              maxlength="30" class="display-inline-block" disabled="${representative?.isRepresentativeLocationAbroad() == false}"
                              validatable="${representative}" validateField="lokalizacjaKraj"/>
        </div>
    </div>

    <div class="acceptorDocumentTypeWrapper ${hasErrors(bean: representative, field: 'typDokumentu', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="typDokumentu">
            <g:eachError bean="${representative}" field="typDokumentu">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].typDokumentu"
                               radioWrapperClass="inlineRadioWrapper display-inline-block" value="${representative?.typDokumentu}"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].seriaNrDokumentu"><g:message code="identity.card.details"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].seriaNrDokumentu" value="${representative?.seriaNrDokumentu}" maxlength="20"
                          validatable="${representative}" validateField="seriaNrDokumentu"/>

        <label for="${prefix}[${seqNo}].obywatelstwo"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].obywatelstwo" value="${representative?.obywatelstwo}" maxlength="30"
                          validatable="${representative}" validateField="obywatelstwo"/>

        <label for="${prefix}[${seqNo}].dataUrodzenia"><g:message code="birth.date.label"/></label>
        <g:textField name="${prefix}[${seqNo}].dataUrodzenia" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.dataUrodzenia)}"
                     maxlength="10" class="date-field"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].adres"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].adres" value="${representative?.adres}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="adres"/>
    </div>

    <div class="isPolitician ${representative?.isRepresentativeLocationAbroad() ?: 'hidden'}">
        <g:radioGroup values="[true, false]" name="${prefix}[${seqNo}].czyStanowiskoPolityczne" value="${representative?.czyStanowiskoPolityczne}"
                      labels="['i.am', 'i.am.not']">
            <div class="acceptorRadioWrapper">
                ${it.radio}
                <div class="label"><g:message code="${it.label}"/></div>
            </div>
        </g:radioGroup>
    </div>

    <div class="${hasErrors(bean: representative, field: 'czyStanowiskoPolityczne', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="czyStanowiskoPolityczne">
            <g:eachError bean="${representative}" field="czyStanowiskoPolityczne">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <label><g:message code="political.position.label"/></label>
        <g:checkBox name="${prefix}[${seqNo}].czyStanowiskoPolityczne" checked="${representative?.czyStanowiskoPolityczne}"/>
    </div>
</div>

<div style="margin-top: 25px">
    <p><g:message code="beneficiary.relation.with.acceptor.label"/></p>

    <div class="${hasErrors(bean: representative, field: 'posiadaAkceptanta', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="posiadaAkceptanta">
            <g:eachError bean="${representative}" field="posiadaAkceptanta">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].posiadaAkceptanta" checked="${representative?.posiadaAkceptanta}"/>
            <label for="beneficiaries[${seqNo}].posiadaAkceptanta"><g:message code="beneficiary.owns.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].kontrolujeAkceptanta" checked="${representative?.kontrolujeAkceptanta}"/>
            <label for="beneficiaries[${seqNo}].kontrolujeAkceptanta"><g:message code="beneficiary.controls.acceptor.label"/></label>
        </div>

        <div>
            <g:checkBox name="beneficiaries[${seqNo}].znaczaceUdzialy" checked="${representative?.znaczaceUdzialy}"/>
            <label for="beneficiaries[${seqNo}].znaczaceUdzialy"><g:message code="beneficiary.majority.acceptor.label"/></label>

            <eumowy:textField name="beneficiaries[${seqNo}].procentUdzialow" class="percent-short"
                              value="${representative?.znaczaceUdzialy ? representative?.procentUdzialow : ""}"
                              validatable="${representative}" validateField="procentUdzialow"/>
            <g:message code="beneficiary.majority.acceptor.closing.label"/>
        </div>
    </div>
</div>
