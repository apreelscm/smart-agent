<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title" default="Lista paneli"/></title>
    <r:require module="filestyle"/>
    <r:require module="newpoint_panel_setup"/>
    <r:require module="validation"/>
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
                var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_saveOnly").val("");
                $j('.panelsForm').append($j(input)).submit()
                false
            });

        $j("#conitnueButton").click(function() {
                if ( $j(".panelsForm").valid()){
                    var input = $j("<input>").attr("type", "hidden").attr("name", "_eventId_continue").val("");
                    $j('.panelsForm').append($j(input)).submit()
                }
                false
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
    </g:javascript>

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
    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title" default="Lista paneli"/></h1>

    <g:hasErrors bean="${data}">
        <ul class="errors" role="alert">
            <g:eachError bean="${data}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <!-- Working with process with id: ${processInstance.id} , and status ${processInstance.status?.toString()}-->


    <g:form class="panelsForm">
        <g:each var="panel" in="${processInstance.panels}" status="i">
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
        </g:each>
        <g:render template="/panels/uwagi"/>
    </g:form>

    <g:render template="/panels/zalaczniki"/>

    <nav style="margin-top: 20px">
        <fieldset>
            <g:link event="back" class="button submit float-left">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
            <button id="conitnueButton" class="button submit float-right">${message(code:'default.navigation.button.next', default: 'Dalej')}</button>
        </fieldset>
    </nav>

</section>

</body>
</html>