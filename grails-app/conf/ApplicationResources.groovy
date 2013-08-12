modules = {
    application {
        resource url: 'js/application.js'
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
		resource url: 'js/jquery.mask.min.js', disposition: 'head'
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
	
	newpoint_panel_setup {
		resource url: 'js/setup.newpoint.panel.js'
		dependsOn 'jquery'
	}
	
}