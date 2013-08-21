package com.eservice.eumowy



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AppParametersService)
class AppParametersServiceTests {

    void testGetParamByName() {
		String param = service.getParamByName("TEMP_STORAGE_PATH")
		assert param == null
	}
}
