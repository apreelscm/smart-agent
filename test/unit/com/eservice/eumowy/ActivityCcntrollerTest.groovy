package com.eservice.eumowy
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ActivityController)
@Mock([EmailService])
class ActivityControllerTests {

    def testValidate_create_defineActivity() {
        params["dodatkowyPunkt"] = 'on'
        params["notes"] = 'sample notes'
        controller.emailService = new EmailService();

        controller.validate_create_defineActivity()

        assert response.redirectedUrl == '/activity/create_chooseCalc'
        assert flash.infoMessage == "Wiadomość została wysłana."
        assert flash.errorMessage == null

        //test invalid
        response.reset()

        params["dodatkowyPunkt"] = ''
        params["notes"] = ''

        controller.validate_create_defineActivity()

       assert response.redirectedUrl == '/activity/create_defineActivity?dodatkowyPunkt=&notes='
//        assert response.redirectedUrl == '/activity/validate_create_defineActivity'

        assert flash.errorMessage == "Należy wybrać aktywności i/lub wypełnić uwagi do COA."
    }
}
