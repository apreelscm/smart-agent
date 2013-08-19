package com.eservice.eumowy

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DictionaryServiceIntegrTests {

    def dictionaryService

    Logger log = LoggerFactory.getLogger(DictionaryServiceIntegrTests)

    @Before
    void setUp() {
      /*  def cbdDAO = new CbdDAO()
        cbdDAO.dataSource = dataSource;
        dictionaryService.cbdDAO = cbdDAO;*/
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