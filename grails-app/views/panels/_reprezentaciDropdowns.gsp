<g:set var="firstNames" value="${[""] + representativesBisnode?.collect {it.firstName}}"/>
<g:set var="lastNames" value="${[""] + representativesBisnode?.collect {it.lastName}}"/>

<g:each in="${0..2}">
    <div class="acceptor">
        <g:render template="/common/representative/basicData" model="[prefix: 'representatives', seqNo: it, dropdowns: true,
            representative: data.representatives[it]]"/>

        <g:render template="/common/representative/acceptorCountry" model="[prefix: 'representatives', seqNo: it,
                additionalClass: data.isAkceptantCountry() ?: 'hidden', representative: data.representatives[it]]"/>

        <g:render template="/common/representative/acceptorAbroad" model="[prefix: 'representatives', seqNo: it,
                additionalClass: data.isAkceptantAbroad() ?: 'hidden', representative: data.representatives[it]]"/>
    </div>
</g:each>