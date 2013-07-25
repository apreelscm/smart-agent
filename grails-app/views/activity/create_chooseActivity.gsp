<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>

</head>


<body>

<section id="create_chooseActivity" style="width: 100%">
    <h1 class="ng linia-bottom">Wybór działania</h1>

    <article >
        <h3>Nowa umowa</h3>

        <g:each var="panel" in="${ (0..<2) }" status="i" >
            <div style="margin: 15px auto 0 auto; text-align: center">
                <label>
                    <g:message code="todo" default="Sygnatura Dokumentu" />
                </label>
                <g:select id="documentSignature${i}" name="documentSignature"
                          from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                          keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                          noSelection="['': i%2 == 0 ?'AP-AG/F/DP/2.003/13-05-10' : 'AP/F/DS/2.000/09-04-22']"
                          style="width: 250px"
                />
            </div>
        </g:each>

    </article>

    <article style="margin-top: 20px">

        <h3>Zmiana warunków handlowych</h3>

        <g:each var="panel" in="${ (0..<2) }" status="i" >
            <div style="margin: 15px auto 0 auto; text-align: center">
                <label>
                    <g:message code="todo" default="Sygnatura Dokumentu" />
                </label>
                <g:select id="documentSignature${i}" name="documentSignature"
                          from="${com.eservice.eumowy.Process$ProcessStatus?.values()}"
                          keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}"
                          noSelection="['': i%2 == 0 ?'AP/UPZ/2.000/13-01-03' : 'AP/UPZBS/2.000/13-01-25']"
                          style="width: 250px"
                />
            </div>
        </g:each>

    </article>


    <fieldset  style="margin-top: 20px;">
        <g:actionSubmit class="przycisk-submit" value="Dalej"/>
    </fieldset>

</section>

</body>
</html>