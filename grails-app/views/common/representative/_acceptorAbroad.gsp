<%@ page import="com.eservice.eumowy.enums.IdentityDocumentType" %>

<div class="acceptorAbroad ${additionalClass}">
    <div class="acceptorPESELCountryWrapper ${hasErrors(bean: representative, field: 'typLokalizacji', 'errorSpan')}">
        <g:hasErrors bean="${representative}" field="typLokalizacji">
            <g:eachError bean="${representative}" field="typLokalizacji">
                <p class="error-message"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>

        <div class="acceptorPESELCountryRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="COUNTRY"
                     checked="${representative?.isRepresentativeLocationCountry()}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].lokalizacjaPesel" value="${representative?.lokalizacjaPesel}"
                              maxlength="11" class="pesel-field display-inline-block" disabled="${representative?.isRepresentativeLocationAbroad()}"
                              validatable="${representative}" validateField="lokalizacjaPesel"/>
        </div>

        <div class="acceptorPESELCountryRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="ABROAD"
                     checked="${representative?.isRepresentativeLocationAbroad()}"/>
            <div class="label"><g:message code="country.label"/></div>

            <eumowy:textField name="${prefix}[${seqNo}].lokalizacjaKraj" value="${representative?.lokalizacjaKraj}"
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
                               radioWrapperClass="acceptorDocumentTypeRadioWrapper display-inline-block" value="${representative?.typDokumentu}"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].seriaNrDokumentu"><g:message code="identity.card.details"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].seriaNrDokumentu" value="${representative?.seriaNrDokumentu}" maxlength="20"
                     validatable="${representative}" validateField="seriaNrDokumentu"/>

        <label for="${prefix}[${seqNo}].dataUrodzenia"><g:message code="birth.date.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].dataUrodzenia" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.dataUrodzenia)}"
                          maxlength="10" class="date-field"
                          validatable="${representative}" validateField="dataUrodzenia"/>

        <label for="${prefix}[${seqNo}].obywatelstwo"><g:message code="citizenship.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].obywatelstwo" value="${representative?.obywatelstwo}" maxlength="30"
                          validatable="${representative}" validateField="obywatelstwo"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].adres"><g:message code="address.label"/></label>
        <eumowy:textField name="${prefix}[${seqNo}].adres" value="${representative?.adres}" maxlength="100" style="width: 750px"
                          validatable="${representative}" validateField="adres"/>
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