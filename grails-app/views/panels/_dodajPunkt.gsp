<div id="addNewPointPanel">
    <fieldset style="text-align: center">
        <div class="belka-glowna"><g:message code="panel.addnewpoint.title"/> </div>
        <div style="text-align: center; padding-top: 20px; width: 350px" class="centre">
            <div style="display:inline-block;"><g:submitButton id="addNewPointButton" name="addNewPoint" class="button submit" value="Dodaj kolejny punkt"/></div>
        </div>
        <div>
        	<g:hiddenField name="newPointPanelCount" value="0"/>
        </div>
    </fieldset>
</div>
<div id="hiddenPanel" style="display: none;">
	<g:render template="../panels/danePunktu" />
</div>
<r:require module="jquery_ui"/>
	
<r:script>
	
	jQuery(document).ready(function() {
		var panelTemplate = jQuery("#hiddenPanel").html();
		var panelCount = 0;
		jQuery("#hiddenPanel").remove();
			
		jQuery("#addNewPointButton").on("click", function() {
			var data = panelTemplate.replace(/%ID%/gm, "-point" + panelCount);
			jQuery("#addNewPointPanel").prepend(data);
			setupNewPointPanelHandlers(panelCount-1, panelCount, "-point");
			setupNewPointPanelData("-point"+(panelCount-1), "-point"+panelCount);
			panelCount++;
			globalPanelCount++;
			jQuery("#newPointPanelCount").val(panelCount);
		});
		
		jQuery("body").on("click", "#removePointButton", function(e) {
			jQuery(e.target).closest("#newPointPanel").remove();
			
			return false;
		});
		
	});
</r:script>