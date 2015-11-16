/**
 * 
 */
package com.live106.mars.protocol.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author live106 @creation Oct 19, 2015
 *
 */
public class Cryptor {
	private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(new byte[16]);
	public final static String RSA = "RSA";
	public final static String AES4CIPHER = "AES/ECB/PKCS5Padding";
	public final static String AES = "AES";
	
	private String algorithm = "RSA";
	private KeyPair keyPair;
	private SecretKey secretKey;
	
//	private static final ThreadLocal<Cryptor> cyrptors = new ThreadLocal<Cryptor>() {
//		@Override
//		protected Cryptor initialValue() {
//			return new Cryptor();
//		}
//	};
	
//	public Cryptor() {
//	}
//	
//	public static Cryptor get(String algorithm) {
//		Cryptor cryptor = cyrptors.get();
//		cryptor.algorithm = algorithm;
//		return cryptor;
//	}
	
	public Cryptor(String algorithm) {
		this.algorithm = algorithm;
	}

	public void generateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		switch (algorithm) {
		case RSA:
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
			keyGen.initialize(512);
			keyPair = keyGen.generateKeyPair();
			
			break;
		}
		case AES:
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom(new byte[16]);
		    kgen.init(128, random);  //set keysize, can be 128, 192, and 256
		    secretKey = kgen.generateKey();
			break;
		}
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param opmode the operation mode of this cipher (this is one of the following: ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE or UNWRAP_MODE) 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 */
	public byte[] doCrypt(byte[] data, int opmode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		switch (algorithm) {
		case RSA:
		{
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(opmode, opmode == Cipher.ENCRYPT_MODE ? keyPair.getPublic() : keyPair.getPrivate());
			return cipher.doFinal(data);
		}
		case AES:
		{
			Cipher cipher = Cipher.getInstance(AES4CIPHER);
//			cipher.init(opmode, secretKey, IV_PARAMETER_SPEC);
			cipher.init(opmode, secretKey);
			return cipher.doFinal(data);
		}
		}
		return null;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		String text = "ÖÐ»ª";
		{
			Cryptor crypto = new Cryptor(RSA);
			crypto.generateKey();
			
			byte[] cryptData = crypto.doCrypt(text.getBytes(), Cipher.ENCRYPT_MODE);
			String cryptStr = Base64.getEncoder().encodeToString(cryptData);
			System.err.println("crypted string : \n" + cryptStr);
			
			byte[] decryptData = crypto.doCrypt(Base64.getDecoder().decode(cryptStr), Cipher.DECRYPT_MODE);
			System.err.println("decrypted string : \n" + new String(decryptData));
		}
		
		{
			Cryptor crypto = new Cryptor(AES);
			crypto.generateKey();
			byte[] cryptData = crypto.doCrypt(text.getBytes(), Cipher.ENCRYPT_MODE);
			String cryptStr = Base64.getEncoder().encodeToString(cryptData);
			System.err.println("crypted string : \n" + cryptStr);
			
			byte[] decryptData = crypto.doCrypt(Base64.getDecoder().decode(cryptStr), Cipher.DECRYPT_MODE);
			System.err.println("decrypted string : \n" + new String(decryptData));
		}
		
	}

	public String getSecretKey() {
		String stringKey = new String(secretKey.getEncoded(), StandardCharsets.UTF_8);
		return new String(Base64.getEncoder().encode(stringKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//		return new String(secretKey.getEncoded(), StandardCharsets.UTF_8);
	}
	
	public void setSecretKey(String key) {
		this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8)), algorithm);
//		this.secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
	}
}
