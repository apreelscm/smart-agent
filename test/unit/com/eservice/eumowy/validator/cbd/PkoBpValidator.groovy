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
class PkoBpValidatorTest {
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
    public void shouldValidateWithTrilateralType() {
        //given
        PkoBpValidator validator = new PkoBpValidator(process)
        validator.cbdService = [getRodzajUmowy: {arg -> return "E"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWithoutExpectedType() {
        //given
        PkoBpValidator validator = new PkoBpValidator(process)
        validator.cbdService = [getRodzajUmowy: {arg -> return "DUNNO"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldValidateWithBilateralTypeAndProperActivity() {
        //given
        process.activities = Sets.newSet(new Activity(code: "wymianaUmowyZaplaty"))
        PkoBpValidator validator = new PkoBpValidator(process)
        validator.cbdService = [getRodzajUmowy: {arg -> return "P"}] as CbdService

        //then
        assertEquals(true, validator.isValid())
    }

    @Test
    public void shouldNotValidateWithoutProperActivity() {
        //given
        PkoBpValidator validator = new PkoBpValidator(process)
        validator.cbdService = [getRodzajUmowy: {arg -> return "P"}] as CbdService

        //then
        assertEquals(false, validator.isValid())
    }
}
