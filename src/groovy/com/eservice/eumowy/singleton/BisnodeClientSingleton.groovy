package com.eservice.eumowy.singleton

import com.eservice.webs.wsclient.bisnode.BisnodeWebServiceClient

class BisnodeClientSingleton {
    private static BisnodeWebServiceClient instance = null

    private BisnodeClientSingleton() {}

    public static BisnodeWebServiceClient getInstance() {
        if(instance == null) {
            instance = new BisnodeWebServiceClient()
        }
        return instance
    }
}
