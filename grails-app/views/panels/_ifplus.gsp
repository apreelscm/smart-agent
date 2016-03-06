<style>
.postfix{
    text-align: right;
}
</style>


<div id="ifplusPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.ifplus.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <ul class="table-list">
                <li>
                    <span class="align-left"><g:message code="panel.visa"/></span>
                    <span class="align-left">
                        <eumowy:percentageField name="ifOplataVISA" value="${data.ifOplataVISA}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.mastercard"/></span>
                    <span class="align-left">
                        <eumowy:percentageField name="ifOplataMasterCard" value="${data.ifOplataMasterCard}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.dinersclub"/></span>
                    <span class="align-left">
                        <eumowy:percentageField name="ifOplataDinersClub" value="${data.ifOplataDinersClub}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.iko"/> </span>
                    <span class="align-left">
                        <eumowy:percentageField  name="ifOplataIKO" value="${data.ifOplataIKO}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.payment.for.pkopb"/></span>
                    <span class="align-left">
                        <eumowy:percentageField  name="ifOplataPKOPB" value="${data.ifOplataPKOPB}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.payment.for.jcb"/></span>
                    <span class="align-left">
                        <eumowy:percentageField  name="ifJCB" value="${data.ifJCB}" readonly="true" validatable="${data}" />
                    </span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.payment.for.upi"/></span>
                    <span class="align-left">
                        <eumowy:percentageField  name="ifUPI" value="${data.ifUPI}" readonly="true" validatable="${data}" />
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>