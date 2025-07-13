package com.eservice.eumowy.documents;

import java.util.Random;

public class DocumentSigningCode {
    private static final int CODE_LEN = 8;
    // Only numeric ASCII characters
    private static final int RANGE_START = 48;
    private static final int RANGE_END = 57;

    public static String generate() {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LEN; i++) {
            int digit = RANGE_START + r.nextInt(RANGE_END - RANGE_START + 1);
            code.append((char) digit);
        }
        return code.toString();
    }
}
