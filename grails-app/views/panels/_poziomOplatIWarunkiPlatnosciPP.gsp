<div id="ppPaymentPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.pp.payment.title"/></div>
        <div style="text-align: center; padding-top: 20px; width: 750px" class="centre">
            <table class="vertical-center">
                <thead>
                    <tr><td></td><td></td><td style="max-width: 120px"><g:message code="panel.percent.discount.telekodzik"/></td><td style="max-width: 120px"><g:message code="panel.percent.discount.telepompka"/></td></tr>
                </thead>
                <tbody>
                    <tr><td><g:message code="panel.orange"/></td><td><g:message code="panel.ptk.company"/></td><td><g:field type="number" step="any"  name="pp_orange_tk" value="${data.pp_orange_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td><g:field type="number" step="any"  name="pp_orange_tp" value="${data.pp_orange_tp}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.plus"/></td><td><g:message code="panel.polkomtel.company"/></td><td><g:field type="number" step="any"  name="pp_plus_tk" value="${data.pp_plus_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td><g:field type="number" step="any"  name="pp_plus_tp" value="${data.pp_plus_tp}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.tmobile"/></td><td><g:message code="panel.ptc.company"/></td><td><g:field type="number" step="any"  name="pp_tmobile_tk" value="${data.pp_tmobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td><g:field type="number" step="any"  name="pp_tmobile_tp" value="${data.pp_tmobile_tp}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.heyah"/></td><td><g:message code="panel.ptc.company"/></td><td><g:field type="number" step="any"  name="pp_heyah_tk" value="${data.pp_heyah_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td><g:field type="number" step="any"  name="pp_heyah_tp" value="${data.pp_heyah_tp}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.play"/></td><td><g:message code="panel.p4.company"/></td><td><g:field type="number" step="any"  name="pp_play_tk" value="${data.pp_play_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td><g:field type="number" step="any"  name="pp_play_tp" value="${data.pp_play_tp}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td></tr>
                    <tr><td><g:message code="panel.telegrosik"/></td><td><g:message code="panel.galena.company"/></td><td><g:field type="number" step="any"  name="pp_telegrosik_tk" value="${data.pp_telegrosik_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.virginmobile"/></td><td><g:message code="panel.virginmobile.company"/></td><td><g:field type="number" step="any"  name="pp_virginmobile_tk" value="${data.pp_virginmobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.lycamobile"/></td><td><g:message code="panel.lycamobile.company"/></td><td><g:field type="number" step="any"  name="pp_lycamobile_tk" value="${data.pp_lycamobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.gtmobile"/></td><td><g:message code="panel.lycamobile.company"/></td><td><g:field type="number" step="any"  name="pp_gtmobile_tk" value="${data.pp_gtmobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.vectonemobile"/></td><td><g:message code="panel.mundio.company"/></td><td><g:field type="number" step="any"  name="pp_vectonemobile_tk" value="${data.pp_vectonemobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
                    <tr><td><g:message code="panel.delightmobile"/></td><td><g:message code="panel.mundio.company"/></td><td><g:field type="number" step="any"  name="pp_delightmobile_tk" value="${data.pp_delightmobile_tk}" readonly="true" style="width: 50px"/> <g:message code="panel.percent"/></td><td></td></tr>
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
                        <span><g:field type="number" step="any"  name="oplataZaOprogramowanieDoDoladowan" value="${data.oplataZaOprogramowanieDoDoladowan}" readonly="true"/></span>
                        <span><g:message code="panel.polish.currency"/></span>
                        <span><g:message code="panel.for.each.pos"/></span>
                    </span>
                </li>
            </ul>
        </div>
    </fieldset>
</div>