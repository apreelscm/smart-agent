<!DOCTYPE html>
<head>
  <title>Preview</title>
  <meta name="layout" content="main">
</head>
<body>
	<p>png 200x100</p>
	<img src="${resource(dir: 'images', file: 'sign.png')}" width="200" height="100"><br/>
	<p>png oryginalny</p>
	<img src="${resource(dir: 'images', file: 'sign.png')}">
	<script>
		var sig = ${signature.signature};

		$(document).ready(function() {
			$('.sigPad').signaturePad({
				displayOnly : true
			}).regenerate(sig);
		});
	</script>
</body>
