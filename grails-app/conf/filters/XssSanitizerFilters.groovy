package filters

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.tonyzampogna.xss.sanitizer.util.XssSanitizerUtil

/**
 * Overridden plugin XssSanitizer because of spring-security order plugin problems
 * @url https://github.com/tonyzampogna/XssSanitizer
 */
class XssSanitizerFilters {

    def grailsApplication

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                if (log.isInfoEnabled()){
                    log.info "XssSanitizerFilters preHandle request " +
                            "'$request.servletPath'/'$request.forwardURI', " +
                            "from $request.remoteHost ($request.remoteAddr) " +
                            " at ${new Date()}, Ajax: $request.xhr, controller: $controllerName, " +
                            "action: $actionName, params: ${new TreeMap(params)}"
                }

                // If enabled, call the sanitize method for each request.
                if (grailsApplication.config.xssSanitizer.enabled){
                    sanitizeParameters(params, request, response)
                }

                return true
            }
            after = { Map model ->
                if (log.isInfoEnabled()){
                    log.info "XssSanitizerFilters postHandle request, params ${new TreeMap(params)}"
                }
            }

        }
    }

    private void sanitizeParameters(parameters, HttpServletRequest request, HttpServletResponse response) {
        // Sanitize Header Parameters
        //Enumeration<String> headerNames = request.getHeaderNames();
        //while (headerNames.hasMoreElements()) {
        //	String headerName = (String) headerNames.nextElement();
        //	String headerValue = request.getHeader(headerName);
        //	int headerValueLength = headerValue.length();
        //	String newHeaderValue = stripXSS(headerValue);
        //	int newHeaderValueLength = newHeaderValue.length();
        //	// Throw an exception. This is illegal.
        //	if (headerValueLength != newHeaderValueLength) {
        //		response.sendError 500
        //	}
        //}

        // Sanitize Request Parameters
        parameters.each { entry ->
            if (entry.value instanceof String) {
                entry.value = XssSanitizerUtil.stripXSS(entry.value)
            }
        }
    }
}
