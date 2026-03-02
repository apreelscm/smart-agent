<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
	<p>
        Witaj,
        <br><br>
        Klient – ${merchantName} (NIP: ${merchantNip}) chce podpisać umowę w wersji papierowej.
        <br><br>
        Jeśli podpis będzie w formie papierowej, wydrukuj dwa egzemplarze dokumentów, zbierz wszystkie wymagane podpisy i wyślij jeden z egzemplarzy do Centrali eService, a drugi pozostaw klientowi.<br>
        Opcjonalnie po zebraniu wymaganych podpisów, wyślij jeden zeskanowany egzemplarz na skrzynkę mailową zespołu Onboardingu, a następnie wyślij oryginał z podpisem klienta do Centrali eService.<br>
        Jeśli podpis będzie w formie elektronicznej, wygeneruj jeden egzemplarz dokumentów, zbierz wszystkie wymagane podpisy i wyślij egzemplarz na skrzynkę mailową zespołu Onboardingu.<br>

        Działania:
        <g:each in="${activities}">
        - ${it}<br>
        </g:each>

        <br><br>
        Pozdrawiamy,<br>
        Biuro Wsparcia Sprzedaży
    </p>
</body>
