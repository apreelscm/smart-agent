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

    def getDefaultResourcePath(){
        System.getProperty("base.dir") + File.separator + "otherResources" + File.separator
    }
	
	def getPdfPreviewPath() {
		return grailsApplication.config.tempPdfPreviewStoragePath
	}

    // TODO warto to wydzielic poza paczke aplikacji i dorobic czyszczenie
	def getPdfImagePath() {
        String tmpPath = grailsApplication.config.tempPdfImageStoragePath

        // TODO pozbyc sie tego, parametr z konfiguracji powinien wystarczyc
		if (new File(tmpPath).isAbsolute()) {
			return tmpPath
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(tmpPath) + File.separator
		}
	}

    def getFontUri() { // TODO powinna wskazywac na katalog ew. podkatalog gdzie sa pdfy
        return grailsApplication.mainContext.getServletContext().getRealPath(File.separator+"fonts")+File.separator
    }

	def getPdfImageUri() {
		return  grailsApplication.config.tempPdfImageStorageUri
	}

	def getPdfTemplatePath() {
        String path = grailsApplication.config.pdfTemplatePath

        // TODO pozbyc sie tych ifow, parametr z konfiguracji powinien wystarczyc
		if (isDevelopmentMode()) {
			if (path == null || path.isEmpty()  || ! new File(path).exists()) {
				path = getDefaultResourcePath() + "pdf_templates" + File.separator
			}
		}
		
		if (new File(path).isAbsolute()) {
			return path
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(path) + File.separator
		}
	}
	
	def getSubscriptionsPath() { // TODO powinna wskazywac na katalog ew. podkatalog gdzie sa pdfy
        String path = grailsApplication.config.subscriptionsPath

        // TODO pozbyc sie tych ifow, parametr z konfiguracji powinien wystarczyc
		if (new File(path).isAbsolute()) {
			return path
		}
		else {
			return grailsApplication.mainContext.getServletContext().getRealPath(path) + File.separator
		}
	}
	
	def getSubscriptionsBlackPrefix() {
		return AppParameters.findByName("SUBSCRIPTIONS_PATH_BLACKPREFIX")?.value
	}
	
	def getManagementSubscriptionFirstScaleX() {
		return AppParameters.findByName("MANAGEMENT_SUBSCRIPTION1_SCALE_X")?.value
	}
	
	def getManagementSubscriptionFirstScaleY() {
		return AppParameters.findByName("MANAGEMENT_SUBSCRIPTION1_SCALE_Y")?.value
	}
	
	def getManagementSubscriptionSecondScaleX() {
		return AppParameters.findByName("MANAGEMENT_SUBSCRIPTION2_SCALE_X")?.value
	}
	
	def getManagementSubscriptionSecondScaleY() {
		return AppParameters.findByName("MANAGEMENT_SUBSCRIPTION2_SCALE_Y")?.value
	}
}
