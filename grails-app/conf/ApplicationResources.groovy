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
}