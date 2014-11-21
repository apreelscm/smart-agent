<g:each in="${0..3}">
    <div class="acceptor">
        <g:render template="/common/representative/basicData" model="[prefix: 'representatives', seqNo: it, dropdowns: hasDropdowns,
                representative: data.representatives[it]]"/>

        <g:if test="${czyNowaUmowa}">
            <g:render template="/common/representative/company" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: 'hidden', representative: data.representatives[it]]"/>

            <g:render template="/common/representative/personOrPartnership" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: 'hidden', representative: data.representatives[it]]"/>
        </g:if>
    </div>
</g:each>