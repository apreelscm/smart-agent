package com.eservice.eumowy

import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.pdfmapper.PEPdeclarationMapper
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class PEPdeclarationMapperTest {
    private Process process
    private Representative representative

    @Before
    void setUp() {
        process = new Process()
        process.processData = new HashSet<ProcessData>()
        process.representatives = new ArrayList<Representative>()

        representative = new Representative(type: Representative.Type.REPRESENTATIVE)
    }

    @Test
    void shouldCreateData() {
        //given
        Map representativeProperties = [czyStanowiskoPolityczne: true]

        //when
        process.processData.add(new ProcessData(name: 'nip', value: '1234'))
        process.processData.add(new ProcessData(name: 'akceptantNazwaOficjalna', value: 'NAZWA'))
        process.processData.add(new ProcessData(name: 'akceptantUlicaTytul', value: 'UL'))
        process.processData.add(new ProcessData(name: 'akceptantUlica', value: 'JAKASTAM'))
        process.processData.add(new ProcessData(name: 'akceptantNrDomu', value: '15'))
        process.processData.add(new ProcessData(name: 'akceptantMiasto', value: 'WARSZAWA'))
        process.processData.add(new ProcessData(name: 'akceptantKodPocztowy', value: '02-222'))
        process.processData.add(new ProcessData(name: 'akceptantPoczta', value: 'WARSZAWA'))

        CommandHelper.setProperties(representative, representativeProperties)
        process.representatives.add(representative)

        Map data = new PEPdeclarationMapper(process, representative).getDataForMapping()

        //then
        assertEquals(data["nip"], ["1234"] as String[])
        assertEquals(data["akceptantNazwaOficjalna"], ["NAZWA"] as String[])
        assertEquals(data["siedzibaAkceptanta"], ["UL JAKASTAM 15 WARSZAWA 02-222 WARSZAWA"] as String[])
        assertEquals(data["PEP_true"], [false, "", "checkbox"] as String[])
        assertEquals(data["PEP_false"], [true, "", "checkbox"] as String[])
    }
}
