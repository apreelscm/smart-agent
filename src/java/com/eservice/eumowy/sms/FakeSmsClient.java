package com.eservice.eumowy.sms;

import com.eservice.eumowy.MobilePhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeSmsClient implements SmsClient {
    private static final Logger log = LoggerFactory.getLogger(FakeSmsClient.class);

    @Override
    public void sendMessage(MobilePhoneNumber phoneNumber, String message) {
        log.info("SMS MESSAGE TO " + phoneNumber.value() + ": " + message);
    }
}
