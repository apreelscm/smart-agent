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
	public void testMapPointDataToPDFData() {
		def testObject = new Expando()
		testObject.nrMerchanta = "123456789098766"
		testObject.ulicaDoKorespondencjiTyp = "al."
		testObject.ulicaDoKorespondencji = "Jerozolimskie"
		
		def data = PdfMapper.mapPointDataToPDFData(testObject)
		
		data.each { key, value ->
			println key + " : " + value
		}
	}
	
	@Test
	public void testMapProcessDataToPDFData() {
		def objList = []
		for (int i = 0; i < 10; i++) {
			def obj = new Expando()
			obj.name = "NazwaPola"+i
			obj.value = "WartoscPola"+i
			
			objList.add(obj)
		}
		
		def checkboxObj1 = new Expando()
		checkboxObj1.name = "Checkbox1"
		checkboxObj1.value = "true"
		objList.add(checkboxObj1)
		
		def checkboxObj2 = new Expando()
		checkboxObj2.name = "Checkbox2"
		checkboxObj2.value = "false"
		objList.add(checkboxObj2)
		
		def data = PdfMapper.mapProcessDataToPDFData(objList)
		
		data.each { key, value ->
			println key + " : " + value
		}
	}

}
