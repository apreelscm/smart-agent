<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="clientSignature.header.title" default="Podpis Klienta"/></title>

    <style>

    .navButtons{
        margin-top: 15px;
    }

    .navButtons td
    {
        padding:5px;
        vertical-align:middle;
    }

    #signatureNavTable{
        border: none;
    }

    #signatureNavTable tbody tr:hover, #signatureNavTable tbody tr td.highlighted {
        background-color: white;
    }
    </style>
	<r:require module="jquery_ui" />
	<r:script>
		var updateSubscriptionStatusCount = 0;
		var isSubscriptionDone = {};
		function updateSubscriptionStatus(status, linkid) {
			if (status == "OK") {
				updateSubscriptionStatusCount++;
				//jQuery('#subscriptionDialog').dialog("close");
				jQuery("#"+linkid).parent().addClass("disabled");
				isSubscriptionDone[linkid] = true;
				
				if (updateSubscriptionStatusCount == 1) {
					jQuery.post("/eumowy/activity/updateProcessStatus", {processId: "${processInstance.id}", processStatus:"WAIT_FOR_SUBSRIPTION"}, function(data) {
						
					});
				}
				
				if (updateSubscriptionStatusCount == 2) { // JUST FOR NOW IT's 2! CHANGE IT!
					jQuery.post("/eumowy/activity/updateProcessStatus", {processId: "${processInstance.id}", processStatus:"SUBSCRIPTIONS_DONE"}, function(data) {
						
					});
				}
			}
		}
		jQuery(document).ready(function(){
			
			jQuery("#noaccept").on("click", function(e) {
				e.preventDefault();
				
				jQuery("#confirm-noaccept-dialog").dialog({
					resizable: true,
					height:200,
					width: 450,
					modal: true,
					buttons: 
						{
							"Tak": function() {
								jQuery( this ).dialog( "close" );
								jQuery.post("/eumowy/activity/updateProcessStatus", {processId: "${processInstance.id}", processStatus:"REJECTED"}, function(data) {
									window.location = "/eumowy";
								});
							},
							"Nie": function() {
								jQuery( this ).dialog( "close" );
							}
						}
				});
				
				return false;
			});
			
			jQuery("#conitnueButton").on("click", function(e) {
				var result = true;
				if (updateSubscriptionStatusCount != 2) {
					result = false;
					jQuery("#confirm-submit-without-subscription-dialog").dialog({
						resizable: true,
						height:200,
						width: 450,
						modal: true,
						buttons: 
							{
								"Tak": function() {
									jQuery( this ).dialog( "close" );
									jQuery.post("/eumowy/activity/updateProcessStatus", {processId: "${processInstance.id}", processStatus:"WAIT_FOR_SUBSRIPTION"}, function(data) {
									});
									result = true;
								},
								"Nie": function() {
									jQuery( this ).dialog( "close" );
									e.preventDefault();
									result = false;
								}
							}
					});
				}
				
				return result;
			});
			
			<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}">
			jQuery("#subscribe-${it.name()}").on('click', function(e) {
				e.preventDefault();
				
				var dialog = jQuery('#subscriptionDialog');
				if (jQuery('#subscriptionDialog').length == 0) {
					dialog = jQuery('<div id="subscriptionDialog" style="display:hidden"></div>').appendTo('body');
				}
				
				dialog.load(
		            "/eumowy/subscription/inlineview?signername=${it.encodeAsURL()}&linkid=subscribe-${it.name()}",
		            {},
		            function(responseText, textStatus, XMLHttpRequest) {
		                dialog.dialog({
		                	modal: true,
      						width: 750
		                });
		            }
		        );
		        
				return false;
			});
			</g:each>
			
			jQuery("#requestVersionTemplates, #requestVersionPaper").on("change", function(e) {
				if (jQuery(e.target).is(":checked")) {
					jQuery("#noaccept").prop("disabled", true);
					
					<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}">
					jQuery("#subscribe-${it.name()}").parent().addClass("disabled");
					</g:each>
				}
			});
			
			jQuery("#requestVersionElectronical").on("change", function(e) {
				if (jQuery(e.target).is(":checked")) {
					jQuery("#noaccept").prop("disabled", false);
					
					<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}">
						if (isSubscriptionDone["subscribe-${it.name()}"] != true) {
							jQuery("#subscribe-${it.name()}").parent().removeClass("disabled");
						}
					</g:each>
				}
			});
			
		});
	</r:script>
</head>

<body>
<div id="confirm-noaccept-dialog" style="display: none;">
	<p><g:message code="process.subscriptions.noaccept.confirm" /></p>
</div>
<div id="confirm-submit-without-subscription-dialog"  style="display: none;">
	<p><g:message code="process.subscriptions.submit.without.subscription.confirm" /></p>
</div>
<section id="create_clientSignature" >

    <h1 class="ng linia-bottom"><g:message code="clientSignature.header.title" default="Podpis Klienta"/></h1>

    <div id="pdfBox" style="height: 680px; overflow: hidden;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <g:render template="../forms/pdf/embedDocument-mobile"
                  model="[pdfDocument: resource(dir:'files', file:'pedef.pdf')]"/>
    </div>

    <nav>
        <g:form>
        	<fieldset id="clientSignaturePersons" class="subpanel-fieldset">
        		<legend><g:message code="clientSignature.signing.people" default="Osoby podpisujące" /></legend>
        		<ul class="table-list">
        			<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}" >
						<li><span><a class="big-link" href="#" id="subscribe-${it.name()}">${it}</a></span></li>
					</g:each>
					<!-- <li><span><a class="big-link" id="subscribe-OTHER" href="#">Andrzej Kmicic - Reprezentant</a></span></li>
					<li><span><a class="big-link" href="#">Stanisław Wokólski - Reprezentant</a></span></li>
					<li><span><a class="big-link" href="#">Jacek Soplica - Pracownik eService</a></span></li> -->
        		</ul>
        	</fieldset>
        	<fieldset class="subpanel-fieldset" id="clientSignatureDocType">
        		<legend><g:message code="clientSignature.version.title" default="Forma" /></legend>
        		<ul class="table-list">
        			<li>
        				<span>
        					<label>
        						<g:radio id="requestVersionElectronical" name="requestVersion" value="electronical" checked="on"/>
        						<g:message code="clientSignature.electronicalVersion.radio" default="Elektroniczna" />
                            </label>
                        </span>
                    </li>
                    <li>
                    	<span>
                    		<label>
                                <g:radio id="requestVersionPaper" name="requestVersion" value="paper"/>
                                <g:message code="clientSignature.paperVersion.radio" default="Papierowa" />
                            </label>
                    	</span>
                  	</li>
                  	<li>
                  		<span>
                  			<label>
                  				<g:radio id="requestVersionTemplates" name="requestVersion" value="templates" />
                  				<g:message code="clientSignature.templatesVersion.radio" default="Żądanie wzorów dokumentów" />
                  			</label>
                  		</span>
                  	</li>
        		</ul>
        	</fieldset>
            <fieldset class="navButtons" style="clear: both;">
                <table id="signatureNavTable">
                    <tbody>
                    <tr>
                    	<td id="clientSignatureBackButton">
                            <g:link style="width: 100%" event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                        </td>
                        <td style="text-align: right;">
                            <g:submitButton id="noaccept" name="noaccept" class="button submit display-inline" style="width: 90%"
                                            value="${message(code: 'clientSignature.noAcceptance.button', default:'Brak akceptacji')}"/>
                        </td>
                        <td>
                            <g:submitButton style="width: 100%" id="conitnueButton" name="submit" class="button submit" value="${message(code: 'default.navigation.button.finish', default: 'Zakończ')}"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </g:form>
    </nav>

</section>

</body>
</html>