package com.eservice.eumowy

import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.helpers.CommandHelpers
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest

import static org.junit.Assert.*
import static org.mockito.Mockito.*;

import com.eservice.eumowy.command.ProcessCommand
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before
import org.junit.Test


@PrepareForTest(CalculatorService.class)
class ProcessCommandTest extends ControllerUnitTestMixin{
    private ProcessCommand command
    private CalculatorService calculatorService = [getCalcProperty: {calc, key -> return null}] as CalculatorService

    @Before
    public void setUp() {
        command = mockCommandObject(ProcessCommand)

        command.calculatorService = calculatorService
        command.calc = []
    }

    @Test
    public void shouldNotValidateWithNullAkceptantLokalizacja() {
        //when
        Boolean validationResult = command.validate()

        //then
        assertEquals(false, validationResult)
        assertNotNull(command.errors['akceptantLokalizacja'])
    }

    @Test
    public void shouldValidateWithAkceptantLokalizacjaCountry() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.COUNTRY]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['czyBeneficjentRzeczywisty'])
    }

    @Test
    public void shouldNotValidateWitEmptyPropertiesRelatedToAkceptantLokalizacjaAbroad() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.ABROAD]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['czyBeneficjentRzeczywisty'])
        assertEquals(command.errors['czyBeneficjentRzeczywisty'].code, "beneficiary.radio.required")
    }

    @Test
    public void shouldNotValidateNotSelectedOptionWhenNotBeneficjentRzeczywisty() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.ABROAD, czyBeneficjentRzeczywisty: false]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['czyBeneficjentRzeczywisty'])
        assertEquals(command.errors['czyBeneficjentRzeczywisty'].code, "atleast.one.option.required")
    }

    @Test
    public void shouldValidateWhenOneOptionIsSelectedWhenNotBeneficjentRzeczywisty() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.ABROAD, czyBeneficjentRzeczywisty: false, akceptantJestSpolka: true]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['czyBeneficjentRzeczywisty'])
    }

    @Test
    public void shouldValidateWhenMoreOptionsIsSelectedWhenNotBeneficjentRzeczywisty() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.ABROAD, czyBeneficjentRzeczywisty: false,
                akceptantJestSpolka: true, akceptantJestPodmiotem: true, akceptantJestOrganem: true]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['czyBeneficjentRzeczywisty'])
    }

    @Test
    public void shouldValidateWhenBeneficjentRzeczywsity() {
        //given
        Map properties = [akceptantLokalizacja: AcceptorLocation.ABROAD, czyBeneficjentRzeczywisty: true]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['czyBeneficjentRzeczywisty'])
    }

    @Test
    public void shouldNotValidateWithEmptyPropertiesRelatedToAkceptantJestSpolka() {
        //given
        Map properties = [akceptantJestSpolka: true]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['nazwaGieldy'])
        assertNotNull(command.errors['isinAkceptanta'])
    }

    @Test
    public void shouldValidateRelatedPropertiesWhenNotAkceptantJestSpolka() {
        //given
        Map properties = [akceptantJestSpolka: false]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['nazwaGieldy'])
        assertNull(command.errors['isinAkceptanta'])
    }

    @Test
    public void shouldValidateWithFilledPropertiesRelatedToAkceptantJestSpolka() {
        //given
        Map properties = [akceptantJestSpolka: true, nazwaGieldy: "Gielda", isinAkceptanta: "US0378331005"]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['nazwaGieldy'])
        assertNull(command.errors['isinAkceptanta'])
    }

    private void setProperties(Map properties) {
        CommandHelpers.setProperties(command, properties)
    }

}
