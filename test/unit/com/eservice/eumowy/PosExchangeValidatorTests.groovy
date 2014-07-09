package com.eservice.eumowy

import com.eservice.eumowy.command.PosExchangeCommand
import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.helpers.CommandHelper
import com.eservice.eumowy.validator.PosExchangeValidator
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.validation.Errors

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.mock


class PosExchangeValidatorTests {
    private static ProcessCommand COMMAND
    private static Errors ERRORS

    @BeforeClass
    public static void setup() {
        ERRORS = mock(Errors.class)
        COMMAND = mock(ProcessCommand.class)
    }

    @Test
    public void shouldValidateEmptyPosExchanges() {
        //given
        List<PosExchangeCommand> posExchangeCommands = []

        //when
        Boolean validationResult = validatePosExchanges(posExchangeCommands)

        //then
        assertTrue(validationResult)
    }

    @Test
    public void shouldNotValidateNotChosenPosExchanges() {
        //given
        List<PosExchangeCommand> posExchangeCommands = []

        PosExchangeCommand posExchangeCommand1 = new PosExchangeCommand(isChoosen: false)
        PosExchangeCommand posExchangeCommand2 = new PosExchangeCommand()

        posExchangeCommands.add(posExchangeCommand1)
        posExchangeCommands.add(posExchangeCommand2)

        //when
        Boolean validationResult = validatePosExchanges(posExchangeCommands)

        //then
        assertFalse(validationResult)
    }

    @Test
    public void shouldValidateChosenPosExchange() {
        //given
        List<PosExchangeCommand> posExchangeCommands = []

        PosExchangeCommand posExchangeCommand1 = new PosExchangeCommand(isChoosen: false)
        PosExchangeCommand posExchangeCommand2 = new PosExchangeCommand(isChoosen: true)

        posExchangeCommands.add(posExchangeCommand1)
        posExchangeCommands.add(posExchangeCommand2)

        //when
        Boolean validationResult = validatePosExchanges(posExchangeCommands)

        //then
        assertTrue(validationResult)
    }

    private boolean validatePosExchanges(List<PosExchangeCommand> posExchanges) {
        return PosExchangeValidator.validate(posExchanges, COMMAND, ERRORS)
    }
}
