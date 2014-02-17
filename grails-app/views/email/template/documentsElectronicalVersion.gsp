<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
	<p>
        Szanowni Państwo,
        <br><br>
        Miło nam powitać Państwa w gronie Klientów eService. Cieszymy się, że okazali nam Państwo zaufanie i wybrali jako partnera w realizacji tak odpowiedzialnej części procesu sprzedażowego, jakim jest procesowanie transakcji bezgotówkowych. Państwa wybór jest dla nas zarówno powodem do dumy, jak i ogromnym zobowiązaniem.
        <br><br>
        Aktualnie Umowa o współpracy jest przez nas weryfikowana, a po jej zarejestrowaniu w naszym systemie rozpocznie się proces wdrożeniowy i zostaną Państwo poinformowani o terminie instalacji terminala. Umowa wejdzie w życie z dniem instalacji sprzętu.
        <br><br>
        Wierzymy, że nasza współpraca przyczyni się do efektywnego działania Państwa firmy i zapewni komfort i bezpieczeństwo korzystania z dostarczanych usług.
        <br><br>

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
