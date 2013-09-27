<div id="acceptorDeclarationPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.declaration.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <div style="text-align: center">
                <ul class="table-list centre">
                    <li><span class="align-center"><g:message code="panel.acceptor.declaration.text"/></span></li>
                </ul>
                <ul class="table-list centre">
                    <li>
                        <div class="${hasErrors(bean:data,field:'hasInformacjaHandlowa','errorContainer')}">
                            <g:hiddenField name="hasInformacjaHandlowa" value="true"/>
                            <g:radioGroup name="informacjaHandlowa"
                                          labels="['panel.agree','panel.dont.agree']"
                                          values="['true', 'false']"
                                          value="${data.informacjaHandlowa}">
                                <span class="align-left"><label> ${it.radio} <g:message code="${it.label}"/></label></span>
                            </g:radioGroup>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </fieldset>
</div>