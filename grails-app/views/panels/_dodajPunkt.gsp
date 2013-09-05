<div id="addNewPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.addnewpoint.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <div style="display:inline-block;">
            	<g:submitButton id="addNewPointButton" name="addNewPoint" class="button submit" value="Dodaj kolejny punkt"/>
           		<g:submitButton id="acceptPointsButton" name="acceptPointsButton" class="button submit" value="Zatwierdź punkty"/>
           	</div>
        </div>
        <div>
        	<g:hiddenField name="newPointPanelCount" value="0"/>
        </div>
    </fieldset>
</div>
<div id="hiddenPanel" style="display: none;">
	<g:render template="../panels/danePunktu" model="[id:'%ID%', panelType: 'points']"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		jQuery("#conitnueButton").prop("disabled", true);
		var panelTemplate = jQuery("#hiddenPanel").html();
		var panelCount = ${data.points.size()};
		var panelInternalCount = ${data.points.size()};
		globalPanelCount = ${data.points.size()};
		jQuery("#newPointPanelCount").val(panelCount);
		
		jQuery("#hiddenPanel").remove();
		
		if (panelInternalCount > 0) {
			jQuery("#conitnueButton").prop("disabled", false);
		}
		
		for (var i = 0; i < panelCount; i++) {
			setupNewPointPanelHandlers(i-1, i, "points");
			setupNewPointPanelData("points\\["+(i-1)+"\\]\\.", "points\\["+i+"\\]\\.");
		}
			
		jQuery("#addNewPointButton").on("click", function(e) {
			e.preventDefault();
			
			if (panelInternalCount < 10) {
				var data = panelTemplate.replace(/%ID%/gm, panelCount);
				jQuery("#addNewPointPanel").prepend(data);
				setupNewPointPanelHandlers(panelCount-1, panelCount, "points");
				setupNewPointPanelData("points\\["+(panelCount-1)+"\\]\\.", "points\\["+panelCount+"\\]\\.");
				panelCount++;
				panelInternalCount++;
				globalPanelCount++;
				jQuery("#newPointPanelCount").val(panelCount);
			}
			
			if (panelInternalCount == 10) {
				jQuery(e.target).prop("disabled", true);
			}
			
			if (panelInternalCount > 0) {
				jQuery("#conitnueButton").prop("disabled", false);
			}
			
			return false;
		});
		
		jQuery("body").on("click", "#removePointButton", function(e) {
			e.preventDefault();
			
			jQuery(e.target).closest("#newPointPanel").remove();
			panelInternalCount--;
			
			if (panelInternalCount < 10) {
				jQuery("#addNewPointButton").prop("disabled", false);
			}
			
			if (panelInternalCount == 0) {
				jQuery("#conitnueButton").prop("disabled", true);
			}
			
			return false;
		});
		
	});
</r:script>