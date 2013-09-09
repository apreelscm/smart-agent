package com.eservice.eumowy

import com.eservice.eumowy.util.DateUtils

import org.junit.Test
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.pdfmapper.PdfMapper

class PdfMapperTest {

//	@Test
//	public void testDynamicMethods() {
//        //TEST bez sensu - nie ma takich pol
//		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapNrMerchanta")
//		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapUlicaDoKorespondencjiTyp")
//		assert PdfMapper.metaClass.respondsTo(PdfMapper, "mapUlicaDoKorespondencji")
//	}
//
//	@Test
//	public void testMapPointDataToPDFData() {
//        //TEST bez sensu - nie ma takich pol
//		def testObject = new Expando()
//		testObject.nrMerchanta = "123456789098766"
//		testObject.ulicaDoKorespondencjiTyp = "al."
//		testObject.ulicaDoKorespondencji = "Jerozolimskie"
//
//		def data = PdfMapper.mapPointDataToPDFData(testObject, 1)
//
//		data.each { key, value ->
//			println key + " : " + value
//		}
//	}
	
	@Test
	public void testMapProcessDataToPDFData() {
		def objList = []

		for (int i = 0; i < 10; i++) {
            objList.add(new ProcessData(name: "NazwaPola"+i, value: "WartoscPola"+i));

		}
        objList.add(new ProcessData(name: "Checkbox1", value: "true"));
        objList.add(new ProcessData(name: "Checkbox2", value: "false"));

        def data =new PdfMapper().mapAllDataToPDFData(objList, new HashSet<PointData>());

		data.each { key, value ->
			println key + " : " + value
		}
	}

    @Test
    public void testReprezentant() {
        println "Test reprezentanta - begin"

        Set<ProcessData> processDatas = new HashSet<ProcessData>();
        processDatas.add(new ProcessData(name: 'reprezentant1Tytul', value: 'Pani'))
        processDatas.add(new ProcessData(name: 'reprezentant1Imie', value: 'Zofia'))
        processDatas.add(new ProcessData(name: 'reprezentant1Nazwisko', value: 'Nowak'))
        processDatas.add(new ProcessData(name: 'reprezentant2Tytul', value: 'Pan'))
        processDatas.add(new ProcessData(name: 'reprezentant2Imie', value: 'Adam'))
        processDatas.add(new ProcessData(name: 'reprezentant2Nazwisko', value: 'Michnik'))

        def data =new PdfMapper().mapAllDataToPDFData(processDatas, new HashSet<PointData>());

        data.each { key, value ->
            println key + " : " + value
        }
        println "Test reprezentanta - end"
    }

    @Test
    public void testNazwaOficjalna() {
        println "testNazwaOficjalna - begin"

        Set<ProcessData> processDatas = new HashSet<ProcessData>();
        processDatas.add(new ProcessData(name: 'akceptantNazwaOficjalna', value: 'KGHM Polska Miedź S.A.'))
        processDatas.add(new ProcessData(name: 'stanZadbany', value: 'true'))
        processDatas.add(new ProcessData(name: 'uslugiPlatneZGory', value: 'false'))

        def data = new PdfMapper().mapAllDataToPDFData(processDatas, new HashSet<PointData>());

        data.each { key, value ->
            println key + " : " + value
        }
        println "testNazwaOficjalna - end"
    }

    @Test
    public void testUmowaCzas() {
        println "testUmowaCzas - begin"

        Set<ProcessData> processDatas = new HashSet<ProcessData>();
        processDatas.add(new ProcessData(name: 'umowaCzas', value: 'oznaczony'))

        def data = new PdfMapper().mapAllDataToPDFData(processDatas, new HashSet<PointData>());

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
    public void testSelectedPoints() {

        PointDataDetails pdd = new PointDataDetails(nazwaDoWydrukuZTerminalaPos: 'Sklep wielobranzowy', wydrukNrDomu: '34', wydrukNrLokalu: '23g', wydrukMiasto: 'Siedlce', wydrukKodPocztowy: '00-123', wydrukUlica: 'Zielona');
        PointDataDetails pdd2 = new PointDataDetails(nazwaDoWydrukuZTerminalaPos: 'Kwiaciarnia', wydrukNrDomu: '5', wydrukNrLokalu: '1', wydrukMiasto: 'Warszawa', wydrukKodPocztowy: '00-123', wydrukUlica: 'Kwiatowa');

        Set<PointData> pointDatas = new HashSet<PointData>();
        pointDatas.add(new PointData(nazwa: 'A', czyWybranyZakresUruchomienia: true, czyWybranyAkceptacjaKart: false, tytulPlatnosci: true, systemKasowy: true, uta: true, pointDetails: pdd))

        HashMap<String, String[]> data = new PdfMapper().mapAllDataToPDFData(new HashSet<ProcessData>(), pointDatas);

        data.each { key, value ->
            println key + ' -----> ' + value
        }
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
