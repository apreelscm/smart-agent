

package com.eservice.eumowy;

import grails.test.mixin.*

import com.eservice.eumowy.pdfmapper.PdfMapper

class PdfMapperTest {

	@Test
	public void testDynamicMethods() {
		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapNrMerchanta")
		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapUlicaDoKorespondencjiTyp")
		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapUlicaDoKorespondencji")
	}
	
	@Test
	public void testPdfMapper() {
		def testObject = new Expando()
		testObject.nrMerchanta = "123456789098766"
		testObject.ulicaDoKorespondencjiTyp = "al."
		testObject.ulicaDoKorespondencji = "Jerozolimskie"
		
		def data = PdfMapper.mapPointDataToPDFData(testObject)
		
		data.each { key, value ->
			println key + " : " + value
		}
	}

}
