<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>


<body>

<section id="create_clientSignature" >

    <h1 class="ng linia-bottom">Podpis Klienta</h1>

    <div id="pdfBox" style="height: 500px; width: 100%;  margin-top: 20px; overflow: hidden;border: solid 1px; border-radius: 5px;">
        <g:render template="../forms/pdf/embedDocument"
                  model="[pdfDocument: resource(dir:'files', file:'pedef.pdf')]"/>
    </div>

    <nav  style="margin-top: 20px" >
        <g:form action="#" >
            <fieldset>
                <div class="float-left" style="width: 48%; text-align: left">

                    <g:submitButton name="Brak akceptacji" class="button submit" style="width: 48%"/>

                    <g:select id="statusSelect" name="status" from="${com.eservice.eumowy.Activity$ClientType?.values()}"
                              keys="${com.eservice.eumowy.Activity$ClientType.values()*.name()}"
                              value="${com.eservice.eumowy.Activity.ClientType.REPRESENTIVE.name()}"
                              noSelection="['': '']"
                              style="margin-left:2%; width:48%"
                    />

                    <g:submitButton name="Oczekiwanie na podpis w formie papierowej"
                                    class="button submit display-block"
                                    style="margin-top: 19px; width: 100%"
                    />

                </div>

                <div class="float-right display-inline" style="width: 48%; text-align: left; margin-left: 10px">

                    <g:submitButton name="Podpisz dokument" class="button submit float-left"
                                    style="width: 48%"/>

                    <div class="float-right" style="margin-left:2%; width: 48%;">
                        <div >
                            <g:checkBox name="dodatkowyPunkt" />
                            <g:message code="todo" default="Żądanie wersji papierowej" />
                        </div>

                        <div style="margin-top: 5px;">
                            <g:checkBox name="dodatkowyPunkt" />
                            <g:message code="todo" default="Żądanie wersji elektronicznej" />
                        </div>
                    </div>

                    <g:submitButton name="Oczekiwanie na podpis w formie elektronicznej" class="button submit"
                                    style="margin-top: 10px; width: 100%"/>

                </div>

                <div class="clear"/>

                <g:submitButton name="Zakończ" class="button submit" style="margin-top: 19px; width: 100%"/>

            </fieldset>
        </g:form>
    </nav>



</section>

</body>
</html>