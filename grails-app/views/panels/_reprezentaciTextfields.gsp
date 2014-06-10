<g:each in="${1..3}">
    <g:render template="/common/acceptor" model="['representative': 'reprezentant' + it, 'dropdowns': false]"/>
</g:each>