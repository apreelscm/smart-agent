<g:if test="${"pesel.label".equals(radio.label)}">
    <g:textField name="reprezentant1PESEL" class="pesel display-block" maxlength="11"/>
</g:if>
<g:else>
    <g:textField name="reprezentant1Kraj" class="display-block" maxlength="30"/>
</g:else>