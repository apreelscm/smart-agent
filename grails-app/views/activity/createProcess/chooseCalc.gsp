<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title> <g:message code="chooseCalc.header.title" default="Wybierz klienta"/></title>
</head>
<body>

<section id="create_chooseCalc">
    <h1 class="ng linia-bottom"><g:message code="chooseCalc.header.title" default="Wybierz klienta"/></h1>

    <g:form>
        <div>
            <div class="display-inline-block">
                <apreel:textField  id="nipField" name="nip"
                                   title="${message(code:'client.nip.label', default:'Wprowadź NIP klienta')}"
                                   value="${nip}" direction="vertical"
                />
            </div>

            <g:submitButton name="getCalculator" class="button action display-inline"
                            value="${message(code:'default.search.button.name', default: 'Wyszukaj')}"/>

            <div id="nipMessageBox">
                 <g:if test="${nipInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: nipInfoMessage]"/>
                 </g:if>
                 <g:if test="${nipErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: nipErrorMessage]"/>
                 </g:if>
            </div>
        </div>

        <div>
            <apreel:textField  name="calc" title="${message(code:'client.lastAcceptedCalc.label', default:'Ostatni zaakceptowany kalkulator')}"
                               direction="vertical"  disabled="true" value="${calcNumber}"/>
            <div id="calcMessageBox">
                 <g:if test="${calcInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: calcInfoMessage]"/>
                 </g:if>
                 <g:if test="${calcErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: calcErrorMessage]"/>
                 </g:if>
            </div>
        </div>

        <fieldset style="margin-top: 20px; left: -32px">
            <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <g:if test="${isContinueEnabled}">
            <g:submitButton id="conitnueButton" name="continue" class="button submit"
                            value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
            </g:if>
        </fieldset>
    </g:form>

</section>

</body>
</html>