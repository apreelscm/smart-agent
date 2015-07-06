package com.eservice.eumowy

import java.nio.file.Paths

class AppParametersService {

	def grailsApplication
	def grailsLinkGenerator

	String getPdfTemplatePath(String fileName) {
        return getPath("pdfTemplates", fileName)
	}

	String getPdfPreviewPath(String fileName) {
        return getPath("pdfPreviews", fileName)
	}

    String getPdfImagePath(String fileName) {
        return getPath("pdfImages", fileName)
    }

    String getFontUri() {
        return getPath("pdfTemplates", "fonts")
    }

    String getSubscriptionsPath() {
        return getPath("pdfTemplates", "subscriptions")
    }

    String getBeneficiaryPath(String filename) {
        return getPath("beneficiary", filename, false)
    }

    private String getPath(String parameterName, String filename) {
        getPath(parameterName, filename, true)
    }

    private String getPath(String parameterName, String filename, Boolean createDirectories) {
        String basePath = grailsApplication.config.appParametersPaths?.get(parameterName)
        String path = basePath ? Paths.get(basePath, filename).normalize().toAbsolutePath().toString() : null
        if(createDirectories) {
            createDirectoryIfNotExists(path)
        }
        return path
    }

    def getPdfImageUri(String fileName) {
        return grailsLinkGenerator.link(controller: 'file', action: 'get', absolute: false, params: [root: "pdfImages", path: fileName])
    }

    def getPdfPreviewPath() {
        return grailsApplication.config.appParametersPaths?.get("pdfPreviews")
    }

	def getPdfPreviewUri(String fileName) {
		return  grailsLinkGenerator.link(controller: 'file', action: 'get', absolute: false, params: [root: "pdfPreviews", path: fileName])
	}
	
	def getPdfPreviewThreadWorkersCount() {
		def threadCount = 4
		
		if (grailsApplication.config.pdfPreviewsThreadWorkers != null) {
            threadCount = grailsApplication.config.pdfPreviewsThreadWorkers.toInteger()
		} else {
            log.debug("Missing pdfPreviewsThreadWorkers parameter. Setting to default: "  + threadCount)
        }
		
		return threadCount
	}

    def getAgeToRemove(){
        //in hours
        int defaultValue = 24;
        def returnValue = AppParameters.findByName("FILES_TO_REMOVE_AGE_IN_HOUR")?.value.toInteger()

        return returnValue?:defaultValue
    }

	def getSubscriptionsBlackPrefix() {
		return AppParameters.findByName("SUBSCRIPTIONS_PATH_BLACKPREFIX")?.value
	}
	
	def getPdfPreviewImageResolution() {
		def resolutionString = AppParameters.findByName("PDFPREVIEW_IMAGE_RESOLUTION")?.value
		def resolution = grailsApplication.config.pdfPreviewImageDefaultResolution != null ? grailsApplication.config.pdfPreviewImageDefaultResolution : 300
		if (resolutionString) {
			resolution = Integer.valueOf(resolutionString)
		}
		return resolution.value
	}
	
	def createDirectoryIfNotExists(String path) {
		try {
			File parentDirectory = new File(path).getParentFile()
			if (!parentDirectory.exists()) {
				parentDirectory.mkdirs()
				log.info "createDirectoryIfNotExists - Creating dir: " + path
			}
		}
		catch(Exception e) {
			log.error "createDirectoryIfNotExists - " + e
			e.printStackTrace()
		}
	}

}
