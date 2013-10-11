modules = {
    application {
        resource url: 'js/application.js'
        dependsOn 'jquery'
    }
	
	json2 {
		resource url: 'js/json2.min.js', disposition: 'head'
	}
	
	signaturepad {
		resource url: 'js/jquery.signaturepad.js', disposition: 'head'
		resource url: 'js/flashcanvas.js', disposition: 'head'
		dependsOn 'jquery', 'json2'
	}
	
	mask {
        resource url: 'js/jquery.mask.js', disposition: 'head'
        resource url: 'js/mask.js', disposition: 'head'
		dependsOn 'jquery'
	}

    validation {
        resource url: 'js/jquery.validate.js', disposition: 'head'
        resource url: 'js/validation_utils.js', disposition: 'head'
        dependsOn 'jquery'
    }

    expandable_menu {
        resource url:'js/menu/expandable-menu.js'
        dependsOn 'jquery'
    }

    expandable_tree {
        resource url:'js/tree/expandable-tree.js'
        dependsOn 'jquery'
    }
	
	session_utils {
		resource url:'js/session_utils.js'
        dependsOn 'jquery'
	}

    chooseActivity {
        resource url:'js/createProcess/chooseActivity.js'
        dependsOn 'jquery'
    }

    listProcess {
        dependsOn 'jquery, jquery_ui'
        resource url:'js/process/listProcess.js'
    }

    jquery_ui {
        resource url: 'js/jquery-ui/jquery-ui-1.10.3.custom.min.js', disposition: 'head'
        resource url: 'css/jquery-ui/jquery-ui-1.10.3.custom.min.css', disposition: 'head'
        dependsOn 'jquery'
    }
	
	jquery_timepicker {
		resource url: 'js/jquery-ui/jquery-ui-timepicker-addon.min.js', disposition: 'head'
		resource url: 'js/jquery-ui/jquery-ui-timepicker-addon.min.css', disposition: 'head'
		dependsOn 'jquery_ui'
	}
	
	jquery_timepicker_pl {
		resource url: 'js/jquery-ui/jquery-ui-timepicker-addon-pl.js', disposition: 'head'
		dependsOn 'jquery_timepicker'
	}

    bootstrap {
        resource url: 'js/bootstrap.min.js'
        dependsOn 'jquery'
    }

    filestyle {
        resource url: 'js/filestyle.min.js'
        dependsOn 'jquery'
        dependsOn 'bootstrap'
    }
	
	newpoint_panel_setup {
		resource url: 'js/setup.newpoint.panel.js'
		dependsOn 'jquery'
	}
	
	panzoom {
		resource url: 'js/jquery.panzoom.min.js'
		dependsOn 'jquery'
	}

    selectedPanels {
        resource url: 'js/createProcess/selectedPanels.js'
        dependsOn 'jquery'
    }
	
}