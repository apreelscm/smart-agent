<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="chooseActivity.header.title" default="Wybór działania"/></title>
    <r:require module="chooseActivity"/>

    <r:script>
    </r:script>

</head>

<body>

<section>
    <h1 class="ng linia-bottom"><g:message code="chooseActivity.header.title" default="Wybór działania"/></h1>

    <g:form id="signaturesFormId">
        <g:each var="activity" in="${processInstance?.activities}">
            <article id="${activity.code}" class="border-article signature-article">
                <g:set var="list1" value="${activity?.activitySignatures?.findAll { it.numberOfList == 1 && it.signature.active}}"/>
                <g:set var="list2" value="${activity?.activitySignatures?.findAll { it.numberOfList == 2 && it.signature.active}}"/>
                <g:set var="listM" value="${activity?.activitySignatures?.findAll { it.mandatory == true && it.signature.active}}"/>

                <g:set var="selectedValue1" value="${activity?.selectedActivitySignatures?.find { it.numberOfList == 1 }}"/>
                <g:set var="selectedValue2" value="${activity?.selectedActivitySignatures?.find { it.numberOfList == 2 }}"/>

                <h3 class="linia-bottom"><g:message code="activity.${activity.code}.name"/></h3>
                <div>
                    <g:hiddenField name="activitySignature_${activity.id}" value="${listM*.id}" />

                    <apreel:selectField id="act_${activity.id}_sig1" name="activitySignature_${activity.id}"
                                        title="${message(code:'signature.sygnaturaDokumentu.name', default:'Sygnatura Dokumentu')}"
                                        from="${list1}"
                                        optionKey="id"
                                        optionValue="signature"
                                        value="${selectedValue1?.id}"
                                        noSelection="[null: '']"/>

                    <g:if test="${list2?.size() > 0}">
                        <apreel:selectField id="act_${activity.id}_sig2"  name="activitySignature_${activity.id}"
                                            title="${message(code:'signature.sygnaturaDokumentu.name', default:'Sygnatura Dokumentu')}"
                                            from="${list2}"
                                            optionKey="id"
                                            optionValue="signature"
                                            value="${selectedValue2?.id}"
                                            noSelection="[null: '']"/>
                    </g:if>
                </div>

            </article>
        </g:each>
        <fieldset style="margin-top: 20px;">
            <g:link event="back" class="button submit float-left">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <g:submitButton id="conitnueButton" name="continue" class="button submit float-right"
                            value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
        </fieldset>
    </g:form>
</section>

</body>
</html>