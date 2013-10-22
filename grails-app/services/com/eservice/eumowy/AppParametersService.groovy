package com.eservice.eumowy

import grails.util.Environment
import java.nio.file.Paths
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

	def getSubscriptionsBlackPrefix() {
		return AppParameters.findByName("SUBSCRIPTIONS_PATH_BLACKPREFIX")?.value
	}
	
	def createDirectoryIfNotExists(String path) {
		try {
			log.info "createDirectoryIfNotExists - Checking dir: " + path
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
