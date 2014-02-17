<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
	<p>
		Witaj,
        <br><br>
		Umowa z Klientem – ${merchantName} (NIP: ${merchantNip}) została zaakceptowana.
		Skontaktuj się z Klientem i poinformuj o dalszych krokach.
        <br><br>
        Działania:
        <g:each in="${activities}">
        - ${it}<br>
        </g:each>
        <br><br>
		Pozdrawiamy,<br>
		Zespół Wsparcia Sprzedaży 
	</p>
</body>
