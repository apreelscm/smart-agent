<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>

    <style>

    #create_chooseActivity > article:not(:first-child){
        padding-top: 1.0em;
    }

    </style>

</head>

<body>

<section id="create_chooseActivity" style="width: 100%">
    <h1 class="ng linia-bottom">Wybór działania</h1>

    <g:each var="activity" in="${processInstance.activities}" >
        <article>
            <g:set var="signaturesList1" value="${activity?.activitySignatures?.findAll { it.numberOfList == 1}}"/>
            <g:set var="signaturesList2" value="${activity?.activitySignatures?.findAll { it.numberOfList == 2}}" />

            <h3><g:message code="activity.${activity.code}.name"/></h3>

            <div style="margin: 15px auto 0 auto; text-align: center">
                <label>
                    <g:message code="todo" default="Sygnatura Dokumentu" />
                </label>
                <g:select id="documentSignature1" name="documentSignature"
                          from="${signaturesList1}"
                          style="width: 250px"
                />

            </div>

            <g:if test="${signaturesList2?.size() > 0}">
                <div style="margin: 15px auto 0 auto; text-align: center;">
                    <label>
                        <g:message code="todo" default="Sygnatura Dokumentu" />
                    </label>
                    <g:select id="documentSignature2" name="documentSignature"
                              from="${activity.activitySignatures.findAll { it.numberOfList == 2}}"
                              style="width: 250px"
                    />
                </div>
            </g:if>

        </article>
    </g:each>

    <g:form>
        <fieldset  style="margin-top: 20px;">
            <g:submitButton name="back" class="button submit" value="Wstecz" />
            <g:submitButton name="continue" class="button submit" value="Dalej" />
        </fieldset>
    </g:form>
</section>

</body>
</html>