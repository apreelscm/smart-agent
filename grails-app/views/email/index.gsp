<!DOCTYPE html>
<head>
  <title><g:message code="email.title" /></title>
  <meta name="layout" content="main">
  <r:require module="mask" />
</head>
<body>
  <r:script>
    jQuery(document).ready(function() {
      jQuery('.nip').mask('9999999999');
    });
  </r:script>
  <form method="post" action="send">
  	<p>NIP</p>
  	<input name="nip" type="text" class="nip" required />
  	<textarea name="content"></textarea>
    <button type="submit" class="przycisk-submit"><g:message code="email.send" /></button>
  </form>
</body>
