package com.eservice.eumowy

import static org.easymock.EasyMock.*
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
        COMMAND = createMock(RepresentativeCommand.class)
        ERRORS = createMock(Errors.class)
        PROPERTYNAME = "whatever"
    }

    @Test
    public void shouldNotValidatePesels() {
        //given
        String peselNumber

        //when
        Boolean validationResult = NumberValidator.validatePesel(peselNumber, COMMAND, ERRORS, PROPERTYNAME)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldNotValidateShortPesel() {
        //given
        String peselNumber = "12345"

        //when
        Boolean validationResult = NumberValidator.validatePesel(peselNumber, COMMAND, ERRORS, PROPERTYNAME)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldNotValidateInvalidPesel() {
        //given
        String peselNumber = "12341234123"

        //when
        Boolean validationResult = NumberValidator.validatePesel(peselNumber, COMMAND, ERRORS, PROPERTYNAME)

        //then
        assertEquals(false, validationResult)
    }

    @Test
    public void shouldValidPesel() {
        //given
        String peselNumber = "91101706333"

        //when
        Boolean validationResult = NumberValidator.validatePesel(peselNumber, COMMAND, ERRORS, PROPERTYNAME)

        //then
        assertEquals(true, validationResult)
    }
}
