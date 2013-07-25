<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>


<body>

<section id="create_chooseCalc" >

    <h1 class="ng linia-bottom">Wybierz klienta</h1>

    <g:form style="display: table; margin: 0 auto">
        <div class="fieldcontain display-block ">

            <label for="nipNumber" style="white-space: nowrap; width:180px; text-align:center; display: block">
                <g:message code="process.calcNumber.label" default="Wprowadź NIP klienta" />
            </label>

            <p class="display-inline" style="margin-top: 5px;">
                <g:textField name="nipNumber" value="${flash.clientNip}"
                             style="width: 180px"/>
            </p>
            <g:actionSubmit class="button action display-inline" action="verifyClientNIP"
                            value="Wyszukaj" style="margin-left: 5px"/>

        </div>

        <div id="nipMessageBox">
            <g:if test="${flash.nipInfoMessage}">
                <g:render template="message/infoMessage" model="[message: flash.nipInfoMessage]"/>
            </g:if>
            <g:if test="${flash.nipErrorMessage}">
                <g:render template="message/errorMessage" model="[message: flash.nipErrorMessage]"/>
            </g:if>
        </div>

        <div class="fieldcontain"  style="margin-top: 20px">
            <label for="calcNumber" style="white-space: nowrap; width:180px; text-align:center; display: block">
                <g:message code="process.calcNumber.label" default="Ostatni zaakceptowany kalkulator"/>
            </label>
            <p style="margin-top: 5px;">
                <g:textField name="calcNumber" disabled="disabled"
                             value="${flash.clientCalcNumber}"
                             style="width: 180px"/>
            </p>
        </div>

        <div id="calcMessageBox">
            <g:if test="${flash.calcInfoMessage}">
                <g:render template="message/infoMessage" model="[message: flash.calcInfoMessage]"/>
            </g:if>
            <g:if test="${flash.calcErrorMessage}">
                <g:render template="message/errorMessage" model="[message: flash.calcErrorMessage]"/>
            </g:if>
        </div>

        <fieldset  style="margin-top: 20px; left: -32px">
            <g:actionSubmit class="button submit" action="create_defineActivity" value="Wstecz"/>
            <g:actionSubmit class="button submit" value="Dalej"/>
        </fieldset>
    </g:form>

</section>

</body>
</html>