package com.eservice.eumowy

import com.eservice.eumowy.command.ProcessCommand
import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.helpers.CommandHelper
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
        command.processCommand = mockCommandObject(ProcessCommand)
    }

    @Test
    void shouldNotValidateEmptyCommand() {
        //then
        assertEquals(false, command.validate())
    }

    @Test
    void shouldValidatePeselWithAkceptantLokalizacjaCountry() {
        //given
        Map properties = [pesel: "91101706333"]

        //when
        setProperties(properties)
        CommandHelper.setProperties(command.processCommand, [akceptantLokalizacja: AcceptorLocation.COUNTRY])
        command.validate()

        //then
        assertEquals(command.errors.errorCount, 0)
        assertNull(command.errors['pesel'])
    }

    //from now on AKCEPTANT_LOKALIZACJA = ABROAD
    @Test
    void shouldNotValidateEmptyFieldsWithAkceptantLokalizacjaAbroad() {
        //when
        CommandHelper.setProperties(command.processCommand, [akceptantLokalizacja: AcceptorLocation.ABROAD])
        command.validate()

        //then
        assertNotNull(command.errors['typLokalizacji'])
        assertNotNull(command.errors['typDokumentu'])
        assertNotNull(command.errors['seriaNrDokumentu'])
        assertNotNull(command.errors['obywatelstwo'])
        assertNotNull(command.errors['adres'])
    }

    @Test
    void shouldValidateFilledFieldsWithAkceptantLokalizacjaAbroad() {
        //given
        Map properties = [typLokalizacji: AcceptorLocation.ABROAD, typDokumentu: IdentityDocumentType.IDENTITY_CARD,
                          seriaNrDokumentu: "TEST", obywatelstwo: "TEST", adres: "TEST", dataUrodzenia: new Date(),
                          czyStanowiskoPolityczne: true, lokalizacjaKraj: "TEST"]

        //when
        setProperties(properties)
        CommandHelper.setProperties(command.processCommand, [akceptantLokalizacja: AcceptorLocation.ABROAD])
        command.validate()

        //then
        assertEquals(command.errors.errorCount, 0)
        assertNull(command.errors['typLokalizacji'])
        assertNull(command.errors['typDokumentu'])
        assertNull(command.errors['seriaNrDokumentu'])
        assertNull(command.errors['obywatelstwo'])
        assertNull(command.errors['adres'])
        assertNull(command.errors['dataUrodzenia'])
        assertNull(command.errors['czyStanowiskoPolityczne'])
        assertNull(command.errors['lokalizacjaKraj'])
    }

    @Test
    void shouldValidatePeselWithTypLokalizacjiCountry() {
        //given
        Map properties = [lokalizacjaPesel: '91101706333']

        //when
        setProperties(properties)
        CommandHelper.setProperties(command.processCommand, [akceptantLokalizacja: AcceptorLocation.ABROAD])
        command.validate()

        //then
        assertNull(command.errors['lokalizacjaPesel'])
    }

    @Test
    void shouldNotValidateEmptyFieldsWithTypLokalizacjiAbroad() {
        //given
        Map properties = [typLokalizacji: AcceptorLocation.ABROAD]

        //when
        setProperties(properties)
        CommandHelper.setProperties(command.processCommand, [akceptantLokalizacja: AcceptorLocation.ABROAD])
        command.validate()

        //then
        assertNotNull(command.errors['lokalizacjaKraj'])
        assertNotNull(command.errors['dataUrodzenia'])
        assertNotNull(command.errors['czyStanowiskoPolityczne'])
    }

    private void setProperties(Map properties) {
        CommandHelper.setProperties(command, properties)
    }

}
