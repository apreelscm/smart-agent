package com.eservice.eumowy

import static org.mockito.Mockito.*;
import static org.junit.Assert.*

import com.eservice.eumowy.command.RepresentativeCommand
import com.eservice.eumowy.validator.NumberValidator
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.validation.Errors

class NumberValidatorTest{

    private static RepresentativeCommand COMMAND
    private static Errors ERRORS
    private static String PROPERTYNAME

    @BeforeClass
    public static void setup() {
        COMMAND = mock(RepresentativeCommand.class)
        ERRORS = mock(Errors.class)
        PROPERTYNAME = "whatever"
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

    private boolean validatePesel(String pesel) {
        return NumberValidator.validatePesel(pesel, COMMAND, ERRORS, PROPERTYNAME)
    }

    private boolean validateIsin(String isin) {
        return NumberValidator.validateIsin(isin, COMMAND, ERRORS, PROPERTYNAME)
    }


}
