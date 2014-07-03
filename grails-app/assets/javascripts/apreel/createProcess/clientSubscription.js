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
        "subscribe-PH" : "#sgnPh"
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
        return false;
    });
}

function refreshSignature(processId, role, subscriberName) {
    jQuery("#dialogInfoText").html('<h2 class="align-center">Zapisywanie...</h2>');
    jQuery("#dialoginfo").show();
    jQuery.post("${createLink(controller: 'subscription', action: 'refreshSubscription')}", {processId: processId, role: role}, function (result) {
        if (result.success) {
            jQuery("#dialogInfoText").html('<h2 class="align-center">Pomyślnie zapisano podpis!</h2>');
            updateSubscriptionStatus(subscriberName, result.subscriptionId);
        } else {
            jQuery("#dialogInfoText").html('<h3 class="align-center" style="color: red;">' + result.text + '</h3>');
        }
        jQuery('#dialog').hide();
        jQuery('#dialogInfo').show();
    });
    return false;
}

function updateSubscriptionStatus(subscriberName, subId) {
    var url = jQuery(location).attr("href");

    updateSubscriptionStatusCount++;
    jQuery("#clientSignatureBackButton").addClass("disabled");

    jQuery("#" + subscriberName).parent().addClass("disabled");
    isSubscriptionDone[subscriberName] = true;

    jQuery("#savingSubscriptionPopup").dialog({
        height: 100,
        width: 220,
        modal: true
    })

    if (updateSubscriptionStatusCount >= 1 && updateSubscriptionStatusCount <= requiredSubscriptionsCount - 1) {
        jQuery.post(url, {_eventId_updateProcessStatus: "", processStatus: "WAIT_FOR_SUBSCRIPTION", subscriptionId: subId})
            .done(function() {
                jQuery(subscriberSubmitButtonName[subscriberName]).removeClass('action').addClass('action_visited');
                jQuery("#savingSubscriptionPopup").dialog("close");
            })
            .fail(function() {
                jQuery("#savingSubscriptionPopup").dialog("close");
                alert('Wystapil blad podczas zapisywania podpisu. Sprawdz swoje polaczenie internetowe i sprobuj ponownie pozniej.');
                jQuery(subscriberSubmitButtonName[subscriberName]).removeClass('action_visited').addClass('action');
            });
    }

    if (updateSubscriptionStatusCount == requiredSubscriptionsCount) {
        jQuery.post(url, {_eventId_updateProcessStatus: "", processStatus: "SUBSCRIPTIONS_DONE", subscriptionId: subId})
            .done(function() {
                jQuery(subscriberSubmitButtonName[subscriberName]).removeClass('action').addClass('action_visited');
                jQuery("#savingSubscriptionPopup").dialog("close");
            })
            .fail(function() {
                jQuery("#savingSubscriptionPopup").dialog("close");
                alert('Wystapil blad podczas zapisywania podpisu. Sprawdz swoje polaczenie internetowe i sprobuj ponownie pozniej. ');
                jQuery(subscriberSubmitButtonName[subscriberName]).removeClass('action_visited').addClass('action');
            });
    }

    jQuery('#dialogInfo').hide();
}

function showSubscriptionPanel(name, surname, role, subscriberName) {
    if (subscriptionDialog != null) {
        subscriptionLinkId = subscriberName;
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

function allSubscriptionsReady() {
    if (requiredSubscriptionsCount == 3) {
        if (isSubscriptionDone["subscribe-REPRESENTATIVE1"] == true &&
            isSubscriptionDone["subscribe-REPRESENTATIVE2"] == true &&
            isSubscriptionDone["subscribe-PH"] == true)
            return true;
    }
    else if (requiredSubscriptionsCount == 2) {
        if ((isSubscriptionDone["subscribe-REPRESENTATIVE1"] == true &&
            isSubscriptionDone["subscribe-PH"] == true) ||
            (isSubscriptionDone["subscribe-REPRESENTATIVE2"] == true &&
                isSubscriptionDone["subscribe-PH"] == true))
            return true;
    }
    else if (requiredSubscriptionsCount == 1 || requiredSubscriptionsCount == 0) {
        if (isSubscriptionDone["subscribe-PH"] == true)
            return true;
    }

    return false;
}

jQuery(document).ready(function() {
    var previewManager = new PdfPreviewManager(),
        subscriptionDialog = jQuery('#subscriptionDialog');

    previewManager.setDocuments(getDocuments(previewManager.scale));

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
                        window.location.href = createProcessRejectLink;
                    });
                },
                "Nie": function() {
                    jQuery( this ).dialog( "close" );
                }
            }
        });

        return false;
    });

    jQuery("#sgnRep1").click(function(e) {
        e.preventDefault();
        var $this = jQuery(this);
        if (!$this.hasClass('action_visited')) {
            refreshSignature(processId,'ACCEPTANT1','subscribe-REPRESENTATIVE1');
        }
        return false;
    });

    jQuery('#sgnRep2').click(function(e) {
        e.preventDefault();
        var $this = jQuery(this);
        if (!$this.hasClass('action_visited')) {
            refreshSignature(processId,'ACCEPTANT2','subscribe-REPRESENTATIVE2');
        }
        return false;
    });

    jQuery("#sgnPh").click(function(e) {
        e.preventDefault();
        var $this = jQuery(this);
        if (!$this.hasClass('action_visited')) {
            refreshSignature(processId,'PH','subscribe-PH');
        }
        return false;
    });

    jQuery("#continueButton").on("click", function(e) {
        e.preventDefault();

        if (allSubscriptionsReady() == false && jQuery("#requestVersionElectronical").is(":checked") == true) {
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
                        jQuery('#confirm-pleasewait h2').text(sendingEmailsMsg);
                        jQuery('#confirm-pleasewait img').show();
                        jQuery('#confirm-pleasewait').dialog({resizable: true,
                            height:200,
                            width: 450,
                            modal: true});
                        jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
                            window.location.href = createProcessPrevActivityMessage;
                        })
                            .fail(function() { jQuery("#confirm-pleasewait h2").text(sendingEmailsErrorMsg); jQuery("#confirm-pleasewait img").hide() });

                        result = true;
                    },
                    "Nie": function() {
                        jQuery( this ).dialog( "close" );
                    }
                }
            });
        }
        else {

            jQuery('#confirm-pleasewait h2').text(sendingEmailsMsg);
            jQuery('#confirm-pleasewait img').show();
            jQuery('#confirm-pleasewait').dialog({resizable: true,
                height:200,
                width: 450,
                modal: true});
            jQuery.post(jQuery(location).attr('href'), {_eventId_submit:"",requestVersion: jQuery("input[name=requestVersion]:checked").val(), numberOfSubscriptions: updateSubscriptionStatusCount}, function(data, textStatus, jqXHR) {
                window.location.href = createProcessPrevActivityMessage;
            })
                .fail(function() { jQuery("#confirm-pleasewait h2").text(sendingEmailsErrorMsg); jQuery("#confirm-pleasewait img").hide() });
        }
        return false;
    });

    if (requiredSubscriptionsCount == 1){
        jQuery("#subscribe-REPRESENTATIVE1").parent().parent().hide();
        jQuery("#subscribe-REPRESENTATIVE2").parent().parent().hide();
    }

    if (jQuery("#subscribe-REPRESENTATIVE1").text() == "  - Reprezentant") {
        jQuery("#subscribe-REPRESENTATIVE1").parent().parent().hide();
    }

    if (jQuery("#subscribe-REPRESENTATIVE2").text() == "  - Reprezentant") {
        jQuery("#subscribe-REPRESENTATIVE2").parent().parent().hide();
    }

    setSubscriptions();

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

            jQuery("#sgnRep1").addClass("disabled");
            jQuery("#sgnRep2").addClass("disabled");
            jQuery("#sgnPh").addClass("disabled");
        }
    });

    jQuery("#requestVersionPaper").on("change", function(e) {
        if (jQuery(e.target).is(":checked")) {
            manageAvailability()
        }
    });

    jQuery("#requestVersionElectronical").on("change", function(e) {
        if (jQuery(e.target).is(":checked")) {
            manageAvailability()
        }
    });

    function manageAvailability() {
        jQuery("#noaccept").prop("disabled", false);

        jQuery("#sgnRep1").removeClass("disabled");
        jQuery("#sgnRep2").removeClass("disabled");
        jQuery("#sgnPh").removeClass("disabled");

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

    function checkEmailKontakt() {
        if(!kontaktEmail && !emailDoWysylkiDokumentu ) {
            jQuery("#requestVersionElectronical").attr("disabled","disabled").removeAttr("checked");
            jQuery("#requestVersionPaper").attr("checked","checked");
            jQuery("#requestVersionTemplates").attr("disabled","disabled").removeAttr("checked");

            jQuery("#noaccept").prop("disabled", true);
            jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
            jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
            jQuery("#subscribe-PH").parent().addClass("disabled");
        } else{
            jQuery("#requestVersionElectronical").attr("checked","checked");
        }
    }
});