package com.suixingpay.takin.druid;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
public class DESUtil {

	private static final String ALGORITHM = "DES";

	public static String decryptHex(String key, String textHex)throws Exception {
		return new String(decrypt(Hex.decodeHex(textHex.toCharArray()), key));
	}
	
	public static String encryptHex(String key, String textHex)throws Exception {
		return new String(encrypt(textHex, key));
	}

	public static byte[] encrypt(String text, String key) throws Exception {
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		return cipher.doFinal(text.getBytes());
	}

	public static byte[] decrypt(byte[] data, String key) throws Exception {
		return decrypt(data, key.getBytes());
	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		SecretKeySpec securekey = new SecretKeySpec(key, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		return cipher.doFinal(data);
	}

}
