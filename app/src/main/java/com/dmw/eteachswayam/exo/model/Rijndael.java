package com.dmw.eteachswayam.exo.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Rijndael {

    private static String password;



    public Rijndael(String password) {
        // TODO Auto-generated constructor stub
        Rijndael.password = password;
    }
    public Rijndael() {
        // TODO Auto-generated constructor stub
        password = "?jzK1^l*f6bL~%v&lgA#l;Yp";				//Change it!
    }

    public static void EDR( InputStream inputFile, OutputStream outputFile, int mode)
    {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec ( password.getBytes( "UTF8"), "AES");
            Cipher        cipher   = Cipher.getInstance( "AES");

            if (mode == 1) {
                cipher.init( Cipher.ENCRYPT_MODE, skeySpec);
                CipherInputStream cis = new CipherInputStream ( inputFile, cipher);
                doCopy(cis, outputFile);
            } else if (mode == 2) {
                cipher.init( Cipher.DECRYPT_MODE, skeySpec);
                CipherOutputStream cos = new CipherOutputStream ( outputFile, cipher);
                doCopy(inputFile, cos);
            }

        } catch (InvalidKeyException e) {

            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void doCopy( InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[5*102400];
        int numBytes;
        try {
            while ((numBytes = is.read(bytes)) != -1) {
                os.write(bytes, 0, numBytes);
            }
            os.flush();
            os.close();
            is.close();

//	        BASE64Decoder decoder = new BASE64Decoder();
//	        byte[] test = decoder.decodeBuffer(strText);
//
//	        SecretKeySpec objKey = new SecretKeySpec(bytKey, "AES");
//	        Cipher objCipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
//	        objCipher.init(Cipher.DECRYPT_MODE, objKey);
//
//	        byte[] bytValue = objCipher.doFinal(test);
//
//	        return new String(bytValue)
//
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

