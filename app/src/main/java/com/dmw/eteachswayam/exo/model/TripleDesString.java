package com.dmw.eteachswayam.exo.model;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TripleDesString {


    private KeySpec         keySpec;
    private SecretKey       key;
    private IvParameterSpec iv;
    SecretKeySpec myKey;
    boolean space = false;
    private String keyString;
    private String ivString;



    public TripleDesString() {

        keyString = "^hyorLuIaU@~t4!$hp#@6lh+";						// CHANGE IT!
        ivString = "@dt6Hj%;";

        try {
            byte[] tdesKeyData = keyString.getBytes();
            myKey = new SecretKeySpec ( tdesKeyData, "DESede");


            final MessageDigest md               = MessageDigest.getInstance( "md5");
            final byte[]        digestOfPassword = md.digest(Base64.decodeBase64(keyString.getBytes("utf-8")));
            final byte[]        keyBytes         = Arrays.copyOf( digestOfPassword, 24);

            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            keySpec = new DESedeKeySpec ( keyBytes);

            key = SecretKeyFactory.getInstance( "DESede").generateSecret( keySpec);

            iv = new IvParameterSpec ( ivString.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    String encryptedData;
    public
    String encrypt( String value) {
        try {
            Cipher ecipher = Cipher.getInstance( "DESede/CBC/PKCS5Padding");
            ecipher.init( Cipher.ENCRYPT_MODE, myKey, iv);

            space = true;
            if(value==null)
                return null;

            byte[] utf8 = value.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            encryptedData = new String ( Base64.encodeBase64( enc), "UTF-8");

            return encryptedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public
    String decrypt( String value) {
        try {
            Cipher dcipher = Cipher.getInstance( "DESede/CBC/PKCS5Padding");
            dcipher.init( Cipher.DECRYPT_MODE, myKey, iv);

            if(value==null)
                return null;

            byte[] dec           = Base64.decodeBase64(value.getBytes());
            byte[] utf8          = dcipher.doFinal(dec);
            String decryptedData =  new String ( utf8, "UTF8");

            return decryptedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

