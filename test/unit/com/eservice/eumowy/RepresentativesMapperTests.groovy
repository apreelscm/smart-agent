package com.eservice.eumowy

import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.pdfmapper.representative.RepresentativesDetailsMapper
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class RepresentativesMapperTests {
    private Process process
    private Representative representative

    @Before
    void setUp() {
        process = new Process()
        process.processData = new HashSet<ProcessData>()
        process.representatives = new HashSet<Representative>()

        representative = new Representative(typ: Representative.Type.REPRESENTATIVE)
    }

    @Test
    void shouldNotCreateData() {
        //when
        Map data = new RepresentativesDetailsMapper(process).getDataForMapping()

        //then
        assertEquals(0, data.size())
    }

    @Test
    void shouldCreateDataForOsobaFizyczna() {
        //given
        Map representativeProperties = [imie: "Jan", nazwisko: "Kowalski",
                typDokumentu: IdentityDocumentType.PASSPORT, lokalizacjaKraj: "Daleko", adres: "JakisAdres",
                seriaNrDokumentu: "PL123", obywatelstwo: "Polskie"]

        //when
        process.processData.add(new ProcessData(name: 'dzialalnoscForma', value: 'spolka_cywilna'))
        CommandHelper.setProperties(representative, representativeProperties)
        process.representatives.add(representative)
        Map data = new RepresentativesDetailsMapper(process).getDataForMapping()
        
        //then
        assertEquals(data["osFiz_reprezentant1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["osFiz_reprezentant1LokalizacjaDane"], ["Daleko"] as String[])
        assertEquals(data["osFiz_reprezentant1DowOsob"], [true, "", "checkbox"] as String[])
        assertEquals(data["osFiz_reprezentant1Paszport"], [false, "", "checkbox"] as String[])
        assertEquals(data["osFiz_reprezentant1SeriaNrDokumentu"], ["PL123"] as String[])
        assertEquals(data["osFiz_reprezentant1Obywatelstwo"], ["Polskie"] as String[])
    }

    @Test
    void shouldCreateDataForOsobaPrawna() {
        //given
        Map representativeProperties = [imie: "Jan", nazwisko: "Kowalski",
                typDokumentu: IdentityDocumentType.IDENTITY_CARD, lokalizacjaKraj: "Daleko", adres: "JakisAdres",
                seriaNrDokumentu: "PL123", obywatelstwo: "Polskie", typLokalizacji: AcceptorLocation.ABROAD,
                lokalizacjaPesel: "91101706344"]

        //when
        process.processData.add(new ProcessData(name: 'dzialalnoscForma', value: 'spolka_zoo'))
        CommandHelper.setProperties(representative, representativeProperties)
        process.representatives.add(representative)
        Map data = new RepresentativesDetailsMapper(process).getDataForMapping()

        //then
        assertEquals(data["osPraw_reprezentant1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["osPraw_reprezentant1LokalizacjaDane"], ["91101706344"] as String[])
        assertEquals(data["osPraw_reprezentant1PozaRP"], [false, "", "checkbox"] as String[])
    }
}
