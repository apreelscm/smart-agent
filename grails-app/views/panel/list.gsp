<%@ page import="com.eservice.eumowy.Panel" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body>
<g:render template="../panels/annexHirePOS"/>
<g:render template="../panels/acceptorData"/>
<g:render template="../panels/aggrementTime"/>
<g:render template="../panels/annexPrepaid"/>

</body>
</html>

