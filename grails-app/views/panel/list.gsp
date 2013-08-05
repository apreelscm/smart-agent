<%@ page import="com.eservice.eumowy.Panel" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>
</head>
<body>
<g:render template="../panels/acceptorAddress"/>
<g:render template="../panels/acceptors"/>
<g:render template="../panels/additionalInformation"/>
<g:render template="../panels/aggrement"/>
<g:render template="../panels/canvasser"/>
<g:render template="../panels/dccRange"/>
<g:render template="../panels/acceptorCorrespondenceAddress"/>
<g:render template="../panels/dcc"/>
<g:render template="../panels/annexHirePOS"/>
<g:render template="../panels/acceptorData"/>
<g:render template="../panels/aggrementTime"/>
<g:render template="../panels/annexPrepaid"/>

</body>
</html>

