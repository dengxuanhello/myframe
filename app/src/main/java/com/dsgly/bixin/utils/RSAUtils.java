package com.dsgly.bixin.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by dengxuan on 2017/7/29.
 */

public class RSAUtils {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPPadding";

    public static byte[] RSAEncode(PublicKey key, byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    private static PublicKey getPublicKeyFromByte(byte[] keyBytes){
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = null;
        try {
            factory = KeyFactory.getInstance(KEY_ALGORITHM,"BC");
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException |InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(byte[] keyBytes,String text){
        PublicKey publicKey = getPublicKeyFromByte(keyBytes);
        byte[] encodedText = RSAEncode(publicKey, text.getBytes());
        return Base64Encoder.encode(encodedText);
    }

    public static String encrypt(String keyS,String text) {
        //byte[] keyBytes = keyS.getBytes("UTF-8");
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Base64.decode(keyS.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PublicKey publicKey = getPublicKeyFromByte(keyBytes);
        if(publicKey != null) {
            byte[] encodedText = RSAEncode(publicKey, text.getBytes());
            return Base64Encoder.encode(encodedText);
        }
        return null;
    }


    public static byte[] generateKeyBytes() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getEncoded();
    }


}
