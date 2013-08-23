<div id="acceptorsPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span><g:select name="reprezentant1Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant1Tytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><g:textField name="reprezentant1Imie" value="${data.reprezentant1Imie}"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><g:textField name="reprezentant1Nazwisko" value="${data.reprezentant1Nazwisko}"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:select name="reprezentant2Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant2Tytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><g:textField name="reprezentant2Imie" value="${data.reprezentant2Imie}" /></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><g:textField name="reprezentant2Nazwisko" value="${data.reprezentant2Nazwisko}" /></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>