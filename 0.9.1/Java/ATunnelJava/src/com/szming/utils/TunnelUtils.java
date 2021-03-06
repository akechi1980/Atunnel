package com.szming.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TunnelUtils {

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	public static byte[] CreateHash(String message) {
		byte[] key = null;
		try {
			key = (message).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}

	// ............encode............
	public static byte[] encode(byte[] inbuf, String method, String pass) {
		byte[] result = null;
		try {
            if(method.equals("aes-128-cbc")) return aes_128_cbc_encrypt(pass.getBytes(),inbuf);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// ............encode............
	public static byte[] decode(byte[] inbuf, String method, String pass) {
		byte[] result = null;
		try {
			  if(method.equals("aes-128-cbc")) return aes_128_cbc_decrypt( pass.getBytes(),inbuf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	//aes_128_cbc
	public static byte[] aes_128_cbc_encrypt(final byte[] key,final byte[] message) throws Exception {
		byte[] bufvi = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 
	            0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10};
		return aes_128_cbcEncryptDecrypt(Cipher.ENCRYPT_MODE, key, message);
	}

	public static byte[] aes_128_cbc_decrypt(final byte[] key,final byte[] message) throws Exception {
		byte[] bufvi = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 
	            0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10};
		return aes_128_cbcEncryptDecrypt(Cipher.DECRYPT_MODE, key, message);
	}

	private static byte[] aes_128_cbcEncryptDecrypt(final int mode, final byte[] key, final byte[] message) {
		
		String ALGORITHM = "AES";
		String AES_CBS_PADDING = "AES/CBC/PKCS5Padding";
		byte[] bufvi = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 
	            0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10};
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(AES_CBS_PADDING);
			SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(bufvi);
			cipher.init(mode, keySpec, ivSpec);
			return cipher.doFinal(message);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cipher = null;
		}
		return new byte[0];
	}

	
	// TEST
	public static void main(String[] args) throws Exception{
		String tContent = "JustTest";
		byte[] buff = tContent.getBytes("utf8");
	
		System.out.println(bytes2HexString(buff));

		System.out.println(bytes2HexString(TunnelUtils.aes_128_cbc_encrypt("1234567890123456".getBytes(),buff)));
	}
}

// 'aes-128-cbc',
// 'aes-128-cbc-hmac-sha1',
// 'aes-128-cbc-hmac-sha256',
// 'aes-128-ccm',
// 'aes-128-cfb',
// 'aes-128-cfb1',
// 'aes-128-cfb8',
// 'aes-128-ctr',
// 'aes-128-ecb',
// 'aes-128-gcm',
// 'aes-128-ofb',
// 'aes-128-xts',
// 'aes-192-cbc',
// 'aes-192-ccm',
// 'aes-192-cfb',
// 'aes-192-cfb1',
// 'aes-192-cfb8',
// 'aes-192-ctr',
// 'aes-192-ecb',
// 'aes-192-gcm',
// 'aes-192-ofb',
// 'aes-256-cbc',
// 'aes-256-cbc-hmac-sha1',
// 'aes-256-cbc-hmac-sha256',
// 'aes-256-ccm',
// 'aes-256-cfb',
// 'aes-256-cfb1',
// 'aes-256-cfb8',
// 'aes-256-ctr',
// 'aes-256-ecb',
// 'aes-256-gcm',
// 'aes-256-ofb',
// 'aes-256-xts',
// 'aes128',
// 'aes192',
// 'aes256',
// 'bf',
// 'bf-cbc',
// 'bf-cfb',
// 'bf-ecb',
// 'bf-ofb',
// 'blowfish',
// 'camellia-128-cbc',
// 'camellia-128-cfb',
// 'camellia-128-cfb1',
// 'camellia-128-cfb8',
// 'camellia-128-ecb',
// 'camellia-128-ofb',
// 'camellia-192-cbc',
// 'camellia-192-cfb',
// 'camellia-192-cfb1',
// 'camellia-192-cfb8',
// 'camellia-192-ecb',
// 'camellia-192-ofb',
// 'camellia-256-cbc',
// 'camellia-256-cfb',
// 'camellia-256-cfb1',
// 'camellia-256-cfb8',
// 'camellia-256-ecb',
// 'camellia-256-ofb',
// 'camellia128',
// 'camellia192',
// 'camellia256',
// 'cast',
// 'cast-cbc',
// 'cast5-cbc',
// 'cast5-cfb',
// 'cast5-ecb',
// 'cast5-ofb',
// 'des',
// 'des-cbc',
// 'des-cfb',
// 'des-cfb1',
// 'des-cfb8',
// 'des-ecb',
// 'des-ede',
// 'des-ede-cbc',
// 'des-ede-cfb',
// 'des-ede-ofb',
// 'des-ede3' ]
// [ 'des-ede3-cbc',
// 'des-ede3-cfb',
// 'des-ede3-cfb1',
// 'des-ede3-cfb8',
// 'des-ede3-ofb',
// 'des-ofb',
// 'des3',
// 'desx',
// 'desx-cbc',
// 'id-aes128-CCM',
// 'id-aes128-GCM',
// 'id-aes128-wrap',
// 'id-aes192-CCM',
// 'id-aes192-GCM',
// 'id-aes192-wrap',
// 'id-aes256-CCM',
// 'id-aes256-GCM',
// 'id-aes256-wrap',
// 'id-smime-alg-CMS3DESwrap',
// 'idea',
// 'idea-cbc',
// 'idea-cfb',
// 'idea-ecb',
// 'idea-ofb',
// 'rc2',
// 'rc2-40-cbc',
// 'rc2-64-cbc',
// 'rc2-cbc',
// 'rc2-cfb',
// 'rc2-ecb',
// 'rc2-ofb',
// 'rc4',
// 'rc4-40',
// 'rc4-hmac-md5',
// 'seed',
// 'seed-cbc',
// 'seed-cfb',
// 'seed-ecb',
// 'seed-ofb' ]
