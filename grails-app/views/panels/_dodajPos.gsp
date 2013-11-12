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
	<g:render template="../panels/danePos" model="[id:'%ID%', panelType: 'poses', pointData: data.defaultPosData]"/>
</div>
<div id="removePosConfirmDialog" style="display: none;">
	<g:message code="panel.addnewpos.confirmRemoval"/>
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		jQuery("#continueButton").prop("disabled", true);
		var panelPosTemplate = jQuery("#hiddenPosPanel").html();
		var maxTerminalCount = parseInt(jQuery("#liczbaTerminali").val());
		var panelPosCount = ${data.poses.size()};
		panelPosInternalCount = ${data.poses.size()};
		globalPanelPosCount = ${data.poses.size()};
		jQuery("#hiddenPosPanel").remove();
		
		if (jQuery(".newPosPanel").length > 0) {
			jQuery("#continueButton").prop("disabled", false);
		}
		
		for( var i = 0; i < panelPosCount; i++) {
			setupNewPosPanelHandlers(i, "poses");
			setupNewPointPanelData("poses",i-1, i);
		}
		
		if (getCurrentTerminalCount("poses") == maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", true);
			jQuery("#addNewPosButton").prop("disabled", true);
		}
		else if (getCurrentTerminalCount("poses") != maxTerminalCount) {
			jQuery("#addNewPointButton").prop("disabled", false);
			jQuery("#addNewPosButton").prop("disabled", false);
		}
			
		jQuery("#addNewPosButton").on("click", function(e) {
			e.preventDefault();
			
			var data = panelPosTemplate.replace(/%ID%/gm, panelPosCount);
			jQuery("#addNewPosPanelPlaceholder").append(data);
			setupNewPosPanelHandlers(panelPosCount, "poses");
			setupNewPointPanelData("poses", panelPosCount-1, panelPosCount);
			panelPosCount++;
			globalPanelPosCount++;
			panelPosInternalCount++;
			jQuery("#newPosPanelCount").val(panelPosCount);
			
			if (getCurrentTerminalCount("poses") == maxTerminalCount) {
				jQuery(e.target).prop("disabled", true);
				jQuery("#addNewPointButton").prop("disabled", true);
			}
			else if (getCurrentTerminalCount("poses") != maxTerminalCount) {
				jQuery(e.target).prop("disabled", false);
				jQuery("#addNewPointButton").prop("disabled", false);
			}
			
			if (panelPosInternalCount > 0) {
				jQuery("#continueButton").prop("disabled", false);
			}

			maskNewPosRefresh();
			manageTKAndTPCheckedProperty();

			return false;
		});
		
		jQuery("body").on("click", "#removePosButton", function(e) {
			jQuery("#removePosConfirmDialog").dialog({
				resizable: true,
				height:200,
				width: 450,
				modal: true,
				buttons: 
					{
						"Tak": function() {
							jQuery( this ).dialog( "close" );
							clearNewPointDataAfterParentDeletion("poses", -1, jQuery(e.target).closest(".newPosPanel").attr('data-js-id'));
							jQuery(e.target).closest(".newPosPanel").remove();
							panelPosInternalCount--;
							
							if (getCurrentTerminalCount("poses") != maxTerminalCount) {
								jQuery("#addNewPointButton").prop("disabled", false);
								jQuery("#addNewPosButton").prop("disabled", false);
							}
							
							if (panelPosInternalCount == 0) {
								jQuery("#continueButton").prop("disabled", true);
							}
						},
						"Nie": function() {
							jQuery( this ).dialog( "close" );
						}
					}
			});
			
			return false;
		});
		
		if (panelPosInternalCount == 0) {
			jQuery("#continueButton").prop("disabled", true);
		}
		
	});
</r:script>