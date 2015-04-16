package com.eservice.eumowy.validator.cbd

import com.eservice.eumowy.Activity
import com.eservice.eumowy.CbdService
import com.eservice.eumowy.Client
import com.eservice.eumowy.Process
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
class PrepaidValidatorTest {
    Process process

    @Before
    public void setup() {
        Holders.grailsApplication = grailsApplication
        defineBeans {
            cbdService(CbdService)
        }
        Client client = new Client(cbdId: 1, nip: "1", mid: "1")
        process = new Process(client: client)
    }

    @Test
    public void shouldValidateWithCooperationType() {
        //given
        PrepaidValidator validator = new PrepaidValidator(process)
        validator.cbdService = [getUmwTyp: {arg -> return "WUN"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWithoutExpectedType() {
        //given
        PrepaidValidator validator = new PrepaidValidator(process)
        validator.cbdService = [getUmwTyp: {arg -> return "DUNNO"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWithLeaseTypeAndWithoutPrepaidActivity() {
        //given
        process.activities = Sets.newSet(new Activity(code: "wymianaUmowyNajmu"))
        PrepaidValidator validator = new PrepaidValidator(process)
        validator.cbdService = [getUmwTyp: {arg -> return "UNA"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWithoutPrepaidActivity() {
        //given
        PrepaidValidator validator = new PrepaidValidator(process)
        validator.cbdService = [getUmwTyp: {arg -> return "UNA"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }
}
