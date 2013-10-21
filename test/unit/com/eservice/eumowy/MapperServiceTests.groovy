package com.eservice.eumowy

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MapperService)
class MapperServiceTests {

    Process process

    @Before
    void init(){
        process = new Process();
        process.processData = new HashSet<ProcessData>()
        process.phNumber = 12345;

    }

    void testMapProcessDataToPDFData() {

        for (int i = 0; i < 10; i++) {
            process.processData.add(new ProcessData(name: "NazwaPola"+i, value: "WartoscPola"+i));

        }
        process.processData.add(new ProcessData(name: "Checkbox1", value: "true"));
        process.processData.add(new ProcessData(name: "Checkbox2", value: "false"));

        def data = service.mapOnlyProcessData(process, -1);

        data.each { key, value ->
            println key + " : " + value
        }
    }

    public void testReprezentant() {
        println "Test reprezentanta - begin"

        process.processData.add(new ProcessData(name: 'reprezentant1Tytul', value: 'Pani'))
        process.processData.add(new ProcessData(name: 'reprezentant1Imie', value: 'Zofia'))
        process.processData.add(new ProcessData(name: 'reprezentant1Nazwisko', value: 'Nowak'))
        process.processData.add(new ProcessData(name: 'reprezentant2Tytul', value: 'Pan'))
        process.processData.add(new ProcessData(name: 'reprezentant2Imie', value: 'Adam'))
        process.processData.add(new ProcessData(name: 'dataAneksowanejUmowyPos', value: '2013-09-03T00:00:00+0200'))
        process.processData.add(new ProcessData(name: 'umowaOznOd', value: '2013-09-04T11:11:11+0200'))
        process.processData.add(new ProcessData(name: 'umowaOznDo', value: ''))
        process.processData.add(new ProcessData(name: 'dataUmowy', value: '2013-09-05T00:20:00+0200'))

        def data = service.mapOnlyProcessData(process, -1);

        data.each { key, value ->
            println key + " : " + value
        }
        println "Test reprezentanta - end"
    }

    @Test
    public void testNazwaOficjalna() {
        println "testNazwaOficjalna - begin"

        process.processData.add(new ProcessData(name: 'akceptantNazwaOficjalna', value: 'KGHM Polska Miedź S.A.'))
        process.processData.add(new ProcessData(name: 'stanZadbany', value: 'true'))
        process.processData.add(new ProcessData(name: 'uslugiPlatneZGory', value: 'false'))

        def data = service.mapOnlyProcessData(process, -1);

        data.each { key, value ->
            println key + " : " + value
        }
        println "testNazwaOficjalna - end"
    }

    @Test
    public void testUmowaCzas() {
        println "testUmowaCzas - begin"

        process.processData.add(new ProcessData(name: 'umowaCzas', value: 'oznaczony'))

        def data = service.mapOnlyProcessData(process, -1);

        data.each { key, value ->
            println key + " : " + value
        }
        println "testUmowaCzas - end"
    }

    @Test
    public void testSelectedPoints() {

        PointDataDetails pdd = new PointDataDetails(nazwaDoWydrukuZTerminalaPos: 'Sklep wielobranzowy', wydrukNrDomu: '34', wydrukNrLokalu: '23g', wydrukMiasto: 'Siedlce', wydrukKodPocztowy: '00-123', wydrukUlica: 'Zielona');
        PointData pd = new PointData(nazwa: 'A', czyWybranyZakresUruchomienia: true, czyWybranyAkceptacjaKart: false, systemKasowy: true, uta: true, pointDetails: pdd)

        HashMap<String, String[]> data = service.mapOnlyPointData(pd);

        data.each { key, value ->
            println key + ' -----> ' + value
        }
    }
}
