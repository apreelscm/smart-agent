<div id="addNewPosPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.addnewpos.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <div style="display:inline-block;"><g:submitButton id="addNewPosButton" name="addNewPos" class="button submit" value="Dodaj kolejny Pos"/></div>
        </div>
        <div>
        	<g:hiddenField name="newPosPanelCount" value="0"/>
        </div>
    </fieldset>
</div>
<div id="hiddenPosPanel" style="display: none;">
	<g:render template="../panels/danePos" />
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		var panelPosTemplate = jQuery("#hiddenPosPanel").html();
		var panelPosCount = 0;
		jQuery("#hiddenPosPanel").remove();
			
		jQuery("#addNewPosButton").on("click", function() {
			var data = panelPosTemplate.replace(/%ID%/gm, "-pos" + panelPosCount);
			jQuery("#addNewPosPanel").prepend(data);
			setupNewPosPanelHandlers(panelPosCount-1, panelPosCount, "-pos");
			setupNewPointPanelData("-pos"+(panelPosCount-1), "-pos"+panelPosCount);
			panelPosCount++;
			globalPanelPosCount++;
			jQuery("#newPosPanelCount").val(panelPosCount);
		});
		
		jQuery("body").on("click", "#removePosButton", function(e) {
			jQuery(e.target).closest("#newPosPanel").remove();
			
			return false;
		});
		
	});
</r:script>