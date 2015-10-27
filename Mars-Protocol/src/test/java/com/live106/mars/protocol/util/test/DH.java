package com.live106.mars.protocol.util.test;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DH {

	// �ǶԳ���Կ�㷨
	public static final String KEY_ALGORITHM = "DH";
	// ������Կ�㷨�����ԳƼ����㷨����ѡdes��aes��desede
	public static final String SECRET_ALGORITHM = "AES";

	/**
	 * ��Կ���ȣ�DH�㷨��Ĭ����Կ������1024 ��Կ���ȱ�����64�ı�������512��1024λ֮��
	 */
	private static final int KEY_SIZE = 512;
	private static final String PUBLIC_KEY = "DHPublicKey";
	private static final String PRIVATE_KEY = "DHPrivateKey";

	/**
	 * @return
	 */
	public static Map<String, Object> generateKey() throws Exception {
		// ʵ������Կ������
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		// ��ʼ����Կ������
		keyPairGenerator.initialize(KEY_SIZE);
		// ������Կ��
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// �׷���Կ
		DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
		// �׷�˽Կ
		DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();
		// ����Կ�洢��map��
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * ˽Կ
	 * 
	 * @param keyMap
	 * @return
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * ��Կ
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * ���ݹ�Կ������Կ��
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> generateKey(byte[] key) throws Exception {
		// �����׷��Ĺ�Կ
		// ת����Կ�Ĳ���
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// ʵ������Կ����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// ������Կ
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// �ɼ׷��Ĺ�Կ�����ҷ���Կ
		DHParameterSpec dhParamSpec = ((DHPublicKey) pubKey).getParams();
		// ʵ������Կ������
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyFactory.getAlgorithm());
		// ��ʼ����Կ������
		keyPairGenerator.initialize(dhParamSpec);
		// ������Կ��
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		// �ҷ���Կ
		DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
		// �ҷ�˽Կ
		DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();
		// ����Կ�洢��Map��
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * ����
	 * 
	 * @param data����������
	 * @param key
	 *            ��Կ
	 * @return byte[] ��������
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// ���ɱ�����Կ
		SecretKey secretKey = new SecretKeySpec(key, SECRET_ALGORITHM);
		// ���ݼ���
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(data);
	}

	/**
	 * ����
	 * 
	 * @param data
	 *            ����������
	 * @param key
	 *            ��Կ
	 * @return byte[] ��������
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// ���ɱ�����Կ
		SecretKey secretKey = new SecretKeySpec(key, SECRET_ALGORITHM);
		// ���ݽ���
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(data);
	}

	/**
	 * ������Կ
	 * 
	 * @param publicKey
	 *            ��Կ
	 * @param privateKey
	 *            ˽Կ
	 * @return byte[] ������Կ
	 */
	public static byte[] getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
		// ʵ������Կ����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// ��ʼ����Կ
		// ��Կ����ת��
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		// ������Կ
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// ��ʼ��˽Կ
		// ��Կ����ת��
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		// ����˽Կ
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// ʵ����
		KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory.getAlgorithm());
		// ��ʼ��
		keyAgree.init(priKey);
		keyAgree.doPhase(pubKey, true);
		// ���ɱ�����Կ
		SecretKey secretKey = keyAgree.generateSecret(SECRET_ALGORITHM);
		return secretKey.getEncoded();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// ���ɼ׷�����Կ��
		Map<String, Object> keyMap1 = DH.generateKey();
		// �׷��Ĺ�Կ �� �׷���˽Կ
		byte[] publicKey1 = DH.getPublicKey(keyMap1);
		byte[] privateKey1 = DH.getPrivateKey(keyMap1);
		System.out.println("�׷���Կ��" + Base64.getEncoder().encodeToString(publicKey1));
		System.out.println("�׷�˽Կ��" + Base64.getEncoder().encodeToString(privateKey1));
		// �ɼ׷��Ĺ�Կ��������Կ��
		Map<String, Object> keyMap2 = DH.generateKey(publicKey1);
		byte[] publicKey2 = DH.getPublicKey(keyMap2);
		byte[] privateKey2 = DH.getPrivateKey(keyMap2);
		System.out.println("�ҷ���Կ��" + Base64.getEncoder().encodeToString(publicKey2));
		System.out.println("�ҷ�˽Կ��" + Base64.getEncoder().encodeToString(privateKey2));
		// ====================================================================//
		// ��װ�׷��ı��ؼ�����Կ,���ҷ��Ĺ�Կ�ͼ׷���˽Կ��϶���
		byte[] key1 = DH.getSecretKey(publicKey2, privateKey1);
		System.out.println("�׷��ı�����Կ��" + Base64.getEncoder().encodeToString(key1));
		// ��װ�ҷ��ı��ؼ�����Կ���ɼ׷��Ĺ�Կ���ҷ���˽Կ��϶���
		byte[] key2 = DH.getSecretKey(publicKey1, privateKey2);
		System.out.println("�ҷ��ı�����Կ��" + Base64.getEncoder().encodeToString(key2));
		// ====================================================================//
		String str = "www.what21.com";
		// �׷��������ݵļ���
		byte[] code1 = DH.encrypt(str.getBytes(), key1);
		System.out.println("���ܺ�����ݣ�" + Base64.getEncoder().encodeToString(code1));
		// ====================================================================//
		// �ҷ��������ݵĽ���
		byte[] decode1 = DH.decrypt(code1, key2);
		System.out.println("�ҷ����ܺ�����ݣ�" + new String(decode1) + "/n/n");
		// ====================================================================//
		str = "http://www.what21.com/";
		// ʹ���ҷ�������Կ�����ݽ��м���
		byte[] code2 = DH.encrypt(str.getBytes(), key2);
		System.out.println("�ҷ����ܺ�����ݣ�" + Base64.getEncoder().encodeToString(code2));
		// ====================================================================//
		// �׷�ʹ�ñ�����Կ�����ݽ��н���
		byte[] decode2 = DH.decrypt(code2, key1);
		System.out.println("�׷����ܺ�����ݣ�" + new String(decode2));
	}

}