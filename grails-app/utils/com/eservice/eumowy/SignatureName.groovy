package com.eservice.eumowy


import java.util.regex.Pattern

enum SignatureName {
    APUW("AP/UW/.*")

    private final Pattern pattern

    private SignatureName(final String pattern) {
        this.pattern = Pattern.compile(pattern)
    }

    boolean matches(String pattern) {
        return this.pattern.matcher(pattern).find();
    }
}