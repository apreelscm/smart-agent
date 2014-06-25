package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.helpers.CommandHelpers
import com.eservice.eumowy.pdfmapper.representative.RepresentativesMapper
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class RepresentativesMapperTests extends ControllerUnitTestMixin {
    private ProcessCommand processCommand
    private RepresentativeCommand representativeCommand

    @Before
    void setUp() {
        representativeCommand = mockCommandObject(RepresentativeCommand)
        processCommand = mockCommandObject(ProcessCommand)
    }

    @Test
    void shouldNotCreateData() {
        //when
        Map data = new RepresentativesMapper(processCommand).getDataForMapping()

        //then
        assertEquals(0, data.size())
    }

    @Test
    void shouldCreateDataForOsobaFizyczna() {
        //given
        Map processProperties = [dzialalnoscForma: "spolka_cywilna"]
        Map beneficiaryProperties = [imie: "Jan", nazwisko: "Kowalski", typDokumentu: IdentityDocumentType.PASSPORT,
                lokalizacjaKraj: "Daleko", adres: "JakisAdres", seriaNrDokumentu: "PL123", obywatelstwo: "Polskie"]

        //when
        CommandHelpers.setProperties(processCommand, processProperties)
        CommandHelpers.setProperties(representativeCommand, beneficiaryProperties)
        processCommand.representatives.add(representativeCommand)
        Map data = new RepresentativesMapper(processCommand).getDataForMapping()
        
        //then
        assertEquals(data["osFiz_reprezentant1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["osFiz_reprezentant1LokalizacjaDane"], ["Daleko"] as String[])
        assertEquals(data["osFiz_reprezentant1DowOsob"], [false, "", "checkbox"] as String[])
        assertEquals(data["osFiz_reprezentant1Paszport"], [true, "", "checkbox"] as String[])
        assertEquals(data["osFiz_reprezentant1SeriaNrDokumentu"], ["PL123"] as String[])
        assertEquals(data["osFiz_reprezentant1Obywatelstwo"], ["Polskie"] as String[])
    }

    @Test
    void shouldCreateDataForNotOsobaFizyczna() {
        //given
        Map processProperties = [dzialalnoscForma: "spolka_zoo"]
        Map beneficiaryProperties = [imie: "Jan", nazwisko: "Kowalski", typDokumentu: IdentityDocumentType.IDENTITY_CARD,
                lokalizacjaKraj: "Daleko", adres: "JakisAdres", seriaNrDokumentu: "PL123", obywatelstwo: "Polskie",
                typLokalizacji: AcceptorLocation.ABROAD, lokalizacjaPesel: "91101706344"]

        //when
        CommandHelpers.setProperties(processCommand, processProperties)
        CommandHelpers.setProperties(representativeCommand, beneficiaryProperties)
        processCommand.representatives.add(representativeCommand)
        Map data = new RepresentativesMapper(processCommand).getDataForMapping()

        //then
        assertEquals(data["osPraw_reprezentant1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["osPraw_reprezentant1LokalizacjaDane"], ["91101706344"] as String[])
        assertEquals(data["osPraw_reprezentant1PozaRP"], [true, "", "checkbox"] as String[])
    }
}
