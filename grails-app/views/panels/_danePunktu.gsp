<div data-js-id="${id}" class="newPointPanel">
<fieldset style="text-align: center">
<div class="belka-glowna">
    <g:message code="panel.newpoint.pointdata.title" />
</div>
<div style="text-align: center; padding-top: 20px;" class="centre">
<div style="float: right;">
    <g:submitButton data-point-id="${pointData?.id}" id="removePointButton" name="removePointButton"
                    class="button submit" value="${message(code: 'delete.point.label')}"
                    style="margin-right: 2em; margin-bottom: 1em;" />
</div>
<div style="clear: both;"></div>
<input type="hidden" id="${panelType}[${id}].id" name="${panelType}[${id}].id"
       value="${pointData?.id}" />
<input type="hidden" name="${panelType}[${id}].parentPosId" value="${pointData?.parentPosId}" />
<g:render template="../panels/danePunktu/opieka" />
<g:render template="../panels/danePunktu/danePunktu"/>
<g:render template="../panels/danePunktu/daneDoWydruku"/>
<g:render template="../panels/danePunktu/adresDoKorespondencjiPunktu" />
<g:render template="../panels/danePunktu/osobaDoKontaktuWPunkcie" />

<g:if test="${data.isBundleActivity}">
    <g:render template="../panels/danePunktu/zestawPosPakiet"/>
</g:if>
<g:else>
    <g:render template="../panels/danePunktu/zestawPos" />
</g:else>

<g:render template="../panels/danePunktu/informacjeTechniczne" />
<g:render template="../panels/danePunktu/funkcjeTerminala" />
<g:render template="../panels/danePunktu/dodatkoweWyposazenie" />
<g:render template="../panels/danePunktu/adresacjaSeciowa" />
</div>

</fieldset>
</div>
