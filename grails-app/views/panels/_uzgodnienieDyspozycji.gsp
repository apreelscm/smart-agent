<%@ page import="com.eservice.eumowy.enums.options.Disposition" %>

<fieldset id="uzgodnienieDyspozycji">
    <header class="belka-glowna"><g:message code="disposition.info.title"/></header>

    <div class="centre">
        <eumowy:enumRadioGroup values="${Disposition.values()}" name="dyspozycja" value="${data.dyspozycja ?: 'PHONE'}" />
    </div>
</fieldset>