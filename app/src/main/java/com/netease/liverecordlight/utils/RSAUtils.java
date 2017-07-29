package com.netease.liverecordlight.utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by dengxuan on 2017/7/29.
 */

public class RSAUtils {

    public static final String KEY_ALGORITHM = "RSA";

    /*public static byte[] RSAEncode(PublicKey key, byte[] plainText) {

    }*/

    private static PublicKey getPublicKeyFromByte(byte[] keyBytes){
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException |InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
