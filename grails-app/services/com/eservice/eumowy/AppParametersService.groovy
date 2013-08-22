package com.eservice.eumowy

class AppParametersService {

	def getParamById(Integer id) {
		return AppParameters.get(id);	
	}
	
    def getParamByName(String name) {
		return AppParameters.findByName(name)?.value
	}
	
	def getPdfImagePath() {
		String tmpPath = AppParameters.findByName("TEMP_PDFIMAGE_STORAGE_PATH")
		
		if (tmpPath == null || tmpPath.isEmpty()) {
			tmpPath = "web-app" + File.separator + "files" + File.separator + "pdf_images" + File.separator
		}
		
		return tmpPath
	}
	
	def getPdfImageUri() {
		String tmpPath = AppParameters.findByName("TEMP_PDFIMAGE_STORAGE_URI")
		
		if (tmpPath == null || tmpPath.isEmpty()) {
			tmpPath = "/eumowy/files/pdf_images/"
		}
		
		return tmpPath
	}
	
	def getPdfTemplatePath() {
		String path = AppParameters.findByName("PDF_TEMPLATE_PATH")
		
		if (path == null || path.isEmpty()) {
			path = System.getProperty("base.dir") + File.separator + "otherResources" + File.separator + "pdf_templates" + File.separator
		}
		
		return path
	}
	
	def getSubscriptionsPath() {
		String path = AppParameters.findByName("SUBSCRIPTIONS_PATH")
		
		if (path == null || path.isEmpty()) {
			path = "web-app" + File.separator + "files" + File.separator 
		}
		
		return path
	}
	
	def getSubscriptionsBlackPath() {
		String path = AppParameters.findByName("SUBSCRIPTIONS_BLACK_PATH")
		
		if (path == null || path.isEmpty()) {
			path = "web-app" + File.separator + "files" + File.separator
		}
		
		return path
	}
}
