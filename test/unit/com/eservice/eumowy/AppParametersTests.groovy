package com.eservice.eumowy

import grails.test.mixin.TestFor

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(AppParameters)
class AppParametersTests {

    void testGetByNameResultNull() {
		def param = AppParameters.findByName("TEMP_STORAGE_PATH")
		assert param == null
	}
	
	void testGetByNameResultNotNull() {
		def param = AppParameters.findByName("TEST_PARAM")
		assert param != null
		assert param.name == "TEST_PARAM"
		assert param.value == "TEST_VALUE"
	}
}
