<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
    <p>
        ${notes?.encodeAsHTML()?.replace('\n', '<br/>\n')}

        <br><br>
        ${merchantNip}
        <br>
        ${merchantName}
        <br>
        Rodzaj odrzuconych dokumentów:
        <g:each in="${activities}">
        - ${it}<br>
        </g:each>

        <br><br>
        ${phNumber}
        <br>
        ${phName}
    </p>
</body>

