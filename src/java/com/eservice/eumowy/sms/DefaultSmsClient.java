package com.eservice.eumowy.sms;

import com.eservice.eumowy.MobilePhoneNumber;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

import static java.lang.Boolean.parseBoolean;

class SmsClientProperties {
    final String uri;
    final String sender;
    final String source;
    final boolean partner;

    private SmsClientProperties(String uri, String sender, String source, boolean partner) {
        this.uri = uri;
        this.sender = sender;
        this.source = source;
        this.partner = partner;
    }

    public static SmsClientProperties of(String uri, String sender, String source, boolean partner) {
        return new SmsClientProperties(uri, sender, source, partner);
    }
}

class Sms {
    String recipient;
    String message;
    String sender;
    String source;
    Boolean partner;

    @Override
    public String toString() {
        return "SMS [" +
                "recipient='" + recipient + '\'' +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", source='" + source + '\'' +
                ", partner=" + partner +
                ']';
    }
}

public class DefaultSmsClient implements SmsClient {
    private static final Logger log = Logger.getLogger(DefaultSmsClient.class);

    private final SmsClientProperties properties;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public DefaultSmsClient(
            String uri,
            String sender,
            String source,
            String partner
    ) {
        this.properties = SmsClientProperties.of(uri, sender, source, parseBoolean(partner));
        this.headers = newHeaders();
        this.restTemplate = newRestTemplate();
    }

    @Override
    public void sendMessage(MobilePhoneNumber phoneNumber, String message) {
        Sms sms = newSms(phoneNumber.value(), message);
        log.info("Sending " + sms);
        sendSms(sms);
    }

    private void sendSms(Sms sms) {
        URI uri = UriComponentsBuilder
                .fromUriString(properties.uri)
                .pathSegment("/api/send")
                .build().encode().toUri();

        ResponseEntity<String> resp = restTemplate.postForEntity(uri, sms, String.class);
        log.info("Sent " + resp.getBody());
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new ErrorSendingSmsException();
        }
    }

    private Sms newSms(String phoneNumber, String message) {
        Sms sms = new Sms();
        sms.recipient = phoneNumber;
        sms.message = message;
        sms.sender = properties.sender;
        sms.partner = properties.partner;
        sms.source = properties.source;
        return sms;
    }

    private HttpHeaders newHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private RestTemplate newRestTemplate() {
        return new RestTemplate();
    }
}
