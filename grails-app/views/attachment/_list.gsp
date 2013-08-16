<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eservice.eumowy.Activity" %>
<table style="margin-top: 10px" class="t">
    <colgroup>
        <col style="width: calc(100% - 40px)" />
        <col style="width: 40px" />
    </colgroup>
    <thead>
    <tr>
        <td>Nazwa</td>
        <td></td>
    </tr>
    </thead>
    <tbody>
    <g:each in="${files}" status="i" var="file">
        <tr>
            <td>${fieldValue(bean: file, field: "name")}
            <td>
                <g:remoteLink  class="button small action" action="deleteFile" params="[id:file.id, processId:processId]"
                              update="attachmentsBox">Usuń</g:remoteLink>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>