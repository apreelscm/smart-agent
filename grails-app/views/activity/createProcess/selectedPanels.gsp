<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title"/></title>

    <asset:javascript src="apreel/createProcess/selectedPanels.js"/>

    <script type="text/javascript">
    jQuery(function(){
        console.log('JS Validation starting...');
        jQuery(".panelsForm").validate({
            errorElement: 'img',
            errorPlacement: function(error, element) {
            },
            success: function(label) {
            },
            focusInvalid: false,
            invalidHandler: function(form, validator) {
                if (!validator.numberOfInvalids())
                    return;

                jQuery('html, body').animate({
                    scrollTop: jQuery(validator.errorList[0].element).offset().top
                }, 100);
            }
        });
        console.log('JS Validation started...');

        jQuery("#hackishSubmit").click(function(event) {
            //eUmowy_ext-364
            console.log('click');
            return false;
        });

	  showSaveLink();
      jQuery("#saveProcessLink").click(function() {
            var input = jQuery("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val(""),
                form = jQuery('.panelsForm');

            form.data("validator").cancelSubmit = true;
            submitForm(form,input, true)

            return false
        });

        jQuery("#acceptPointsButton").click(function() {
              var input = jQuery("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val(""),
                form = jQuery('.panelsForm');

            if ( form.valid()){
                submitForm(form,input,true)
            }
            return false;
        });


        jQuery("#continueButton").click(function() {
            var input = jQuery("<input>").attr("type", "hidden").attr("name", "_eventId_continue").val("");
            var form = jQuery(".panelsForm");

            if ( form.valid()){

              var kontaktEmail = jQuery("#kontaktEmail").val()
              var emailDoWysylkiDokumentu = jQuery("#emailDoWysylkiDokumentu").val()

               if(emailDoWysylkiDokumentu || kontaktEmail){
                    submitForm(form,input, true)
               }else{
                    //sprawdzanie maila akceptanta
                     jQuery("#confirm-submit-without-emailKontakt-dialog").dialog({
                        resizable: true,
                        height:200,
                        width: 450,
                        modal: true,
                        buttons:
                            {
                                "Dalej": function() {
                                    jQuery( this ).dialog( "close" );
                                    submitForm(form,input, true)

                                },
                                "Popraw": function() {
                                    jQuery( this ).dialog( "close" );
                                }
                            }
                    });
               }
            }

            return false;
        });

        function submitForm(form, input, disableButtons){
           var submitButtons = jQuery("input[type='submit']")
           var continueButton = jQuery("#continueButton")

                form.append(jQuery(input))

                if(disableButtons){
                  submitButtons.attr('disabled', 'disabled');
                  continueButton.attr('disabled', 'disabled');
                }

                showLoadingDialog()

                setTimeout(function() {
                    form.submit()
                }, 1);
        }


        refreshAttachmentList()

        jQuery("#fileUploadInput").change(function (){
                 jQuery('#spinner2').removeClass("display-none");
                 jQuery('#uploadForm').submit();
             });

         jQuery("#hidden-upload-frame").load(function(){

         jQuery('#spinner2').addClass("display-none");

         var content = this.contentDocument.body.innerHTML

         //resetting file input
         jQuery('#uploadForm').each(function(){
             this.reset();
         });


         jQuery('#statusBox').html(content);

         var isError = jQuery('#statusBox ul').hasClass("errors")
         if(!isError){
              refreshAttachmentList()
         }
    });

        function refreshAttachmentList(){
        ${remoteFunction(
            action:'getAttachmentList',
            update:'attachmentsBox',
            params: [processId: processInstance.id])}
        }
    });

         jQuery("#mockBtn").click(function() {
         var possibleLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         var possibleNumbers = "123456789";

         jQuery( "#addNewPointButton" ).trigger( "click" );
              jQuery(":required").each(function(){
                var type = jQuery(this).attr("type")
                var name = jQuery(this).attr("name")

                if( jQuery(this).attr("value")){return}

                if( name == "akceptantRegon"){
                   jQuery(this).attr("value",randomString(possibleNumbers,9))
                }
                else if( name.indexOf("KodPocztowy") != -1 ){
                   jQuery(this).attr("value",randomString(possibleNumbers,5))
                }
                 else if(name.indexOf("kodMCC") != -1){
                   jQuery(this).attr("value",randomString(possibleNumbers,4))
                }
                  else if(name.indexOf("TelKomorkowy") != -1){
                   jQuery(this).attr("value",randomString(possibleNumbers,9))
                }
               else if( name == "akceptantNrDomu" || name ==  "akceptantKontaktNrDomu" || name.indexOf("korespondencjaNrDomu") != -1){
                   jQuery(this).attr("value",randomString(possibleNumbers,1))
                }
                else if(type == "text"){
                 jQuery(this).attr("value",randomString(possibleLetters,Math.floor((Math.random()+0.1)*10)))
                }
                else if(type == "number"){
                  jQuery(this).attr("value",randomString(possibleNumbers,2))
                }
                console.info("2:"+jQuery(this).attr("type"))
              })
        });

        function randomString(chars,length){
           var result = ""
             for(var i = 0; i < length; i++) {
                var randomChar = chars.charAt(Math.floor(Math.random() * chars.length));
                result += randomChar
            }
            return result
        }

        jQuery(document).on("click", "input.doladowanie", refreshTelepomkaAndTelekodzikPercentValues);

        refreshTelepomkaAndTelekodzikPercentValues()
        jQuery.datepicker.setDefaults( jQuery.datepicker.regional[ "pl" ] );
    </script>
    <asset:javascript src="apreel/panels/common.js"/>
</head>
<body>

<div id="confirm-submit-without-emailKontakt-dialog"  style="display: none;">
    <p><g:message code="process.subscriptions.submit.without.emailKontakt.confirm" /></p>
</div>

<section id="create-activity">

    <g:if env="development">
        <button id="mockBtn">MOCK</button>
    </g:if>

    <g:if env="production">
    %{--empty--}%
    </g:if>
    <g:else>
        <a href="/eumowy/calculator?nip=${data.nip}" target="_blank"><g:message code='calculator.overview.label'/></a>
    </g:else>
    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title"/></h1>

    <g:hasErrors bean="${data}">
        <ul class="errors" role="alert">
            <g:eachError bean="${data}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

<!-- Working with process with id: ${processInstance?.id} , and status ${processInstance?.status?.toString()}-->

    <div id="noCitiesFound" style="display: none;">
        <p><g:message code="noCityFound"/></p>
    </div>

    <g:form class="panelsForm">
        %{--eUmowy_ext-364--}%
        <g:submitButton id="hackishSubmit" name="hackishSubmit" style="visibility: hidden"/>

        <g:hiddenField name="globalMCC" value="${data.scoringMcc}"/>
        <g:hiddenField name="nip" value="${data.nip}"/>
        <g:hiddenField name="liczbaTerminali" value="${data.liczbaTerminali}"/>
        <g:hiddenField name="liczbaPosZCbd" value="${data.liczbaPosZCbd}"/>
        <g:hiddenField id="progrnozaMiesieczna" name="progrnozaMiesieczna" value="${data.progrnozaMiesieczna}"/>
        <g:hiddenField id="liczbaPtkCbd" name="liczbaPtkCbd" value="${data.liczbaPtkCbd}"/>
        <g:hiddenField id="hasPrepaid" name="hasPrepaid" value="${data.hasPrepaid}"/>
        <g:hiddenField id="hasNewUmowaAndPrepaid" name="hasNewUmowaAndPrepaid" value="${data.hasNewUmowaAndPrepaid}"/>
        <g:hiddenField name="isRozszerzenie" value="${data.isRozszerzenie}"/>
        <g:hiddenField id="hasDodaniePrepaid" name="hasDodaniePrepaid" value="${data.hasDodaniePrepaid}"/>
        <g:hiddenField name="czyGift" value="${data.czyGift}"/>

        <g:each var="panel" in="${processInstance.panels.sort(false){p -> p?.orderNo}}" status="i">

            <g:if test="${panel != null}">
                <g:if test="${panel.name.equals('danePunktu') == false && panel.name.equals('danePos') == false}">
                    <g:render template="/panels/${panel.name}"/>
                </g:if>
                <g:if test="${panel.name.equals('danePunktu')}">
                    <g:each var="point" in="${data.points.sort(false){p -> p?.id }}" status="j">
                    	<g:if test="${point != null}" >
                       		<g:render template="/panels/danePunktu" model="[panelType: 'points', id: j, pointData: point]" />
                    	</g:if>
                    </g:each>
                </g:if>
                <g:if test="${panel.name.equals('danePos')}">
                    <g:each var="pos" in="${data.poses.sort(false){p -> p?.id }}" status="j">
                        <g:if test="${pos != null && pos.isChildCopy() == false}" >
                        	<g:render template="/panels/danePos" model="[panelType: 'poses', id: j, pointData: pos]" />
                        </g:if>
                    </g:each>
                </g:if>
            </g:if>
        </g:each>
        <g:render template="/panels/uwagi"/>
    </g:form>

    <g:render template="/panels/zalaczniki"/>

    <nav style="margin-top: 20px">
        <fieldset>
            <g:link event="reject" class="button submit float-left">${message(code:'default.navigation.button.reject', default: 'Odrzuć')}</g:link>
            <button id="continueButton" class="button submit float-right">${message(code:'default.navigation.button.next', default: 'Dalej')}</button>
        </fieldset>
        <div id="loadingDialog" style="display: none;">
            <p><g:message code="loading"/></p>
        </div>
    </nav>

</section>

</body>
</html>

