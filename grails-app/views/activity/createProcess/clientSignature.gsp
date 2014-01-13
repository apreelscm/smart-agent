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
<r:require module="pdf" />

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
	
	
	/* Documents Preview Functions */
	
	// PdfPage Object
	function PPage(scale, num, data) {
		this.num = num;
		this.scale = scale;
		this.page = data;
		this.viewport = data.getViewport(this.scale);
        this.buffer = null;
        this.ctx = null;
        
        this.isReady = false;
       	//this.render();
	}
	
	// PdfPage Object Methods
	PPage.prototype.renderToBuffer = function(width, height) {
		var buffer = document.createElement('canvas');
	    buffer.width = width;
	    buffer.height = height;
	    return buffer;
	}
	
	PPage.prototype.render = function(buffer) {
		//if(this.isReady == true)
		//	return;
		console.log("Page " + this.num + " rendering...");
		
		//this.buffer = this.renderToBuffer(this.viewport.width, this.viewport.height);
		this.buffer = buffer.buff;
        this.ctx = buffer.ctx;
        
        this.buffer.width = this.viewport.width;
        this.buffer.height = this.viewport.height;
		
		var that = this;
		var renderContext = {
			canvasContext: that.ctx,
        	viewport: that.viewport
		};
		this.page.render(renderContext).then(function() {
			console.log("Page " + that.num + " rendering complete!");
			//that.buffer = null;
			that.ctx = null;
			that.isReady = true;
		});
	}
	
	PPage.prototype.getImage = function() {
		return this.buffer;
	}
	
	PPage.prototype.release = function() {
		delete this.buffer;
        delete this.ctx;
        this.isReady = false;
	}
	
	// PdfDocumentObject
	function PDocument(num, name, scale, pdfData) {
		this.num = num;
		this.name = name;
		this.pdfData = pdfData;
		this.scale = scale;
		this.pdf = null;
		this.pages = [];
		this.pagesCount = 0;
		this.currentPage = 1;
		this.isReady = false;
	}
	
	PDocument.prototype.init = function() {
		var that = this;
		PDFJS.getDocument(this.pdfData).then(function(pdf) {
			that.pdf = pdf;
			that.currentPage = 1;
			that.pagesCount = pdf.numPages;
			that.isReady = true;
			// Prerendering document
			that.preRender();
		});
	}
	
	// PdfDocument Object Methods
	PDocument.prototype.preRender = function() {
		console.log("Prerendering document["+this.num+"]: " + this.name + " in progress...");
		if (this.pages == undefined)
			this.pages = [];
		var that = this;
		for (var i = 1; i <= this.pagesCount; i++) {
			this.pdf.getPage(i).then(function(page) { 
				var f = function(idx, p, t) {
					return function() { t.pages.push(new PPage(t.scale, idx, p)); };
				} (i, page, that);
				f();
			});
		}
	}
	
	PDocument.prototype.getCurrentPage = function() {
		return this.pages != undefined ? this.pages[this.currentPage-1] : undefined;
	}
	
	PDocument.prototype.renderAll = function(buffers) {
		this.init();
		var that = this;
		var intervalId = setInterval(function() {
			if (that.isReady == true) {
				for(var i = 0; i < that.pagesCount; i++)
					that.pages[i].render(buffers[i]);
				clearInterval(intervalId);
			}
		}, 1000);
	}
	
	PDocument.prototype.releaseAll = function() {
		//delete this.pdfData;
		delete this.pdf;
		if (this.pages != undefined) {
			for(var i = 0; i < this.pagesCount; i++) {
				this.pages[i].release();
			}
			delete this.pages;
		}
		this.isReady = false;
	}
	
	// PdfPreviewManager Object
	function PdfPreviewManager() {
		this.scale = 1.5;
		this.currentDocument = 0;
		this.buffers = [];
		
		this.$currentPageEl = jQuery("#page_num");
		this.$totalPagesEl = jQuery("#page_count");
		this.$prevButtonEl = jQuery("#prevPdfPage");
		this.$nextButtonEl = jQuery("#nextPdfPage");
		this.$loadingBoxEl = jQuery("#pdfBox-content-loading");
		
		this.$pdfBoxEl = jQuery("#pdfPage");
		this.canvas = document.getElementById('pdfPage');
		this.globalContext = this.canvas.getContext('2d');
		
		var that = this;
		this.$prevButtonEl.on("click", function(e) {
    		e.preventDefault();
    		that.navigatePdfPageView("prev");
	     	return false;
    	});
    	
    	this.$nextButtonEl.on("click", function(e) {
    		e.preventDefault();
    		that.navigatePdfPageView("next");
	     	return false;
    	});
	}
	
	PdfPreviewManager.prototype.navigatePdfPageView = function (direction) {
        if (direction == "prev") {
            if (this.documents[this.currentDocument].currentPage <= 1)
                return;
            
            this.documents[this.currentDocument].currentPage--;
            
            if (this.documents[this.currentDocument].currentPage == 1) {
            	this.$prevButtonEl.addClass("disabled");
            }
            else {
            	this.$prevButtonEl.removeClass("disabled");
            }
        }
        else if (direction == "next") {
           	this.$prevButtonEl.removeClass("disabled");
            
            if (this.documents[this.currentDocument].currentPage >= this.documents[this.currentDocument].pagesCount) {
            	this.$nextButtonEl.addClass("disabled");
                return;
            }
            
            this.documents[this.currentDocument].currentPage++;

            if (this.documents[this.currentDocument].getCurrentPage().isReady == false) {
            	this.$nextButtonEl.addClass("disabled");
           	}
           	
           	if (this.documents[this.currentDocument].currentPage >= this.documents[this.currentDocument].pagesCount) {
           		this.$nextButtonEl.addClass("disabled");
           	}
        }

        this.$pdfBoxEl.css("display", "none");
        this.$loadingBoxEl.show();

        this.refreshPreview();
     	this.$currentPageEl.html(this.documents[this.currentDocument].currentPage);
	}
	
	PdfPreviewManager.prototype.drawPage = function() {
		//this.$pdfBoxEl.attr('style', '');
		this.canvas.width = this.documents[this.currentDocument].getCurrentPage().viewport.width;
	    this.canvas.height = this.documents[this.currentDocument].getCurrentPage().viewport.height;
	    this.globalContext.clearRect( 0, 0, this.canvas.width, this.canvas.height );
		this.globalContext.drawImage(this.documents[this.currentDocument].getCurrentPage().getImage(), 0, 0);
	}
	
	PdfPreviewManager.prototype.refreshPreview = function() {
		if (this.documents[this.currentDocument].getCurrentPage() != undefined && this.documents[this.currentDocument].getCurrentPage().isReady == true) {
			this.$currentPageEl.html(this.documents[this.currentDocument].currentPage);
			this.$totalPagesEl.html(this.documents[this.currentDocument].pagesCount);
			this.$loadingBoxEl.hide();
			this.$pdfBoxEl.css("display", "block");
			if (this.documents[this.currentDocument].currentPage < this.documents[this.currentDocument].pagesCount) {
				this.$nextButtonEl.removeClass("disabled");
			}
			else {
				this.$nextButtonEl.addClass("disabled");
			}
			this.drawPage();
			return true;
		}
		return false;
	}
	
	PdfPreviewManager.prototype.setDocuments = function(documents) {
		this.documents = documents;
	}
	
	PdfPreviewManager.prototype.showDocument = function(id) {
		console.log("Showing document["+id+"]: " + this.documents[id].name);
		this.$loadingBoxEl.show();
		this.$pdfBoxEl.css("display", "none");
		this.currentDocument = id;
		
		for(var i = 0; i < this.documents.length; i++) {
			if(i != this.currentDocument) {
				this.documents[i].releaseAll();
			}
		}
		
		this.documents[this.currentDocument].currentPage = 1;
		
		for(var i = 0; i < this.buffers.length; i++) {
			//this.buffers[i].ctx.clearRect(0,0, this.buffers[i].buff.width, this.buffers[i].buff.height);
			//this.buffers.pop();
			delete this.buffers[i]
		}
		this.buffers = [];
		for(var i = 0; i < 12; i++) {
			var buff = {};
			
			buff.buff = document.createElement('canvas');
			buff.ctx = buff.buff.getContext('2d');
			this.buffers.push(buff);
		}
		
		this.documents[this.currentDocument].renderAll(this.buffers);
		var that = this;
		this.$prevButtonEl.addClass("disabled");
		var intervalId = setInterval(function() {
			var result = that.refreshPreview();
			if (result == true) {
				clearInterval(intervalId);
			}
		}, 1000);
	}
	
	jQuery(document).ready(function(){
	    var previewManager = new PdfPreviewManager(),
    		documents = [],
			subscriptionDialog = jQuery('#subscriptionDialog');
		
		<g:each in="${processInstance.documents.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}?.findAll {it.signature?.showOnPreview} }" status="i" var="document"> 
        	documents.push(new PDocument(${i} ,'${document?.clientName}', previewManager.scale, base64DecToArr('${document?.content?.getPreviewContent().encodeBase64().toString()}').buffer));
	    </g:each>
	    
	    previewManager.setDocuments(documents);
	
		setUpSignaturePad();
	    checkEmailKontakt();
	
		jQuery('a#previewPdfButton').on('click', function(e) {
			e.preventDefault();
			var i = parseInt(jQuery(e.target).attr('data-document-index'));
			previewManager.showDocument(i);
		    return false;
		});
	
		jQuery("#pdfPage").panzoom({
			$zoomIn: jQuery("#zoomInPdfPage"),
			$zoomOut: jQuery("#zoomOutPdfPage"),
			contain: 'invert'
		});
		
		//jQuery("#pdfBox-content-loading").show();
		//previewManager.showDocument(0);
		    	
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
	                var kontaktEmail = "${processInstance.processData.find { it.name == 'kontaktEmail'}?.value}";
	                var emailDoWysylkiDokumentu = "${processInstance.processData.find { it.name == 'emailDoWysylkiDokumentu'}?.value}";
	
	               // console.info("checkEmailKontakt - kontaktEmail:"+kontaktEmail+" emailDoWysylkiDokumentu:"+emailDoWysylkiDokumentu)
	
	               if(!kontaktEmail && !emailDoWysylkiDokumentu ){
	                      jQuery("#requestVersionElectronical").attr("disabled","disabled").removeAttr("checked");
	                      jQuery("#requestVersionPaper").attr("checked","checked");
	                      jQuery("#requestVersionTemplates").attr("disabled","disabled").removeAttr("checked");

	                      jQuery("#noaccept").prop("disabled", true);
	                      jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
	                      jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
	                      jQuery("#subscribe-PH").parent().addClass("disabled");
	               }else{
	                   jQuery("#requestVersionElectronical").attr("checked","checked");
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
    <table border="0" align="center" cellpadding="3" cellspacing="1" class="table"
       style="margin: 20px 15px 15px 15px; width: 950px;">
	    <thead>
	    <tr>
	        <th style="text-align: center"><g:message code="process.document"/></th>
	        <th style="text-align: center"><g:message code="process.update.date"/></th>
	        <th style="text-align: center"></th>
	    </tr>
	    </thead>
	    <tbody>
	    <g:each in="${processInstance.documents.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}?.findAll {it.signature?.showOnPreview} }" status="i" var="document">
	        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	            <td class="tableCellLeft" style="vertical-align: middle"><a id="previewPdfButton" data-document-index="${i}" href="#">${document.clientName}</td>
	            <td class="tableCell" style="vertical-align: middle"><g:formatDate date="${document.lastUpdated}" format="yyyy-MM-dd HH:mm"/></td>
	            <td class="tableCell" style="vertical-align: middle">
	                <g:link class="button action" style="margin: 0 auto" action="downloadDoc" params="[id: document.id]">Pobierz</g:link>
	            </td>
	        </tr>
	    </g:each>
	    </tbody>
	</table>
	<div id="pdfBox" style="background-color: #F2F2F2; height: 680px; overflow: auto;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <div id="pdfBox-nav" style="padding: 1em; border-bottom: solid 1px;">
            <div style="display: inline-block; float: left">
                <a id="zoomOutPdfPage" class="button submit">-</a>
                <a id="zoomInPdfPage" class="button submit">+</a>
            </div>
            <div style="display: inline-block; float: right">
                <a id="nextPdfPage" class="button submit disabled" style="float: right"><g:message code="process.subscriptions.nextPage" /></a>
                <a id="prevPdfPage" class="button submit disabled"><g:message code="process.subscriptions.previousPage" /></a>
            </div>
            <div style="text-align: center; padding-left: 165px; padding-top: 5px">
                <span style="font-weight: bold"><g:message code="process.subscriptions.page" />: <span id="page_num">-</span> / <span id="page_count">-</span></span>
            </div>
            <div style="clear: both"></div>
        </div>
        <div id="pdfBox-content" style="margin: 1em;">
            <div id="pdfBox-content-loading" style="text-align: center; width: 200px; display: none; margin: 0 auto;">
                <h2 style="padding-top: 100px;"><g:message code="process.subscriptions.loadingPage" /></h2>
                <img style="width: 40px;" src="/eumowy/images/document-loading.gif" />
            </div>
            <canvas id="pdfPage" style="border:1px solid gray; display: none; width: 440px; height: 570px; display: none; margin-left: auto; margin-right: auto; vertical-align: middle; text-align: center;"></canvas>
            <!-- <img id="pdfPage" style="border:1px solid gray; display: none; width: 440px; height: 570px; display: none; margin-left: auto; margin-right: auto; vertical-align: middle; text-align: center;"/> -->
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
                            <a href="" id="sgnRep1" onclick="if (!jQuery('#sgnPh').hasClass('visited')){refreshSignature('${processInstance.id}','ACCEPTANT1','subscribe-REPRESENTATIVE1'); jQuery('#sgnRep1').addClass('visited');} return false;" class="button action"><g:message code="subscription.refresh" /></a>
                        </span>
                    </li>

                    <li><span><a class="big-link" id="subscribe-REPRESENTATIVE2"
                                 href="eumowysig://data/${representative2.name.encodeAsURL()}/${representative2.surname.encodeAsURL()}/ACCEPTANT2/${message(code:'subscription.agreement').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${representative2.name} ${representative2.surname} - Reprezentant</a>
                    </span>
                    <span>
                            <a href="" id="sgnRep2" onclick="if (!jQuery('#sgnPh').hasClass('visited')){refreshSignature('${processInstance.id}','ACCEPTANT2','subscribe-REPRESENTATIVE2'); jQuery('#sgnRep2').addClass('visited');} return false;" class="button action"><g:message code="subscription.refresh" /></a>
                    </span>
                    </li>
						
                    <li>
                      <span><a class="big-link" id="subscribe-PH"
                                 href="eumowysig://data/${processInstance.phFirstName.encodeAsURL()}/${processInstance.phSurname.encodeAsURL()}/PH/${message(code:'subscription.agreement.ph').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${processInstance.phFirstName} ${processInstance.phSurname} - Pracownik eService</a>
                      </span>
                      <span>
                            <a href="" id="sgnPh" onclick="if (!jQuery('#sgnPh').hasClass('visited')){ refreshSignature('${processInstance.id}','PH','subscribe-PH'); jQuery('#sgnPh').addClass('visited');} return false;" class="button action"><g:message code="subscription.refresh" /></a>
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
                            <g:link style="width: 100%" event="back" class="button submit link-button ${isUzupelnijPodpisy ? "disabled" : ""}">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
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