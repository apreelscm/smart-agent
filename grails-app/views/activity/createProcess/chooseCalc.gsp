<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title> <g:message code="chooseCalc.header.title" default="Wybierz klienta"/></title>

    <g:javascript>
        var $j = jQuery.noConflict();

        $j(function () {
            $j('form').submit(function (e) {
                return validateForm();
            });

            function validateForm() {
                var isValid = true;
                var nipInput = $j("#nipField input[type='text']")

                if(!validateNip(nipInput.val())){
                    isValid = false;
                    makeInvalid(nipInput)
                }
                else{
                    makeValid(nipInput);
                }
                return isValid;
            }

           function validateNip(nip){
                var weights = [6, 5, 7, 2, 3, 4, 5, 6, 7];
                nip = nip.replace(/[\s-]/g, '');

                if (nip.length == 10 && parseInt(nip, 10) > 0) {
                    var sum = 0;
                    for (var i = 0; i < 9; i++) {
                        sum += nip[i] * weights[i];
                    }
                    return (sum % 11) == nip[9];
                }
                return false;
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

</head>
<body>

<section id="create_chooseCalc">
    <h1 class="ng linia-bottom"><g:message code="chooseCalc.header.title" default="Wybierz klienta"/></h1>

    <g:form>
        <div>
            <div class="display-inline-block">
                <apreel:textField  id="nipField" name="nip"
                                   title="${message(code:'client.nip.label', default:'Wprowadź NIP klienta')}"
                                   value="${nip}" direction="vertical" errorMessage="Wprowadzono niepoprawny NIP"
                />
            </div>

            <g:submitButton name="getCalculator" class="button action display-inline"
                            value="${message(code:'default.search.button.name', default: 'Wyszukaj')}"/>

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
            <apreel:textField  name="calc" title="${message(code:'client.lastAcceptedCalc.label', default:'Ostatni zaakceptowany kalkulator')}"
                               direction="vertical"  disabled="true" value="${processInstance.calcNumber}"/>
            <div id="calcMessageBox">
                 <g:if test="${calcInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: calcInfoMessage]"/>
                 </g:if>
                 <g:if test="${calcErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: calcErrorMessage]"/>
                 </g:if>
            </div>
        </div>

        <fieldset style="margin-top: 20px; left: -32px">
            <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <g:submitButton id="conitnueButton" name="continue" class="button submit"
                            value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
        </fieldset>
    </g:form>

</section>

</body>
</html>