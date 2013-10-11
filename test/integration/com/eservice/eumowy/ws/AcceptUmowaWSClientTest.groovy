package com.eservice.eumowy.ws

import com.eservice.eum.ws.xml.Result

/**
 * User: Dominik Walczak
 * Date: 03.10.13 Time: 10:47
 *
 */

class AcceptUmowaWSClientTest extends GroovyTestCase {

    def acceptUmowaWSClient
    def webServiceTemplate
    def webServiceClient
    def userService

    void setUp() {
       /** print beans to see if exists in context**/
       System.out.println("userService " + userService)
       System.out.println("webServiceTemplate " + webServiceTemplate)
       System.out.println("webServiceClient " + webServiceClient)
       System.out.println("acceptUmowaWSClient " + acceptUmowaWSClient)
    }

    void testClient() {
        assert(acceptUmowaWSClient != null)
        Result result = acceptUmowaWSClient.acceptEUmowa(506, 288)
        String resultMsg = "Result akceptacji umowy "+ result.wynik + ", " + result.wynikString + ", " + result.stackString
        System.out.println(resultMsg)
    }

}
