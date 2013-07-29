class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/"(controller: 'home')
		"/index"(view:'/index')
		"/ping"(view:'/ping')
        "500"(view:'/error')
	}
}
