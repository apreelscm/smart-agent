package com.eservice.eumowy

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EmailServiceTests extends GroovyTestCase {

    def emailService

    public static final String RECIPIENT = "dwalczak@apreel.com"
    public static final String MERCHANT_NAME = "Jaś Fasola"

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSendNotesToCOA() {
        Assert.assertTrue(emailService.sendNotesToCOA("tekst uwag","123",MERCHANT_NAME))
    }

    @Test
    void testSendDocumentsPaperVersion() {
        emailService.sendDocumentsPaperVersion(RECIPIENT, [], MERCHANT_NAME)
    }

    @Test
    void testSendDocumentsTemplateVersion() {
        emailService.sendDocumentsTemplateVersion(RECIPIENT, [])
    }

    @Test
    void testSendDocumentsElectronicalVersion() {
        emailService.sendDocumentsElectronicalVersion(RECIPIENT, [])
    }

    @Test
    void testSendDocumentsAccepted() {
        Assert.assertTrue(emailService.sendProcessAcceptedMails(RECIPIENT, [], MERCHANT_NAME))
    }

    @Test
    void testSendNewAggrementDocumentsAccepted() {
        Assert.assertTrue(emailService.sendDocumentsNotNewAggrementElectronicalVersion(RECIPIENT, [], MERCHANT_NAME))
    }
}
