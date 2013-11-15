package com.eservice.eumowy

import grails.util.Environment

import java.nio.file.Paths

import org.codehaus.groovy.grails.commons.GrailsApplication;

import com.eservice.eumowy.util.EumowyCustomEnvironment

class AppParametersService {

	def grailsApplication
	def grailsLinkGenerator

	def getPdfTemplatePath(String fileName) {
		String basePath = grailsApplication.config.appParametersPaths?.get("pdfTemplates")
		String path = basePath ? Paths.get(basePath, fileName).normalize().toAbsolutePath().toString() : null
		createDirectoryIfNotExists(basePath)
		return path
	}

	def getPdfPreviewPath(String fileName) {
		String basePath = grailsApplication.config.appParametersPaths?.get("pdfPreviews")
		String path = basePath ? Paths.get(basePath, fileName).normalize().toAbsolutePath().toString() : null
		createDirectoryIfNotExists(basePath)
		return path
	}

	def getPdfPreviewUri(String fileName) {
		return  grailsLinkGenerator.link(controller: 'file', action: 'get', absolute: false, params: [root: "pdfPreviews", path: fileName])
	}

	def getPdfImagePath(String fileName) {
		String basePath = grailsApplication.config.appParametersPaths?.get("pdfImages")
		String path = basePath ? Paths.get(basePath, fileName).normalize().toAbsolutePath().toString() : null
		createDirectoryIfNotExists(basePath)
		return path
	}

	def getPdfImageUri(String fileName) {
		return grailsLinkGenerator.link(controller: 'file', action: 'get', absolute: false, params: [root: "pdfImages", path: fileName])
	}

	def getFontUri() {
		String basePath = grailsApplication.config.appParametersPaths?.get("pdfTemplates")
		String path = basePath ? Paths.get(basePath, "fonts").normalize().toAbsolutePath().toString() : null
		createDirectoryIfNotExists(path)
		return path
	}

	def getSubscriptionsPath() {
		String basePath = grailsApplication.config.appParametersPaths?.get("pdfTemplates")
		String path = basePath ? Paths.get(basePath, "subscriptions").normalize().toAbsolutePath().toString() : null
		createDirectoryIfNotExists(path)
		return path
	}

    def getMobileAppPath() {
        String basePath = grailsApplication.config.appParametersPaths?.get("mobileAppPath")
        String path = basePath ? Paths.get(basePath, "subscriptions").normalize().toAbsolutePath().toString() : null
        return path
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
			def file = new File(path)
			if (file.exists() == false) {
				file.mkdirs()
				log.info "createDirectoryIfNotExists - Creating dir: " + path
			}
		}
		catch(Exception e) {
			log.error "createDirectoryIfNotExists - " + e
			e.printStackTrace()
		}
	}

}
