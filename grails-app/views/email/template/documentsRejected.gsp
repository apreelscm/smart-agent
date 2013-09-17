<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
    <p>
        Dokumentacja odrzucona po procesie weryfikacji O-ZRD
        <br><br>
        Rodzaj odrzuconych dokumentów:
        <br><br>
        <g:each in="${activities}">
        - ${it}<br>
        </g:each>

        <br><br>
        Powód odrzucenia:
        <br><br>
        ${rejectReason?.encodeAsHTML()?.replace('\n', '<br/>\n')}
        <br><br>
        --------------------------------------------------------------------------------
        <br><br>
        Powyższe braki/błędy spowodowały odrzucenie dokumentu. W związku z tym, dokument nie może wejść w życie. Termin na poprawienie/uzupełnienie błędów/braków wynosi 7 dni od dnia otrzymania niniejszej informacji. Skorzystaj z opcji „Popraw już wprowadzone dane”.
        W przypadku braku poprawienia/uzupełnienia błędu/braku należy skontaktować się z klientem i poinformować o odrzuceniu dokumentacji oraz odebrać dokumenty od klienta (jeśli otrzymał swoją wersję).
        <br><br>
        ${merchantNip}
        <br>
        ${merchantName}
        <br><br>
        Pozdrawiamy,<br>
        Zespół Rejestracji Danych
    </p>
</body>