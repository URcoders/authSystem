package com.qgailab.authsystem.utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author linxu
 * @version 1.0
 * @date 2018/11/16
 * this class is used to digest the string .
 */
public class DigestUtil {
    /**
     * default seed
     */
    private static final int DEFAULT_SEED = 5;

    /**
     * Digest the str by XOR, and used the default seed(5)
     *
     * @param s wait to digest
     * @return digested words
     */
    private static String xOrDigest(String s) {
        if (s == null || "".equals(s)) {
            throw new NullPointerException("the string is null");
        }
        byte[] b = s.getBytes();
        for (int i = 0; i < b.length; i++) {
            b[i] ^= DEFAULT_SEED;
        }
        return new String(b);
    }

    /**
     * Digest the str by XOR, and used the definition seed
     *
     * @param s wait to digest
     * @return digested words
     */
    private static String xOrDigest(String s, int seed) {
        if (s == null || "".equals(s)) {
            throw new NullPointerException("the string is null");
        }
        byte[] b = s.getBytes();
        for (int i = 0; i < b.length; i++) {
            b[i] ^= seed;
        }
        return new String(b);
    }

    /**
     * using the default seed to digest
     *
     * @param s wait to digest
     * @return digested string
     */
    public static String digest(String s) {
        if (s == null || "".equals(s)) {
            throw new NullPointerException("the string is null");
        }
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    xOrDigest(s).getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("lacking of MD5 Algorithm");
        }
        //trans the byte[] to 16
        return new BigInteger(1, secretBytes).toString(16);
    }

    /**
     * using the  seed to digest
     *
     * @param s    wait to digest
     * @param seed seed
     * @return digested string
     */
    public static String digest(String s, int seed) {
        if (s == null || "".equals(s)) {
            throw new NullPointerException("the string is null");
        }
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    xOrDigest(s, seed).getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("lacking of MD5 Algorithm");
        }
        //trans the byte[] to 16
        return new BigInteger(1, secretBytes).toString(16);
    }

}
