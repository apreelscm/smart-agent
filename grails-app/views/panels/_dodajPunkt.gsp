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
	<g:render template="../panels/danePunktu" model="[id:'%ID%']"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		var panelTemplate = jQuery("#hiddenPanel").html();
		var panelCount = 0;
		var panelInternalCount = 1;
		
		jQuery("#hiddenPanel").remove();
			
		jQuery("#addNewPointButton").on("click", function(e) {
			e.preventDefault();
			
			if (panelInternalCount < 10) {
				var data = panelTemplate.replace(/%ID%/gm, panelCount);
				jQuery("#addNewPointPanel").prepend(data);
				setupNewPointPanelHandlers(panelCount-1, panelCount, "points");
				setupNewPointPanelData("points["+(panelCount-1)+"].", "points["+panelCount+"].");
				panelCount++;
				panelInternalCount++;
				globalPanelCount++;
				jQuery("#newPointPanelCount").val(panelCount);
			}
			
			if (panelInternalCount == 10) {
				jQuery(e.target).prop("disabled", true);
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
			
			return false;
		});
		
	});
</r:script>