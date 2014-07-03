<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title> <g:message code="chooseCalc.header.title"/></title>

    <asset:javascript src="apreel/mask.js"/>
    <script type="text/javascript">
        jQuery(document).ready(function(){
            jQuery("#nipField").bind('input', function(){
                jQuery("#continueButton").attr('disabled', 'disabled');
            });

            jQuery("#continueButton, .getCalculator").click(function(){
                var self= jQuery(this),
                    tempElement = jQuery("<input type='hidden'/>"),
                    form = jQuery("form");

                tempElement                    //for web flow <3
                        .attr("name", this.name)
                        .val(self.val())
                        .appendTo(form);

                self.attr('disabled', 'disabled');

                showLoadingDialog()

                setTimeout(function() { //hax for android 4.1.3 native browser
                    form.submit()
                }, 1);

                return false;
            });
        })
    </script>
</head>
<body>

<section id="create_chooseCalc">
    <h1 class="ng linia-bottom"><g:message code="chooseCalc.header.title"/></h1>

    <g:form class="calcForm">
        <div>
            <div class="display-inline-block">
                <apreel:textField  id="nipField" name="nip" class="nip"
                                   title="${message(code:'client.nip.label')}"
                                   value="${nip}" direction="vertical"
                />
            </div>

            <g:submitButton name="getCalculator" class="button action display-inline getCalculator"
                            value="${message(code:'default.search.button.name')}"/>

            <div id="nipMessageBox">
                 <g:if test="${nipInfoMessage}">
                     <g:render template="message/infoMessage" model="[message: nipInfoMessage]"/>
                 </g:if>
                 <g:if test="${nipErrorMessage}">
                     <g:render template="message/errorMessage" model="[message: nipErrorMessage]"/>
                 </g:if>
                <g:if test="${bisnodeMessage}">
                    <g:render template="message/infoMessage" model="[message: bisnodeMessage]"/>
                </g:if>
                <g:if test="${representativesNotFound}">
                    <g:render template="message/infoMessage" model="[message: representativesNotFound]"/>
                </g:if>
            </div>
        </div>

        <div>
            <apreel:textField  name="calc" title="${message(code:'client.lastAcceptedCalc.label')}"
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
            <g:link event="back" class="button submit">${message(code:'default.navigation.button.prev')}</g:link>
            <input id="continueButton" class="button submit" type="submit" value="${message(code:'default.navigation.button.next')}" name="_eventId_continue" ${(!isContinueEnabled)?"disabled":""} />
        </fieldset>
    </g:form>


</section>

</body>
</html>