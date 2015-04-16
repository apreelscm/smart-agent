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

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals

@TestMixin(GrailsUnitTestMixin)
class CashbackTerminalValidatorTest {
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
        process.activities = Sets.newSet(new Activity(code: "aneks"))
        CashbackTerminalValidator validator = new CashbackTerminalValidator(process, calc)
        addToCalc("CASHBACK_A", "TAK")
        validator.cbdService = [czyTermialCashback: {arg -> return true}] as CbdService

        //then
        assertEquals(false, validator.isValid())
    }

    @Test
    public void shouldValidateWhenWrongActivity() {
        //given
        CashbackTerminalValidator validator = new CashbackTerminalValidator(process, calc)
        addToCalc("CASHBACK_A", "TAK")
        validator.cbdService = [czyTermialCashback: {arg -> return true}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWhenNoPropertyInCalc() {
        //given
        process.activities = Sets.newSet(new Activity(code: "aneks"))
        CashbackTerminalValidator validator = new CashbackTerminalValidator(process, calc)
        validator.cbdService = [czyTermialCashback: {arg -> return true}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWhenNotCashbackTerminal() {
        //given
        process.activities = Sets.newSet(new Activity(code: "aneks"))
        addToCalc("CASHBACK_A", "TAK")
        CashbackTerminalValidator validator = new CashbackTerminalValidator(process, calc)
        validator.cbdService = [czyTermialCashback: {arg -> return false}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    private void addToCalc(String key, String value) {
        calc.add([POLEAPREEL: key, WARTOSCAPREEL: value])
    }
}
