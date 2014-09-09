package com.eservice.eumowy

import grails.test.mixin.TestFor
import org.junit.Before
import org.junit.Test

@TestFor(MapperService)
class MapperServiceTests {

    Process process

    @Before
    void init(){
        process = new Process()
        process.activities = new HashSet<>()
    }

    @Test
    void shouldContainsBothAttachmentsData() {
        //given
        fillProcessWithActivities(['nowaUmowa', 'dodaniePrepaid', 'promocyjneObnizenieNajmu'])

        //when
        Map data = service.mapOnlyProcessData(process, null)

        //then
        assertTrue(data.containsKey("zalacznikNr4"))
        assertTrue(data.containsKey("zalacznikNr5"))
    }

    @Test
    void shouldContainsOnlyFirstAttachmentData() {
        //given
        fillProcessWithActivities(['wymianaUmowyNajmu', 'zmianaWarunkowPrepaid'])

        //when
        Map data = service.mapOnlyProcessData(process, null)

        //then
        assertTrue(data.containsKey("zalacznikNr4"))
        assertFalse(data.containsKey("zalacznikNr5"))
    }

    @Test
    void shouldContainsOnlySecondAttachmentData() {
        //given
        fillProcessWithActivities(['nowaUmowa', 'promocyjneObnizenieNajmu'])

        //when
        Map data = service.mapOnlyProcessData(process, null)

        //then
        assertTrue(data.containsKey("zalacznikNr4"))
        assertFalse(data.containsKey("zalacznikNr5"))
    }

    private void fillProcessWithActivities(List<String> activities) {
        activities.each { String activityCode ->
            Activity activity = new Activity(code: activityCode)
            process.activities.add(activity)
        }
    }
}
