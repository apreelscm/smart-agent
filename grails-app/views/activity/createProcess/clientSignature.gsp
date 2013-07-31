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

    <div id="pdfBox" style="height: 500px; overflow: hidden;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <g:render template="../forms/pdf/embedDocument-mobile"
                  model="[pdfDocument: resource(dir:'files', file:'pedef.pdf')]"/>
    </div>

    <nav>

        <g:form >
            <fieldset >
                    <g:submitButton name="noaccept" value="Brak akceptacji" class="button submit display-inline" style="height: 45px; width: 23%"/>

                    <g:select id="statusSelect" name="status" from="${com.eservice.eumowy.Activity$ClientType?.values()}"
                              keys="${com.eservice.eumowy.Activity$ClientType.values()*.name()}"
                              value="${com.eservice.eumowy.Activity.ClientType.REPRESENTIVE.name()}"
                              noSelection="['': '']"
                              class="display-inline"
                        style="width:20%; margin-right: 5px"
                    />

                    <g:submitButton name="subscribe" value="Podpisz dokument" class="button submit display-inline"
                                    style=" height: 45px;width: 23%;"/>

                    <div class="display-inline-block" style="width: 31%; position: relative; top: 10px" >
                        <div style="text-align: left">
                            <label>
                                <g:radio name="requestVersion" value="electronical" checked="on"/>
                                <g:message code="todo" default="Żądanie wersji elektronicznej" />
                            </label>
                        </div>

                        <div style="text-align: left">
                            <label>
                                <g:radio name="requestVersion" value="paper"/>
                                <g:message code="todo" default="Żądanie wersji papierowej" />
                            </label>
                        </div>
                    </div>

  %{--              <g:submitButton name="submit" class="button submit display-block" value="Zakończ"
                                style="margin: 19px auto 0 auto; width: 100px"/>--}%

                <g:submitButton name="back" class="button submit" value="Wstecz" style="margin-top: 15px" />
                <g:submitButton name="submit" class="button submit" value="Zakończ" />
            </fieldset>
        </g:form>
    </nav>

</section>

</body>
</html>