package py.com.sodep.mf.license_server;

import java.io.IOException;
import java.security.KeyPair;

import py.com.sodep.mf.license_server.core.crypto.services.KeyPairIOUtils;
import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

public class GenerateKeysExample {

	public static void main(String[] args) throws IOException {
		KeyPair generated = CryptoUtils.generate();
		KeyPairIOUtils.saveToFile("/home/jmpr/sodep/pretioso/workspace/mf_license_server/src/main/resources/keys",
				generated);
	}

}
