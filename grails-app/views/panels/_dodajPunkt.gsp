<div id="addNewPointPanelPlaceholder"></div>
<div id="addNewPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.addnewpoint.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <div style="display:inline-block;">
            	<g:submitButton id="addNewPointButton" name="addNewPoint" class="button submit" value="Dodaj punkt"/>
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
<div id="removePointConfirmDialog" style="display: none;">
	<g:message code="panel.addnewpoint.confirmRemoval"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>

	jQuery(document).ready(function() {
		jQuery("#continueButton").prop("disabled", true);
		
		var maxTerminalCount = parseInt(jQuery("#liczbaTerminali").val());
		var panelTemplate = jQuery("#hiddenPanel").html();
		var panelCount = ${data.points.size()};
		panelInternalCount.value = ${data.points.size()};
		globalPanelCount = ${data.points.size()};
		jQuery("#newPointPanelCount").val(panelCount);
		jQuery("#hiddenPanel").remove();
		
		if (jQuery(".newPointPanel").length > 0) {
			jQuery("#continueButton").prop("disabled", false);
		}
		
		if (panelInternalCount.value > 0) {
			jQuery("#continueButton").prop("disabled", false);
		}
		
		if (getCurrentTerminalCount("points") == maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", true);
			jQuery("#addNewPosButton").prop("disabled", true);
		}
		else if (getCurrentTerminalCount("points") != maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", false);
			jQuery("#addNewPosButton").prop("disabled", false);
		}
		
		for (var i = 0; i < panelCount; i++) {
			setupNewPointPanelHandlers(i, "points");
			setupNewPointPanelData("points",i-1, i);
		}
			
		jQuery("#addNewPointButton").on("click", function(e) {
			e.preventDefault();
			
			if (panelInternalCount.value < 10) {
				var data = panelTemplate.replace(/%ID%/gm, panelCount);
				jQuery("#addNewPointPanelPlaceholder").append(data);
				setupNewPointPanelHandlers(panelCount, "points");
				setupNewPointPanelData("points", panelCount-1, panelCount);
				panelCount++;
				panelInternalCount.value++;
				globalPanelCount++;
				jQuery("#newPointPanelCount").val(panelCount);
                maskNewPointRefresh();
			}
			
			if (panelInternalCount.value == 10) {
				jQuery(e.target).prop("disabled", true);
			}
			
			if (panelInternalCount.value > 0) {
				jQuery("#continueButton").prop("disabled", false);
			}

			if (getCurrentTerminalCount("points") == maxTerminalCount) {
				jQuery(e.target).prop("disabled", true);
				jQuery("#addNewPosButton").prop("disabled", true);
			}
			else if (getCurrentTerminalCount("points") != maxTerminalCount) {
				jQuery(e.target).prop("disabled", false);
				jQuery("#addNewPosButton").prop("disabled", false);
			}

			manageTKAndTPCheckedProperty();

			return false;
		});
		
		jQuery("body").on("click", "#removePointButton", function(e) {
			e.preventDefault();
			
			jQuery("#removePointConfirmDialog").dialog({
				resizable: true,
				height:200,
				width: 450,
				modal: true,
				buttons: {
						"Tak": function() {
							jQuery( this ).dialog( "close" );
							clearNewPointDataAfterParentDeletion("points", -1, jQuery(e.target).closest(".newPointPanel").attr('data-js-id'));
							jQuery(e.target).closest(".newPointPanel").remove();
							panelInternalCount.value--;
							
							if (panelInternalCount.value < 10) {
								jQuery("#addNewPointButton").prop("disabled", false);
							}
							
							if (panelInternalCount.value == 0) {
								jQuery("#continueButton").prop("disabled", true);
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
						},
						"Nie": function() {
							jQuery( this ).dialog( "close" );
						}
					}
			});
			
			if (panelInternalCount.value < 10) {
				jQuery("#addNewPointButton").prop("disabled", false);
			}
			
			if (panelInternalCount.value == 0) {
				jQuery("#continueButton").prop("disabled", true);
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