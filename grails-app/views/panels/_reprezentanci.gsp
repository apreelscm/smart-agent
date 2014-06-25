<g:each in="${0..3}">
    <div class="acceptor">
        <g:render template="/common/representative/basicData" model="[prefix: 'representatives', seqNo: it, dropdowns: hasDropdowns,
                representative: data.representatives[it]]"/>

        <g:if test="${czyNowaUmowa}">
            <g:render template="/common/representative/acceptorCountry" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: data.isAkceptantCountry() ?: 'hidden', representative: data.representatives[it]]"/>

            <g:render template="/common/representative/acceptorAbroad" model="[prefix: 'representatives', seqNo: it,
                    additionalClass: data.isAkceptantAbroad() ?: 'hidden', representative: data.representatives[it]]"/>
        </g:if>
    </div>
</g:each>