<%@ page import="com.eservice.eumowy.Subscription; com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <meta name="viewport" content="width=960, initial-scale=1, maximum-scale=1"/>
    <title><g:message code="clientSignature.header.title"/></title>

    <asset:javascript src="apreel/createProcess/clientSubscription.js"/>

    <style>
    .navButtons {
        margin-top: 15px;
    }

    .navButtons td {
        padding: 5px;
        vertical-align: middle;
    }

    #signatureNavTable {
        border: none;
    }

    #signatureNavTable tbody tr:hover, #signatureNavTable tbody tr td.highlighted {
        background-color: white;
    }
    </style>

    <script type="text/javascript">
        var requiredSubscriptionsCount = ${requiredNumberOfSubscriptions},
                agreementLabel = {
                    "PH": "${message(code:'subscription.agreement.ph')}",
                    "ACCEPTANT1": "${message(code:'subscription.agreement')}",
                    "ACCEPTANT2": "${message(code:'subscription.agreement')}",
                    "ACCEPTANT3": "${message(code:'subscription.agreement')}",
                    "ACCEPTANT4": "${message(code:'subscription.agreement')}"
                },
                //createProcessRejectLink = '<g:createLink controller="activity" action="createProcess" params="[message: rejectedDocumentsMessage]"/>',
                //createProcessPrevActivityMessage = '<g:createLink controller="activity" action="createProcess" params="[message: prevActivityMessage]"/>',
                refreshSubscriptionLink = '${createLink(controller: 'subscription', action: 'refreshSubscription')}',
                processId = '${processInstance.id}',
                //kontaktEmail = "${processInstance.getData("kontaktEmail")}",
                //emailDoWysylkiDokumentu = "${processInstance.getData("emailDoWysylkiDokumentu")}",
                //sendingEmailsErrorMsg = "${message(code: "process.subscriptions.sendingEmails.error")}",
                //sendingEmailsMsg = "${message(code: "process.subscriptions.sendingEmails")}",
                refreshSubscriptionUrl = '${createLink(controller: 'subscription', action: 'refreshSubscription')}';

        function getDocuments(scale) {
            var documents = [];

            <g:each in="${processInstance.documentsForPreview}" status="i" var="document">
            documents.push(new PDocument(${i}, '${document?.clientName}', scale, base64DecToArr('${document?.content?.getPreviewContent().encodeBase64().toString()}').buffer));
            </g:each>

            return documents
        }

        %{--function setSubscriptions() {--}%
        %{--    <g:each in="${processInstance.subscriptions}">--}%
        %{--    if (jQuery("#subscribe-REPRESENTATIVE1").attr("data-type") == "${it.personRole}") {--}%
        %{--        jQuery("#subscribe-REPRESENTATIVE1").parent().addClass("disabled");--}%
        %{--        jQuery("#sgnRep1").removeClass('action').addClass('action_visited');--}%
        %{--        updateSubscriptionStatusCount++;--}%
        %{--        jQuery("#clientSignatureBackButton").addClass("disabled");--}%
        %{--        isSubscriptionDone["subscribe-REPRESENTATIVE1"] = true;--}%
        %{--    }--}%

        %{--    if (jQuery("#subscribe-REPRESENTATIVE2").attr("data-type") == "${it.personRole}") {--}%
        %{--        jQuery("#subscribe-REPRESENTATIVE2").parent().addClass("disabled");--}%
        %{--        jQuery("#sgnRep2").removeClass('action').addClass('action_visited');--}%
        %{--        updateSubscriptionStatusCount++;--}%
        %{--        jQuery("#clientSignatureBackButton").addClass("disabled");--}%
        %{--        isSubscriptionDone["subscribe-REPRESENTATIVE2"] = true;--}%
        %{--    }--}%

        %{--    if (jQuery("#subscribe-REPRESENTATIVE3").attr("data-type") == "${it.personRole}") {--}%
        %{--        jQuery("#subscribe-REPRESENTATIVE3").parent().addClass("disabled");--}%
        %{--        jQuery("#sgnRep3").removeClass('action').addClass('action_visited');--}%
        %{--        updateSubscriptionStatusCount++;--}%
        %{--        jQuery("#clientSignatureBackButton").addClass("disabled");--}%
        %{--        isSubscriptionDone["subscribe-REPRESENTATIVE3"] = true;--}%
        %{--    }--}%

        %{--    if (jQuery("#subscribe-REPRESENTATIVE4").attr("data-type") == "${it.personRole}") {--}%
        %{--        jQuery("#subscribe-REPRESENTATIVE4").parent().addClass("disabled");--}%
        %{--        jQuery("#sgnRep4").removeClass('action').addClass('action_visited');--}%
        %{--        updateSubscriptionStatusCount++;--}%
        %{--        jQuery("#clientSignatureBackButton").addClass("disabled");--}%
        %{--        isSubscriptionDone["subscribe-REPRESENTATIVE4"] = true;--}%
        %{--    }--}%

        %{--    if (jQuery("#subscribe-PH").attr("data-type") == "${it.personRole}") {--}%
        %{--        jQuery("#subscribe-PH").parent().addClass("disabled");--}%
        %{--        jQuery("#sgnPh").removeClass('action').addClass('action_visited');--}%
        %{--        updateSubscriptionStatusCount++;--}%
        %{--        jQuery("#clientSignatureBackButton").addClass("disabled");--}%
        %{--        isSubscriptionDone["subscribe-PH"] = true;--}%
        %{--    }--}%
        %{--    </g:each>--}%
        %{--}--}%


    </script>
</head>

<body>

<div id="savingSubscriptionPopup" style="display: none">Trwa zapisywanie podpisu...</div>

<div id="confirm-noaccept-dialog" style="display: none;">
    <p><g:message code="process.subscriptions.noaccept.confirm"/></p>
</div>

<div id="confirm-submit-without-subscription-dialog" style="display: none;">
    <p><g:message code="process.subscriptions.submit.without.subscription.confirm"/></p>
</div>

<div id="confirm-pleasewait" class="align-center" style="display: none;">
    <h2 style="padding-top: 20px;"><g:message code="process.subscriptions.sendingEmails"/></h2>
    <asset:image src="document-loading.gif" style="width: 40px;"/>
</div>
<section id="create_clientSignature">

    <h1 class="ng linia-bottom"><g:message code="clientSignature.header.title"/></h1>
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
        <g:each in="${processInstance.documentsForPreview}" status="i" var="document">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td class="tableCellLeft" style="vertical-align: middle"><a id="previewPdfButton"
                                                                            data-document-index="${i}"
                                                                            href="#">${document.clientName}</td>
                <td class="tableCell" style="vertical-align: middle"><g:formatDate date="${document.lastUpdated}"
                                                                                   format="yyyy-MM-dd HH:mm"/></td>
                <td class="tableCell" style="vertical-align: middle">
                    <g:link class="button action" style="margin: 0 auto" action="downloadDoc"
                            params="[id: document.id]"><g:message code="download.label"/></g:link>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div id="pdfBox"
         style="background-color: #F2F2F2; height: 680px; overflow: auto;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <div id="pdfBox-nav" style="padding: 1em; border-bottom: solid 1px;">
            <div style="display: inline-block; float: left">
                <a id="zoomOutPdfPage" class="button submit">-</a>
                <a id="zoomInPdfPage" class="button submit">+</a>
            </div>

            <div style="display: inline-block; float: right">
                <a id="nextPdfPage" class="button submit disabled" style="float: right"><g:message
                        code="process.subscriptions.nextPage"/></a>
                <a id="prevPdfPage" class="button submit disabled"><g:message
                        code="process.subscriptions.previousPage"/></a>
            </div>

            <div style="text-align: center; padding-left: 165px; padding-top: 5px">
                <span style="font-weight: bold"><g:message code="process.subscriptions.page"/>: <span
                        id="page_num">-</span> / <span id="page_count">-</span></span>
            </div>

            <div style="clear: both"></div>
        </div>

        <div id="pdfBox-content" style="margin: 1em;">
            <div id="pdfBox-content-loading" style="text-align: center; width: 200px; display: none; margin: 0 auto;">
                <h2 style="padding-top: 100px;"><g:message code="process.subscriptions.loadingPage"/></h2>
                <asset:image src="document-loading.gif" style="width: 40px;"/>
            </div>
            <canvas id="pdfPage"
                    style="border:1px solid gray; display: none; width: 440px; height: 570px; display: none; margin-left: auto; margin-right: auto; vertical-align: middle; text-align: center;"></canvas>
            <!-- <img id="pdfPage" style="border:1px solid gray; display: none; width: 440px; height: 570px; display: none; margin-left: auto; margin-right: auto; vertical-align: middle; text-align: center;"/> -->
        </div>
    </div>
    <nav>
        <g:form>
            <fieldset id="documentsSigningPanel" class="subpanel-fieldset">
                <legend><g:message code="clientSignature.signing.people"/></legend>
                <ul class="table-list" style="width: 99%;">
                    <g:if test="${representative1 && representative1.hasSignedContract}">
                        <li id="representative1SignatureContainer" data-personRole="ACCEPTANT1">
                            <span class="signatory">
                                ${representative1.name} ${representative1.surname} - Reprezentant
                            </span>
                            <span>
                                <p>${representative1.mobilePhone}</p>
                            </span>
                            <span>
                                <input placeholder="Kod SMS" type='text' style="width: 70px;" name='representative1SigningCode' id='representative1SigningCode'/>
                            </span>
                            <span>
                                <button id="representative1SignDocuments" class="button action"><g:message code="subscription.sign"/></button>
                            </span>
                            <span>
                                <button id="representative1RefreshSigningCode" class="button action"><i class="icon-refresh"></i></button>
                            </span>
                        </li>
                    </g:if>

                    <g:if test="${representative2 && representative2.hasSignedContract}">
                        <li id="representative2SignatureContainer" data-personRole="ACCEPTANT2">
                            <span class="signatory">
                                ${representative2.name} ${representative2.surname} - Reprezentant
                            </span>
                            <span>
                                <p>${representative2.mobilePhone}</p>
                            </span>
                            <span>
                                <input placeholder="Kod SMS" type='text' style="width: 70px;" name='representative2SigningCode' id='representative2SigningCode'/>
                            </span>
                            <span>
                                <button id="representative2SignDocuments" class="button action"><g:message code="subscription.sign"/></button>
                            </span>
                            <span>
                                <button id="representative2RefreshSigningCode" class="button action"><i class="icon-refresh"></i></button>
                            </span>
                        </li>
                    </g:if>


                    <g:if test="${representative3 && representative3.hasSignedContract}">
                        <li id="representative3SignatureContainer" data-personRole="ACCEPTANT3">
                            <span class="signatory">
                                ${representative3.name} ${representative3.surname} - Reprezentant
                            </span>
                            <span>
                                <p>${representative3.mobilePhone}</p>
                            </span>
                            <span>
                                <input placeholder="Kod SMS" type='text' style="width: 70px;" name='representative3SigningCode' id='representative3SigningCode'/>
                            </span>
                            <span>
                                <button id="representative3SignDocuments" class="button action"><g:message code="subscription.sign"/></button>
                            </span>
                            <span>
                                <button id="representative3RefreshSigningCode" class="button action"><i class="icon-refresh"></i></button>
                            </span>
                        </li>
                    </g:if>


                    <g:if test="${representative4 && representative4.hasSignedContract}">
                        <li id="representative4SignatureContainer" data-personRole="ACCEPTANT4">
                            <span class="signatory">
                                ${representative4.name} ${representative4.surname} - Reprezentant
                            </span>
                            <span>
                                <p>${representative4.mobilePhone}</p>
                            </span>
                            <span>
                                <input placeholder="Kod SMS" type='text' style="width: 70px;" name='representative4SigningCode' id='representative4SigningCode'/>
                            </span>
                            <span>
                                <button id="representative4SignDocuments" class="button action"><g:message code="subscription.sign"/></button>
                            </span>
                            <span>
                                <button id="representative4RefreshSigningCode" class="button action"><i class="icon-refresh"></i></button>
                            </span>
                        </li>
                    </g:if>

                    <li id="phSignatureContainer" data-personRole="PH">
                        <span class="signatory">
                            ${processInstance.phFirstName} ${processInstance.phSurname} - Pracownik eService
                        </span>
                        <span>
                            <p>${processInstance.phMobilePhone}</p>
                        </span>
                        <span>
                            <input placeholder="Kod SMS" type='text' style="width: 70px;" name='phSigningCode' id='phSigningCode'/>
                        </span>
                        <span>
                            <button id="phSignDocuments" class="button action"><g:message code="subscription.sign"/></button>
                        </span>
                        <span>
                            <button id="phRefreshSigningCode" class="button action"><i class="icon-refresh"></i></button>
                        </span>
                    </li>
                </ul>
            </fieldset>
            <fieldset class="subpanel-fieldset" id="clientSignatureDocType">
                <legend><g:message code="clientSignature.version.title"/></legend>
                <ul class="table-list">
                    <li>
                        <span>
                            <label>
                                <g:radio id="requestVersionElectronical" name="requestVersion" value="electronical"/>
                                <g:message code="clientSignature.electronicalVersion.radio"/>
                            </label>
                        </span>
                    </li>
                    <li>
                        <span>
                            <label>
                                <g:radio id="requestVersionPaper" name="requestVersion" value="paper"/>
                                <g:message code="clientSignature.paperVersion.radio"/>
                            </label>
                        </span>
                    </li>
                    <li>
                        <span>
                            <label>
                                <g:radio id="requestVersionTemplates" name="requestVersion" value="templates"/>
                                <g:message code="clientSignature.templatesVersion.radio"/>
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
                            <g:link style="width: 100%" event="back"
                                    class="button submit link-button ${isUzupelnijPodpisy ? "disabled" : ""}">${message(code: 'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                        </td>
                        <td style="text-align: right;">
                            <g:submitButton id="noaccept" name="noaccept" class="button submit display-inline"
                                            style="width: 90%"
                                            value="${message(code: 'clientSignature.noAcceptance.button', default: 'Brak akceptacji')}"/>
                        </td>
                        <td>
                            <g:submitButton style="width: 100%" id="continueButton" name="submit" class="button submit"
                                            value="${message(code: 'default.navigation.button.finish', default: 'Zakończ')}"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </g:form>
    </nav>

</section>
<script>
    function isEmpty(s) {
        return s === undefined || s === null || s === "";
    }

    // GSP variables
    let contactEmail = "${processInstance.getData("kontaktEmail")}";
    let emailForDocuments = "${processInstance.getData("emailDoWysylkiDokumentu")}";

    let createProcessRejectLink = '<g:createLink controller="activity" action="createProcess" params="[message: rejectedDocumentsMessage]"/>';
    let createProcessPrevActivityMessage = '<g:createLink controller="activity" action="createProcess" params="[message: prevActivityMessage]"/>';

    let sendingEmailsErrorMsg = "${message(code: "process.subscriptions.sendingEmails.error")}";
    let sendingEmailsMsg = "${message(code: "process.subscriptions.sendingEmails")}";

    let signatures = [];
    <g:each in="${processInstance.subscriptions}">
    signatures.push({
        personRole: "${it.personRole}",
    });
    </g:each>

    let requiredSignatures = [{
        personRole: "PH",
    }];
    <g:if test="${representative1 && representative1.hasSignedContract}">
    requiredSignatures.push({
        personRole: "ACCEPTANT1",
    });
    </g:if>
    <g:if test="${representative2 && representative2.hasSignedContract}">
    requiredSignatures.push({
        personRole: "ACCEPTANT2",
    });
    </g:if>
    <g:if test="${representative3 && representative3.hasSignedContract}">
    requiredSignatures.push({
        personRole: "ACCEPTANT3",
    });
    </g:if>
    <g:if test="${representative4 && representative4.hasSignedContract}">
    requiredSignatures.push({
        personRole: "ACCEPTANT4",
    });
    </g:if>

    // Panels
    let panels = [
        EUMOWY.Panels.DocumentsSigningPanel('#documentsSigningPanel', {
            processId: ${processInstance.id},
            signatures: signatures,
            signUrl: '${createLink(controller: "documentsSigningRest", action: "signDocuments", absolute: false)}',
            refreshSigningCodeUrl: '${createLink(controller: "documentsSigningRest", action: "refreshSigningCode", absolute: false)}',
        }),
        EUMOWY.Panels.DocumentsFormatPanel('#clientSignatureDocType', {
            contactEmail: contactEmail,
            emailForDocuments: emailForDocuments,
        }),
        EUMOWY.Panels.DocumentsControlPanel('#signatureNavTable', {
            documentsFormat: !isEmpty(contactEmail) || !isEmpty(emailForDocuments) ? "ELECTRONIC" : "PAPER",
            requiredSignatures: requiredSignatures,
            signatures: signatures,
            prevUrl: createProcessPrevActivityMessage,
            rejectUrl: createProcessRejectLink,
            inProgressMsg: sendingEmailsMsg,
            errorMsg: sendingEmailsErrorMsg,
        }),
    ];
    // Mount panels once all of them are initialized.
    // This ensures that all panels have setup their event handlers
    panels.forEach(panel => panel.mount());

    // Dialog functions using jQuery that won't work inside modules
    // because jQuery UI requires global state
    const $ = jQuery
    function showNoAcceptDialog(yesCallback) {
        $("#confirm-noaccept-dialog").dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true,
            buttons: {
                "Tak": function () {
                    $(this).dialog("close");
                    const url = $(window.location).attr('href');
                    yesCallback(url);
                },
                "Nie": function () {
                    $(this).dialog("close");
                }
            }
        });
    }

    function showSubmitWithoutSigningDialog(callback) {
        $("#confirm-submit-without-subscription-dialog").dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true,
            buttons: {
                "Tak": function () {
                    $(this).dialog("close");
                    self.showLoadingDialog(sendingEmailsMsg);

                    callback();

                    result = true;
                },
                "Nie": function () {
                    $(this).dialog("close");
                }
            }
        });
    }

    function showLoadingDialog(msg) {
        $('#confirm-pleasewait h2').text(msg);
        $('#confirm-pleasewait img').show();
        $('#confirm-pleasewait').dialog({
            resizable: true,
            height: 200,
            width: 450,
            modal: true
        });
    }

    function showErrorDialog(msg) {
        $("#confirm-pleasewait h2").text(msg);
        $("#confirm-pleasewait img").hide();
    }

    function closeDialog() {
        $('#confirm-pleasewait').dialog("close");
    }

    window.showNoAcceptDialog = showNoAcceptDialog;
    window.showSubmitWithoutSigningDialog = showSubmitWithoutSigningDialog;
    window.showLoadingDialog = showLoadingDialog;
    window.showLoadingDialog = showLoadingDialog;
    window.closeDialog = closeDialog;
</script>
</body>
</html>
