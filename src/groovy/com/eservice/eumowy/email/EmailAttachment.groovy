package com.eservice.eumowy.email

class EmailAttachment {
    private final String name
    private final String contentType
    private final byte[] content

    EmailAttachment(String name, String contentType, byte[] content) {
        this.name = name
        this.contentType = contentType
        this.content = content
    }

    String getName() {
        return name
    }

    String getContentType() {
        return contentType
    }

    byte[] getContent() {
        return content
    }
}
