<!DOCTYPE html>
<head>
    <meta name="layout" content="blank">
    <title><g:message code="subscription.title" /></title>
    <r:require module="signaturepad" />
    <r:require module="jquery_ui" />
</head>
<body>
<r:script>

    jQuery(document).ready(function() {
      jQuery('#subscriberData').html(decodeURI('${params.name}') + " " + decodeURI('${params.surname}'));
      jQuery('#padPlaceholder').html('<div class="typed"></div><canvas id="pad" class="pad" width="700" height="300"></canvas><input type="hidden" name="content" class="output">');
      jQuery('.sigPad').signaturePad({errorMessageDraw: '<g:message code="subscription.error" />'});
      
      jQuery('form').on("submit", function(e) {
      	e.preventDefault();
      	
      	jQuery.post("/eumowy/subscription/saveSubscription", $(this).serialize(), function(data) {
      		var result = JSON.parse(data);
      		if (result.status == "OK") {
      			jQuery("#dialog").html('<h2 class="align-center">Pomyślnie zapisano podpis!</h2>');
      			updateSubscriptionStatus("OK", "${params.linkid}", result.subscriptionId);
      		}
      		else {
      			jQuery("#dialog").html('<p class="align-center">'+result.text+'</p>');
      		}
     		
      	});
      	
      	return false;
      });
      
    }); 

</r:script>
<div id="dialog">
<section id="index-subscription">
    <h1 class="ng linia-bottom">Podpis</h1>

    <h3 style="margin-top: 20px" id="subscriberData"></h3>

    <g:form  id="subscriptionForm" action="saveSubscription" class="sigPad">
        <p>
            <g:checkBox name="agreement" required="required"/>
            <label for="agreement">
                <g:if test="${params.personRole == 'PH'}">
                    <g:message code="subscription.agreement.ph" />
                </g:if>
                <g:else>
                    <g:message code="subscription.agreement" />
                </g:else>
            </label>
        </p>

        <div id="padPlaceholder" class="sig sigWrapper" style="margin-top: 20px">
            
        </div>

		<input type="hidden" name="name" value="${params.name}" />
		<input type="hidden" name="surname" value="${params.surname}" />
		<input type="hidden" name="personRole" value="${params.personRole}" />
        <fieldset style="margin-top: 20px;">
            <a href="#clear" class="button action clearButton"><g:message code="subscription.clear" /></a>
            <g:submitButton id="submitSubscription" name="Złożono podpis" class="button submit"/>
        </fieldset>

    </g:form>
</section>
</div>
</body>
