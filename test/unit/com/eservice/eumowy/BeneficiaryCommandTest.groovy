package com.eservice.eumowy

import com.eservice.eumowy.command.BeneficiaryCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.helpers.CommandHelpers
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
        assertEquals(command.errors['ownsAcceptor'].code, "atleast.one.relation.required")
    }

    @Test
    void shouldValidateWithTwoOptionsSelected() {
        //given
        Map properties = [ownsAcceptor: true, controlAcceptor: true]

        //when
        CommandHelpers.setProperties(command, properties)
        command.validate()

        //then
        assertNull(command.errors['ownsAcceptor'])
    }

    @Test
    void shouldNotValidateEmptyPercentOfVotes() {
        //given
        Map properties = [overQuarterOfVotes: true]

        //when
        CommandHelpers.setProperties(command, properties)
        command.validate()

        //then
        assertEquals(command.errors['percentOfVotes'].code, "beneficiary.percentOfVotes.required")
    }

    @Test
    void shouldNotValidateLowPercentOfVotes() {
        //given
        Map properties = [overQuarterOfVotes: true, percentOfVotes: 22]

        //when
        CommandHelpers.setProperties(command, properties)
        command.validate()

        //then
        assertEquals(command.errors['percentOfVotes'].code, "min.notmet")
    }

    @Test
    void shouldValidateFilledPercentOfVotes() {
        //given
        Map properties = [overQuarterOfVotes: true, percentOfVotes: 44]

        //when
        CommandHelpers.setProperties(command, properties)
        command.validate()

        //then
        assertNull(command.errors['percentOfVotes'])
    }
}
