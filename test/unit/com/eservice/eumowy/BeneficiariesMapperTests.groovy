package com.eservice.eumowy

import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.pdfmapper.representative.BeneficiariesMapper
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class BeneficiariesMapperTests {
    private Process process
    private Representative representative

    @Before
    void setUp() {
        process = new Process()
        process.processData = new HashSet<ProcessData>()
        process.representatives = new HashSet<Representative>()

        representative = new Representative(type: Representative.Type.BENEFICIARY)
    }

    @Test
    void shouldNotCreateData() {
        //when
        Map data = new BeneficiariesMapper(process).getDataForMapping()

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
        CommandHelper.setProperties(representative, beneficiaryProperties)
        process.representatives.add(representative)
        Map data = new BeneficiariesMapper(process).getDataForMapping()

        //then
        assertEquals(data["beneficjent1Nazwa"], ["Jan Kowalski"] as String[])
        assertEquals(data["beneficjent1LokalizacjaDane"], ["Daleko"] as String[])
        assertEquals(data["beneficjent1Adres"], ["JakisAdres"] as String[])
        assertEquals(data["beneficjent1SeriaNrDokumentu"], ["PL123"] as String[])
        assertEquals(data["beneficjent1Obywatelstwo"], ["Polskie"] as String[])
        assertEquals(data["beneficjent1ProcentUdzialow"], [59] as String[])
        assertEquals(data["beneficjent1DowOsob"], [true, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1Paszport"], [false, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1PosiadaAkceptanta"], [false, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1KontrolujeAkceptanta"], [true, "", "checkbox"] as String[])
        assertEquals(data["beneficjent1ZnaczaceUdzialy"], [false, "", "checkbox"] as String[])
    }
}
