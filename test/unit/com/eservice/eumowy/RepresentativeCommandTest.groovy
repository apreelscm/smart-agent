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
        command = mockCommandObject(RepresentativeCommand.class)
        command.processCommand = mockCommandObject(ProcessCommand.class)
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
        assertNotNull(command.errors['locationType'])
        assertNotNull(command.errors['documentType'])
        assertNotNull(command.errors['documentNumber'])
        assertNotNull(command.errors['citizenship'])
        assertNotNull(command.errors['address'])
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
        assertNull(command.errors['locationType'])
        assertNull(command.errors['documentType'])
        assertNull(command.errors['documentNumber'])
        assertNull(command.errors['citizenship'])
        assertNull(command.errors['address'])
        assertNull(command.errors['birthDate'])
        assertNull(command.errors['isPolitician'])
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
        assertNotNull(command.errors['birthDate'])
        assertNotNull(command.errors['isPolitician'])
    }

    private void setProperties(Map properties) {
        CommandHelper.setProperties(command, properties)
    }

}
