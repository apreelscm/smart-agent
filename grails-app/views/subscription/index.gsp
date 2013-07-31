<!DOCTYPE html>
<head>
  <meta name="layout" content="main">
  <title><g:message code="subscription.title" /></title>
  <r:require module="signaturepad" />
</head>
<body>
  <r:script>
    jQuery(document).ready(function() {
      jQuery('.sigPad').signaturePad({errorMessageDraw: '<g:message code="subscription.error" />'});
    });
  </r:script>
	<div id="index-subscription" class="content">
		<g:form  action="save" class="sigPad">
			<h3>REPREZENTANT</h3>
			<p>
				<g:message code="subscription.agreement" />
			</p>
			<div class="sig sigWrapper">
				<div class="typed"></div>
				<canvas id="pad" class="pad" width="600" height="250"></canvas>
				<input type="hidden" name="subscription" class="output">
			</div>


            <g:link  action="#clear"  >Usuń1</g:link>
            <g:actionSubmit class="button action" action="#clear" value="Wyczyść"/>
	%{--		<a href="#clear" class="button action clearButton">
				<g:message code="subscription.clear" />
			</a>--}%

            <g:submitButton name="Złożono podpis" class="button submit"/>

        </g:form>
	</div>
</body>
