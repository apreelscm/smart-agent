package com.eservice.eumowy

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.enums.IdentityDocumentType
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.pdfmapper.representative.BeneficiariesMapper
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class BeneficiariesMapperTests extends ControllerUnitTestMixin {
    private ProcessCommand processCommand
    private BeneficiaryCommand beneficiaryCommand

    @Before
    void setUp() {
        beneficiaryCommand = mockCommandObject(BeneficiaryCommand)
        processCommand = mockCommandObject(ProcessCommand)
    }

    @Test
    void shouldNotCreateData() {
        //when
        Map data = new BeneficiariesMapper(processCommand).getDataForMapping()

        //then
        assertEquals(0, data.size())
    }

    @Test
    void shouldCreateDataForBeneficiary() {
        //given
        Map beneficiaryProperties = [imie: "Jan", nazwisko: "Kowalski", typDokumentu: IdentityDocumentType.PASSPORT,
                lokalizacjaKraj: "Daleko", adres: "JakisAdres", seriaNrDokumentu: "PL123", obywatelstwo: "Polskie",
                procentUdzialow: 59, posiadaAkceptanta: true, znaczaceUdzialy: true]

        //when
        CommandHelper.setProperties(beneficiaryCommand, beneficiaryProperties)
        processCommand.beneficiaries.add(beneficiaryCommand)
        Map data = new BeneficiariesMapper(processCommand).getDataForMapping()

        //then
        assertEquals(data["beneficjent1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["beneficjent1LokalizacjaDane"], ["Daleko"] as String[])
        assertEquals(data["beneficjent1Adres"], ["JakisAdres"] as String[])
        assertEquals(data["beneficjent1SeriaNrDokumentu"], ["PL123"] as String[])
        assertEquals(data["beneficjent1Obywatelstwo"], ["Polskie"] as String[])
        assertEquals(data["beneficjent1ProcentUdzialow"], [59] as String[])
        assertEquals(data["beneficjent1DowOsob"], [false, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1Paszport"], [true, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1PosiadaAkceptanta"], [true, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1KontrolujeAkceptanta"], [false, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1ZnaczaceUdzialy"], [true, "", "checkbox"] as String[])
    }
}
