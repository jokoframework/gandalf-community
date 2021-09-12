package py.com.sodep.mf.license_server.core.crypto.services;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.repositories.FormServerRepository;
import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

@Service
@Transactional
public class KeyPairServiceImpl implements KeyPairService {

	private static Logger logger = LoggerFactory.getLogger(KeyPairServiceImpl.class);

	private Object lock = new Object();

	private KeyPair serverKeyPair;

	private static final String PRIVATE_KEY = "/keys/private.key";

	private static final String PUBLIC_KEY = "/keys/public.key";

	@Autowired
	private FormServerRepository repository;

	@Override
	public KeyPair getLSKeyPair() {
		synchronized (lock) {
			if (serverKeyPair == null) {
				try {
					PrivateKey privateKey = getPrivateKey();
					PublicKey publicKey = getPublicKey();
					serverKeyPair = new KeyPair(publicKey, privateKey);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new RuntimeException("Fatal error retrieving server keys!", e);
				}
			}
		}
		return serverKeyPair;
	}

	private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		InputStream privateKeyStream = KeyPairServiceImpl.class.getResourceAsStream(PRIVATE_KEY);
		byte[] bytes = CryptoUtils.getBytes(privateKeyStream);
		PrivateKey privateKey = CryptoUtils.getPrivateKey(bytes);
		return privateKey;
	}

	private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		InputStream publicKeyStream = KeyPairServiceImpl.class.getResourceAsStream(PUBLIC_KEY);
		byte[] bytes = CryptoUtils.getBytes(publicKeyStream);
		return CryptoUtils.getPublicKey(bytes);
	}

	@Override
	public KeyPair createFSKeyPair(Long id) {
		FormServer server = getFormServerOrThrowException(id);
		try {
			KeyPair generated = CryptoUtils.generate();
			PrivateKey privateKey = generated.getPrivate();
			PublicKey publicKey = generated.getPublic();

			String privateKeyHexString = CryptoUtils.toHexString(privateKey.getEncoded());
			String publicKeyHexString = CryptoUtils.toHexString(publicKey.getEncoded());

			server.setPrivateKey(privateKeyHexString);
			server.setPublicKey(publicKeyHexString);
			return generated;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private FormServer getFormServerOrThrowException(Long id) {
		FormServer server = repository.findOne(id);
		if (server == null) {
			throw new RuntimeException("Invalid id");
		}
		return server;
	}

	@Override
	public KeyPair getFSKeyPair(Long id) {
		FormServer server = getFormServerOrThrowException(id);
		if (server.getPrivateKey() == null || server.getPublicKey() == null) {
			return null;
		}

		String privateKeyHex = server.getPrivateKey();
		String publicKeyHex = server.getPublicKey();

		try {
			KeyPair keyPair = CryptoUtils.getKeyPair(privateKeyHex, publicKeyHex);
			return keyPair;
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException("Error while getting KeyPair", e);
		}
	}

	// public void setFSKeyPair(KeyPair keyPair, Long id) {
	// FormServer server = getFormServerOrThrowException(id);
	// PublicKey publicKey = keyPair.getPublic();
	// PrivateKey privateKey = keyPair.getPrivate();
	//
	// String hexPublicKey = CryptoUtils.toHexString(publicKey.getEncoded());
	// String hexPrivateKey = CryptoUtils.toHexString(privateKey.getEncoded());
	//
	// server.setPrivateKey(hexPrivateKey);
	// server.setPublicKey(hexPublicKey);
	// }

	// @Override
	// public String getFSEncryptedPublicKey(Long id) {
	// KeyPair fsKeyPair = getFSKeyPair(id);
	// return getEncryptedHexString(fsKeyPair.getPublic());
	// }
	//
	// @Override
	// public String getFSEncryptedPrivateKey(Long id) {
	// KeyPair fsKeyPair = getFSKeyPair(id);
	// return getEncryptedHexString(fsKeyPair.getPrivate());
	// }

	@Override
	public String getEncryptedHexString(Key key) {
		KeyPair licenseServerKeyPair = getLSKeyPair();
		byte[] keyBytes = key.getEncoded();
		byte[] encryptedKeyBytes = CryptoUtils.encrypt(keyBytes, licenseServerKeyPair.getPrivate());
		return CryptoUtils.toHexString(encryptedKeyBytes);
	}

}
