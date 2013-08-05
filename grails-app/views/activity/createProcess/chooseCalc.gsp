<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <g:javascript library="jquery" />

    <g:javascript>
        var $j = jQuery.noConflict();

        var signatureExceptions = ["zmianaWarunkowDcc"]

        $j(function () {
            $j('form').submit(function (e) {
                return validateForm();
            });

            function validateForm() {
                var isValid = true;
                var nipInput = $j("#nipField input[type='text']")

                if( nipInput.val() == ""){
                    isValid = false;
                    makeInvalid(nipInput)
                }
                else{
                    makeValid(nipInput);
                }
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
        });

        function verifyClientNIP_success(){
            alert('success');
        }
    </g:javascript>

    <style>


    #create_chooseCalc > form {
        width: 340px;
        margin: 20px auto 0;
    }


    .requiredField{
        width: 240px;
    }

    .requiredField input[type='text']{
        width: 200px;
    }

    .requiredField label{
        width: 210px;
        text-align: center;
    }

    form > div:not(:first-child) {
        margin: 15px 0 0 0px;
    }


    </style>
</head>
<body>

<section id="create_chooseCalc">
    <h1 class="ng linia-bottom">Wybierz klienta</h1>

    <g:form>
        <div>
            <div class="display-inline-block">
                <apreel:textField  id="nipField" name="nip"
                                   title="${message(code:'todo', default:'Wprowadź NIP klienta')}"
                                   value="${nip}" direction="vertical" errorMessage="Wprowadzono niepoprawny NIP"
                />
            </div>

            <g:submitButton id="searchButton" name="getCalculator" value="Wyszukaj" class="button action display-inline"/>


            <div id="nipMessageBox">
                 <g:if test="${nipInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: nipInfoMessage]"/>
                 </g:if>
                 <g:if test="${nipErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: nipErrorMessage]"/>
                 </g:if>
            </div>
        </div>

        <div>
            <apreel:textField  name="calc" title="${message(code:'todo', default:'Ostatni zaakceptowany kalkulator')}"
                               direction="vertical"  disabled="true" value="${calcNumber}"/>

            <div id="calcMessageBox">
                 <g:if test="${calcInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: calcInfoMessage]"/>
                 </g:if>
                 <g:if test="${calcErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: calcErrorMessage]"/>
                 </g:if>
            </div>
        </div>

        <fieldset  style="margin-top: 20px; left: -32px">
            <g:link event="back" class="button submit">Wstecz</g:link>
            <g:if test="${calcInfoMessage}">
                <g:submitButton name="continue" class="button submit" value="Dalej" />
            </g:if>

        </fieldset>
    </g:form>

</section>

</body>
</html>