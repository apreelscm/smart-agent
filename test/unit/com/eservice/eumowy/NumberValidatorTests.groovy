package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.validator.NumberValidator
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.validation.Errors

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.mock

class NumberValidatorTests {
    private static ProcessCommand COMMAND
    private static Errors ERRORS

    @BeforeClass
    public static void setup() {
        ERRORS = mock(Errors.class)
        COMMAND = mock(ProcessCommand.class)
    }

    @Test
    public void shouldNotValidateNullAccountNumber() {
        //given
        String accountNumber

        //when
        Boolean validationResult = validateAccountNumber(accountNumber)

        //then
        assertFalse(validationResult)
    }

    @Test
    public void shouldNotValidateIncorrectAccountNumbers() {
        //given
        String nullAccountNumber
        String incorrectAccountNUmber = "23 1020 3974 0000 5602 0213 4444"
        String tooLongAccountNumber = "23 1020 3974 0000 5602 0213 444455 323"

        //when
        Boolean nullAccountNumberValidationResult = validateAccountNumber(nullAccountNumber)
        Boolean incorrectAccountNumbervalidationResult = validateAccountNumber(incorrectAccountNUmber)
        Boolean tooLongAccountNumbervalidationResult = validateAccountNumber(tooLongAccountNumber)

        //then
        assertFalse(nullAccountNumberValidationResult)
        assertFalse(incorrectAccountNumbervalidationResult)
        assertFalse(tooLongAccountNumbervalidationResult)
    }

    @Test
    public void shouldValidateAccountNumber() {
        //given
        String accountNumber = "23 1020 3974 0000 5602 0213 3205"

        //when
        Boolean validationResult = validateAccountNumber(accountNumber)

        //then
        assertTrue(validationResult)
    }

    private boolean validateAccountNumber(String accountNumber) {
        return NumberValidator.accountNumber(accountNumber, COMMAND, ERRORS, "whatever")
    }
}
