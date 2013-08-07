<%@ page import="com.eservice.eumowy.Panel" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body>
<g:render template="../panels/uwagi"/>
<g:render template="../panels/serwis"/>
<g:render template="../panels/serwisEkonomiczny"/>
<g:render template="../panels/serwisPrzestiz"/>
<g:render template="../panels/serwisKomfort"/>
<g:render template="../panels/dodatkoweUslugi"/>
<g:render template="../panels/scoring"/>
<g:render template="../panels/osobaDoKontaktu"/>
<g:render template="../panels/siedzibaAkceptanta"/>
<g:render template="../panels/osobaUprawnionaDoPodpisaniaUmowy"/>
<g:render template="../panels/informacjeDodatkowe"/>
<g:render template="../panels/umowa2"/>
<g:render template="../panels/osobaKtoraPozyskalaAkceptanta"/>
<g:render template="../panels/dCCZakresUruchomienia"/>
<g:render template="../panels/adresDoKorespondencjizAkecptantem"/>
<g:render template="../panels/dCC"/>
<g:render template="../panels/aneksDoUmowyNajmuZestawuPos"/>
<g:render template="../panels/daneAkceptanta"/>
<g:render template="../panels/czasObowiazywaniaUmowy"/>
<g:render template="../panels/aneksDoUmowyPrepaid"/>

</body>
</html>

