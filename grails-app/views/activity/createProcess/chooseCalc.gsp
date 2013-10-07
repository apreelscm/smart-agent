<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title> <g:message code="chooseCalc.header.title" default="Wybierz klienta"/></title>
    <r:require module="mask"/>
    <r:require module="jquery_ui" />
    <r:script>
        jQuery(document).ready(function(){
            jQuery("#nipField").bind('input', function(){
                jQuery("#continueButton").attr('disabled', 'disabled');
            });

            jQuery("#continueButton").click(function(){
                var self= jQuery(this),
                    tempElement = jQuery("<input type='hidden'/>"),
                    form = jQuery("form");

                tempElement                    //for web flow <3
                        .attr("name", this.name)
                        .val(self.val())
                        .appendTo(form);

                self.attr('disabled', 'disabled');

                jQuery("#loadingDialog").dialog({
                    resizable: true,
                    height: 100,
                    width: 250,
                    modal: true
                });

                form.submit();

                return false;
            });
        })
    </r:script>
</head>
<body>

<section id="create_chooseCalc">
    <h1 class="ng linia-bottom"><g:message code="chooseCalc.header.title" default="Wybierz klienta"/></h1>

    <g:form class="calcForm">
        <div>
            <div class="display-inline-block">
                <apreel:textField  id="nipField" name="nip" class="nip"
                                   title="${message(code:'client.nip.label', default:'Wprowadź NIP klienta')}"
                                   value="${nip}" direction="vertical"
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

        <fieldset style="margin-top: 20px; left: -32px">
            <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <input id="continueButton" class="button submit" type="submit" value="Dalej" name="_eventId_continue" ${(!isContinueEnabled)?"disabled":""} />
        </fieldset>
    </g:form>

    <div id="loadingDialog" style="display: none;">
        <p><g:message code="loading" default="Trwa ładowanie danych..."/></p>
    </div>

</section>

</body>
</html>