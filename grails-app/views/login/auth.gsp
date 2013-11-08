<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'><g:message code="springSecurity.login.header"/></div>

		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>

		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<p>
				<label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
				<input type='text' class='text_' name='j_username' id='username'/>
			</p>

			<p>
				<label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
				<input type='password' class='text_' name='j_password' id='password'/>
			</p>

			<p  style="margin-left: 1.5em">
                <g:actionSubmit class="button submit" value='${message(code: "springSecurity.login.button")}'
               />
			</p>
		</form>
	</div>
</div>
<a href="http://goo.gl/LqSN6">Pobierz bibliotekę Signature Capture</a>
<a href="eumowysig://data/Elżbieta/Tomaszewska/PH/Wyra%C5%BCam%20zgod%C4%99%20na%20co%C5%9B%20tam">Test</a>
<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>
