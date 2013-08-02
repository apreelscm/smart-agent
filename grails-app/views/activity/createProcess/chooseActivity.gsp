<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>

    <style>
    .signature-article {
        width: 450px;
        margin: 25px auto 0;
        padding: 15px 20px 20px;
    }

    .signature-article select {
        width: 250px;
    }

    .border-article {
        border: #c5c5c5 solid thin;
        border-radius: 3px;
        background-color: #f7f8f6;
    }

    .signature-article > .requiredField {
        margin: 15px 0 0 35px;
        text-align: center;
    }


    </style>


    <g:javascript>

        var $j = jQuery.noConflict();

        $j(function () {

            $j('form').submit(function (e) {
                e.preventDefault()
                var self = this;

                if (validateArticle()) {
                    var hiddenContinue = '<input type="hidden" name="_eventId_continue" value="1" id="_eventId_continue" />'
                    var input = $j("<input>").attr("type", "hidden").val("Bla");
                    $j(self).append(hiddenContinue);
                    $j(self).unbind('submit').submit()
                }
            });


            function validateArticle(event) {

                var isValid = true;

                $j("article").each(function (index) {
                    var article = $j(this);
                    var selects = $j(article).find("select")

                    if (selects.length > 0) {
                        var select1 = selects[0];
                        if (selects.length == 1) {
                            if (select1.value == "null") {
                                isValid = false;
                            }

                            select1.value == "null" ? makeInvalid(select1) : makeValid(select1);
                        }
                        else if (selects.length == 2) {
                            var select2 = selects[1];

                            if (select1.value == "null" || select2.value == "null") {
                                isValid = false;
                            }

                            select1.value == "null" ? makeInvalid(select1) : makeValid(select1);
                            select2.value == "null" ? makeInvalid(select2) : makeValid(select2);
                        }
                    }
                })
                return isValid;
            }

            function makeInvalid(obj) {
                $j(obj).parent().addClass("error");
                $j(obj).parent().find("img").removeClass("display-none");
            }

            function makeValid(obj) {
                $j(obj).parent().removeClass("error");
                $j(obj).parent().find("img").addClass("display-none");
            }
        });


    </g:javascript>
</head>

<body>

<section id="create_chooseActivity" style="width: 100%">
    <h1 class="ng linia-bottom">Wybór działania</h1>

    <g:form id="signaturesFormId">
        <g:each var="activity" in="${processInstance.activities}">
            <article class="border-article signature-article">
                <g:set var="signaturesList1" value="${activity?.activitySignatures?.findAll { it.numberOfList == 1 }}"/>
                <g:set var="signaturesList2" value="${activity?.activitySignatures?.findAll { it.numberOfList == 2 }}"/>

                <h3 class="linia-bottom"><g:message code="activity.${activity.code}.name"/></h3>
                <apreel:selectField id="act_${activity.id}_sig1" name="activitySignature${activity.id}"
                                    title="Sygnatura Dokumentu"
                                    from="${signaturesList1}"
                                    optionKey="id"
                                    optionValue="signature"
                                    noSelection="[null: '']"/>

                <g:if test="${signaturesList2?.size() > 0}">
                    <apreel:selectField id="act_${activity.id}_sig2" name="documentSignature2"
                                        title="Sygnatura Dokumentu"
                                        from="${signaturesList2}"
                                        optionKey="id"
                                        optionValue="signature"
                                        noSelection="[null: '']"/>
                </g:if>

            </article>
        </g:each>
        <fieldset style="margin-top: 20px;">
            <g:link event="back" class="button submit">Wstecz</g:link>
            <g:submitButton id="conitnueButton" name="continue" class="button submit" value="Dalej"/>
        </fieldset>
    </g:form>
</section>

</body>
</html>