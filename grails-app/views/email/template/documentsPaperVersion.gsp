<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
	<p>
        Witaj,
        <br><br>
        Klient – ${merchantName} (NIP: ${merchantNip}) chce podpisać umowę w wersji papierowej.
        <br><br>
        Wydrukuj dwa egzemplarze dokumentów i wyślij do przedstawiciela handlowego ${phNumber} ${phFirstName} ${phSurname}.

        Działania:
        <g:each in="${activities}">
        - ${it}<br>
        </g:each>

        <br><br>
        Pozdrawiamy,<br>
        Zespół Wsparcia Sprzedaży
	</p>
</body>
