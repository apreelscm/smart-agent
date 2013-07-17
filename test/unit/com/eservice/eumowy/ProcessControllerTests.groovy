package com.eservice.eumowy

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(ProcessController)
@Mock(Process)
class ProcessControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/process/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.processInstanceList.size() == 0
        assert model.processInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.processInstance != null
    }

    void testSave() {
        controller.save()

        assert model.processInstance != null
        assert view == '/process/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/process/show/1'
        assert controller.flash.message != null
        assert Process.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/process/list'

        populateValidParams(params)
        def process = new Process(params)

        assert process.save() != null

        params.id = process.id

        def model = controller.show()

        assert model.processInstance == process
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/process/list'

        populateValidParams(params)
        def process = new Process(params)

        assert process.save() != null

        params.id = process.id

        def model = controller.edit()

        assert model.processInstance == process
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/process/list'

        response.reset()

        populateValidParams(params)
        def process = new Process(params)

        assert process.save() != null

        // test invalid parameters in update
        params.id = process.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/process/edit"
        assert model.processInstance != null

        process.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/process/show/$process.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        process.clearErrors()

        populateValidParams(params)
        params.id = process.id
        params.version = -1
        controller.update()

        assert view == "/process/edit"
        assert model.processInstance != null
        assert model.processInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/process/list'

        response.reset()

        populateValidParams(params)
        def process = new Process(params)

        assert process.save() != null
        assert Process.count() == 1

        params.id = process.id

        controller.delete()

        assert Process.count() == 0
        assert Process.get(process.id) == null
        assert response.redirectedUrl == '/process/list'
    }
}
