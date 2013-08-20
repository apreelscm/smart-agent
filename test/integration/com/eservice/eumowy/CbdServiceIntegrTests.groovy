package com.eservice.eumowy
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class  CbdServiceIntegrTests {

    Logger log = LoggerFactory.getLogger(CbdServiceIntegrTests)
    def cbdService

    def nip = '8356944170';
    
    @Before
    void setUp() {
        // Setup logic here
    }


    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void findCalcByNip() {
        def result = cbdService.findCalculatorByNip(nip)
        assert result != null
    }

    @Ignore
    void findClientIdByNip() {
        def result = cbdService.findClientIdByNip(nip)
        assert result != null
    }

    @Ignore
    void getAdresDaneDoWydrukuTest() {
        def result = cbdService.getAdresDaneDoWydruku(nip)
        assert result != null
    }

     @Ignore
    void getAdresDoKorespondencjiTest() {
        def result = cbdService.getAdresDoKorespondencji(nip)
        assert result != null
    }

    @Ignore
    void getAdresDoKorespondencjizAkceptantemTest() {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(nip)
        assert result != null
    }


   @Ignore
    void getDaneAkceptantaTest() {
        def result = cbdService.getDaneAkceptanta(nip)
        assert result != null
    }


   @Ignore
    void getNazwaBankuTest() {
        def result = cbdService.getNazwaBanku(nip)
        assert result != null
    }


   @Ignore
    void getNumerRachunkuBankowegoTest() {
        def result = cbdService.getNumerRachunkuBankowego(nip)
        assert result != null
    }


   @Ignore
    void getOsoba1UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdService.getOsoba1UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Ignore
    void getOsoba2UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdService.getOsoba2UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Ignore
    void getOsobaKtoraPozyskalaAkceptantaTest() {
        def result = cbdService.getOsobaKtoraPozyskalaAkceptanta(nip)
        assert result != null
    }

   @Ignore
    void getPromocyjneObinzenieOplatGridTest() {
        def result = cbdService.getPromocyjneObinzenieOplatGrid(nip)
        assert result != null
    }

   @Ignore
    void getOsobaDoKontaktuTest() {
        def result = cbdService.getOsobaDoKontaktu(nip)
        assert result != null
    }

   @Ignore
    void getWykazPunktowGridTest() {
        def result = cbdService.getWykazPunktowGrid(nip)
        assert result != null
    }

   @Ignore
    void getZakresUruchomieniaPunktyGridTest() {
        def result = cbdService.getZakresUruchomieniaPunktyGrid(nip)
        assert result != null
    }
}