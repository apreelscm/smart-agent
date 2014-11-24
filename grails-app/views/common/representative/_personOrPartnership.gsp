<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation; com.eservice.eumowy.enums.options.IdentityDocumentType" %>

<div id="personData" class="acceptorAbroad ${additionalClass}">
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
    </div>

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

    <div>
        <eumowy:enumRadioGroup values="${AcceptorLocation.values()}" name="${prefix}[${seqNo}].typLokalizacji" value="${representative?.typLokalizacji}"
                               radioWrapperClass="acceptorLocationRadioWrapper"/>
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

    <div>
        <label for="${prefix}[${seqNo}].adres"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].adres" value="${representative?.adres}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="adres"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].obywatelstwo"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].obywatelstwo" value="${representative?.obywatelstwo}" maxlength="30"
                          validatable="${representative}" validateField="obywatelstwo"/>
    </div>
</div>