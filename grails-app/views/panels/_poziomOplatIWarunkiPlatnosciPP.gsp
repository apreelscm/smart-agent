<div id="ppPaymentPanel">
    <g:hiddenField name="hasAtLeastOneDoladowanie" value="false"/> %{-- Wartosc zmienia sie w selectedPanels.js --}%
    <g:hiddenField name="hasDoladowania" value="true"/>
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.pp.payment.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 600px" class="centre">
            <g:hiddenField name="isDoladowania_tp" value="${data.isDoladowania_tp}"/>
            <g:hiddenField name="isDoladowania_tk" value="${data.isDoladowania_tk}"/>
            <div id="formaDoladowania">

            <g:set var="hasNewUmowaAndPrepaid" value="${data.hasNewUmowaAndPrepaid}"/>
            <g:set var="isRozszerzenie" value="${data.isRozszerzenie}"/>
            <g:set var="tpFromCalcEnabled" value="${data.isDoladowania_tp}"/>
            <g:set var="tkFromCalcEnabled" value="${data.isDoladowania_tk}"/>
            <g:set var="hasPrepaid" value="${data.hasPrepaid}"/>
            <g:set var="tpEnabled" value="${hasNewUmowaAndPrepaid || tpFromCalcEnabled || (tpFromCalcEnabled && isRozszerzenie && hasPrepaid)}"/>
            <g:set var="tkEnabled" value="${hasNewUmowaAndPrepaid || tkFromCalcEnabled || (tkFromCalcEnabled && isRozszerzenie && hasPrepaid)}"/>

                <g:if test="${!tpEnabled}">
                    <g:hiddenField name="doladowania_tp" value="${data.doladowania_tp}"/>
                </g:if>

                <g:if test="${!tkEnabled}">
                    <g:hiddenField name="doladowania_tk" value="${data.doladowania_tk}"/>
                </g:if>

                <div style="display: inline">
                    <label><g:checkBox name="doladowania_tk" value="${data.doladowania_tk}" disabled="${!tkEnabled}" class="doladowanie" data-doladowanie="telekodzik"/> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telekodzik"/></label>
                </div>
                <div style="display: inline">
                    <label><g:checkBox name="doladowania_tp" value="${data.doladowania_tp}" disabled="${!tpEnabled}" class="doladowanie" data-doladowanie="telepompka"/> <g:message code="panel.newpoint.terminaloptions.phonecreditsrecharge.telepompka"/></label>
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

        <div style="text-align: center; padding-top: 20px; width: 800px" class="centre">
            <table class="vertical-center">
                <thead>
                    <tr><td colspan="2"></td><td style="max-width: 120px"><g:message code="panel.percent.discount.telekodzik"/></td><td style="max-width: 120px"><g:message code="panel.percent.discount.telepompka"/></td></tr>
                </thead>
                <tbody id="ppPayment">
                    <g:hiddenField name="pp_orange_tk" value="${data.pp_orange_tk}"/>               <g:hiddenField name="pp_gtmobile_tk" value="${data.pp_gtmobile_tk}"/>
                    <g:hiddenField name="pp_plus_tk" value="${data.pp_plus_tk}"/>                   <g:hiddenField name="pp_vectonemobile_tk" value="${data.pp_vectonemobile_tk}"/>
                    <g:hiddenField name="pp_tmobile_tk" value="${data.pp_tmobile_tk}"/>             <g:hiddenField name="pp_delightmobile_tk" value="${data.pp_delightmobile_tk}"/>
                    <g:hiddenField name="pp_heyah_tk" value="${data.pp_heyah_tk}"/>                 <g:hiddenField name="pp_orange_tp" value="${data.pp_orange_tp}"/>
                    <g:hiddenField name="pp_play_tk" value="${data.pp_play_tk}"/>                   <g:hiddenField name="pp_plus_tp" value="${data.pp_plus_tp}"/>
                    <g:hiddenField name="pp_telegrosik_tk" value="${data.pp_telegrosik_tk}"/>       <g:hiddenField name="pp_tmobile_tp" value="${data.pp_tmobile_tp}"/>
                    <g:hiddenField name="pp_virginmobile_tk" value="${data.pp_virginmobile_tk}"/>   <g:hiddenField name="pp_heyah_tp" value="${data.pp_heyah_tp}"/>
                    <g:hiddenField name="pp_lycamobile_tk" value="${data.pp_lycamobile_tk}"/>       <g:hiddenField name="pp_play_tp" value="${data.pp_play_tp}"/>

                    <tr><td><g:message code="panel.orange"/></td><td><g:message code="panel.ptk.company"/></td><td> <div class="telekodzikValue">${data.pp_orange_tk}</div> <g:message code="panel.percent"/></td><td><div class="telepompkaValue">${data.pp_orange_tp}</div> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.plus"/></td><td><g:message code="panel.polkomtel.company"/></td><td> <div class="telekodzikValue">${data.pp_plus_tk}</div> <g:message code="panel.percent"/></td><td><div class="telepompkaValue">${data.pp_plus_tp}</div> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.tmobile"/></td><td><g:message code="panel.ptc.company"/></td><td> <div class="telekodzikValue">${data.pp_tmobile_tk}</div> <g:message code="panel.percent"/></td><td><div class="telepompkaValue">${data.pp_tmobile_tp}</div> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.heyah"/></td><td><g:message code="panel.ptc.company"/></td><td> <div class="telekodzikValue">${data.pp_heyah_tk}</div> <g:message code="panel.percent"/></td><td><div class="telepompkaValue">${data.pp_heyah_tp}</div> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.play"/></td><td><g:message code="panel.p4.company"/></td><td><div class="telekodzikValue">${data.pp_play_tk}</div> <g:message code="panel.percent"/></td><td><div class="telepompkaValue">${data.pp_play_tp}</div> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.telegrosik"/></td><td><g:message code="panel.galena.company"/></td><td><div class="telekodzikValue">${data.pp_telegrosik_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.virginmobile"/></td><td><g:message code="panel.virginmobile.company"/></td><td><div class="telekodzikValue">${data.pp_virginmobile_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.lycamobile"/></td><td><g:message code="panel.lycamobile.company"/></td><td><div class="telekodzikValue">${data.pp_lycamobile_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.gtmobile"/></td><td><g:message code="panel.lycamobile.company"/></td><td><div class="telekodzikValue">${data.pp_gtmobile_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.vectonemobile"/></td><td><g:message code="panel.mundio.company"/></td><td><div class="telekodzikValue">${data.pp_vectonemobile_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.delightmobile"/></td><td><g:message code="panel.mundio.company"/></td><td><div class="telekodzikValue">${data.pp_delightmobile_tk}</div> <g:message code="panel.percent"/></td><td></td></tr>
                </tbody>
            </table>
            <ul class="table-list align-center centre">
                <li>
                    <span><g:message code="panel.payment.for.selling.recharge"/></span>
                </li>
            </ul>
            <ul class="table-list align-center centre">
                <li>
                    <span>
                        <span><g:field type="text" name="oplataZaOprogramowanieDoDoladowan" value="${data.oplataZaOprogramowanieDoDoladowan}" readonly="true"/></span>
                        <span><g:message code="panel.polish.currency"/></span>
                        <span><g:message code="panel.for.each.pos"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>