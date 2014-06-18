<%@ page import="com.eservice.eumowy.enums.IdentityDocumentType" %>

<div class="acceptorAbroad ${additionalClass}">
    <div class="acceptorPESELCountryWrapper">
        <div class="acceptorPESELCountryRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="COUNTRY" required="required"
                     checked="${representative?.isRepresentativeLocationAbroad() == false}"/>
            <div class="label"><g:message code="pesel.label"/></div>

            <g:textField name="${prefix}[${seqNo}].lokalizacjaPesel" value="${representative?.lokalizacjaPesel}" maxlength="11" class="pesel-field display-block"/>
        </div>

        <div class="acceptorPESELCountryRadioWrapper">
            <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="ABROAD" required="required"
                     checked="${representative?.isRepresentativeLocationAbroad()}"/>
            <div class="label"><g:message code="country.label"/></div>

            <g:textField name="${prefix}[${seqNo}].lokalizacjaKraj" value="${representative?.lokalizacjaKraj}" maxlength="30" class="display-block"/>
        </div>
    </div>

    <div class="acceptorDocumentTypeWrapper">
        <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].typDokumentu" required="true"
                               radioWrapperClass="acceptorDocumentTypeRadioWrapper display-inline-block" value="${representative?.typDokumentu}"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].seriaNrDokumentu"><g:message code="identity.card.details"/></label>
        <g:textField name="${prefix}[${seqNo}].seriaNrDokumentu" value="${representative?.seriaNrDokumentu}" maxlength="20"/>

        <label for="${prefix}[${seqNo}].dataUrodzenia"><g:message code="birth.date.label"/></label>
        <g:textField name="${prefix}[${seqNo}].dataUrodzenia" value="${formatDate(format: 'yyyy-MM-dd', date: representative?.dataUrodzenia)}" maxlength="10" class="date-field"/>

        <label for="${prefix}[${seqNo}].obywatelstwo"><g:message code="citizenship.label"/></label>
        <g:textField name="${prefix}[${seqNo}].obywatelstwo" value="${representative?.obywatelstwo}" maxlength="30"/>
    </div>

    <div>
        <label for="${prefix}[${seqNo}].adres"><g:message code="address.label"/></label>
        <g:textField name="${prefix}[${seqNo}].adres" value="${representative?.adres}" maxlength="100" style="width: 750px"/>
    </div>

    <div>
        <label><g:message code="political.position.label"/></label>
        <g:checkBox name="${prefix}[${seqNo}].czyStanowiskoPolityczne" checked="${representative?.czyStanowiskoPolityczne}"/>
    </div>
</div>