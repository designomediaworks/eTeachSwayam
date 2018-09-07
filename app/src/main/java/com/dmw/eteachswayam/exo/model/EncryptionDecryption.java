package com.dmw.eteachswayam.exo.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryption 
{
	private FileInputStream  fileInputStream;
	private FileOutputStream fileOutputStream;
	private  int Iterations = 1024;
	SecretKeySpec secret;
	public EncryptionDecryption( String encryptPath, String decyrptPath, int mode, String key, String salt, String iv)
	{
		try
		{
			fileInputStream=new FileInputStream ( encryptPath);
			fileOutputStream=new FileOutputStream ( decyrptPath);
			getsalt(fileInputStream, fileOutputStream,mode,key,salt,iv);

		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	public void getsalt( FileInputStream fileInputStream, FileOutputStream fileOutputStream, int mode, String key, String salt, String iv)
	{
		try 
		{
			SecretKeyFactory factory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1");
			KeySpec          spec    = new PBEKeySpec ( key.toCharArray(), salt.getBytes(), Iterations, 128);
			SecretKey        tmp     = factory.generateSecret( spec);
			secret = new SecretKeySpec ( tmp.getEncoded(), "AES");
			if(mode==1)
			{
				decrypt(secret, fileInputStream, fileOutputStream,iv);
			}
			if(mode==2)
			{
				encrypt(secret, fileInputStream, fileOutputStream,iv);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void decrypt( SecretKeySpec secret2, FileInputStream fileInputStream2, FileOutputStream fileOutputStream2, String iv) {
		try 
		{
			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding");
			cipher.init( Cipher.DECRYPT_MODE, secret2, new IvParameterSpec ( iv.getBytes()));
			CipherOutputStream cos = new CipherOutputStream ( fileOutputStream2, cipher);
			doCopy(fileInputStream2, cos);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public void encrypt( SecretKeySpec secretKeySpec, FileInputStream fileInputStream, FileOutputStream fileOutputStream, String iv)
	{
		try {
			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding");
			cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec ( iv.getBytes()));
			CipherOutputStream cos = new CipherOutputStream ( fileOutputStream, cipher);
			doCopy(fileInputStream, cos);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void doCopy( InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[4096*1024];
		int numBytes;
		try {
			while ((numBytes = is.read(bytes)) != -1) {
				os.write(bytes, 0, numBytes);
			}
			os.flush();
			os.close();
			is.close();

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
