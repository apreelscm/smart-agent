<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
    <p>
        Witaj,
        <br><br>
        Klient – ${merchantName}/${merchantNip} podpisał umowę oraz oczekuje przesłania wersji papierowej.
        <br><br>
        Wydrukuj dwa egzemplarze dokumentów oraz wyślij do Klienta na adres korespondencyjny Merchanta.
        <br><br>
        Działania:
        <g:each in="${activities}">
            - ${it}<br>
        </g:each>
        <br><br>
    </p>
</body>