package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.validator.NumberValidator
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.validation.Errors

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.mock

class NumberValidatorTests {
    private static ProcessCommand COMMAND
    private static Errors ERRORS
    private static String PROPERTYNAME

    @BeforeClass
    public static void setup() {
        ERRORS = mock(Errors.class)
        COMMAND = mock(ProcessCommand.class)
        PROPERTYNAME = "whatever"
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

    @Test
    public void shouldNotValidateNullPesel() {
        //given
        String peselNumber

        //when
        Boolean validationResult = validatePesel(peselNumber)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldNotValidateShortPesel() {
        //given
        String peselNumber = "12345"

        //when
        Boolean validationResult = validatePesel(peselNumber)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldNotValidateInvalidPesel() {
        //given
        String peselNumber = "12341234123"

        //when
        Boolean validationResult = validatePesel(peselNumber)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldValidPesel() {
        //given
        String peselNumber = "91101706333"

        //when
        Boolean validationResult = validatePesel(peselNumber)

        //then
        assertEquals(true, validationResult)
    }

    @Test
    public void shouldNotValidateEmptyIsin() {
        //given
        String isin

        //when
        Boolean validationResult = validateIsin(isin)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldNotValidateInvalidIsin() {
        //given
        String isin = "US594918133q"

        //when
        Boolean validationResult = validateIsin(isin)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldValidateIsin() {
        //given
        String isin = "US0378331005"

        //when
        Boolean validationResult = validateIsin(isin)

        //then
        assertEquals(true, validationResult)
    }

    @Test
    public void shouldValidateNip() {
        //given
        String nip = "5329767003"

        //then
        assertEquals(true, NumberValidator.validateNip(nip))
    }

    @Test
    public void shouldNotValidateEmptyNip() {
        //given
        String nip = ""

        //then
        assertEquals(false, NumberValidator.validateNip(nip))
    }

    @Test
    public void shouldNotValidateIncorrectNip() {
        //given
        String tooLongNip = "532976712003"
        String incorrectNip = "5329712003"

        //then
        assertEquals(false, NumberValidator.validateNip(tooLongNip))
        assertEquals(false, NumberValidator.validateNip(incorrectNip))
    }

    private boolean validateAccountNumber(String accountNumber) {
        return NumberValidator.accountNumber(accountNumber, COMMAND, ERRORS, "whatever")
    }

    private boolean validatePesel(String pesel) {
        return NumberValidator.validatePesel(pesel, COMMAND, ERRORS, PROPERTYNAME)
    }

    private boolean validateIsin(String isin) {
        return NumberValidator.validateIsin(isin, COMMAND, ERRORS, PROPERTYNAME)
    }
}
