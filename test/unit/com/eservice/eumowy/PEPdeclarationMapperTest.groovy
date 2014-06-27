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
        process.representatives = new HashSet<Representative>()

        representative = new Representative(typ: Representative.Type.REPRESENTATIVE)
    }

    @Test
    void shouldCreateData() {
        //given
        Map representativeProperties = [czyStanowiskoPolityczne: true]

        //when
        process.processData.add(new ProcessData(name: 'nip', value: '1234'))
        process.processData.add(new ProcessData(name: 'akceptantNazwaOficjalna', value: 'NAZWA'))
        process.processData.add(new ProcessData(name: 'akceptantAdres', value: 'ADRES'))

        CommandHelper.setProperties(representative, representativeProperties)
        process.representatives.add(representative)

        Map data = new PEPdeclarationMapper(process, representative).getDataForMapping()

        //then
        assertEquals(data["nip"], ["1234"] as String[])
        assertEquals(data["akceptantNazwaOficjalna"], ["NAZWA"] as String[])
        assertEquals(data["akceptantAdres"], ["ADRES"] as String[])
        assertEquals(data["PEP_true"], [false, "", "checkbox"] as String[])
        assertEquals(data["PEP_false"], [true, "", "checkbox"] as String[])
    }
}
