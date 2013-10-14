<div id="rechargeTypePanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.recharge.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 600px" class="centre">
            <g:hiddenField name="hasDoladowania" value="true"/>
            <div class="${hasErrors(bean:data,field:'hasDoladowania','errorContainer')}" id="formaDoladowania">

                <g:set var="hasNewUmowaAndPrepaid" value="${processInstance?.activities?.any{it.code.equals('nowaUmowa')} && processInstance?.activities?.any{it.code.equals('dodaniePrepaid')}}"/>
                <g:set var="tpEnabled" value="${hasNewUmowaAndPrepaid || data.isDoladowania_tp}"/>
                <g:set var="tkEnabled" value="${hasNewUmowaAndPrepaid || data.isDoladowania_tk}"/>

                <g:if test="${!tpEnabled}">
                    <g:hiddenField name="doladowania_tp" value="${data.doladowania_tp}"/>
                </g:if>

                <g:if test="${!tkEnabled}">
                    <g:hiddenField name="doladowania_tk" value="${data.doladowania_tk}"/>
                </g:if>

                <div style="display: inline">
                    <label><g:checkBox name="doladowania_tp" value="${data.doladowania_tp}" disabled="${!tpEnabled}" class="doladowanie" data-doladowanie="telepompka"/> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka"/></label>
                </div>
                <div style="display: inline">
                    <label><g:checkBox name="doladowania_tk" value="${data.doladowania_tk}" disabled="${!tkEnabled}" class="doladowanie" data-doladowanie="telekodzik"/> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik"/></label>
                </div>
            </div>

            <div class="align-center" style="padding-top: 10px"><g:message code="panel.declared.selling.electronic.recharch"/></div>
            <table style="padding-top: 10px">
                <tr class="vertical-center">
                    <td>&nbsp;</td>
                    <td class="align-right"><eumowy:textField name="srednia_sprzedaz_doladowan" value="${data.srednia_sprzedaz_doladowan}" validatable="${data}" style="width: 100px"  readonly="true"/></td>
                    <td class="align-left"><g:message code="panel.for.each.pos"/></td>
                </tr>
                <tr class="vertical-center">
                    <td class="align-right">słownie</td>
                    <td class="align-right"><eumowy:textField name="srednia_sprzedaz_doladowan_slownie" value="${data.srednia_sprzedaz_doladowan_slownie}" validatable="${data}" style="width: 300px" required="true"/></td>
                    <td class="align-left"><g:message code="panel.for.each.pos"/></td>
                </tr>
            </table>
        </div>
    </fieldset>
</div>