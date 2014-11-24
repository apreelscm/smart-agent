<%@ page import="com.eservice.eumowy.enums.options.AcceptorLocation" %>
<div id="companyData" class="acceptorCountry ${additionalClass}">
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
            <g:radio name="${prefix}[${seqNo}].detail" value="BIRTH_DATE"
                     checked="${representative?.detail}"/>
            <label for="${prefix}[${seqNo}].dataUrodzenia"><g:message code="birth.date.label"/></label>

            <g:textField name="${prefix}[${seqNo}].dataUrodzenia" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.dataUrodzenia)}" maxlength="10" class="date-field"/>
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