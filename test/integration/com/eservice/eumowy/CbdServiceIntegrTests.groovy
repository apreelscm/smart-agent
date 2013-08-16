package com.eservice.eumowy
import org.junit.After
import org.junit.Before
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
    void findClientIdByNip() {
        def result = cbdService.findClientIdByNip(nip)
        assert result != null
    }

    @Test
    void getAdresDaneDoWydrukuTest() {
        def result = cbdService.getAdresDaneDoWydruku(nip)
        assert result != null
    }

     @Test
    void getAdresDoKorespondencjiTest() {
        def result = cbdService.getAdresDoKorespondencji(nip)
        assert result != null
    }

    @Test
    void getAdresDoKorespondencjizAkceptantemTest() {
        def result = cbdService.getAdresDoKorespondencjizAkceptantem(nip)
        assert result != null
    }


   @Test
    void getDaneAkceptantaTest() {
        def result = cbdService.getDaneAkceptanta(nip)
        assert result != null
    }


   @Test
    void getNazwaBankuTest() {
        def result = cbdService.getNazwaBanku(nip)
        assert result != null
    }


   @Test
    void getNumerRachunkuBankowegoTest() {
        def result = cbdService.getNumerRachunkuBankowego(nip)
        assert result != null
    }


   @Test
    void getOsoba1UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdService.getOsoba1UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Test
    void getOsoba2UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdService.getOsoba2UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Test
    void getOsobaKtoraPozyskalaAkceptantaTest() {
        def result = cbdService.getOsobaKtoraPozyskalaAkceptanta(nip)
        assert result != null
    }

   @Test
    void getPromocyjneObinzenieOplatGridTest() {
        def result = cbdService.getPromocyjneObinzenieOplatGrid(nip)
        assert result != null
    }

   @Test
    void getOsobaDoKontaktuTest() {
        def result = cbdService.getOsobaDoKontaktu(nip)
        assert result != null
    }

   @Test
    void getWykazPunktowGridTest() {
        def result = cbdService.getWykazPunktowGrid(nip)
        assert result != null
    }

   @Test
    void getZakresUruchomieniaPunktyGridTest() {
        def result = cbdService.getZakresUruchomieniaPunktyGrid(nip)
        assert result != null
    }
}