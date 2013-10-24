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
<r:require module="jquery_ui" />
<r:require module="panzoom" />

<r:script>
		var updateSubscriptionStatusCount = 0;
		var isSubscriptionDone = {};
		var requiredSubscriptionsCount = ${requiredNumberOfSubscriptions}
function updateSubscriptionStatus(status, linkid, subId) {
    if (status == "OK") {
        updateSubscriptionStatusCount++;
        jQuery("#clientSignatureBackButton").addClass("disabled");

        jQuery("#"+linkid).parent().addClass("disabled");
        isSubscriptionDone[linkid] = true;

        if (updateSubscriptionStatusCount >= 1 && updateSubscriptionStatusCount <= requiredSubscriptionsCount - 1) {
            jQuery.post($(location).attr("href"), {_eventId_updateProcessStatus: "", processStatus: "WAIT_FOR_SUBSCRIPTION", subscriptionId: subId}, function(data){});
        }

        if (updateSubscriptionStatusCount == requiredSubscriptionsCount) {
            jQuery.post($(location).attr("href"), {_eventId_updateProcessStatus: "", processStatus: "SUBSCRIPTIONS_DONE", subscriptionId: subId}, function(data){});
        }
    }
}
jQuery(document).ready(function(){
    var pageNum = 1;
    var documentPages = [];


    checkEmailKontakt()

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

    var currentTarget = jQuery(e.currentTarget),
        firstName = currentTarget.attr('data-firstName'),
        lastName = currentTarget.attr('data-lastName'),
        role = currentTarget.attr('data-role'),
        linkId = currentTarget.attr('id');

    var dialog = jQuery('#subscriptionDialog');
    if (jQuery('#subscriptionDialog').length == 0) {
        dialog = jQuery('<div id="subscriptionDialog" style="display:hidden"></div>').appendTo('body');
				}

				dialog.load(
                    "${createLink(controller: 'subscription', action: 'inlineview')}&name=" + encodeURI(firstName) + "&surname=" + encodeURI(lastName) + "&personRole=" + role + "&linkid=" + linkId,
		            {},
		            function(responseText, textStatus, XMLHttpRequest) {
		                dialog.dialog({
		                	modal: true,
      						width: 750,
      						open : function() {
							    var t = jQuery(this).parent(), w = jQuery(window);
							    t.offset({
							        top: (w.height() / 2) - (t.height() / 2) - 50,
							        left: (w.width() / 2) - (t.width() / 2)
							    });
							    w.scrollTop(0);
							    setUpSignaturePad();
							    jQuery('body').addClass('stop-scrolling');
							},
							close: function() {
								jQuery('body').removeClass('stop-scrolling');
							}
		                });
		                
		            }
		        );
		        
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
                var kontaktEmail = ${processInstance.processData.any { it.name == 'kontaktEmail'}}
                var hasKontaktEmail = ${processInstance.processData.any { it.name == 'kontaktEmail' && it.value}}

                if(kontaktEmail && !hasKontaktEmail){
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

    <nav>
        <g:form>
            <fieldset id="clientSignaturePersons" class="subpanel-fieldset">
                <legend><g:message code="clientSignature.signing.people" default="Osoby podpisujące" /></legend>
                <ul class="table-list">
                    <li><span><a class="big-link showSignatureDialog" data-firstName="${representative1.name}"
                                 data-lastName="${representative1.surname}" data-role="ACCEPTANT1" id="subscribe-REPRESENTATIVE1"
                                 href="#">${representative1.name} ${representative1.surname} - Reprezentant</a></span></li>

                    <li><span><a class="big-link showSignatureDialog" data-firstName="${representative2.name}"
                                 data-lastName="${representative2.surname}" data-role="ACCEPTANT2" id="subscribe-REPRESENTATIVE2"
                                 href="#">${representative2.name} ${representative2.surname} - Reprezentant</a></span></li>

                    <li><span><a class="big-link showSignatureDialog" data-firstName="${processInstance.phFirstName}"
                                 data-lastName="${processInstance.phSurname}" data-role="PH" id="subscribe-PH"
                                 href="#">${processInstance.phFirstName} ${processInstance.phSurname} - Pracownik eService</a></span></li>
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