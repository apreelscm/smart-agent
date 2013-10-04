<div id="addNewPosPanelPlaceholder"></div>
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
	<g:render template="../panels/danePos" model="[id:'%ID%', panelType: 'poses']"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		var panelPosTemplate = jQuery("#hiddenPosPanel").html();
		var maxTerminalCount = jQuery("#liczbaTerminali").val();
		var panelPosCount = ${data.poses.size()};
		globalPanelPosCount = ${data.poses.size()};
		jQuery("#hiddenPosPanel").remove();
		
		for( var i = 0; i < panelPosCount; i++) {
			setupNewPosPanelHandlers(i-1, i, "poses");
			setupNewPointPanelData("poses\\["+(i-1)+"\\]\\.", "poses\\["+i+"\\]\\.");
		}
		
		if (getCurrentTerminalCount("poses") == maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", true);
			jQuery("#addNewPosButton").prop("disabled", true);
		}
		else if (getCurrentTerminalCount("poses") < maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", false);
			jQuery("#addNewPosButton").prop("disabled", false);
		}
			
		jQuery("#addNewPosButton").on("click", function(e) {
			e.preventDefault();
			
			if (getCurrentTerminalCount("poses") == maxTerminalCount) {
				jQuery(e.target).prop("disabled", true);
				jQuery("#addNewPointButton").prop("disabled", true);
			}
			else if (getCurrentTerminalCount("poses") < maxTerminalCount) {
				jQuery(e.target).prop("disabled", false);
				jQuery("#addNewPointButton").prop("disabled", false);
			}
			
			var data = panelPosTemplate.replace(/%ID%/gm, panelPosCount);
			jQuery("#addNewPosPanelPlaceholder").append(data);
			setupNewPosPanelHandlers(panelPosCount-1, panelPosCount, "poses");
			setupNewPointPanelData("poses\\["+(panelPosCount-1)+"\\]\\.", "poses\\["+panelPosCount+"\\]\\.");
			panelPosCount++;
			globalPanelPosCount++;
			jQuery("#newPosPanelCount").val(panelPosCount);
			
			return false;
		});
		
		jQuery("body").on("click", "#removePosButton", function(e) {
			jQuery(e.target).closest("#newPosPanel").remove();
			
			return false;
		});
		
	});
</r:script>