<%@ page import="com.eservice.eumowy.enums.AcceptorPESELCountry; com.eservice.eumowy.enums.IdentityDocumentType" %>

<div class="acceptor">
    <g:if test="${dropdowns}">
        <div>
            <eumowy:textField name="${prefix}[${seqNo}].tytul" value="${representative?.tytul}" validatable="${representative}" readonly="readonly" class="tytulField"/>

            <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
            <g:select name="${prefix}[${seqNo}].imie" from="${firstNames}" value="${representative?.imie}" validatable="${representative}" class="imieField"/>

            <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
            <g:select name="${prefix}[${seqNo}].nazwisko" from="${lastNames}" value="${representative?.nazwisko}" validatable="${representative}" class="nazwiskoField"/>

            <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}" validatable="${representative}" readonly="readonly" class="positionField"/>
        </div>
    </g:if>
    <g:else>
        <div>
            <g:select name="${prefix}[${seqNo}].tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${representative?.tytul}" validatable="${representative}"/>

            <label for="${prefix}[${seqNo}].imie"><g:message code="panel.first.name"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].imie" value="${representative?.imie}" validatable="${representative}" maxlength ="13" required="true"/>

            <label for="${prefix}[${seqNo}].nazwisko"><g:message code="panel.last.name"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].nazwisko" value="${representative?.nazwisko}" validatable="${representative}" maxlength ="35" required="true" class="nazwiskoField"/>

            <label for="${prefix}[${seqNo}].stanowisko"><g:message code="panel.position"/>:</label>
            <eumowy:textField name="${prefix}[${seqNo}].stanowisko" value="${representative?.stanowisko}" validatable="${representative}"/>
        </div>
    </g:else>

    <div class="acceptorCountry hidden">
        <div class="acceptorPESELWrapper">
            <label for="${prefix}[${seqNo}].pesel"}"><g:message code="pesel.label"/></label>
            <g:textField name="${prefix}[${seqNo}].pesel" value="${representative?.pesel}" maxlength="11" class="pesel-field" validatable="${representative}"/>
        </div>
    </div>

    <div class="acceptorAbroad hidden">
        <div class="acceptorPESELCountryWrapper">
            <div class="acceptorPESELCountryRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="COUNTRY" required="required"/>
                <div class="label"><g:message code="pesel.label"/></div>

                <g:textField name="${prefix}[${seqNo}].lokalizacjaPesel" value="${representative?.lokalizacjaPesel}" validatable="${representative}"
                             maxlength="11" class="pesel-field display-block" disabled="disabled"/>
            </div>

            <div class="acceptorPESELCountryRadioWrapper">
                <g:radio name="${prefix}[${seqNo}].typLokalizacji" value="ABROAD" required="required"/>
                <div class="label"><g:message code="country.label"/></div>

                <g:textField name="${prefix}[${seqNo}].lokalizacjaKraj" value="${representative?.lokalizacjaKraj}" validatable="${representative}"
                             maxlength="30" class="display-block" disabled="disabled"/>
            </div>
        </div>

        <div class="acceptorDocumentTypeWrapper">
            <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${prefix}[${seqNo}].typDokumentu" required="true"
                                   radioWrapperClass="acceptorDocumentTypeRadioWrapper display-inline-block"/>
        </div>

        <div>
            <label for="${prefix}[${seqNo}].seriaNrDokumentu"><g:message code="identity.card.details"/></label>
            <g:textField name="${prefix}[${seqNo}].seriaNrDokumentu}" value="${representative?.seriaNrDokumentu}" validatable="${representative}"/>

            <label for="${prefix}[${seqNo}].dataUrodzenia"><g:message code="birth.date.label"/></label>
            <g:textField name="${prefix}[${seqNo}].dataUrodzenia" value="${representative?.dataUrodzenia}" validatable="${representative}"
                         maxlength="10" class="date-field"/>

            <label for="${prefix}[${seqNo}].obywatelstwo"><g:message code="citizenship.label"/></label>
            <g:textField name="${prefix}[${seqNo}].obywatelstwo" value="${representative?.obywatelstwo}" validatable="${representative}"
                         maxlength="30"/>
        </div>

        <div>
            <label for="${prefix}[${seqNo}].adres"><g:message code="address.label"/></label>
            <g:textField name="${prefix}[${seqNo}].adres" value="${representative?.adres}" validatable="${representative}"
                         maxlength="100" style="width: 750px"/>
        </div>

        <div>
            <label><g:message code="political.position.label"/></label>
            <input type="checkbox" name="${prefix}[${seqNo}].czyStanowiskoPolityczne"
                   value="${representative?.czyStanowiskoPolityczne}" validatable="${representative}"/>
        </div>
    </div>
</div>