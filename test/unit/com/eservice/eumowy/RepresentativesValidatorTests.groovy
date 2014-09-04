package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.validator.RepresentativesValidator
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.validation.Errors

import static org.mockito.Mockito.*;
import static org.junit.Assert.*

class RepresentativesValidatorTests extends ControllerUnitTestMixin {
    private ProcessCommand processCommand
    private List<RepresentativeCommand> representatives
    private static Errors ERRORS
    private static String PROPERTY_NAME

    @BeforeClass
    static void beforeClass() {
        ERRORS = mock(Errors.class)
        PROPERTY_NAME = "whatever"
    }

    @Before
    void setUp() {
        processCommand = mockCommandObject(ProcessCommand)
        representatives = []
    }

    @Test
    void shouldValidateEmptyRepresentatives() {
        //when
        Boolean validationResult = validateRepresentatives()
        assertEquals(true, validationResult)
    }

    @Test
    void shouldNotValidateEmptyRepresentative() {
        //given
        addRepresentative([:])

        //when
        Boolean validationResult = validateRepresentatives()

        //then
        assertEquals(false, validationResult)
    }

    private boolean validateRepresentatives() {
        RepresentativesValidator.validate(representatives, processCommand, ERRORS, PROPERTY_NAME)
    }

    private RepresentativeCommand addRepresentative(Map parameters) {
        RepresentativeCommand command = mockCommandObject(RepresentativeCommand)
        CommandHelper.setProperties(command, parameters)

        representatives.add(command)

        return command
    }
}
