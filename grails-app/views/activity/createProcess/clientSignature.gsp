<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="clientSignature.header.title" default="Podpis Klienta"/></title>

    <style>

    .navButtons{
        margin-top: 15px;
    }

    .navButtons td
    {
        padding:5px;
        vertical-align:middle;
    }

    #signatureNavTable{
        border: none;
    }

    #signatureNavTable tbody tr:hover, #signatureNavTable tbody tr td.highlighted {
        background-color: white;
    }
    </style>

</head>

<body>

<section id="create_clientSignature" >

    <h1 class="ng linia-bottom"><g:message code="clientSignature.header.title" default="Podpis Klienta"/></h1>

    <div id="pdfBox" style="height: 500px; overflow: hidden;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <g:render template="../forms/pdf/embedDocument-mobile"
                  model="[pdfDocument: resource(dir:'files', file:'pedef.pdf')]"/>
    </div>

    <nav>
        <g:form>
            <fieldset class="navButtons" >
                <table id="signatureNavTable">
                    <colgroup>
                        <col style="width: 25%;" />
                        <col style="width: 20%" />
                        <col style="width: 25%" />
                        <col style="width: 30%" />
                    </colgroup>
                    <tbody>
                    <tr>
                        <td>
                            <g:submitButton name="noaccept" class="button submit display-inline" style="height: 45px; width: 100%"
                                            value="${message(code: 'clientSignature.noAcceptance.button', default:'Brak akceptacji')}"/>
                        </td>
                        <td>
                            <g:select id="statusSelect" name="status" from="${com.eservice.eumowy.Activity$ClientType?.values()}"
                                      keys="${com.eservice.eumowy.Activity$ClientType.values()*.name()}"
                                      value="${com.eservice.eumowy.Activity.ClientType.REPRESENTIVE.name()}"
                                      noSelection="['': '']"
                                      class="display-inline"
                                      style="width: 100%"
                            />
                        </td>
                        <td>
                            <g:submitButton name="subscribe" class="button submit display-inline" style=" height: 45px;width: 100%;"
                                            value="${message(code: 'clientSignature.signDocument.button', default:'Podpisz dokument')}"/>
                        </td>
                        <td>
                            <div class="display-inline-block" style="width: 100%;" >
                                <div style="text-align: left">
                                    <label>
                                        <g:radio name="requestVersion" value="electronical" checked="on"/>
                                        <g:message code="clientSignature.electronicalVersion.radio" default="Żądanie wersji elektronicznej" />
                                    </label>
                                </div>
                                <div style="text-align: left">
                                    <label>
                                        <g:radio name="requestVersion" value="paper"/>
                                        <g:message code="clientSignature.paperVersion.radio" default="Żądanie wersji papierowej" />
                                    </label>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                <g:submitButton id="conitnueButton" name="submit" class="button submit" value="${message(code: 'default.navigation.button.finish', default: 'Zakończ')}"/>

            </fieldset>
        </g:form>
    </nav>

</section>

</body>
</html>