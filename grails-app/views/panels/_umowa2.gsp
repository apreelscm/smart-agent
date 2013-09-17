<%@ page import="java.text.DateFormat" %>
<div id="aggrementPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.aggrement.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span><g:message code="panel.aggrement.place"/>: </span>
                    <span><g:textField name="miejsceUmowy" value="${data.miejsceUmowy}"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>