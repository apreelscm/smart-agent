package com.eservice.eumowy

import com.eservice.eumowy.util.EumowyCustomEnvironment
import grails.util.Environment

class AppParametersService {

	/* Consts */
	public static int SUBSCRIPTION_SCALE_X = 74
	public static int SUBSCRIPTION_SCALE_Y = 43
	
	def grailsApplication
	
	def getParamById(Integer id) {
		return AppParameters.get(id);	
	}
	
    def getParamByName(String name) {
		return AppParameters.findByName(name)?.value
	}

    def isDevelopmentMode(){
        return Environment.isDevelopmentMode() ||
                Environment.getCurrent().getName().equalsIgnoreCase(EumowyCustomEnvironment.MOCK.getName())
    }
	
	def getPdfPreviewPath() {
		String tmpPath = AppParameters.findByName("TEMP_PDFPREVIEW_STORAGE_PATH")?.value
		
		if (isDevelopmentMode()) {
			if (tmpPath == null || tmpPath.isEmpty()) {
				tmpPath = "tmp"
			}
		}
		
		return tmpPath
	}
	
	def getPdfImagePath() {
		String tmpPath = AppParameters.findByName("TEMP_PDFIMAGE_STORAGE_PATH")?.value
		// FIXME dodac tworzenie katalogu tmp jesli go nie ma
		if (isDevelopmentMode()) {
			if (tmpPath == null || tmpPath.isEmpty()) {
				tmpPath = File.separator + "files" + File.separator + "pdf_images" + File.separator
			}
		}
		
		if (new File(tmpPath).isAbsolute()) {
			return tmpPath
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(tmpPath) + File.separator
		}
	}

    def getFontUri() {
        def fontUri = grailsApplication.mainContext.getServletContext().getRealPath(File.separator+"fonts")+File.separator
        fontUri
    }

	def getPdfImageUri() {
		String tmpPath = AppParameters.findByName("TEMP_PDFIMAGE_STORAGE_URI")?.value
		
		if (isDevelopmentMode()) {
			if (tmpPath == null || tmpPath.isEmpty()) {
				tmpPath = "/eumowy/files/pdf_images/"
			}
		}
		
		return tmpPath
	}
	
	def getPdfTemplatePath() {
		String path = AppParameters.findByName("PDF_TEMPLATE_PATH")?.value
		
		if (isDevelopmentMode()) {
            File checkPath = new File(path);
			if (path == null || path.isEmpty() || ! checkPath.exists()) {
				path = System.getProperty("base.dir") + File.separator + "otherResources" + File.separator + "pdf_templates" + File.separator
			}
		}
		
		if (new File(path).isAbsolute()) {
			return path
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(path) + File.separator
		}
	}
	
	def getSubscriptionsPath() {
		String path = AppParameters.findByName("SUBSCRIPTIONS_PATH")?.value
		
		if (isDevelopmentMode()) {
			if (path == null || path.isEmpty()) {
				path = File.separator + "files" + File.separator 
			}
		}
		
		if (new File(path).isAbsolute()) {
			return path
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(path) + File.separator
		}
	}
	
	def getSubscriptionsBlackPrefix() {
		String prefix = AppParameters.findByName("SUBSCRIPTIONS_PATH_BLACKPREFIX")?.value
		
		if (isDevelopmentMode()) {
			if (prefix == null || prefix.isEmpty()) {
				prefix = "black_"
			}
		}
		
		return prefix
	}
	
	def getTemplateNameForNewPoint() {
		String name = AppParameters.findByName("TEMPLATE_NAME_FOR_NEW_POINT")?.value
		
		if (isDevelopmentMode()) {
			if (name == null || name.isEmpty()) {
				name = "BRAK TEMPLATE DLA NOWEGO PUNKTU"
			}
		}
		
		return name
	}
	
	def getTemplateNameForNewPos() {
		String name = AppParameters.findByName("TEMPLATE_NAME_FOR_NEW_POS")?.value
		
		if (isDevelopmentMode()) {
			if (name == null || name.isEmpty()) {
				name = "BRAK TEMPLATE DLA NOWEGO POS"
			}
		}
		
		return name
	}
	
	def getManagementSubscriptionFirstScaleX() {
		String scaleXString = AppParameters.findByName("MANAGEMENT_SUBSCRIPTION1_SCALE_X")?.value
		
		if (isDevelopmentMode()) {
			if (scaleXString == null || scaleXString.isEmpty()) {
				scaleXString = "85"
			}
		}
		
		return scaleXString
	}
	
	def getManagementSubscriptionFirstScaleY() {
		String scaleYString = AppParameters.findByName("MANAGEMENT_SUBSCRIPTION1_SCALE_Y")?.value
		
		if (isDevelopmentMode()) {
			if (scaleYString == null || scaleYString.isEmpty()) {
				scaleYString = "58"
			}
		}
		
		return scaleYString
	}
	
	def getManagementSubscriptionSecondScaleX() {
		String scaleXString = AppParameters.findByName("MANAGEMENT_SUBSCRIPTION2_SCALE_X")?.value

		if (isDevelopmentMode()) {
			if (scaleXString == null || scaleXString.isEmpty()) {
				scaleXString = "56"
			}
		}

		return scaleXString
	}
	
	def getManagementSubscriptionSecondScaleY() {
		String scaleYString = AppParameters.findByName("MANAGEMENT_SUBSCRIPTION2_SCALE_Y")?.value
		
		if (isDevelopmentMode()) {
			if (scaleYString == null || scaleYString.isEmpty()) {
				scaleYString = "59"
			}
		}
		
		return scaleYString
	}
}
