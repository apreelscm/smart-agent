package com.eservice.eumowy

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(PanelController)
@Mock(Panel)
class PanelControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/panel/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.panelInstanceList.size() == 0
        assert model.panelInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.panelInstance != null
    }

    void testSave() {
        controller.save()

        assert model.panelInstance != null
        assert view == '/panel/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/panel/show/1'
        assert controller.flash.message != null
        assert Panel.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/panel/list'

        populateValidParams(params)
        def panel = new Panel(params)

        assert panel.save() != null

        params.id = panel.id

        def model = controller.show()

        assert model.panelInstance == panel
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/panel/list'

        populateValidParams(params)
        def panel = new Panel(params)

        assert panel.save() != null

        params.id = panel.id

        def model = controller.edit()

        assert model.panelInstance == panel
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/panel/list'

        response.reset()

        populateValidParams(params)
        def panel = new Panel(params)

        assert panel.save() != null

        // test invalid parameters in update
        params.id = panel.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/panel/edit"
        assert model.panelInstance != null

        panel.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/panel/show/$panel.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        panel.clearErrors()

        populateValidParams(params)
        params.id = panel.id
        params.version = -1
        controller.update()

        assert view == "/panel/edit"
        assert model.panelInstance != null
        assert model.panelInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/panel/list'

        response.reset()

        populateValidParams(params)
        def panel = new Panel(params)

        assert panel.save() != null
        assert Panel.count() == 1

        params.id = panel.id

        controller.delete()

        assert Panel.count() == 0
        assert Panel.get(panel.id) == null
        assert response.redirectedUrl == '/panel/list'
    }
}
