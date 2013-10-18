package com.eservice.eumowy

class FileController {

	def fileService
	
    def index() {
    }
	
	def get() {
		def root = params.root
		def path = params.path
		
		if (grailsApplication.config.appParametersDisallowDownloads?.find { it == root } != null) {
			response.status = 404
			return
		}
		
		def basePath = grailsApplication.config.appParametersPaths?.get(root)
		File file = basePath ? fileService.loadFile(basePath, path) : null

		if (file) {
			log.info("$root/$path, sending file: $file.absolutePath")
			response.outputStream << file.bytes
		} else {
			log.info("$root/$path, file not found - dir: $basePath, file: $path")
			response.status = 404
		}
	}

    private String getPath(uri, path) {
        String last = uri.substring(uri.lastIndexOf('/') + 1)
        String path2 = path.substring(0, path.lastIndexOf('/') + 1)
        return path2 + last
    }

}
