<div class="newPosPanel">
	<input type="hidden" id="${panelType}[${id}].id" name="${panelType}[${id}].id" value="${pointData?.id}" />
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.newpos.posdata.title" /></div>
        <div style="text-align: center; padding-top: 20px;" class="centre">
        	<div style="float: right;">
        		<g:submitButton id="removePosButton" name="removePosButton" class="button submit" value="Usuń Pos" style="margin-right: 2em; margin-bottom: 1em;"/>
        	</div>
        	<div style="clear: both;"></div>
        	<fieldset class="subpanel-fieldset">
        		<legend><g:message code="panel.newpos.listofactivepoints.title" /></legend>
        		<div class="subpanel-fieldset-centercontent" >
        			<p><g:message code="panel.newpos.listofpointsfromcbd" /></p>
        			<dict:cbdPointsSelect id="${panelType}[${id}].cbdId" name="${panelType}[${id}].cbdId" nip="${data.nip}" />
        		</div>
        	</fieldset>
        	<g:render template="../panels/zestawPos" />
            <g:render template="../panels/informacjeTechniczne" />
            <g:render template="../panels/funkcjeTerminala" />
            <g:render template="../panels/dodatkoweWyposazenie" />
            <g:render template="../panels/adresacjaSeciowa" />
       	</div>
   	</fieldset>
</div>

<r:require module="jquery_ui" />
<r:require module="jquery_timepicker_pl" />

<r:script>
    function setFieldPropertiesInDodatkoweWyposazenie(element, value){
        var posPanel = jQuery(element).closest("div.newPosPanel");
        if(value == "Verifone Vx670 GPRS"){
            posPanel.find("tr.baseRow").show();
            setRequiredForSimCard(true, posPanel)
        } else if (value !== ""){
            posPanel.find("tr.baseRow").hide();
            setRequiredForSimCard(true, posPanel)
        }
        else{
            posPanel.find("tr.baseRow").hide();
            setRequiredForSimCard(false, posPanel)
        }
    }

    function setRequiredForSimCard(isRequired, posPanel){
        if(isRequired){
            posPanel.find("select.kartaSimTyp").attr("required", true);
            posPanel.find("input.kartaSimIlosc").attr("required", true);
        } else {
            posPanel.find("select.kartaSimTyp").removeAttr("required", true);
            posPanel.find("input.kartaSimIlosc").removeAttr("required", true);
        }
    }
</r:script>