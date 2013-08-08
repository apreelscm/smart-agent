<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title" default="Lista paneli"/></title>
</head>
<body>

<section id="create-activity">

    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title" default="Lista paneli"/></h1>

    <g:each var="panel" in="${processInstance.panels}" status="i">
        <g:render template="/panels/${panel.name}"/>
    </g:each>

    <g:form>
        <nav style="margin-top: 20px">
            <fieldset>
                <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                <g:submitButton id="conitnueButton" name="continue" class="button submit"
                                value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
            </fieldset>
        </nav>
    </g:form>

</section>

</body>
</html>