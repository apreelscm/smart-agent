package com.eservice.eumowy.sms;

import com.eservice.eumowy.MobilePhoneNumber;

public interface SmsClient {
    void sendMessage(MobilePhoneNumber phoneNumber, String message);
}
