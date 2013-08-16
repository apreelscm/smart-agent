package com.eservice.eumowy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class  CbdServiceIntegrTests {

    Logger log = LoggerFactory.getLogger(CbdServiceIntegrTests)
    def cbdDAOService

    def nip = '1022034800';
    
    @Before
    void setUp() {
        // Setup logic here
    }


    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void getAdresDaneDoWydrukuTest() {
        def result = cbdDAOService.getAdresDaneDoWydruku(nip)
        assert result != null
    }


     @Test
    void getAdresDoKorespondencjiTest() {
        def result = cbdDAOService.getAdresDoKorespondencji(nip)
        assert result != null
    }

    @Test
    void getAdresDoKorespondencjizAkceptantemTest() {
        def result = cbdDAOService.getAdresDoKorespondencjizAkceptantem(nip)
        assert result != null
    }


   @Test
    void getDaneAkceptantaTest() {
        def result = cbdDAOService.getDaneAkceptanta(nip)
        assert result != null
    }


   @Test
    void getNazwaBankuTest() {
        def result = cbdDAOService.getNazwaBanku(nip)
        assert result != null
    }


   @Test
    void getNumerRachunkuBankowegoTest() {
        def result = cbdDAOService.getNumerRachunkuBankowego(nip)
        assert result != null
    }


   @Test
    void getOsoba1UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdDAOService.getOsoba1UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Test
    void getOsoba2UprawnionaDoPodpisaniaUmowyTest() {
        def result = cbdDAOService.getOsoba2UprawnionaDoPodpisaniaUmowy(nip)
        assert result != null
    }


   @Test
    void getOsobaKtoraPozyskalaAkceptantaTest() {
        def result = cbdDAOService.getOsobaKtoraPozyskalaAkceptanta(nip)
        assert result != null
    }

   @Test
    void getPromocyjneObinzenieOplatGridTest() {
        def result = cbdDAOService.getPromocyjneObinzenieOplatGrid(nip)
        assert result != null
    }

   @Test
    void getOsobaDoKontaktuTest() {
        def result = cbdDAOService.getOsobaDoKontaktu(nip)
        assert result != null
    }

   @Test
    void getWykazPunktowGridTest() {
        def result = cbdDAOService.getWykazPunktowGrid(nip)
        assert result != null
    }

   @Test
    void getZakresUruchomieniaPunktyGridTest() {
        def result = cbdDAOService.getZakresUruchomieniaPunktyGrid(nip)
        assert result != null
    }
}