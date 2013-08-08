function setupNewPointPanelHandlers(panelId) {
	
	jQuery(document).ready(function() {
        jQuery("#plannedInstallationDate"+panelId).datepicker({ dateFormat: 'yy-mm-dd' });
        jQuery("#dayCloseFrom"+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
        	onClose: function( selectedDate ) {
				jQuery( "#dayCloseTo"+panelId).datepicker( "option", "minDate", selectedDate );
			}
       	});
        jQuery("#dayCloseTo"+panelId).datepicker({ 
        	dateFormat: 'yy-mm-dd',
       	 	onClose: function( selectedDate ) {
				jQuery( "#dayCloseFrom"+panelId).datepicker( "option", "maxDate", selectedDate );
			}
       	});
        
        jQuery("#dataforprinting-asAbove"+panelId).on("click", function(e) {
        	if(e.target.checked) {
        		jQuery("#pointNameForSearchEngine"+panelId).val(jQuery("#pointNameForPrintingFromPOSTerminal"+panelId).val());
        	} else {
        		jQuery("#pointNameForSearchEngine"+panelId).val("");
        	}
        });
        
        jQuery("#dataforprinting-asForMerchant"+panelId).on("click", function(e) { 
        	if(e.target.checked) {
        		jQuery("#dataforprinting-addressStreet"+panelId).val(jQuery("#addressStreet").val());
        		jQuery("#dataforprinting-addressHomeNumber"+panelId).val(jQuery("#addressHomeNumber").val());
        		jQuery("#dataforprinting-addressFlatNumber"+panelId).val(jQuery("#addressFlatNumber").val());
        		jQuery("#dataforprinting-addressCity"+panelId).val(jQuery("#addressCity").val());
        		jQuery("#dataforprinting-addressPostalCode"+panelId).val(jQuery("#addressPostalCode").val());
        		jQuery("#dataforprinting-addressPostOffice"+panelId).val(jQuery("#addressPostal").val());
        	} else {
        		jQuery("#dataforprinting-addressStreet"+panelId).val("");
        		jQuery("#dataforprinting-addressHomeNumber"+panelId).val("");
        		jQuery("#dataforprinting-addressFlatNumber"+panelId).val("");
        		jQuery("#dataforprinting-addressCity"+panelId).val("");
        		jQuery("#dataforprinting-addressPostalCode"+panelId).val("");
        		jQuery("#dataforprinting-addressPostOffice"+panelId).val("");
        	}
        });
        
        jQuery("#contactAddress-asForMerchant").on("change", function(e) {
       		jQuery("#contactAddress-addressStreet"+panelId).val(jQuery("#addressStreet").val());
       		jQuery("#contactAddress-addressHomeNumber"+panelId).val(jQuery("#addressHomeNumber").val());
       		jQuery("#contactAddress-addressFlatNumber"+panelId).val(jQuery("#addressFlatNumber").val());
       		jQuery("#contactAddress-addressCity"+panelId).val(jQuery("#addressCity").val());
       		jQuery("#contactAddress-addressPostalCode"+panelId).val(jQuery("#addressPostalCode").val());
       		jQuery("#contactAddress-addressPostOffice"+panelId).val(jQuery("#addressPostal").val());
        });
        
        jQuery("#contactAddress-asOnPrint").on("change", function(e) {
       		jQuery("#contactAddress-addressStreet"+panelId).val(jQuery("#dataforprinting-addressStreet"+panelId).val());
       		jQuery("#contactAddress-addressHomeNumber"+panelId).val(jQuery("#dataforprinting-addressHomeNumber"+panelId).val());
       		jQuery("#contactAddress-addressFlatNumber"+panelId).val(jQuery("#dataforprinting-addressFlatNumber"+panelId).val());
       		jQuery("#contactAddress-addressCity"+panelId).val(jQuery("#dataforprinting-addressCity"+panelId).val());
       		jQuery("#contactAddress-addressPostalCode"+panelId).val(jQuery("#dataforprinting-addressPostalCode"+panelId).val());
       		jQuery("#contactAddress-addressPostOffice"+panelId).val(jQuery("#dataforprinting-addressPostOffice"+panelId).val());
        });
        
        jQuery("#persontocontact-asForMerchant"+panelId).on("click", function(e) {
        	if (e.target.checked) {
        		jQuery("#contactAtPointFax"+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointPhone"+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointMobilePhone"+panelId).val(jQuery("#").val());
        		jQuery("#contactAtPointEmail"+panelId).val(jQuery("#").val());
        	} else {
        		jQuery("#contactAtPointFax"+panelId).val("");
        		jQuery("#contactAtPointPhone"+panelId).val("");
        		jQuery("#contactAtPointMobilePhone"+panelId).val("");
        		jQuery("#contactAtPointEmail"+panelId).val("");
        	}
        }); 
    });
	
}