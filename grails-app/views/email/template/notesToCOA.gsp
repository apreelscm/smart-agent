<%@ page contentType="text/html"%>
<!DOCTYPE html>
<body>
    <p>
        ${notes?.encodeAsHTML()?.replace('\n', '<br/>\n')}
        <br><br>
        ${phNumber}
        <br>
        ${phName}
    </p>
</body>

