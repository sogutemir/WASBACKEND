package com.codefusion.wasbackend.config.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CryptoTool {

    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789*/+-,?&";

    public static int getRandomInt(SecureRandom random, int max) {
        return Math.abs(random.nextInt() % max);
    }

    public static String randomKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = CHAR_LIST.charAt(getRandomInt(secureRandom, CHAR_LIST.length()));
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

    public static String toMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(message.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b: bytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find MD5 algorithm", e);
        }
    }
}