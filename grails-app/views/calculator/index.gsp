<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 10.10.13
  Time: 09:41
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    NIP: ${nip}
    <br/>
    ILOSC REKORDOW Z KALKULATORA: ${result.size()}
    <br/>
    <br/>
    <table border="1">
        <tr>
            <td>NAZWA APREEL</td>
            <td>WARTOSC</td>
        </tr>
        <g:each in="${result}">
            <tr>
                <td>${it.POLEAPREEL}</td>
                <td>${it.WARTOSCAPREEL}</td>
            </tr>
        </g:each>
    </table>
</body>
</html>