<!DOCTYPE html>
<head>
  <meta name="layout" content="main">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'signaturepad.css')}" type="text/css">
  <title>Sign</title>
  <r:require module="signaturepad" />
</head>
<body>
  <r:script>
    jQuery(document).ready(function() {
      jQuery('.sigPad').signaturePad();
    });
  </r:script>
  <form method="post" action="save" class="sigPad">
    <ul class="sigNav">
      <li class="clearButton"><a href="#clear">Wyczyść</a></li>
    </ul>
    <div class="sig sigWrapper">
      <div class="typed"></div>
      <canvas class="pad" width="400" height="200"></canvas>
      <input type="hidden" name="signature" class="output">
    </div>
    <button type="submit">Submit</button>
  </form>
</body>
