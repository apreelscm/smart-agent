<fieldset id="packagePrice">
    <header class="belka-glowna"><g:message code="package.price.title"/></header>

    <div class="centre">
        <label for="cenaPakietu"><g:message code="package.price.title"/></label>
        <eumowy:currencyField name="cenaPakietu" validatable="${data}" value="${data.cenaPakietu}" readonly="readonly"/>
    </div>
</fieldset>