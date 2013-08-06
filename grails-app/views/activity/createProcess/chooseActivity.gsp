<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="chooseActivity.header.title" default="Wybór działania"/></title>

    <g:javascript>
        var $j = jQuery.noConflict();

        var signatureExceptions = ["zmianaWarunkowDcc"]

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

            function validateArticle() {
                var isValid = true;
                $j("article").each(function () {
                    var article = this;
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

                            if (signatureExceptions.indexOf(article.id) != -1){

                                if(select1.value == "null" && select2.value == "null"){
                                    isValid = false;
                                    makeArticleInvalid(article);
                                }
                                else{
                                    makeArticleValid(article)
                                }
                            }
                            else{

                                if (select1.value == "null" || select2.value == "null"){
                                    isValid = false;
                                }

                                select1.value == "null" ? makeInvalid(select1) : makeValid(select1);
                                select2.value == "null" ? makeInvalid(select2) : makeValid(select2);
                            }
                        }
                    }
                })
                return isValid;
            }

            function makeInvalid(obj) {
                $j(obj).parent().addClass("error");
                $j(obj).parent().find("img").removeClass("visibility-hidden");
            }

            function makeValid(obj) {
                $j(obj).parent().removeClass("error");
                $j(obj).parent().find("img").addClass("visibility-hidden");
            }


            function makeArticleInvalid(obj) {
                $j(obj).addClass("article-error");
            }
            function makeArticleValid(obj) {
                $j(obj).removeClass("article-error");
            }
        });

    </g:javascript>
</head>

<body>

<section id="create_chooseActivity">
    <h1 class="ng linia-bottom"><g:message code="chooseActivity.header.title" default="Wybór działania"/></h1>

    <g:form id="signaturesFormId">
        <g:each var="activity" in="${processInstance.activities}">
            <article id="${activity.code}" class="border-article signature-article">
                <g:set var="list1" value="${activity?.activitySignatures?.findAll { it.numberOfList == 1 }}"/>
                <g:set var="list2" value="${activity?.activitySignatures?.findAll { it.numberOfList == 2 }}"/>
                <g:set var="listM" value="${activity?.activitySignatures?.findAll { it.mandatory == true }}"/>

                <h3 class="linia-bottom"><g:message code="activity.${activity.code}.name"/></h3>

                <div>
                    <g:hiddenField name="activitySignature_${activity.id}" value="${listM*.id}" />

                    <apreel:selectField id="act_${activity.id}_sig1" name="activitySignature_${activity.id}"
                                        title="${message(code:'signature.sygnaturaDokumentu.name', default:'Sygnatura Dokumentu')}"
                                        from="${list1}"
                                        optionKey="id"
                                        optionValue="signature"
                                        noSelection="[null: '']"/>

                    <g:if test="${list2?.size() > 0}">
                        <apreel:selectField id="act_${activity.id}_sig2"  name="activitySignature_${activity.id}"
                                            title="${message(code:'signature.sygnaturaDokumentu.name', default:'Sygnatura Dokumentu')}"
                                            from="${list2}"
                                            optionKey="id"
                                            optionValue="signature"
                                            noSelection="[null: '']"/>
                    </g:if>
                </div>

            </article>
        </g:each>
        <fieldset style="margin-top: 20px;">
            <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <g:submitButton id="conitnueButton" name="continue" class="button submit"
                            value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
        </fieldset>
    </g:form>
</section>

</body>
</html>