<%@ page import="com.eservice.eumowy.Subscription; com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<meta name="viewport" content="width=960, initial-scale=1, maximum-scale=1"/>
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
<r:require module="panzoom" />
<r:require module="jsignature" />

<r:script>
	var updateSubscriptionStatusCount = 0;
	var isSubscriptionDone = {};
	var requiredSubscriptionsCount = ${requiredNumberOfSubscriptions};
	var subscriptionDialog = null;
	var subscriptionLinkId = null;
	var agreementLabel = {
		"PH": "${message(code:'subscription.agreement.ph')}",
		"ACCEPTANT1": "${message(code:'subscription.agreement')}",
		"ACCEPTANT2": "${message(code:'subscription.agreement')}"
	};
	
	function setUpSignaturePad() {
		jQuery('#padPlaceholder').jSignature({'width': 700, 'height': 300, 'decor-color': 'transparent'});

	    jQuery('.clearButton').on('click', function(e) { 
	    	e.preventDefault();
	    	jQuery('#padPlaceholder').jSignature('reset');
	    	return false;
	    });
	    jQuery('.closeButton').on('click', function(e) {
	    	e.preventDefault();
	    	hideSubscriptionPanel();
	    	return false;
	    });
	    
	    jQuery('form[data-id="subscriptionForm"]').on("submit", function(e) {
	    	e.preventDefault();
	    	jQuery("#sigContent").val(jQuery('#padPlaceholder').jSignature("getData"));
	    	jQuery("#nativeContent").val(JSON.stringify(jQuery('#padPlaceholder').jSignature("getData", "native")));
	    	jQuery.post("${createLink(controller: 'subscription', action: 'saveSubscription')}", jQuery(this).serialize(), function(data) {
	    		var result = JSON.parse(data);
	    		if (result.status == "OK") {
	    			jQuery("#dialogInfoText").html('<h2 class="align-center">Pomyślnie zapisano podpis!</h2>');
	    			updateSubscriptionStatus("OK", subscriptionLinkId, result.subscriptionId);
				}
				else {
					jQuery("#dialogInfoText").html('<h3 class="align-center" style="color: red;">'+result.text+'</h3>');
				}
				jQuery('#dialog').hide();
				jQuery('#dialogInfo').show();
			});
			return false;
		});
	}

	function refreshSignature(processId, role, linkId){
	    jQuery.post("${createLink(controller: 'subscription', action: 'refreshSubscription')}", {processId : processId ,role: role}, function(data) {
	    		var result = JSON.parse(data);
	    		if (result.status == "OK") {
	    			jQuery("#dialogInfoText").html('<h2 class="align-center">Pomyślnie zapisano podpis!</h2>');
	    			updateSubscriptionStatus("OK", linkId, result.subscriptionId);
				}
				else {
					jQuery("#dialogInfoText").html('<h3 class="align-center" style="color: red;">'+result.text+'</h3>');
				}
				jQuery('#dialog').hide();
				jQuery('#dialogInfo').show();
			});
			return false;
	}

	function showSubscriptionPanel(name, surname, role, linkid) {
		if (subscriptionDialog != null) {
			subscriptionLinkId = linkid;
			jQuery('#agreement').attr('checked', false);
			jQuery('#padPlaceholder').jSignature('reset');
			jQuery('#dialog').show();
			jQuery('#dialogInfo').hide();
			jQuery('#subscriberData').html(name + " " + surname);
			jQuery('#subscriberName').val(name);
			jQuery('#subscriberSurname').val(surname);
			jQuery('#subscriberRole').val(role);
			jQuery('#agreementLabel').html(agreementLabel[role]);
			subscriptionDialog.css({opacity: 100});
			subscriptionDialog.slideDown(function() {
				var offset = jQuery(this).offset().top
				jQuery('html').animate({scrollTop: offset+"px" });
			});
		}
	}
	
	function hideSubscriptionPanel() {
		if (subscriptionDialog != null) {
			subscriptionDialog.animate({opacity: 0}, function() {
	    		jQuery(this).slideUp();
	    	});
			jQuery('#dialogInfo').hide();
		}
	}
	
	function updateSubscriptionStatus(status, linkid, subId) {
	    if (status == "OK") {
	        updateSubscriptionStatusCount++;
	        jQuery("#clientSignatureBackButton").addClass("disabled");
	
	        jQuery("#"+linkid).parent().addClass("disabled");
	        isSubscriptionDone[linkid] = true;
	
	        if (updateSubscriptionStatusCount >= 1 && updateSubscriptionStatusCount <= requiredSubscriptionsCount - 1) {
	            jQuery.post(jQuery(location).attr("href"), {_eventId_updateProcessStatus: "", processStatus: "WAIT_FOR_SUBSCRIPTION", subscriptionId: subId}, function(data){});
	        }
	
	        if (updateSubscriptionStatusCount == requiredSubscriptionsCount) {
	            jQuery.post(jQuery(location).attr("href"), {_eventId_updateProcessStatus: "", processStatus: "SUBSCRIPTIONS_DONE", subscriptionId: subId}, function(data){});
	        }
	    }
	}
	jQuery(document).ready(function(){
	    var pageNum = 1;
	    var documentPages = [];
		subscriptionDialog = jQuery('#subscriptionDialog');
	
		setUpSignaturePad();
	    checkEmailKontakt();
	
	    function refreshPdfPageView(pageNum, pid) {
	        console.log("PageNum: " + pageNum + " PID: " + pid)
	        if (documentPages[pageNum] == null || documentPages[pageNum] == undefined) {
	             jQuery.get("/eumowy/activity/getDocumentPage", {processId: pid, pageNumber: pageNum}, function(data) {
	                 jQuery("#pdfBox-content-loading").hide();
	                 documentPages[pageNum] = data;
	                 jQuery("img#pdfPage").attr("src", data).css("display", "block");
	             });
	         }
	         else {
	             jQuery("#pdfBox-content-loading").hide();
	             jQuery("img#pdfPage").attr("src", documentPages[pageNum]).css("display", "block");
	         }
	    }
	
	    function navigatePdfPageView(direction, totalPagesCount) {
	        if (direction == "prev") {
	            if (pageNum <= 1)
	                return;
	            pageNum--;
	        }
	        else if (direction == "next") {
	            if (pageNum >= totalPagesCount)
	                return;
	            pageNum++;
	        }
	
	        jQuery("img#pdfPage").css("display", "none");
	        jQuery("#pdfBox-content-loading").show();
	
	        refreshPdfPageView(pageNum, "${processInstance.id}");
			     	
			     	jQuery("#page_num").html(pageNum);
				}
				
				jQuery("#page_num").html(pageNum);
				
				jQuery("#pdfPage").panzoom({
					$zoomIn: jQuery("#zoomInPdfPage"),
					$zoomOut: jQuery("#zoomOutPdfPage"),
					contain: 'invert'
				});
				
				refreshPdfPageView(pageNum, "${processInstance.id}");
				
		    	jQuery("#prevPdfPage").on("click", function(e) {
		    		e.preventDefault();
		    		navigatePdfPageView("prev", ${totalPagesCount});
			     	return false;
		    	});
		    	
		    	jQuery("#nextPdfPage").on("click", function(e) {
		    		e.preventDefault();
		    		navigatePdfPageView("next", ${totalPagesCount});
			     	return false;
		    	});
		    	
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
									jQuery.post(jQuery(location).attr('href'), {_eventId_noaccept:""}, function(data) {
										window.location.href = '<g:createLink controller="activity" action="createProcess" params="[message: rejectedDocumentsMessage]"/>';
									});
								},
								"Nie": function() {
									jQuery( this ).dialog( "close" );
								}
							}
					});
					
					return false;
				});
				
				jQuery("#continueButton").on("click", function(e) {
					e.preventDefault();
	
					if (updateSubscriptionStatusCount != requiredSubscriptionsCount && jQuery("#requestVersionElectronical").is(":checked") == true) {
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
										jQuery('#confirm-pleasewait h2').text("${message(code: "process.subscriptions.sendingEmails")}");
										jQuery('#confirm-pleasewait img').show();
										jQuery('#confirm-pleasewait').dialog({resizable: true,
																				height:200,
																				width: 450,
																				modal: true});
										jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
											window.location.href = '<g:createLink controller="activity" action="createProcess" params="[message: prevActivityMessage]"/>';
										})
										.fail(function() { jQuery("#confirm-pleasewait h2").text("${message(code: "process.subscriptions.sendingEmails.error")}"); jQuery("#confirm-pleasewait img").hide() });
										
										result = true;
									},
									"Nie": function() {
										jQuery( this ).dialog( "close" );
									}
								}
						});
					}
					else {
	
						jQuery('#confirm-pleasewait h2').text("${message(code: "process.subscriptions.sendingEmails")}");
						jQuery('#confirm-pleasewait img').show();
						jQuery('#confirm-pleasewait').dialog({resizable: true,
																height:200,
																width: 450,
																modal: true});
						jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
							window.location.href = '<g:createLink controller="activity" action="createProcess" params="[message: prevActivityMessage]"/>';
						})
						.fail(function() { jQuery("#confirm-pleasewait h2").text("${message(code: "process.subscriptions.sendingEmails.error")}"); jQuery("#confirm-pleasewait img").hide() });
					}
					return false;
				});
				
				if (jQuery("#subscribe-REPRESENTATIVE1").text() == "  - Reprezentant") {
					jQuery("#subscribe-REPRESENTATIVE1").parent().parent().hide();
				}
				
				if (jQuery("#subscribe-REPRESENTATIVE2").text() == "  - Reprezentant") {
					jQuery("#subscribe-REPRESENTATIVE2").parent().parent().hide();
				}
	
	<g:each in="${processInstance.subscriptions}">
	    console.log("${it.name + ' ' + it.surname} - Reprezentant");
					console.log("${it.name + ' ' + it.surname} - Reprezentant");
					console.log("${it.name + ' ' + it.surname} - Pracownik eService");
					if(jQuery("#subscribe-REPRESENTATIVE1").text() == "${it.name + ' ' + it.surname} - Reprezentant") {
						jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
						updateSubscriptionStatusCount++;
						jQuery("#clientSignatureBackButton").addClass("disabled");
						isSubscriptionDone["subscribe-REPRESENTATIVE1"] = true;
					}
					if(jQuery("#subscribe-REPRESENTATIVE2").text() == "${it.name + ' ' + it.surname} - Reprezentant") {
						jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
						updateSubscriptionStatusCount++;
						jQuery("#clientSignatureBackButton").addClass("disabled");
						isSubscriptionDone["subscribe-REPRESENTATIVE2"] = true;
					}
					if(jQuery("#subscribe-PH").text() == "${it.name + ' ' + it.surname} - Pracownik eService") {
						jQuery("#subscribe-PH").parent().addClass("disabled");
						updateSubscriptionStatusCount++;
						jQuery("#clientSignatureBackButton").addClass("disabled");
						isSubscriptionDone["subscribe-PH"] = true;
					}
	</g:each>
	
	jQuery(".showSignatureDialog").on('click', function(e) {
	    e.preventDefault();
		if (isSubscriptionDone[linkId] == true) {
			return false;
		}
		
	    var currentTarget = jQuery(e.currentTarget),
	        firstName = currentTarget.attr('data-firstName'),
	        lastName = currentTarget.attr('data-lastName'),
	        role = currentTarget.attr('data-role'),
	        linkId = currentTarget.attr('id');
		
		if (isSubscriptionDone[linkId] != true) {
			showSubscriptionPanel(firstName, lastName, role, linkId);
        }
		return false;
	});
				
				jQuery("#requestVersionTemplates").on("change", function(e) {
					if (jQuery(e.target).is(":checked")) {
						jQuery("#noaccept").prop("disabled", true);
						jQuery("a.big-link").parent().addClass("disabled");
					}
				});
	
				jQuery("#requestVersionPaper").on("change", function(e) {
				    if (jQuery(e.target).is(":checked")) {
						jQuery("#noaccept").prop("disabled", false);
	                    jQuery("a.big-link").parent().removeClass("disabled");
					}
				});
				
				jQuery("#requestVersionElectronical").on("change", function(e) {
					if (jQuery(e.target).is(":checked")) {
						jQuery("#noaccept").prop("disabled", false);
						
						if (isSubscriptionDone["subscribe-REPRESENTATIVE1"] != true) {
							jQuery("#subscribe-REPRESENTATIVE1").parent().removeClass("disabled");
						}
						
						if (isSubscriptionDone["subscribe-REPRESENTATIVE2"] != true) {
							jQuery("#subscribe-REPRESENTATIVE2").parent().removeClass("disabled");
						}
						
						if (isSubscriptionDone["subscribe-PH"] != true) {
							jQuery("#subscribe-PH").parent().removeClass("disabled");
						}
					}
				});
	
				function checkEmailKontakt(){
	                var kontaktEmail = "${processInstance.processData.find { it.name == 'kontaktEmail'}?.value}"
	                var emailDoWysylkiDokumentu = "${processInstance.processData.find { it.name == 'emailDoWysylkiDokumentu'}?.value}"
	
	               // console.info("checkEmailKontakt - kontaktEmail:"+kontaktEmail+" emailDoWysylkiDokumentu:"+emailDoWysylkiDokumentu)
	
	               if(!kontaktEmail && !emailDoWysylkiDokumentu){
	                      jQuery("#requestVersionElectronical").attr("disabled","disabled").removeAttr("checked")
	                      jQuery("#requestVersionPaper").attr("checked","checked")
	
	                      jQuery("#noaccept").prop("disabled", true);
	                      jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
	                      jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
	                      jQuery("#subscribe-PH").parent().addClass("disabled");
	               }else{
	                   jQuery("#requestVersionElectronical").attr("checked","checked")
	               }
	          }
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
<div id="confirm-pleasewait" class="align-center" style="display: none;">
    <h2 style="padding-top: 20px;"><g:message code="process.subscriptions.sendingEmails" /></h2>
    <img style="width: 40px;" src="/eumowy/images/document-loading.gif" />
</div>
<section id="create_clientSignature" >

    <h1 class="ng linia-bottom"><g:message code="clientSignature.header.title" default="Podpis Klienta"/></h1>

    <div id="pdfBox" style="background-color: #F2F2F2; height: 680px; overflow: auto;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <div id="pdfBox-nav" style="padding: 1em; border-bottom: solid 1px;">
            <div style="display: inline-block; float: left">
                <a id="zoomOutPdfPage" class="button submit">-</a>
                <a id="zoomInPdfPage" class="button submit">+</a>
            </div>
            <div style="display: inline-block; float: right">
                <a id="nextPdfPage" class="button submit" style="float: right"><g:message code="process.subscriptions.nextPage" /></a>
                <a id="prevPdfPage" class="button submit"><g:message code="process.subscriptions.previousPage" /></a>
            </div>
            <div style="text-align: center; padding-left: 165px; padding-top: 5px">
                <span style="font-weight: bold"><g:message code="process.subscriptions.page" />: <span id="page_num"></span> / <span id="page_count">${totalPagesCount}</span></span>
            </div>
            <div style="clear: both"></div>
        </div>
        <div id="pdfBox-content" style="margin: 1em;">
            <div id="pdfBox-content-loading" style="text-align: center; width: 200px; display: block; margin: 0 auto;">
                <h2 style="padding-top: 100px;"><g:message code="process.subscriptions.loadingPage" /></h2>
                <img style="width: 40px;" src="/eumowy/images/document-loading.gif" />
            </div>
            <img id="pdfPage" style="border:1px solid gray; display: none; width: 440px; height: 570px; display: none; margin-left: auto; margin-right: auto; vertical-align: middle; text-align: center;"/>
        </div>
    </div>
	<div id="subscriptionDialog" style="display: none; padding: 10px; overflow: auto;border: solid 1px; border-radius: 5px;  margin: 20px 15px 15px 15px;">
		<div id="dialog">
			<section id="index-subscription">
			    <h3 id="subscriberData" style="margin-top: 20px"></h3>
			
			    <g:form data-id="subscriptionForm"  id="subscriptionForm" action="saveSubscription" class="sigPad">
			        <p>
			            <g:checkBox name="agreement" required="required"/>
			            <label id="agreementLabel" for="agreement"></label>
			        </p>
			        <div id="padPlaceholder" class="sig sigWrapper" style="border: 1px solid black; height: 300px; width: 700px; margin-top: 20px" ></div>
			
					<input id="subscriberName" type="hidden" name="name" value="" />
					<input id="subscriberSurname" type="hidden" name="surname" value="" />
					<input id="subscriberRole" type="hidden" name="personRole" value="" />
					<input id="sigContent" type="hidden" name="content" value="" />
					<input id="nativeContent" type="hidden" name="nativeContent" value="" />
			        <fieldset style="margin-top: 20px;">
			            <a href="#clear" class="button action clearButton"><g:message code="subscription.clear" /></a>
			            <g:submitButton id="submitSubscription" name="Złożono podpis" value="Złożono podpis" class="button submit"/>
			            <a href="#close" class="button submit closeButton"><g:message code="subscription.close" /></a>
			        </fieldset>
			
			    </g:form>
			</section>
		</div>
		<div id="dialogInfo">
			<span id="dialogInfoText"></span>
			<a id="closeSubscriptionDialog" href="#close" class="button submit closeButton" style="float: right;"><g:message code="subscription.close" /></a>
		</div>
	</div>
    <nav>
        <g:form>
            <fieldset id="clientSignaturePersons" class="subpanel-fieldset">
                <legend><g:message code="clientSignature.signing.people" default="Osoby podpisujące" /></legend>
                <ul class="table-list">
                    <li>
                    	<span>
                    	<a class="big-link" id="subscribe-REPRESENTATIVE1"
                                 href="eumowysig://data/${representative1.name.encodeAsURL()}/${representative1.surname.encodeAsURL()}/ACCEPTANT1/${message(code:'subscription.agreement').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${representative1.name} ${representative1.surname} - Reprezentant</a>
                        </span>
                        <span>
                            <a href="" onclick="refreshSignature('${processInstance.id}','ACCEPTANT1','subscribe-REPRESENTATIVE1');return false;" class="button action"><g:message code="subscription.refresh" /></a>
                        </span>
                    </li>

                    <li><span><a class="big-link" id="subscribe-REPRESENTATIVE2"
                                 href="eumowysig://data/${representative2.name.encodeAsURL()}/${representative2.surname.encodeAsURL()}/ACCEPTANT2/${message(code:'subscription.agreement').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${representative2.name} ${representative2.surname} - Reprezentant</a>
                    </span>
                    <span>
                            <a href="" onclick="refreshSignature('${processInstance.id}','ACCEPTANT2','subscribe-REPRESENTATIVE2');return false;" class="button action"><g:message code="subscription.refresh" /></a>
                    </span>
                    </li>
						
                    <li>
                      <span><a class="big-link" id="subscribe-PH"
                                 href="eumowysig://data/${processInstance.phFirstName.encodeAsURL()}/${processInstance.phSurname.encodeAsURL()}/PH/${message(code:'subscription.agreement.ph').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${processInstance.phFirstName} ${processInstance.phSurname} - Pracownik eService</a>
                      </span>
                      <span>
                            <a href="" onclick="refreshSignature('${processInstance.id}','PH','subscribe-PH');return false;" class="button action"><g:message code="subscription.refresh" /></a>
                      </span>
                    </li>

                    <li>
                        <a href="<g:createLink controller="file" action="get" params="[root: 'mobileAppPath', path: 'eumowy-mobile.apk']"/>">${message(code:'subscription.download.mobileApp')}</a>
                    </li>
                </ul>
            </fieldset>
            <fieldset class="subpanel-fieldset" id="clientSignatureDocType">
                <legend><g:message code="clientSignature.version.title" default="Forma" /></legend>
                <ul class="table-list">
                    <li>
                        <span>
                            <label>
                                <g:radio id="requestVersionElectronical" name="requestVersion" value="electronical"/>
                                <g:message code="clientSignature.electronicalVersion.radio" default="Elektroniczna" />
                            </label>
                        </span>
                    </li>
                    <li>
                        <span>
                            <label>
                                <g:radio id="requestVersionPaper" name="requestVersion" value="paper" />
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
                            <g:link style="width: 100%" event="back" class="button submit link-button">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                        </td>
                        <td style="text-align: right;">
                            <g:submitButton id="noaccept" name="noaccept" class="button submit display-inline" style="width: 90%"
                                            value="${message(code: 'clientSignature.noAcceptance.button', default:'Brak akceptacji')}"/>
                        </td>
                        <td>
                            <g:submitButton style="width: 100%" id="continueButton" name="submit" class="button submit" value="${message(code: 'default.navigation.button.finish', default: 'Zakończ')}"/>
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