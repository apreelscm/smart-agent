<!DOCTYPE html>
<head>
  <title>Sign</title>
  <meta name="layout" content="main">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.signaturepad.css')}">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
  <script src="${resource(dir: 'js', file: 'jquery.signaturepad.min.js')}"></script>
  <script src="${resource(dir: 'js', file: 'json2.min.js')}"></script>
  <script src="${resource(dir: 'js', file: 'flashcanvas.js')}"></script>
</head>
<body>
  <form method="post" action="save" class="sigPad">
    <ul class="sigNav">
      <li class="drawIt"><a href="#draw-it" class="current">Draw It</a></li>
      <li class="clearButton"><a href="#clear">Clear</a></li>
    </ul>
    <div class="sig sigWrapper">
      <div class="typed"></div>
      <canvas class="pad" width="500" height="500"></canvas>
      <input type="hidden" name="signature" class="output">
    </div>
    <button type="submit">Submit</button>
  </form>
  
  <script>
    $(document).ready(function() {
      $('.sigPad').signaturePad();
    });
  </script>
</body>
