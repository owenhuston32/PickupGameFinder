package com.example.pickupgamefinder.Singletons;

import android.graphics.Color;
import android.widget.TextView;
import java.security.MessageDigest;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class HashHandler {

    private static volatile HashHandler INSTANCE = null;
    private HashHandler(){};

    public static HashHandler getInstance() {
        if(INSTANCE == null) {
            synchronized (HashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HashHandler();
                }
            }
        }
        return INSTANCE;
    }

    public String createHash(String str)
    {
        String hashedResult = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256HashBytes = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            hashedResult = bytesToHex(sha256HashBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedResult;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Source: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * Input: Byte array
     * Output: Hexadecimal string corresponding to input (with leading zeroes)
     *
     * @author acc
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}