package com.eservice.eumowy.ws

import com.eservice.eum.ws.client.AcceptUmowaWSClient
import com.eservice.eum.ws.client.WebServiceClient

/**
 * User: Dominik Walczak
 * Date: 11.10.13 Time: 01:28
 *
 */
class AcceptUmowaWSClientFactory {

    public static AcceptUmowaWSClient INSTANCE

    private static WebServiceClient webServiceClient

    public AcceptUmowaWSClientFactory(WebServiceClient webServiceClient) {
        this.webServiceClient = webServiceClient
    }

    public AcceptUmowaWSClient getInstance(){
        if (INSTANCE == null) {
            AcceptUmowaWSClient acceptUmowaWSClient = new AcceptUmowaWSClient()
            acceptUmowaWSClient.webServiceClient = this.webServiceClient
            INSTANCE =  acceptUmowaWSClient
        }
        return INSTANCE
    }
}
