<div id="canvasserPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.canvasser.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span><g:select name="pozyskujacyTytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.pozyskujacyTytul}"/></span>
                    <span><g:message code="panel.first.name"/>: </span>
                    <span><g:textField name="pozyskujacyImie" value="${data.pozyskujacyImie}" readonly="true" style="width: 120px"/></span>
                    <span><g:message code="panel.last.name"/>: </span>
                    <span><g:textField name="pozyskujacyNazwisko" value="${data.pozyskujacyNazwisko}" readonly="true"/></span>
                    <span><g:message code="panel.number"/>: </span>
                    <span><g:textField name="pozyskujacyNumer" value="${data.pozyskujacyNumer}" readonly="true" style="width: 100px"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>