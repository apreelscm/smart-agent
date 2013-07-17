<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Proces')}" />
    <title><g:message code="process.list.label"  /></title>

</head>
<body>
<a href="#list-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <sec:ifAnyGranted roles="PH_ROLE">
            <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </sec:ifAnyGranted>
    </ul>
</div>

<div>
    %{--TABELA PROCESOW DLA PH--}%
    <sec:ifAnyGranted roles="PH_ROLE">
        <g:render template="table/phtable"/>
    </sec:ifAnyGranted>

    %{--TABELA PROCESOW DLA ADMINA--}%
    <sec:ifAnyGranted roles="ADM_ROLE">
        <g:render template="table/admtable"/>
    </sec:ifAnyGranted>
</div>


</body>
</html>
