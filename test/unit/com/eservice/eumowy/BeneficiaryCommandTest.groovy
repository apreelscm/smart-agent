package com.eservice.eumowy

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.helpers.CommandHelper
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

class BeneficiaryCommandTest extends ControllerUnitTestMixin {
    private BeneficiaryCommand command

    @Before
    void setup() {
        command = mockCommandObject(BeneficiaryCommand)
        command.processCommand = mockCommandObject(ProcessCommand)
    }

    @Test
    void shouldNotValidateEmptyCommand() {
        //when
        Boolean validationResult = command.validate()

        //then
        assertEquals(false, validationResult)
    }

    @Test
    void shouldNotValidateWithoutOptionSelected() {
        //when
        command.validate()

        //then
        assertEquals(command.errors['posiadaAkceptanta'].code, "atleast.one.relation.required")
    }

    @Test
    void shouldValidateWithTwoOptionsSelected() {
        //given
        Map properties = [posiadaAkceptanta: true, kontrolujeAkceptanta: true]

        //when
        CommandHelper.setProperties(command, properties)
        command.validate()

        //then
        assertNull(command.errors['posiadaAkceptanta'])
    }

    @Test
    void shouldNotValidateEmptyPercentOfVotes() {
        //given
        Map properties = [znaczaceUdzialy: true]

        //when
        CommandHelper.setProperties(command, properties)
        command.validate()

        //then
        assertEquals(command.errors['procentUdzialow'].code, "beneficiary.percentOfVotes.required")
    }

    @Test
    void shouldNotValidateLowPercentOfVotes() {
        //given
        Map properties = [znaczaceUdzialy: true, procentUdzialow: 22]

        //when
        CommandHelper.setProperties(command, properties)
        command.validate()

        //then
        assertEquals(command.errors['procentUdzialow'].code, "min.notmet")
    }

    @Test
    void shouldValidateFilledPercentOfVotes() {
        //given
        Map properties = [znaczaceUdzialy: true, procentUdzialow: 44]

        //when
        CommandHelper.setProperties(command, properties)
        command.validate()

        //then
        assertNull(command.errors['procentUdzialow'])
    }
}
