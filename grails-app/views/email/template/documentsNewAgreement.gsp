<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
<p>
    Szanowni Państwo,
    <br><br>
    Jest nam bardzo miło, że wybrali Państwo eService na partnera biznesowego i zapewniamy, że dołożymy wszelkich starań, aby nasza współpraca przebiegała sprawnie i zaowocowała wieloma sukcesami.
    <br/>
    Nip: ${merchantNip}
    <br>
    Nazwa akceptanta: ${merchantName}
    <br>
    Działania:
    <g:each in="${activities}">
        - ${it}<br>
    </g:each>
    <br><br>

    Z wyrazami szacunku,<br>
    Zespół eService
</p>
</body>
