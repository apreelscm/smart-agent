<div id="additionalServices2Panel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.additional.services2.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-left"><g:message code="panel.payment.logo"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any" name="wydrukGrafikiCena" value="${data.wydrukGrafikiCena}" style="width: 100px" readonly="true"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.payment.calc"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any"  name="dzialaniaMatematyczneCena" value="${data.dzialaniaMatematyczneCena}" style="width: 100px" readonly="true"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.payment.title.payment"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any" name="tytulPlatnosciCena" value="${data.tytulPlatnosciCena}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.payment.first.session"/></span>
                    <span class="align-left">
                        <g:field type="number" step="any" name="pierwszaSesjaCena" value="${data.pierwszaSesjaCena}" style="width: 100px"/> <g:message code="panel.polish.currency"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>