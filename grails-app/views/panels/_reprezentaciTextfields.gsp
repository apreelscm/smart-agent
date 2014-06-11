<g:each in="${0..2}">
    <g:render template="/common/representative" model="['prefix': 'representatives', 'seqNo': it, 'dropdowns': false]"/>
</g:each>