<!DOCTYPE html>
<head>
  <meta name="layout" content="main">
  <title><g:message code="signature.title" /></title>
  <r:require module="signaturepad" />
</head>
<body>
  <r:script>
    jQuery(document).ready(function() {
      jQuery('.sigPad').signaturePad({errorMessageDraw: '<g:message code="signature.error" />'});
    });
  </r:script>
	<div id="index-signature" class="content">
		<form method="post" action="save" class="sigPad">
			<h3>REPREZENTANT</h3>
			<p>
				<g:message code="signature.agreement" />
			</p>
			<div class="sig sigWrapper">
				<div class="typed"></div>
				<canvas id="pad" class="pad" width="600" height="250"></canvas>
				<input type="hidden" name="signature" class="output">
			</div>
			<a href="#clear" class="button action clearButton">
				<g:message code="signature.clear" />
			</a>
			<button type="submit" class="button submit">
				<g:message code="signature.save" />
			</button>
		</form>
	</div>
</body>
