package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand;

//import grails.test.mixin.*

import com.eservice.eumowy.pdfmapper.PdfMapper
import com.eservice.eumowy.util.DateUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.junit.Test

import javax.xml.bind.DatatypeConverter

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
		
		def data = PdfMapper.mapPointDataToPDFData(testObject, 1)
		
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

    @Test
    public void testReprezentant() {

        println "Test reprezentanta - begin"

        def objList = [
                new Expando(['name': 'reprezentant1Tytul', 'value': 'Pani']),
                new Expando(['name':'reprezentant1Imie', 'value': 'Zofia']),
                new Expando(['name':'reprezentant1Nazwisko', 'value': 'Nowak']),
                new Expando(['name':'reprezentant2Tytul', 'value': 'Pan']),
                new Expando(['name':'reprezentant2Imie', 'value': 'Adam']),
                new Expando(['name':'reprezentant2Nazwisko', 'value': 'Michnik']),
        ]

        def data = PdfMapper.mapProcessDataToPDFData(objList)

        data.each { key, value ->
            println key + " : " + value
        }
        println "Test reprezentanta - end"
    }

    @Test
    public void testNazwaOficjalna() {
        println "testNazwaOficjalna - begin"

        def objList = [
                new Expando(['name': 'akceptantNazwaOficjalna', 'value': 'KGHM Polska Miedź S.A.']),
                new Expando(['name': 'stanZadbany', 'value': 'true']),
                new Expando(['name': 'uslugiPlatneZGory', 'value': 'false'])
        ]

        def data = PdfMapper.mapProcessDataToPDFData(objList)

        data.each { key, value ->
            println key + " : " + value
        }
        println "testNazwaOficjalna - end"
    }

    @Test
    public void testUmowaCzas() {
        println "testUmowaCzas - begin"

        def objList = [
                new Expando(['name': 'umowaCzas', 'value': 'oznaczony'])
        ]

        def data = PdfMapper.mapProcessDataToPDFData(objList)

        data.each { key, value ->
            println key + " : " + value
        }
        println "testUmowaCzas - end"
    }

    @Test
    public void testStringNull() {

        def a = null;
        def b
        def c = ''
        def d = ''

        println 'a: ' + (a?.trim())?a?.trim():"NOT"
        println 'b: ' + (b?.trim())?b?.trim():"NOT"
        println 'c: ' + (c?.trim())?c?.trim():"NOT"
        println 'd: ' + (d?.trim())?d?.trim():"NOT"


        def result = (''?.trim())?true:false

        println 'result: ' + result

        boolean ala

        println "empty boolean" + ala

    }

    @Test
    public void testJodaTime() {

        println '-----------------------'
        def dateStr = '2013-09-03T00:00:00+0200'
        def dateStr2 = '2013-09-04T11:11:11+0200'
        def dateStr3 = '2013-09-05T00:20:00+0200'

        Date d1 = DateUtils.parseWithTimezone(dateStr);
        Date d2 = DateUtils.parseWithTimezone(dateStr2);
        Date d3 = DateUtils.parseWithTimezone(dateStr3);

        assert dateStr.equals(DateUtils.formatWithTimezone(d1))
        assert dateStr2.equals(DateUtils.formatWithTimezone(d2))
        assert dateStr3.equals(DateUtils.formatWithTimezone(d3))
        println '-----------------------'










        def current = new Date();

        println 'Test date: ' + current

        String dateString = "2010-03-01T00:00:00-08:00";
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);

        println 'String reprezentation: ' +  dtf.print(new DateTime(current.getTime()));

        DateTime dateTime = dtf.parseDateTime(dtf.print(new DateTime(current.getTime())));
        println 'Data po przeformatowaniu: ' + new Date(dateTime.getMillis());


//
//        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
//        String jtdate = "2010-01-01T12:00:00+01:00";
//        println parser2.parseDateTime(jtdate)
    }


    @Test
    public void testProcessCommand() {

        def pc = new ProcessCommand();
        pc.akceptantNazwaOficjalna = 'KGHM POLSKA MIEDZ SA'
        pc.akceptantNazwaOficjalnaCbd = 'KGHM POLSKA MIEDZ'

        pc.akceptantFax = ''
        pc.akceptantFaxCbd = ''

        pc.akceptantMiasto = null
        pc.akceptantMiastoCbd = null

        assert pc.isFromCbd('akceptantNazwaOficjalna')
        assert !pc.isFromCbd('akceptantFax')
        assert !pc.isFromCbd('akceptantMiasto')
        assert !pc.isFromCbd('dupaBlada')
        assert !pc.isFromCbd('nip')


    }
}
