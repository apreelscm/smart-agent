<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="selectedPanels.header.title" default="Lista paneli"/></title>
    <r:require module="filestyle"/>

    <g:javascript>

        var $j = jQuery.noConflict();

        $j(function(){ //this is regular jQuery code. It waits for the dom to load fully the first time you open the page.


            <g:remoteFunction action="getAttachmentList" update="attachmentsBox"/>

        $j("#fileUploadInput").change(function (){
                 $j('#spinner2').removeClass("display-none");
                 $j('#uploadForm').submit();
             });

         $j("#hidden-upload-frame").load(function(){

          $j('#spinner2').addClass("display-none");
          fileUploadInput

            var content = this.contentDocument.body.innerHTML

             //resetting file input
             $j('#uploadForm').each(function(){
                 this.reset();
             });


             $j('#statusBox').html(content);

             var isError = $j('#statusBox ul').hasClass("errors")
             if(!isError){
        <g:remoteFunction action="getAttachmentList" update="attachmentsBox"/>
        }

    });
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

<section id="create-activity">

    <h1 class="ng linia-bottom"><g:message code="selectedPanels.header.title" default="Lista paneli"/></h1>

    <g:each var="panel" in="${processInstance.panels}" status="i">
        <g:render template="/panels/${panel.name}"/>
    </g:each>

    <g:render template="/panels/uwagi"/>

    <g:render template="/panels/zalaczniki"/>

    <g:form>
        <nav style="margin-top: 20px">
            <fieldset>
                <g:link event="back" class="button submit float-left">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                <g:submitButton id="conitnueButton" name="continue" class="button submit float-right"
                                value="${message(code:'default.navigation.button.next', default: 'Dalej')}"/>
            </fieldset>
        </nav>
    </g:form>

</section>

</body>
</html>