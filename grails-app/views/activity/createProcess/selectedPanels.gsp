<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title"/></title>
    <r:require module="filestyle"/>
    <r:require module="jquery_ui" />
    <r:require module="selectedPanels"/>
    <r:require module="validation"/>
    <r:require module="newpoint_panel_setup"/>

    <g:javascript>

    var $j = jQuery.noConflict();

    $j(function(){
        console.log('JS Validation starting...');
        $j(".panelsForm").validate({
            errorElement: 'img',
            errorPlacement: function(error, element) {
            },
            success: function(label) {
            },
            focusInvalid: false,
            invalidHandler: function(form, validator) {
                if (!validator.numberOfInvalids())
                    return;

                $j('html, body').animate({
                    scrollTop: $j(validator.errorList[0].element).offset().top
                }, 100);
            }
        });
        console.log('JS Validation started...');

        $j("#hackishSubmit").click(function(event) {
            //eUmowy_ext-364
            console.log('click');
            return false;
        });

	  showSaveLink();
      $j("#saveProcessLink").click(function() {
            var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val(""),
                form = $j('.panelsForm');

            form.data("validator").cancelSubmit = true;
            submitForm(form,input, true)

            return false
        });

        $j("#acceptPointsButton").click(function() {
              var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val(""),
                form = $j('.panelsForm');

            if ( form.valid()){
                submitForm(form,input,true)
            }
            return false;
        });


        $j("#continueButton").click(function() {
            var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_continue").val("");
            var form = $j(".panelsForm");

            if ( form.valid()){

              var kontaktEmail = $j("#kontaktEmail").val()
              var emailDoWysylkiDokumentu = $j("#emailDoWysylkiDokumentu").val()

               if(emailDoWysylkiDokumentu || kontaktEmail){
                    submitForm(form,input, true)
               }else{
                    //sprawdzanie maila akceptanta
                     $j("#confirm-submit-without-emailKontakt-dialog").dialog({
                        resizable: true,
                        height:200,
                        width: 450,
                        modal: true,
                        buttons:
                            {
                                "Dalej": function() {
                                    $j( this ).dialog( "close" );
                                    submitForm(form,input, true)

                                },
                                "Popraw": function() {
                                    $j( this ).dialog( "close" );
                                }
                            }
                    });
               }
            }

            return false;
        });

        function submitForm(form, input, disableButtons){
           var submitButtons = $j("input[type='submit']")
           var continueButton = $j("#continueButton")

                form.append($j(input))

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

        $j("#fileUploadInput").change(function (){
                 $j('#spinner2').removeClass("display-none");
                 $j('#uploadForm').submit();
             });

         $j("#hidden-upload-frame").load(function(){

         $j('#spinner2').addClass("display-none");

         var content = this.contentDocument.body.innerHTML

         //resetting file input
         $j('#uploadForm').each(function(){
             this.reset();
         });


         $j('#statusBox').html(content);

         var isError = $j('#statusBox ul').hasClass("errors")
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

         $j("#mockBtn").click(function() {
         var possibleLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         var possibleNumbers = "123456789";

         $j( "#addNewPointButton" ).trigger( "click" );
              $j(":required").each(function(){
                var type = $j(this).attr("type")
                var name = $j(this).attr("name")

                if( $j(this).attr("value")){return}

                if( name == "akceptantRegon"){
                   $j(this).attr("value",randomString(possibleNumbers,9))
                }
                else if( name.indexOf("KodPocztowy") != -1 ){
                   $j(this).attr("value",randomString(possibleNumbers,5))
                }
                 else if(name.indexOf("kodMCC") != -1){
                   $j(this).attr("value",randomString(possibleNumbers,4))
                }
                  else if(name.indexOf("TelKomorkowy") != -1){
                   $j(this).attr("value",randomString(possibleNumbers,9))
                }
               else if( name == "akceptantNrDomu" || name ==  "akceptantKontaktNrDomu" || name.indexOf("korespondencjaNrDomu") != -1){
                   $j(this).attr("value",randomString(possibleNumbers,1))
                }
                else if(type == "text"){
                 $j(this).attr("value",randomString(possibleLetters,Math.floor((Math.random()+0.1)*10)))
                }
                else if(type == "number"){
                  $j(this).attr("value",randomString(possibleNumbers,2))
                }
                console.info("2:"+$j(this).attr("type"))
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

        $j("input.doladowanie").live("click", function(){refreshTelepomkaAndTelekodzikPercentValues()});

        refreshTelepomkaAndTelekodzikPercentValues()
        $j.datepicker.setDefaults( jQuery.datepicker.regional[ "pl" ] );
    </g:javascript>
</head>
<body>
<r:require module="mask"/>

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

