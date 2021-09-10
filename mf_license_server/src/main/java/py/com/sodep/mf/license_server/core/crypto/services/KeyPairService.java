package py.com.sodep.mf.license_server.core.crypto.services;

import java.security.Key;
import java.security.KeyPair;

public interface KeyPairService {

	KeyPair getLSKeyPair();

	KeyPair createFSKeyPair(Long id);

	KeyPair getFSKeyPair(Long id);

	String getEncryptedHexString(Key key);

	// String getFSEncryptedPublicKey(Long id);

	// String getFSEncryptedPrivateKey(Long id);

}
