package py.com.sodep.mf.license_server.core.crypto.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

public class KeyPairIOUtils {

	private static final String PRIVATE_KEY_FILE = "private.key";

	private static final String PUBLIC_KEY_FILE = "public.key";

	private static final String CHARSET = "UTF-8";

	/**
	 * Saves the KeyPair in the given directory.
	 * 
	 * The public key is saved in the file public.key and the private key in the
	 * file private.key.
	 * 
	 * If the directory doesn't exist it should be created.
	 * 
	 * @param dirPath
	 * @param keyPair
	 * @throws IOException
	 */
	public static void saveToFile(String dirPath, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		File folder = new File(dirPath);
		folder.getAbsoluteFile();
		folder.mkdirs();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(folder, PUBLIC_KEY_FILE)),
				CHARSET);
		writer.write(CryptoUtils.toHexString(x509EncodedKeySpec.getEncoded()));
		writer.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		writer = new OutputStreamWriter(new FileOutputStream(new File(folder, PRIVATE_KEY_FILE)), CHARSET);
		writer.write(CryptoUtils.toHexString(pkcs8EncodedKeySpec.getEncoded()));
		writer.close();
	}

	/**
	 * Loads the KeyPair by reading the files public.key and private.key from
	 * the the directory.
	 * 
	 * @param dirPath
	 * @return
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 */
	public static KeyPair loadFromFile(String dirPath) throws IOException, InvalidKeySpecException {
		File filePublicKey = new File(new File(dirPath), PUBLIC_KEY_FILE);
		byte[] encodedPublicKey = CryptoUtils.getBytes(filePublicKey);
		PublicKey publicKey = CryptoUtils.getPublicKey(encodedPublicKey);

		File filePrivateKey = new File(new File(dirPath), PRIVATE_KEY_FILE);
		byte[] encodedPrivateKey = CryptoUtils.getBytes(filePrivateKey);
		PrivateKey privateKey = CryptoUtils.getPrivateKey(encodedPrivateKey);

		return new KeyPair(publicKey, privateKey);
	}

}
