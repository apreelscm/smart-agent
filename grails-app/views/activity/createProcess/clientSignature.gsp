<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
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
	<r:require module="jquery_ui" />
	<r:script>
		jQuery(document).ready(function(){
			
			<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}">
			jQuery("#subscribe-${it.name()}").on('click', function(e) {
				e.preventDefault();
				
				var dialog = jQuery('#subscriptionDialog');
				if (jQuery('#subscriptionDialog').length == 0) {
					dialog = jQuery('<div id="subscriptionDialog" style="display:hidden"></div>').appendTo('body');
				}
				
				dialog.load(
		            "/eumowy/subscription/inlineview",
		            {},
		            function(responseText, textStatus, XMLHttpRequest) {
		                dialog.dialog({
		                	modal: true,
      						width: 750
		                });
		            }
		        );
		        
				return false;
			});
			</g:each>
			
			jQuery("#requestVersionTemplates").on("change", function(e) {
				if (e.checked) {
					jQuery("#noaccept").disable();
					
					<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}">
					jQuery("#subscribe-${it.name()}").disable();
					</g:each>
				}
			});
		});
	</r:script>
</head>

<body>

<section id="create_clientSignature" >

    <h1 class="ng linia-bottom"><g:message code="clientSignature.header.title" default="Podpis Klienta"/></h1>

    <div id="pdfBox" style="height: 680px; overflow: hidden;border: solid 1px; border-radius: 5px;  margin: 20px 15px 0">
        <g:render template="../forms/pdf/embedDocument-mobile"
                  model="[pdfDocument: resource(dir:'files', file:'pedef.pdf')]"/>
    </div>

    <nav>
        <g:form>
        	<fieldset id="clientSignaturePersons" class="subpanel-fieldset">
        		<legend><g:message code="clientSignature.signing.people" default="Osoby podpisujące" /></legend>
        		<ul class="table-list">
        			<g:each in="${com.eservice.eumowy.Activity$ClientType?.values()}" >
						<li><span><a class="big-link" href="#" id="subscribe-${it.name()}">${it}</a></span></li>
					</g:each>
					<!-- <li><span><a class="big-link" id="subscribe-OTHER" href="#">Andrzej Kmicic - Reprezentant</a></span></li>
					<li><span><a class="big-link" href="#">Stanisław Wokólski - Reprezentant</a></span></li>
					<li><span><a class="big-link" href="#">Jacek Soplica - Pracownik eService</a></span></li> -->
        		</ul>
        	</fieldset>
        	<fieldset class="subpanel-fieldset" id="clientSignatureDocType">
        		<legend><g:message code="clientSignature.version.title" default="Forma" /></legend>
        		<ul class="table-list">
        			<li>
        				<span>
        					<label>
        						<g:radio name="requestVersion" value="electronical" checked="on"/>
        						<g:message code="clientSignature.electronicalVersion.radio" default="Elektroniczna" />
                            </label>
                        </span>
                    </li>
                    <li>
                    	<span>
                    		<label>
                                <g:radio name="requestVersion" value="paper"/>
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
                            <g:link style="width: 100%" event="back" class="button submit">${message(code:'default.navigation.button.prev', default: 'Wstecz')}</g:link>
                        </td>
                        <td style="text-align: right;">
                            <g:submitButton name="noaccept" class="button submit display-inline" style="width: 90%"
                                            value="${message(code: 'clientSignature.noAcceptance.button', default:'Brak akceptacji')}"/>
                        </td>
                        <td>
                            <g:submitButton style="width: 100%" id="conitnueButton" name="submit" class="button submit" value="${message(code: 'default.navigation.button.finish', default: 'Zakończ')}"/>
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