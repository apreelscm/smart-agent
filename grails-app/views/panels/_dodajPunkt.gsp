<div id="addNewPointPanelPlaceholder"></div>
<div id="addNewPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.addnewpoint.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <div style="display:inline-block;">
            	<g:submitButton id="addNewPointButton" name="addNewPoint" class="button submit" value="Dodaj kolejny punkt"/>
           		<g:submitButton id="acceptPointsButton" name="saveOnly" class="button submit" value="Zatwierdź punkty"/>
           	</div>
        </div>
        <div>
        	<g:hiddenField name="newPointPanelCount" value="0"/>
        </div>
    </fieldset>
</div>
<div id="hiddenPanel" style="display: none;">
	<g:render template="../panels/danePunktu" model="[id:'%ID%', panelType: 'points', pointData: data.defaultPointData]"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		jQuery("#conitnueButton").prop("disabled", true);
		var maxTerminalCount = jQuery("#liczbaTerminali").val();
		var panelTemplate = jQuery("#hiddenPanel").html();
		var panelCount = ${data.points.size()};
		var panelInternalCount = ${data.points.size()};
		globalPanelCount = ${data.points.size()};
		jQuery("#newPointPanelCount").val(panelCount);
		
		jQuery("#hiddenPanel").remove();
		
		if (panelInternalCount > 0) {
			jQuery("#conitnueButton").prop("disabled", false);
		}
		
		if (getCurrentTerminalCount("points") == maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", true);
			jQuery("#addNewPosButton").prop("disabled", true);
		}
		else if (getCurrentTerminalCount("points") < maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", false);
			jQuery("#addNewPosButton").prop("disabled", false);
		}
		
		for (var i = 0; i < panelCount; i++) {
			setupNewPointPanelHandlers(i-1, i, "points");
			setupNewPointPanelData("points\\["+(i-1)+"\\]\\.", "points\\["+i+"\\]\\.");
		}
			
		jQuery("#addNewPointButton").on("click", function(e) {
			e.preventDefault();
			
			if (panelInternalCount < 10) {
				var data = panelTemplate.replace(/%ID%/gm, panelCount);
				jQuery("#addNewPointPanelPlaceholder").append(data);
				setupNewPointPanelHandlers(panelCount-1, panelCount, "points");
				setupNewPointPanelData("points\\["+(panelCount-1)+"\\]\\.", "points\\["+panelCount+"\\]\\.");
				panelCount++;
				panelInternalCount++;
				globalPanelCount++;
				jQuery("#newPointPanelCount").val(panelCount);
                maskNewPointRefresh();
			}
			
			if (panelInternalCount == 10) {
				jQuery(e.target).prop("disabled", true);
			}
			
			if (panelInternalCount > 0) {
				jQuery("#conitnueButton").prop("disabled", false);
			}
			
			if (getCurrentTerminalCount("points") == maxTerminalCount) {
				jQuery(e.target).prop("disabled", true);
				jQuery("#addNewPosButton").prop("disabled", true);
			}
			else if (getCurrentTerminalCount("points") < maxTerminalCount) {
				jQuery(e.target).prop("disabled", false);
				jQuery("#addNewPosButton").prop("disabled", false);
			}

			return false;
		});
		
		jQuery("body").on("click", "#removePointButton", function(e) {
			e.preventDefault();
			
			jQuery(e.target).closest(".newPointPanel").remove();
			panelInternalCount--;
			
			if (panelInternalCount < 10) {
				jQuery("#addNewPointButton").prop("disabled", false);
			}
			
			if (panelInternalCount == 0) {
				jQuery("#conitnueButton").prop("disabled", true);
			}
			
			if (getCurrentTerminalCount("points") < maxTerminalCount) {
				jQuery("#addNewPointButton").prop("disabled", false);
				jQuery("#addNewPosButton").prop("disabled", false);
			}

			refreshTelepomkaAndTelekodzikPercentValues();
			
			var dbPointId = parseInt(jQuery(e.target).attr('data-point-id'));
			if (dbPointId) {
				console.log("Usuwam punkt o id: " + dbPointId);
				jQuery.post(jQuery(location).attr("href"), {_eventId_deletePoint: "", pointId: dbPointId}, function(data){});			
			}
			
			return false;
		});

	});
</r:script>