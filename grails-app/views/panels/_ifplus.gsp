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
                        <eumowy:currencyField name="ifOplataVISA" value="${data.ifOplataVISA}" readonly="true" validatable="${data}" offset="27"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.mastercard"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="ifOplataMasterCard" value="${data.ifOplataMasterCard}" readonly="true" validatable="${data}" offset="27"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.dinersclub"/></span>
                    <span class="align-left">
                        <eumowy:currencyField name="ifOplataDinersClub" value="${data.ifOplataDinersClub}" readonly="true" validatable="${data}" offset="27"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.iko"/> </span>
                    <span class="align-left">
                        <eumowy:currencyField  name="ifOplataIKO" value="${data.ifOplataIKO}" readonly="true" validatable="${data}" offset="27"/>
                    </span>
                </li>
                <li>
                    <span class="align-right"><g:message code="panel.payment.for.pkopb"/></span>
                    <span class="align-left">
                        %{--TODO readonly = true--}%
                        <eumowy:currencyField  name="ifOplataPKOPB" value="${data.ifOplataPKOPB}" readonly="true" validatable="${data}" offset="27" />
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>