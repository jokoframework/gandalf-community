package py.com.sodep.mf.license_server.web.controllers;

import java.io.IOException;
import java.security.KeyPair;

import javax.servlet.http.HttpServletResponse;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.springframework.beans.factory.annotation.Autowired;

import py.com.sodep.mf.license_server.core.crypto.services.KeyPairService;
import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class LicenseController {

	@Autowired
	protected KeyPairService keyPairService;

	private ObjectMapper mapper = new ObjectMapper();

	public static class LicenseEnvelope {

		private String license;

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

	}

	protected String getEncrypted(Object object, Long formServerId) throws JsonProcessingException {
		byte[] licenseBytes = mapper.writeValueAsBytes(object);
		KeyPair licenseServerKeyPair = keyPairService.getLSKeyPair();
		KeyPair formServerKeyPair = keyPairService.getFSKeyPair(formServerId);
		// first we encrypt with the Form Server's private key
		if (formServerKeyPair == null) {
			throw new RuntimeException("No Key Pair");
		}
		byte[] encryptedBytes = CryptoUtils.encrypt(licenseBytes, formServerKeyPair.getPrivate());
		// then with the license server's key
		byte[] twiceEncryptedBytes = CryptoUtils.encrypt(encryptedBytes, licenseServerKeyPair.getPrivate());

		return CryptoUtils.toHexString(twiceEncryptedBytes);
	}

	protected void respondWithZipArchive(HttpServletResponse response, String fileName, GenericArchive archive)
			throws IOException {
		response.setContentType("application/zip");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		archive.as(ZipExporter.class).exportTo(response.getOutputStream());
	}

	protected void respondWithTextArchive(HttpServletResponse response, String fileName, String content)
			throws IOException {
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.getWriter().write(content);
	}

}
