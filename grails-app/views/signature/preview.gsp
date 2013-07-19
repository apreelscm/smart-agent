<!DOCTYPE html>
<head>
  <title>Preview</title>
  <meta name="layout" content="main">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.signaturepad.css')}">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
  <script src="${resource(dir: 'js', file: 'jquery.signaturepad.min.js')}"></script>
  <script src="${resource(dir: 'js', file: 'json2.min.js')}"></script>
  <script src="${resource(dir: 'js', file: 'flashcanvas.js')}"></script>
</head>
<body>
	<p>js</p>
	<div class="sigPad signed">
		<div class="sigWrapper">
			<canvas class="pad" width="400" height="200"></canvas>
		</div>
	</div>
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
