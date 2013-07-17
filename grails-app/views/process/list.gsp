
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
    <title><g:message code="process.list.label"  /></title>
    <r:require modules="grailsui-dialog"/>
</head>
<body>
<a href="#list-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>


<sec:ifAnyGranted roles="PH_ROLE">
    <g:render template="phtable"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ADM_ROLE">
    <g:render template="admtable"/>
</sec:ifAnyGranted>
<sec:ifNotLoggedIn >
    <g:render template="phtable"/>
</sec:ifNotLoggedIn>

</body>
</html>
