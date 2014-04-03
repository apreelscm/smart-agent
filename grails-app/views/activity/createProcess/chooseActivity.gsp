<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="chooseActivity.header.title"/></title>
    <r:require module="chooseActivity"/>

    <r:script>
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
    </r:script>

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
                <g:set var="list1" value="${activity?.activitySignatures?.findAll { it.numberOfList == 1 && it.signature.active}}"/>
                <g:set var="list2" value="${activity?.activitySignatures?.findAll { it.numberOfList == 2 && it.signature.active}}"/>
                <g:set var="listM" value="${activity?.activitySignatures?.findAll { it.mandatory == true && it.signature.active}}"/>

                <g:set var="selectedValue1" value="${activity?.selectedActivitySignatures?.find { it.numberOfList == 1 }}"/>
                <g:set var="selectedValue2" value="${activity?.selectedActivitySignatures?.find { it.numberOfList == 2 }}"/>

                <h3 class="linia-bottom"><g:message code="activity.${activity.code}.name"/></h3>
                <div>
                    <g:hiddenField name="activitySignature_${activity.id}" value="${listM*.id}" />

                    <apreel:selectField data-activity="${activity.id}" data-id="sig1" id="act_${activity.id}_sig1" name="activitySignature_${activity.id}"
                                        title="${message(code:'signature1.sygnaturaDokumentu.'+activity.code+'.name')}"
                                        from="${list1}"
                                        optionKey="id"
                                        optionValue="signature"
                                        value="${selectedValue1?.id}"
                                        noSelection="[null: '']"/>
                    
                    <g:each var="sig" in="${list1}">
                   		<g:if test="${sig.signature.description != null}">                
							<p class="sig-${activity.id}-description-1" id="sig_${sig.id}_desc" style="display: none;">${sig.signature.description}</p>
						</g:if>
					</g:each>
                    <g:if test="${list2?.size() > 0}">
                        <apreel:selectField data-activity="${activity.id}" data-id="sig2" id="act_${activity.id}_sig2"  name="activitySignature_${activity.id}"
                                            title="${message(code:'signature2.sygnaturaDokumentu.'+activity.code+'.name')}"
                                            from="${list2}"
                                            optionKey="id"
                                            optionValue="signature"
                                            value="${selectedValue2?.id}"
                                            noSelection="[null: '']"/>
                    	<g:each var="sig" in="${list2}">
                    		<g:if test="${sig.signature.description != null}">
                    			<p class="sig-${activity.id}-description-2" id="sig_${sig.id}_desc" style="display: none;">${sig.signature.description}</p>
                    		</g:if>
                    	</g:each>
                    </g:if>
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