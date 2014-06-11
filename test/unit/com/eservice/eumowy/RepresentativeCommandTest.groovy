package com.eservice.eumowy

import com.eservice.eumowy.enums.LocationType
import org.junit.Test

import static org.junit.Assert.*

import com.eservice.eumowy.command.RepresentativeCommand
import grails.test.mixin.web.ControllerUnitTestMixin
import org.junit.Before


class RepresentativeCommandTest extends ControllerUnitTestMixin {
    private RepresentativeCommand command

    @Before
    void setUp() {
        command = mockCommandObject(RepresentativeCommand)
    }

    @Test
    void shouldNotValidateEmptyCommand() {
        //then
        assertEquals(false, command.validate())
    }

    @Test
    void shouldNotValidatePesel() {
        //given
        Map properties = [pesel: "12345"]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['pesel'])
    }

    @Test
    void shouldNotValidateEmptyPropertiesRelatedToAbroadLocationType() {
        //given
        Map properties = [typLokalizacji: LocationType.ABROAD]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['lokalizacjaKraj'])
        assertNotNull(command.errors['dataUrodzenia'])
        assertNotNull(command.errors['czyStanowiskoPolityczne'])
    }

    @Test
    void shouldValidateFilledPropertiesRelatedToAbroadLocationType() {
        //given
        Map properties = [typLokalizacji: LocationType.ABROAD, lokalizacjaKraj: "Norwegia", dataUrodzenia: "2014-06-20",
                          czyStanowiskoPolityczne: true]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['lokalizacjaKraj'])
        assertNull(command.errors['dataUrodzenia'])
        assertNull(command.errors['czyStanowiskoPolityczne'])
    }

    @Test
    void shouldNotValidateEmptyPropertiesRelatedToCountryLocationType() {
        //given
        Map properties = [typLokalizacji: LocationType.COUNTRY]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNotNull(command.errors['lokalizacjaPesel'])
    }

    @Test
    void shouldValidateFilledPropertiesRelatedToCountryLocationType() {
        //given
        Map properties = [typLokalizacji: LocationType.COUNTRY, lokalizacjaPesel: "66666666666"]

        //when
        setProperties(properties)
        command.validate()

        //then
        assertNull(command.errors['lokalizacjaPesel'])

    }

    private void setProperties(Map properties) {
        properties.each {
            command[it.key] = it.value
        }
    }

}
