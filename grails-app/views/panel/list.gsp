<%@ page import="com.eservice.eumowy.Panel" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body>

%{--<r:require module="newpoint_panel_setup"/>--}%
%{--<r:require module="mask"/>--}%

<section id="create-activity">
    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title" default="Lista paneli"/></h1>

    <g:hasErrors bean="${data}">
        <ul class="errors" role="alert">
            <g:eachError bean="${data}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

<!-- Working with process with id: ${processInstance?.id} , and status ${processInstance?.status?.toString()}-->


    <g:form class="someForm">
        <g:render template="../panels/adresacjaSeciowa"/>
        %{--<g:render template="../panels/adresDoKorespondencjiPunktu"/>--}%
        <g:render template="../panels/adresDoKorespondencjizAkecptantem"/>
        <g:render template="../panels/aneksDoUmowyNajmuZestawuPos"/>
        <g:render template="../panels/aneksDoUmowyPrepaid"/>
        <g:render template="../panels/czasObowiazywaniaUmowy"/>
        <g:render template="../panels/daneAkceptanta"/>
        <g:render template="../panels/daneDoWydruku"/>
        %{--<g:render template="../panels/danePunktu"/>--}%
        <g:render template="../panels/dcc"/>
        <g:render template="../panels/dccZakresUruchomienia"/>
        <g:render template="../panels/deklaracjeAkceptanta"/>
        %{--<g:render template="../panels/dodajPunkt"/>--}%
        <g:render template="../panels/dodatkoweUslugi"/>
        <g:render template="../panels/dodatkoweUslugi2"/>
        <g:render template="../panels/dodatkoweUslugiMud"/>
        <g:render template="../panels/dodatkoweUslugiUTAIntegracja"/>
        <g:render template="../panels/dodatkoweWyposazenie"/>
        <g:render template="../panels/formaDoladowania"/>
        %{--<g:render template="../panels/funkcjeTerminala"/>--}%
        <g:render template="../panels/ifplus"/>
        <g:render template="../panels/informacjeDodatkowe"/>
        %{--<g:render template="../panels/informacjeTechniczne"/>--}%
        <g:render template="../panels/okresLojalnosciowy"/>
        %{--<g:render template="../panels/opieka"/>--}%
        <g:render template="../panels/oplatyDCC"/>
        <g:render template="../panels/osobaDoKontaktu"/>
        <g:render template="../panels/osobaDoKontaktuWPunkcie"/>
        <g:render template="../panels/osobaKtoraPozyskalaAkceptanta"/>
        <g:render template="../panels/osobaUprawnionaDoPodpisaniaUmowy"/>
        <g:render template="../panels/poziomOplatiWarunkiPlatnosciKarty"/>
        <g:render template="../panels/poziomOplatIWarunkiPlatnosciPP"/>
        <g:render template="../panels/promocyjneObnizenieOplatyZaZestawPos"/>
        <g:render template="../panels/scoring"/>
        <g:render template="../panels/serwisEkonomiczny"/>
        <g:render template="../panels/serwisKomfort"/>
        <g:render template="../panels/serwisPrzestiz"/>
        <g:render template="../panels/siedzibaAkceptanta"/>
        <g:render template="../panels/umowa2"/>
        <g:render template="../panels/uwagi"/>
        <g:render template="../panels/wykazPunktowAkceptujacychKartyPlatnicze"/>
        %{--<g:render template="../panels/zestawPos"/>--}%
        <g:render template="../panels/zestawPosOdplatneUzywanie"/>
        <g:render template="../panels/zestawPosStawkiPreferencyjne"/>
        <g:render template="../panels/serwis"/>
        <g:render template="../panels/rachunekBankowyKlienta"/>
        <g:render template="../panels/oplataDCCZaUruchomienie"/>
        %{--<g:render template="../panels/liczbaMiesiecyZwolnieniaZNajmu"/>--}%
%{--        <g:render template="../panels/danePos"/>
        <g:render template="../panels/dodajPos"/>--}%

        <g:actionSubmit class="button submit" action="save" value="Zapisz"
                        style="float: left;margin-right: 1em;display:block"
                        />
    </g:form>

</section>

</body>
</html>