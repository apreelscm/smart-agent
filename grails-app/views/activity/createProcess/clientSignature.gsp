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
<r:require module="documentPreview" />

<script type="text/javascript">
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

    var subscriberSubmitButtonName = {
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
                documents = [],
                subscriptionDialog = jQuery('#subscriptionDialog');

        <g:each in="${processInstance.documents?.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}?.findAll {it.signature?.showOnPreview} }" status="i" var="document">
        documents.push(new PDocument(${i} ,'${document?.clientName}', previewManager.scale, base64DecToArr('${document?.content?.getPreviewContent().encodeBase64().toString()}').buffer));
        </g:each>

        previewManager.setDocuments(documents);

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

        jQuery("#sgnRep1").click(function(e) {
            e.preventDefault();
            var $this = jQuery(this);
            if (!$this.hasClass('action_visited')) {
                refreshSignature('${processInstance.id}','ACCEPTANT1','subscribe-REPRESENTATIVE1');
            }
            return false;
        });

        jQuery('#sgnRep2').click(function(e) {
            e.preventDefault();
            var $this = jQuery(this);
            if (!$this.hasClass('action_visited')) {
                refreshSignature('${processInstance.id}','ACCEPTANT2','subscribe-REPRESENTATIVE2');
            }
            return false;
        });

        jQuery("#sgnPh").click(function(e) {
            e.preventDefault();
            var $this = jQuery(this);
            if (!$this.hasClass('action_visited')) {
                refreshSignature('${processInstance.id}','PH','subscribe-PH');
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

        <g:each in="${processInstance.subscriptions}">
            console.log("${it.personRole}");
            if(jQuery("#subscribe-REPRESENTATIVE1").attr("data-type") == "${it.personRole}") {
                jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");
                jQuery("#sgnRep1").removeClass('action').addClass('action_visited');
                updateSubscriptionStatusCount++;
                jQuery("#clientSignatureBackButton").addClass("disabled");
                isSubscriptionDone["subscribe-REPRESENTATIVE1"] = true;
            }
            if(jQuery("#subscribe-REPRESENTATIVE2").attr("data-type") == "${it.personRole}") {
                jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");
                jQuery("#sgnRep2").removeClass('action').addClass('action_visited');
                updateSubscriptionStatusCount++;
                jQuery("#clientSignatureBackButton").addClass("disabled");
                isSubscriptionDone["subscribe-REPRESENTATIVE2"] = true;
            }
            if(jQuery("#subscribe-PH").attr("data-type") == "${it.personRole}") {
                jQuery("#subscribe-PH").parent().addClass("disabled");
                jQuery("#sgnPh").removeClass('action').addClass('action_visited');
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

        function checkEmailKontakt() {
            var kontaktEmail = "${processInstance.processData.find { it.name == 'kontaktEmail'}?.value}";
            var emailDoWysylkiDokumentu = "${processInstance.processData.find { it.name == 'emailDoWysylkiDokumentu'}?.value}";

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
</script>
</head>

<body>

<div id="savingSubscriptionPopup" style="display: none">Trwa zapisywanie podpisu...</div>

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
        <g:each in="${processInstance.documents?.sort(false){a,b -> a.signature.signatureOrder.compareTo(b.signature.signatureOrder)}?.findAll {it.signature?.showOnPreview} }" status="i" var="document">
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
    <nav>
        <g:form>
            <fieldset id="clientSignaturePersons" class="subpanel-fieldset">
                <legend><g:message code="clientSignature.signing.people" default="Osoby podpisujące" /></legend>
                <ul class="table-list">
                    <li>
                        <span>
                            <a class="big-link" id="subscribe-REPRESENTATIVE1" data-type="ACCEPTANT1"
                               href="eumowysig://data/${representative1.name.encodeAsURL()}/${representative1.surname.encodeAsURL()}/ACCEPTANT1/${message(code:'subscription.agreement').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${representative1.name} ${representative1.surname} - Reprezentant</a>
                        </span>
                        <span>
                            <a href="" id="sgnRep1" class="button action"><g:message code="subscription.refresh" /></a>
                        </span>
                    </li>

                    <li><span><a class="big-link" id="subscribe-REPRESENTATIVE2" data-type="ACCEPTANT2"
                                 href="eumowysig://data/${representative2.name.encodeAsURL()}/${representative2.surname.encodeAsURL()}/ACCEPTANT2/${message(code:'subscription.agreement').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${representative2.name} ${representative2.surname} - Reprezentant</a>
                    </span>
                        <span>
                            <a href="" id="sgnRep2" class="button action"><g:message code="subscription.refresh" /></a>
                        </span>
                    </li>

                    <li>
                        <span><a class="big-link" id="subscribe-PH" data-type="PH"
                                 href="eumowysig://data/${processInstance.phFirstName.encodeAsURL()}/${processInstance.phSurname.encodeAsURL()}/PH/${message(code:'subscription.agreement.ph').encodeAsURL()}/${processInstance.id}/${session.id}/${createLink(controller: "subscriptionEx", action:"saveSubscription", absolute: true).encodeAsURL()}">${processInstance.phFirstName} ${processInstance.phSurname} - Pracownik eService</a>
                        </span>
                        <span>
                            <a href="" id="sgnPh" class="button action"><g:message code="subscription.refresh" /></a>
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