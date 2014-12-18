<g:each in="${0..3}">
    <div class="acceptor ${czyNowaUmowa && it != 0 && (it >= data.representatives.size()) ? 'hidden' : ''}">
        <g:render template="/common/representative/basicData" model="[prefix: 'representatives', seqNo: it, dropdowns: hasDropdowns,
                representative: data.representatives[it]]"/>

        <g:if test="${czyNowaUmowa}">
            <g:render template="/common/representative/company" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: (data.isPersonForm() == true) ? 'hidden' : '', representative: data.representatives[it]]"/>

            <g:render template="/common/representative/personOrPartnership" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: (data.isPersonForm() == true) ? '' : 'hidden', representative: data.representatives[it]]"/>
        </g:if>
    </div>
</g:each>

<g:if test="${czyNowaUmowa}">
    <div class="text-center" style="margin-bottom: 15px">
        <button type="button" id="addAnotherAcceptor" class="button submit"><g:message code="add.acceptor.button"/></button>
    </div>
</g:if>

