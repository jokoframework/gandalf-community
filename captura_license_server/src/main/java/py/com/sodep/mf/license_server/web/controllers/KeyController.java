package py.com.sodep.mf.license_server.web.controllers;

import java.security.Key;
import java.security.KeyPair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import py.com.sodep.mf.license_server.core.crypto.services.KeyPairService;
import py.com.sodep.mobileforms.license.crypto.CryptoUtils;
import py.com.sodep.mobileforms.license.json.keys.KeyEnvelope;
import py.com.sodep.mobileforms.license.json.keys.KeyPairEnvelope;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class KeyController {

	@Autowired
	private KeyPairService keyPairService;

	/**
	 * Returns the License Server's public key
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "Returns the License Server's public key")
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	KeyEnvelope getLicenseServerPublicKey(HttpServletRequest request, HttpServletResponse response) {
		KeyPair keyPair = keyPairService.getLSKeyPair();
		KeyEnvelope envelope = new KeyEnvelope();
		String hexString = CryptoUtils.toHexString(keyPair.getPublic().getEncoded());
		envelope.setKey(hexString);
		return envelope;
	}

	/**
	 * Returns the form server's public key, encrypted with the License Server's
	 * public key
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Returns the form server's public key, encrypted with the License Server's private key")
	@RequestMapping(value = "/formServer/public_key", method = RequestMethod.GET)
	public @ResponseBody
	KeyEnvelope getFormServerPublicKey(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("serverId") Long id) {
		KeyPair keyPair = keyPairService.getFSKeyPair(id);
		if (keyPair != null) {
			KeyEnvelope envelope = getKeyEnvelope(keyPair.getPublic());
			return envelope;
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

	}

	/**
	 * Returns the form server's public key, encrypted with the License Server's
	 * public key
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Returns the form server's private key, encrypted with the License Server's private key")
	@RequestMapping(value = "/formServer/private_key", method = RequestMethod.GET)
	public @ResponseBody
	KeyEnvelope getFormServerPrivateKey(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("serverId") Long id) {
		KeyPair keyPair = keyPairService.getFSKeyPair(id);
		if (keyPair != null) {
			KeyEnvelope envelope = getKeyEnvelope(keyPair.getPrivate());
			return envelope;
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	/**
	 * Generates a Key Pair for the given server.
	 * 
	 * The returned keys are encrypted with the License Server's private key.
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Generates a Key Pair for the given server. The returned keys are encrypted with the License Server's private key.")
	@RequestMapping(value = "/formServer/generate_keys", method = RequestMethod.POST)
	public @ResponseBody
	KeyPairEnvelope generateFormServerKeyPair(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("serverId") Long id) {
		KeyPair keyPair = keyPairService.createFSKeyPair(id);
		KeyEnvelope publicKeyEnvelope = getKeyEnvelope(keyPair.getPublic());
		KeyEnvelope privateKeyEnvelope = getKeyEnvelope(keyPair.getPrivate());
		KeyPairEnvelope keyPairEnvelope = new KeyPairEnvelope();
		keyPairEnvelope.setPublicKey(publicKeyEnvelope);
		keyPairEnvelope.setPrivateKey(privateKeyEnvelope);
		return keyPairEnvelope;
	}

	private KeyEnvelope getKeyEnvelope(Key key) {
		KeyEnvelope envelope = new KeyEnvelope();
		String encryptedHexString = keyPairService.getEncryptedHexString(key);
		envelope.setKey(encryptedHexString);
		return envelope;
	}

}
