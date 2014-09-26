<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="chooseActivity.header.title"/></title>
    <asset:javascript src="apreel/createProcess/chooseActivity.js"/>

    <script type="text/javascript">
    	jQuery(document).ready(function() {
    		jQuery('select[data-id="sig1"], select[data-id="sig2"]').on('change', function(e) { 
    			var id = jQuery(e.target).val();
    			var activity = jQuery(e.target).attr('data-activity');
    			if (jQuery(e.target).attr('data-id') == 'sig1') {
    				jQuery('.sig-'+activity+'-description-1').hide();
    			}
    			else {
    				jQuery('.sig-'+activity+'-description-2').hide();
    			}
    			jQuery('#sig_'+id+'_desc').show();
    		});
    	});
    </script>

</head>

<body>

<section>
    <h1 class="ng linia-bottom"><g:message code="chooseActivity.header.title"/></h1>

    <div id="errorBox">
        <g:if test="${errorMessage}">
            <g:render template="message/errorMessage" model="[message: errorMessage]"/>
        </g:if>
    </div>

    <g:form id="signaturesFormId">
        <g:each var="activity" in="${processInstance?.activities?.sort(false){it.numerPozycji}}">
            <article id="${activity.code}" class="border-article signature-article">
                <h3 class="linia-bottom"><g:message code="activity.${activity.code}.name"/></h3>
                <div>
                    <sig:mandatory activity="${activity}" process="${processInstance}"/>

                    <sig:list activity="${activity}" process="${processInstance}" listNumber="1"/>

                    <sig:list activity="${activity}" process="${processInstance}" listNumber="2"/>
                </div>
            </article>
        </g:each>
        <fieldset style="margin-top: 20px;">
            <g:link event="back" class="button submit float-left">${message(code:'default.navigation.button.prev')}</g:link>
            <g:submitButton id="conitnueButton" name="continue" class="button submit float-right"
                            value="${message(code:'default.navigation.button.next')}"/>
        </fieldset>
    </g:form>
</section>

</body>
</html>