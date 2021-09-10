package py.com.sodep.mobileforms.license.tests.unit;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;

import org.junit.Assert;
import org.junit.Test;

import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

public class CryptoUtilsTest {

	@Test
	public void hexStringTest() {
		byte[] byteArray = new byte[255];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) i;
		}
		String hexString = CryptoUtils.toHexString(byteArray);
		Assert.assertNotNull(hexString);
		// two characters should be used for each byte 0xFF
		Assert.assertEquals(byteArray.length * 2, hexString.length());
		for (int i = 0; i < byteArray.length; i++) {
			int index = i * 2;
			String currentByte = hexString.substring(index, index + 2);
			// test that each pair in the hex String is equal to the byte it's
			// suppose to represent
			Assert.assertEquals(byteArray[i], (byte) Short.parseShort(currentByte, 16));
		}

		byte[] fromHexString = CryptoUtils.fromHexString(hexString);
		Assert.assertNotNull(fromHexString);
		Assert.assertEquals(byteArray.length, fromHexString.length);
		for (int i = 0; i < byteArray.length; i++) {
			Assert.assertEquals(byteArray[i], fromHexString[i]);
		}
	}

	@Test
	public void hexStringTest2() {
		KeyPair keyPair = CryptoUtils.generate();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		String hexStringPrivate = CryptoUtils.toHexString(privateKeyBytes);
		String hexStringPublic = CryptoUtils.toHexString(publicKeyBytes);

		byte[] privateKeyBytes2 = CryptoUtils.fromHexString(hexStringPrivate);
		byte[] publicKeyBytes2 = CryptoUtils.fromHexString(hexStringPublic);

		Assert.assertEquals(privateKeyBytes.length, privateKeyBytes2.length);
		Assert.assertEquals(publicKeyBytes.length, publicKeyBytes2.length);

		for (int i = 0; i < privateKeyBytes.length; i++) {
			Assert.assertEquals(privateKeyBytes[i], privateKeyBytes2[i]);
		}

		for (int i = 0; i < publicKeyBytes.length; i++) {
			Assert.assertEquals(publicKeyBytes[i], publicKeyBytes2[i]);
		}
	}

	@Test
	public void loadKeys() throws InvalidKeySpecException {
		KeyPair keyPair = CryptoUtils.generate();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

		CryptoUtils.getPrivateKey(privateKeyBytes);
		CryptoUtils.getPublicKey(publicKeyBytes);
	}

	private static final String[] toEncrypt = {
			"{\"field\" : \"Hello\"}",
			"Nonsense is a communication, via speech, writing, or any other symbolic system, "
					+ "that lacks any coherent meaning. Sometimes in ordinary usage, "
					+ "nonsense is synonymous with absurdity or the ridiculous",
			"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
			"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" };

	@Test
	public void encryptionTest() throws UnsupportedEncodingException {
		KeyPair keyPair = CryptoUtils.generate();

		for (String strToEncrypt : toEncrypt) {
			byte[] encryptedBytes = CryptoUtils.encryptStr(strToEncrypt, keyPair.getPrivate());
			String decryptedStr = CryptoUtils.decryptStr(encryptedBytes, keyPair.getPublic());
			Assert.assertEquals(strToEncrypt, decryptedStr);
		}
	}

	@Test
	public void encryptionTest2() throws UnsupportedEncodingException {
		KeyPair keyPair = CryptoUtils.generate();
		KeyPair keyPair2 = CryptoUtils.generate();

		for (String strToEncrypt : toEncrypt) {
			byte[] encryptedBytes = CryptoUtils.encryptStr(strToEncrypt, keyPair.getPrivate());
			encryptedBytes = CryptoUtils.encrypt(encryptedBytes, keyPair2.getPrivate());

			byte[] decryptedBytes = CryptoUtils.decrypt(encryptedBytes, keyPair2.getPublic());
			String decryptedStr = CryptoUtils.decryptStr(decryptedBytes, keyPair.getPublic());

			Assert.assertEquals(strToEncrypt, decryptedStr);
		}
	}
}
