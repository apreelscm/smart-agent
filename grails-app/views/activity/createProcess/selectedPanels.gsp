<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title" default="Lista paneli"/></title>
    <r:require module="filestyle"/>
    <r:require module="jquery_ui" />
    <r:require module="selectedPanels"/>

    <g:javascript>

    var $j = jQuery.noConflict();

    $j(function(){
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

                $j('html, body').animate({
                    scrollTop: $j(validator.errorList[0].element).offset().top
                }, 100);
            }
        });
        console.log('JS Validation started...');

	  showSaveLink();
      $j("#saveProcessLink").click(function() {
            var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val(""),
                form = $j('.panelsForm');
            form.data("validator").cancelSubmit = true;
            form.append($j(input));

            showLoadingDialog()

            setTimeout(function() {
                form.submit()
            }, 1);
            return false
        });

        $j("#acceptPointsButton").click(function() {
            var submitButtons = $j("input[type='submit']"),
                continueButton = $j("#continueButton"),
                form = $j(".panelsForm");

            if ( form.valid()){
                var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val("");
                form.append($j(input))

                submitButtons.attr('disabled', 'disabled');
                continueButton.attr('disabled', 'disabled');
                showLoadingDialog()

                setTimeout(function() {
                    form.submit()
                }, 1);
            }
            return false;
        });

        $j("#continueButton").click(function() {
                var submitButtons = $j("input[type='submit']"),
                    continueButton = $j("#continueButton"),
                    form = $j(".panelsForm");

                if ( form.valid()){
                    var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_continue").val("");
                    submitButtons.attr('disabled', 'disabled');
                    continueButton.attr('disabled', 'disabled');

                    form.append($j(input))
                    showLoadingDialog()

                    setTimeout(function() {
                        form.submit()
                    }, 1);
                }

                return false;
            });

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
    </g:javascript>
    <r:require module="validation"/>
    <r:require module="newpoint_panel_setup"/>

    <style>
    #uploads {
        margin: 6px 0;
        padding: 0 0 9px;
        position: relative;
        width: 310px;
    }
    #uploads div.fakeupload {
        background: #FF0000 no-repeat scroll 100% 50% transparent;
        cursor: pointer;
    }
    #uploads div.fakeupload input {
        width: 219px;
        height: 29px;
    }
    #uploads input.realupload {
        opacity: 0;
        position: absolute;
        right: 0;
        top: 0;
        width: 310px;
        z-index: 2;
    }
    </style>
</head>
<body>

<r:require module="mask"/>

<section id="create-activity">

    <g:if env="development">
        <button id="mockBtn">MOCK</button>
    </g:if>

    <g:if env="production">
        %{--empty--}%
    </g:if>
    <g:else>
        <a href="/eumowy/calculator?nip=${data.nip}" target="_blank">Zobacz kalkulator</a>
    </g:else>
    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title" default="Lista paneli"/></h1>

    <g:hasErrors bean="${data}">
        <ul class="errors" role="alert">
            <g:eachError bean="${data}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

<!-- Working with process with id: ${processInstance?.id} , and status ${processInstance?.status?.toString()}-->


    <g:form class="panelsForm">
        <g:hiddenField name="globalMCC" value="${data.scoringMcc}"/>
        <g:hiddenField name="nip" value="${data.nip}"/>
        <g:hiddenField name="liczbaTerminali" value="${data.liczbaTerminali}"/>
        <g:each var="panel" in="${processInstance.panels}" status="i">
            <g:if test="${panel != null}">
                <g:if test="${panel.name.equals('danePunktu') == false && panel.name.equals('danePos') == false}">
                    <g:render template="/panels/${panel.name}"/>
                </g:if>
                <g:if test="${panel.name.equals('danePunktu')}">
                    <g:each var="point" in="${data.points}" status="j">
                        <g:render template="/panels/danePunktu" model="[panelType: 'points', id: j, pointData: point]" />
                    </g:each>
                </g:if>
                <g:if test="${panel.name.equals('danePos')}">
                    <g:each var="pos" in="${data.poses}" status="j">
                        <g:render template="/panels/danePos" model="[panelType: 'poses', id: j, pointData: pos]" />
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
            <p><g:message code="loading" default="Trwa ładowanie danych..."/></p>
        </div>
    </nav>

</section>

</body>
</html>