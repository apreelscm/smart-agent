package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.Activity
import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
import com.google.common.collect.Lists
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.util.Holders
import org.junit.Before
import org.junit.Test
import org.mockito.internal.util.collections.Sets


@TestMixin(GrailsUnitTestMixin)
class DccTerminalValidatorTest {
    Process process
    List calc

    @Before
    public void setup() {
        Holders.grailsApplication = grailsApplication
        defineBeans {
            cbdService(CbdService)
        }
        Client client = new Client(cbdId: 1, nip: "1", mid: "1")
        process = new Process(client: client)
        calc = Lists.newArrayList()
    }

    @Test
    public void shouldNotValidate() {
        //given
        process.activities = Sets.newSet(new Activity(code: "wymianaUmowyZaplaty"))
        addToCalc("DCC_A", "TAK")
        DccTerminalValidator validator = new DccTerminalValidator(process, calc)
        validator.cbdService = [czyTerminalDcc: {arg -> return true}] as CbdService

        //then
        assertEquals(false, validator.isValid())
    }

    @Test
    public void shouldValidate() {
        DccTerminalValidator validator = new DccTerminalValidator(process, calc)
        validator.cbdService = [czyTerminalDcc: {arg -> return true}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    private void addToCalc(String key, String value) {
        calc.add([POLEAPREEL: key, WARTOSCAPREEL: value])
    }
}
