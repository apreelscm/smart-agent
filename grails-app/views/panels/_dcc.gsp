<div id="dccPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.dcc.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <ul class="table-list centre">
                <li>
                    <span class="align-left"><g:message code="panel.visa"/></span>
                    <span>
                        <eumowy:percentageField name="oplataVISAPr" validatable="${data}" value="${data.oplataVISAPr}" readonly="true"/>
                    </span>
                    <span>
                        <eumowy:currencyField name="oplataVISA" validatable="${data}" value="${data.oplataVISA}" readonly="true"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.mastercard"/></span>
                    <span>
                        <eumowy:percentageField name="oplataMasterCardPr" validatable="${data}" value="${data.oplataMasterCardPr}" readonly="true"/>
                    </span>
                    <span>
                        <eumowy:currencyField name="oplataMasterCard" validatable="${data}" value="${data.oplataMasterCard}" readonly="true"/>
                    </span>
                </li>
                <li>
                    <span class="align-left"><g:message code="panel.maestro"/></span>
                    <span>
                        <eumowy:percentageField name="oplataMaestroPr" validatable="${data}" value="${data.oplataMaestroPr}" readonly="true"/>
                    </span>
                    <span>
                        <eumowy:currencyField name="oplataMaestro" validatable="${data}" value="${data.oplataMaestro}" readonly="true"/>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>