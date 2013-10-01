<div id="canvasserPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.canvasser.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span>
                        <g:select name="pozyskujacyTytulSelect" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.pozyskujacyTytul}" disabled="true"/>
                        <g:hiddenField name="pozyskujacyTytul" value="${data.pozyskujacyTytul}"/>
                    </span>
                    <span><g:message code="panel.first.name"/>: </span>
                    <span><eumowy:textField name="pozyskujacyImie" value="${data.pozyskujacyImie}" validatable="${data}" readonly="true" style="width: 120px"/></span>
                    <span style="padding-left: 10px"><g:message code="panel.last.name"/>: </span>
                    <span><eumowy:textField name="pozyskujacyNazwisko" value="${data.pozyskujacyNazwisko}" validatable="${data}"  readonly="true"/></span>
                    <span style="padding-left: 10px"><g:message code="panel.number"/>: </span>
                    <span><eumowy:textField name="pozyskujacyNumer" value="${data.pozyskujacyNumer}" validatable="${data}"" readonly="true" style="width: 100px"/></span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>