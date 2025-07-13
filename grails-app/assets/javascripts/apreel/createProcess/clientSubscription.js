//= require vendors/panzoom.min
//= require_tree vendors/jsignature
//= require vendors/base64utils
//= require vendors/pdf.js
//= require apreel/document-preview/PdfPreviewManager

var updateSubscriptionStatusCount = 0,
    isSubscriptionDone = {},
    subscriptionDialog = null,
    subscriptionLinkId = null,
    subscriberSubmitButtonName = {
        "subscribe-REPRESENTATIVE1" : "#sgnRep1",
        "subscribe-REPRESENTATIVE2" : "#sgnRep2",
        "subscribe-REPRESENTATIVE3" : "#sgnRep3",
        "subscribe-REPRESENTATIVE4" : "#sgnRep4",
        "subscribe-PH" : "#sgnPh"
    };

function allSubscriptionsReady() {
    if (requiredSubscriptionsCount == 0) return isSubscriptionDone["subscribe-PH"];

    var subscriptions = 0;

    for (var key in subscriberSubmitButtonName) {
        if (subscriberSubmitButtonName.hasOwnProperty(key) && isSubscriptionDone[key]) {
            subscriptions++;
        }
    }

    return requiredSubscriptionsCount == subscriptions;
}

jQuery(document).ready(function() {
    var previewManager = new PdfPreviewManager(),
        subscriptionDialog = jQuery('#subscriptionDialog');

    previewManager.setDocuments(getDocuments(previewManager.scale));

    // checkEmailKontakt();

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

    // jQuery("#noaccept").on("click", function(e) {
    //     e.preventDefault();
    //     jQuery("#confirm-noaccept-dialog").dialog({
    //         resizable: true,
    //         height:200,
    //         width: 450,
    //         modal: true,
    //         buttons:
    //         {
    //             "Tak": function() {
    //                 jQuery( this ).dialog( "close" );
    //                 jQuery.post(jQuery(location).attr('href'), {_eventId_noaccept:""}, function(data) {
    //                     window.location.href = createProcessRejectLink;
    //                 });
    //             },
    //             "Nie": function() {
    //                 jQuery( this ).dialog( "close" );
    //             }
    //         }
    //     });
    //
    //     return false;
    // });

    // jQuery("#continueButton").on("click", function(e) {
    //     e.preventDefault();
    //
    //     if (allSubscriptionsReady() == false && jQuery("#requestVersionElectronical").is(":checked") == true) {
    //         result = false;
    //         jQuery("#confirm-submit-without-subscription-dialog").dialog({
    //             resizable: true,
    //             height:200,
    //             width: 450,
    //             modal: true,
    //             buttons:
    //             {
    //                 "Tak": function() {
    //                     jQuery( this ).dialog( "close" );
    //                     jQuery('#confirm-pleasewait h2').text(sendingEmailsMsg);
    //                     jQuery('#confirm-pleasewait img').show();
    //                     jQuery('#confirm-pleasewait').dialog({resizable: true,
    //                         height:200,
    //                         width: 450,
    //                         modal: true});
    //                     jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
    //                         window.location.href = createProcessPrevActivityMessage;
    //                     })
    //                         .fail(function() { jQuery("#confirm-pleasewait h2").text(sendingEmailsErrorMsg); jQuery("#confirm-pleasewait img").hide() });
    //
    //                     result = true;
    //                 },
    //                 "Nie": function() {
    //                     jQuery( this ).dialog( "close" );
    //                 }
    //             }
    //         });
    //     }
    //     else {
    //
    //         jQuery('#confirm-pleasewait h2').text(sendingEmailsMsg);
    //         jQuery('#confirm-pleasewait img').show();
    //         jQuery('#confirm-pleasewait').dialog({resizable: true,
    //             height:200,
    //             width: 450,
    //             modal: true});
    //         jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
    //             window.location.href = createProcessPrevActivityMessage;
    //         })
    //             .fail(function() { jQuery("#confirm-pleasewait h2").text(sendingEmailsErrorMsg); jQuery("#confirm-pleasewait img").hide() });
    //     }
    //     return false;
    // });

    // if (requiredSubscriptionsCount == 1){
    //     jQuery("#subscribe-REPRESENTATIVE1").parent().parent().hide();
    //     jQuery("#subscribe-REPRESENTATIVE2").parent().parent().hide();
    //     jQuery("#subscribe-REPRESENTATIVE3").parent().parent().hide();
    //     jQuery("#subscribe-REPRESENTATIVE4").parent().parent().hide();
    // }

    // hideIfNoRepresentativeData("#subscribe-REPRESENTATIVE1");
    // hideIfNoRepresentativeData("#subscribe-REPRESENTATIVE2");
    // hideIfNoRepresentativeData("#subscribe-REPRESENTATIVE3");
    // hideIfNoRepresentativeData("#subscribe-REPRESENTATIVE4");

    // setSubscriptions();

    // jQuery("#requestVersionTemplates").on("change", function(e) {
    //     if (jQuery(e.target).is(":checked")) {
    //         jQuery("#noaccept").prop("disabled", true);
    //         jQuery("a.big-link").parent().addClass("disabled");
    //
    //         jQuery("#sgnRep1").addClass("disabled");
    //         jQuery("#sgnRep2").addClass("disabled");
    //         jQuery("#sgnRep3").addClass("disabled");
    //         jQuery("#sgnRep4").addClass("disabled");
    //         jQuery("#sgnPh").addClass("disabled");
    //     }
    // });

    // jQuery("#requestVersionPaper").on("change", function(e) {
    //     if (jQuery(e.target).is(":checked")) {
    //         manageAvailability()
    //     }
    // });

    // jQuery("#requestVersionElectronical").on("change", function(e) {
    //     if (jQuery(e.target).is(":checked")) {
    //         manageAvailability()
    //     }
    // });

    // function hideIfNoRepresentativeData(id) {
    //     if (jQuery(id).text() == "  - Reprezentant") {
    //         jQuery(id).parent().parent().hide();
    //     }
    // }

    // function manageAvailability() {
    //     jQuery("#noaccept").prop("disabled", false);
    //
    //     jQuery("#sgnRep1").removeClass("disabled");
    //     jQuery("#sgnRep2").removeClass("disabled");
    //     jQuery("#sgnRep3").removeClass("disabled");
    //     jQuery("#sgnRep4").removeClass("disabled");
    //     jQuery("#sgnPh").removeClass("disabled");
    //
    //     if (isSubscriptionDone["subscribe-REPRESENTATIVE1"] != true) {
    //         jQuery("#subscribe-REPRESENTATIVE1").parent().removeClass("disabled");
    //     }
    //
    //     if (isSubscriptionDone["subscribe-REPRESENTATIVE2"] != true) {
    //         jQuery("#subscribe-REPRESENTATIVE2").parent().removeClass("disabled");
    //     }
    //
    //     if (isSubscriptionDone["subscribe-REPRESENTATIVE3"] != true) {
    //         jQuery("#subscribe-REPRESENTATIVE3").parent().removeClass("disabled");
    //     }
    //
    //     if (isSubscriptionDone["subscribe-REPRESENTATIVE4"] != true) {
    //         jQuery("#subscribe-REPRESENTATIVE4").parent().removeClass("disabled");
    //     }
    //
    //     if (isSubscriptionDone["subscribe-PH"] != true) {
    //         jQuery("#subscribe-PH").parent().removeClass("disabled");
    //     }
    // }

    // function checkEmailKontakt() {
    //     if(!kontaktEmail && !emailDoWysylkiDokumentu ) {
    //         jQuery("#requestVersionElectronical").attr("disabled","disabled").removeAttr("checked");
    //         jQuery("#requestVersionPaper").attr("checked","checked");
    //         jQuery("#requestVersionTemplates").attr("disabled","disabled").removeAttr("checked");
    //
    //         jQuery("#noaccept").prop("disabled", true);
    //         jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
    //         jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
    //         jQuery("#subscribe-REPRESENTATIVE3").parent().addClass("disabled");
    //         jQuery("#subscribe-REPRESENTATIVE4").parent().addClass("disabled");
    //         jQuery("#subscribe-PH").parent().addClass("disabled");
    //     } else{
    //         jQuery("#requestVersionElectronical").attr("checked","checked");
    //     }
    // }
});
