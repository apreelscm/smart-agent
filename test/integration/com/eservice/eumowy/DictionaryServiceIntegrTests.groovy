package com.eservice.eumowy
import org.junit.After
import org.junit.Before
import org.junit.Test

class DictionaryServiceIntegrTests {

    def dictionaryService

    private static final def GET_ULICA_COMBOBOX = "getUlicaComboBox"
    private static final def GET_PAN_PANI = "getPanPaniComboBox"

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void getUlicaComboBoxTest() {
        def result = dictionaryService.getUlicaComboBox()
        assert result != null
    }

    @Test
    void getPanPaniComboBoxTest() {
        def result = dictionaryService.getPanPaniComboBox()
        assert result != null
    }
}