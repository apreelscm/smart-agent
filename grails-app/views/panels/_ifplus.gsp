<div id="ifplusPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.ifplus.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <ul class="table-list">
                <li>
                    <span class="align-left"><g:message code="panel.visa"/></span>
                    <span class="align-left">
                        <g:textField name="ifOplataVISA" value="${data.ifOplataVISA}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.mastercard"/></span>
                    <span class="align-left">
                        <g:textField name="ifOplataMasterCard" value="${data.ifOplataMasterCard}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.dinersclub"/></span>
                    <span class="align-left">
                        <g:textField name="ifOplataDinersClub" value="${data.ifOplataDinersClub}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.iko"/> </span>
                    <span class="align-left">
                        <g:textField name="ifOplataIKO" value="${data.ifOplataIKO}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.payment.for.pkopb"/></span>
                    <span class="align-left">
                        <g:textField name="ifOplataPKOPB" value="${data.ifOplataPKOPB}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>