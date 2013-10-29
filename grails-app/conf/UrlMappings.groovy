class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/"(controller: 'home')
        "/tmp/$id"(controller: 'file', action: "showTmpFile")
		"/index"(view:'/index')
		"/ping"(view:'/ping')
        /*"500"(view:'/error')*/
        "500"(controller: 'error', action: 'handle')
	}
}
