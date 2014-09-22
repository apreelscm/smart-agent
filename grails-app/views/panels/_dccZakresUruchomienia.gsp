<div id="dccRangePanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.dcc.range.title"/> </div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 600px">
        	<g:hiddenField name="hasDccZakresUruchomienia" value="true"/>
            <div class="${hasErrors(bean:data,field:'dccZakresUruchomienia','errorContainer')}" style="text-align: left">
                <g:radioGroup name="dccZakresUruchomienia"
                              labels="['panel.dcc.range.current.and.new']"
                              values="['obecne_i_nowe']"
                              value="obecne_i_nowe" readonly="readonly">
                    <p><label> ${it.radio} <g:message code="${it.label}"/></label></p>
                </g:radioGroup>
            </div>
        </div>
        <div id="dccRange" class="centre hidden" style="text-align: center; padding-top: 20px; width: 800px; max-width: 950px">
            <table class="t">
                <thead>
                <tr>
                    <td><g:message code="panel.dcc.table.full.name" /></td>
                    <td><g:message code="panel.dcc.table.street" /></td>
                    <td><g:message code="panel.dcc.table.city" /></td>
                    <td><g:message code="panel.dcc.table.house.number" /></td>
                    <td><g:message code="panel.dcc.table.code" /></td>
                    <td><g:message code="panel.dcc.table.poz.count" /></td>
                    <td>&nbsp;</td>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" var="point" in="${data.allPoints}">
                    <tr>
                        <td>${point.nazwa}
                        	<input type="hidden" name="allPoints[${i}].nazwa" value="${point.nazwa}" />
                       		<input type="hidden" name="allPoints[${i}].id" value="${point.id}" />
                       		<input type="hidden" name="allPoints[${i}].cbdId" value="${point.cbdId}" />
                       		<input type="hidden" name="allPoints[${i}].nip" value="${point.nip}" />
                      	</td>
                        <td>${point.ulica}<input type="hidden" name="allPoints[${i}].ulica" value="${point.ulica}" /></td>
                        <td>${point.miejscowosc}<input type="hidden" name="allPoints[${i}].miejscowosc" value="${point.miejscowosc}" /></td>
                        <td>${point.nrBudynku}<input type="hidden" name="allPoints[${i}].nrBudynku" value="${point.nrBudynku}" /></td>
                        <td>${point.kodPocztowy}<input type="hidden" name="allPoints[${i}].kodPocztowy" value="${point.kodPocztowy}" /></td>
                        <td>${point.liczbaPos}<input type="hidden" name="allPoints[${i}].liczbaPos" value="${point.liczbaPos}" /></td>
                        <td><g:checkBox name="allPoints[${i}].czyWybranyZakresUruchomienia" checked="${point.czyWybranyZakresUruchomienia}" /></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>