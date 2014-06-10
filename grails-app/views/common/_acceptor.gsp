<%@ page import="enums.AcceptorPESELCountry; enums.IdentityDocumentType" %>

<g:set var="firstNames" value="${[""] + representativesBisnode?.collect {it.firstName}}"/>
<g:set var="lastNames" value="${[""] + representativesBisnode?.collect {it.lastName}}"/>

<div class="acceptor">
    <g:if test="${dropdowns}">
        <div>
            <eumowy:textField name="${representative + "Tytul"}" value="${data.(representative + "Tytul")}" validatable="${data}" readonly="readonly" class="tytulField"/>

            <label for="${representative + "Imie"}"><g:message code="panel.first.name"/>:</label>
            <g:select name="${representative + "Imie"}" from="${firstNames}" value="${data.(representative + "Imie")}" validatable="${data}" class="imieField"/>

            <label for="${representative + "Nazwisko"}"><g:message code="panel.last.name"/>:</label>
            <g:select name="${representative + "Nazwisko"}" from="${lastNames}" value="${data.(representative + "Nazwisko")}" validatable="${data}" class="nazwiskoField"/>

            <label for="${representative + "Stanowisko"}"><g:message code="panel.position"/>:</label>
            <eumowy:textField name="${representative + "Stanowisko"}" value="${data.(representative + "Stanowisko")}" validatable="${data}" readonly="readonly" class="positionField"/>
        </div>
    </g:if>
    <g:else>
        <div>
            <g:select name="${representative + "Tytul"}" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.(representative + "Tytul")}"/>

            <label for="${representative + "Imie"}"><g:message code="panel.first.name"/>:</label>
            <eumowy:textField name="${representative + "Imie"}" value="${data.(representative + "Imie")}" validatable="${data}" maxlength ="13" required="true"/>

            <label for="${representative + "Nazwisko"}"><g:message code="panel.last.name"/>:</label>
            <eumowy:textField name="${representative + "Nazwisko"}" value="${data.(representative + "Nazwisko")}" validatable="${data}" maxlength ="35" required="true" class="nazwiskoField"/>

            <label for="${representative + "Stanowisko"}"><g:message code="panel.position"/>:</label>
            <eumowy:textField name="${representative + "Stanowisko"}" value="${data.(representative + "Stanowisko")}" validatable="${data}"/>
        </div>
    </g:else>

    <div class="acceptorCountry hidden">
        <div class="acceptorPESELWrapper">
            <label for="${representative} + PESEL"><g:message code="pesel.label"/></label>
            <g:textField name="${representative} + PESEL" maxlength="11"/>
        </div>
    </div>

    <div class="acceptorAbroad hidden">
        <div class="acceptorPESELCountryWrapper">
            <eumowy:enumRadioGroup values="${AcceptorPESELCountry.values()}" name="${representative + "PESELKraj"}" required="true"
                                   additionalView="akceptantPESELKraj" radioWrapperClass="acceptorPESELCountryRadioWrapper"/>
        </div>

        <div class="acceptorDocumentTypeWrapper">
            <eumowy:enumRadioGroup values="${IdentityDocumentType.values()}" name="${representative + "TypDokumentu"}" required="true"
                                   radioWrapperClass="acceptorDocumentTypeRadioWrapper display-inline-block"/>
        </div>

        <div>
            <label for="${representative + "SeriaNrDokumentu"}"><g:message code="identity.card.details"/></label>
            <g:textField name="${representative + "SeriaNrDokumentu"}"/>

            <label for="${representative + "DataUrodzenia"}"><g:message code="birth.date.label"/></label>
            <g:textField name="${representative + "DataUrodzenia"}" maxlength="10"/>

            <label for="${representative + "Obywatelstwo"}"><g:message code="citizenship.label"/></label>
            <g:textField name="${representative + "Obywatelstwo"}" maxlength="30"/>
        </div>

        <div>
            <label for="${representative + "Adres"}"><g:message code="address.label"/></label>
            <g:textField name="${representative + "Adres"}" maxlength="100" style="width: 750px"/>
        </div>

        <div>
            <label><g:message code="political.position.label"/></label>
            <input type="checkbox" name="${representative + "StanowiskoPolityczne"}"/>
        </div>
    </div>
</div>