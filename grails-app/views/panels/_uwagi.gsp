<%@ page contentType="text/html;charset=UTF-8" %>
<div id="notesPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.notes.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 500px">
            <div style="text-align: left">

                <div style="padding-bottom: 10px"><g:message code="panel.notes"/></div>

                <g:textArea name="notes" style="width: 100%" value="${data.notes}"/>

    </fieldset>
</div>