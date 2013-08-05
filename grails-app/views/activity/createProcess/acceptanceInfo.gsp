<%@ page import="eumowy.RequiredFieldTagLib; com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
<title><g:message code="default.edit.label" args="[entityName]"/></title>


</head>

<body>

<section id="create_acceptanceInfo" >
    <h1 class="ng linia-bottom">Dane Akceptanta</h1>

    <g:form class="panelForm">

    <apreel:textField name="acceptanceName" direction="vertical" isError="true"
                      title="Oficjalna nazwa Akceptanta (Do druku na fakturze VAT)"
                      errorMessage="Wymagane Pole (test)"/>

    <apreel:textField name="acceptanceWebName" direction="vertical"
                      title="Nazwa sieciowa akceptanta"
                      errorMessage="Wymagane Pole (test2)"/>

    <div>

        <apreel:textField name="acceptanceNip"  direction="horizontal" isError="true"
                          title="NIP"
                          errorMessage="Wymagane Pole (test3)"/>

        <apreel:textField name="acceptanceRegon" direction="horizontal"
                          title="REGON"
                          errorMessage="Wymagane Pole (test4)"/>

    </div>


    <fieldset id="buttons">
        <g:submitButton name="back" class="button submit" value="Wstecz" />
        <g:submitButton name="continue" class="button submit" value="Dalej" />
    </fieldset>
</g:form>

    </section>

    </body>
    </html>