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
			<input type="text" name="nip">
			<p>
				<g:message code="signature.agreement" />
			</p>
			<div class="sig sigWrapper">
				<div class="typed"></div>
				<canvas class="pad" width="400" height="200"></canvas>
				<input type="hidden" name="signature" class="output">
			</div>
			<button type="submit" class="przycisk-submit">
				<g:message code="signature.save" />
			</button>
			<a href="#clear" class="przycisk-submit clearButton">
				<g:message code="signature.clear" />
			</a>
		</form>
	</div>
</body>
