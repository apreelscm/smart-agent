package com.eservice.eumowy;

import java.util.Objects;

public final class MobilePhoneNumber {
    private String value;

    private MobilePhoneNumber(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static MobilePhoneNumber of(String value) {
        String v = value
                .replace("-", "")
                .replace(" ", "")
                .trim();
        if (v.length() != 9) {
            throw new IllegalArgumentException("Unsupported mobile phone number format");
        }
        return new MobilePhoneNumber(v);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MobilePhoneNumber)) return false;
        MobilePhoneNumber that = (MobilePhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
