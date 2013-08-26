<div id="dccPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.dcc.title"/> </div>
            <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
                <ul class="table-list centre">
                    <li>
                        <span class="align-left"><g:message code="panel.visa"/></span><span><g:field name="oplataVISAPr" type="number" step="any" value="${data.oplataVISAPr}" readonly="true" style="width: 70px;"/> <g:message code="panel.percent"/></span><span><g:message code="panel.flat.rate"/></span><span><g:field name="oplataVISA" type="number" step="any" value="${data.oplataVISA}" readonly="true" style="width: 70px;"/> <g:message code="panel.polish.currency"/></span>
                    </li>
                    <li>
                        <span class="align-left"><g:message code="panel.mastercard"/></span><span><g:field name="oplataMasterCardPr" type="number" step="any" value="${data.oplataMasterCardPr}" readonly="true" style="width: 70px;"/> <g:message code="panel.percent"/></span><span><g:message code="panel.flat.rate"/></span><span><g:field name="oplataMasterCard" type="number" step="any" value="${data.oplataMasterCard}" readonly="true" style="width: 70px;"/> <g:message code="panel.polish.currency"/></span>
                    </li>
                    <li>
                        <span class="align-left"><g:message code="panel.maestro"/></span><span><g:field name="oplataMaestroPr" type="number" step="any" value="${data.oplataMasteroPr}" readonly="true" style="width: 70px;"/> <g:message code="panel.percent"/></span><span><g:message code="panel.flat.rate"/></span><span><g:field name="oplataMaestro" type="number" step="any" value="${data.oplataMaestro}" readonly="true" style="width: 70px;"/> <g:message code="panel.polish.currency"/></span>
                    </li>
                </ul>
            </div>
    </fieldset>
</div>