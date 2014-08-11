<div id="activePointsPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.active.points.list.title"/> </div>
        <div id="activePoints" class="centre" style="text-align: center; padding-top: 20px; width: 800px; max-width: 950px">
            <table class="t">
                <thead>
                <tr>
                    <td><g:message code="panel.dcc.table.full.name" /></td>
                    <td><g:message code="panel.dcc.table.street" /></td>
                    <td><g:message code="panel.dcc.table.city" /></td>
                    <td><g:message code="panel.dcc.table.house.number" /></td>
                    <td><g:message code="panel.dcc.table.code" /></td>
                    <td><g:message code="panel.label.choosen" /></td>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" var="point" in="${data.allPoints}">
                    <g:if test="${point?.czyCbd}">
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
                            <td><g:checkBox name="allPoints[${i}].czyWybranyWymianaUmowy" checked="${point.czyWybranyWymianaUmowy}" /></td>
                        </tr>
                    </g:if>
                </g:each>
                </tbody>
            </table>
        </div>
    </fieldset>
</div>