package py.com.sodep.mf.license_server.web.controllers;

import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ArchiveExportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import py.com.sodep.mf.license_server.core.business.services.FormServerLicenseService;
import py.com.sodep.mf.license_server.core.business.services.FormServerService;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.core.translators.FormServerLicenseTranslator;
import py.com.sodep.mf.license_server.dtos.DeleteResponse;
import py.com.sodep.mf.license_server.dtos.FormServerLicenseDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class FormServerLicenseController extends LicenseController {

	@Autowired
	private FormServerService formServerService;

	@Autowired
	private FormServerLicenseService formServerLicenseService;

	@Autowired
	private FormServerLicenseTranslator translator;

	/**
	 * Creates a new license for the Form Server
	 * 
	 * @param request
	 * @param response
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "Creates a new license for the Form Server")
	@RequestMapping(value = "/formServer/license", method = RequestMethod.POST)
	public @ResponseBody
	FormServerLicenseDTO post(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FormServerLicenseDTO dto) {
		FormServerLicense saved = formServerLicenseService.save(dto);
		return translator.toDTO(saved);
	}

	// Do we want to update a license?
	// @RequestMapping(value = "/formServer/license", method =
	// RequestMethod.PUT)
	// public @ResponseBody
	// FormServerLicenseDTO put(HttpServletRequest request, HttpServletResponse
	// response,
	// @RequestBody FormServerLicenseDTO dto) {
	// FormServerLicense udpated = service.update(dto);
	// return translator.toDTO(udpated);
	// }

	/**
	 * Get the license with the given id
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "Returns the license with the given id")
	@RequestMapping(value = "/formServer/license", method = RequestMethod.GET)
	public @ResponseBody
	FormServerLicenseDTO get(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws IOException {
		FormServerLicense license = formServerLicenseService.findOne(id);
		if (license == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No license found with id " + id);
			return null;
		}
		return translator.toDTO(license);
	}
	
	@ApiOperation(value = "Returns the license with the given id")
	@RequestMapping(value = "/formServer/license", method = RequestMethod.DELETE)
	public @ResponseBody
	DeleteResponse delete(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws IOException {
		FormServerLicense license = formServerLicenseService.findOne(id);
		DeleteResponse deleteResponse = new DeleteResponse();
		deleteResponse.setId(id);
		if (license == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No license found with id " + id);
			deleteResponse.setDeleted(false);
		} else {
			formServerLicenseService.delete(id);
			deleteResponse.setDeleted(true);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return deleteResponse;
	}

	/**
	 * Get the license with the given id, encrypted
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Returns the license with the given id, encrypted. ")
	@RequestMapping(value = "/formServer/license/encrypted", method = RequestMethod.GET)
	public @ResponseBody
	LicenseEnvelope getEncrypted(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws JsonProcessingException {
		LicenseEnvelope envelope = null;
		FormServerLicense license = formServerLicenseService.findOne(id);
		
		if (license != null) {
			FormServerLicenseDTO dto = translator.toDTO(license);
			envelope = new LicenseEnvelope();
			String encryptedLicense = getEncrypted(dto, license.getFormServer().getId());
			envelope.setLicense(encryptedLicense);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return envelope;
	}

	@ApiOperation(value = "Returns a list of all the server's licenses")
	@RequestMapping(value = "/formServer/license/list", method = RequestMethod.GET)
	public @ResponseBody
	List<FormServerLicenseDTO> list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "serverId") Long id) throws IOException {
		FormServer server = formServerService.findOne(id);
		if (server == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Form Server with id " + id + " wasn't found");
		}
		List<FormServerLicense> licenses = formServerLicenseService.listServerLicenses(id);
		return translator.toDTO(licenses);
	}

	@ApiOperation(value = "Returns a zip file that must be unpacked in the form server home directory")
	@RequestMapping(value = "/formServer/license/download", method = RequestMethod.GET)
	public void downloadZip(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws ArchiveExportException, IllegalArgumentException,
			IOException {
		FormServerLicense formServerLicense = formServerLicenseService.findOne(id);
		if (formServerLicense != null) {
			FormServer server = formServerLicense.getFormServer();
			FormServerLicenseDTO dto = translator.toDTO(formServerLicense);
			// add form server properties to the license
			addFormServerProperties(dto, server);
			GenericArchive archive = generateFormServerLicenseArchive(server, dto);

			String fileName = "license-" + id + ".zip";
			respondWithZipArchive(response, fileName, archive);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private GenericArchive generateFormServerLicenseArchive(FormServer server, FormServerLicenseDTO dto)
			throws JsonProcessingException {
		GenericArchive archive = ShrinkWrap.create(GenericArchive.class);

		String encrypteLicense = getEncrypted(dto, server.getId());
		StringAsset licenseAsset = new StringAsset(encrypteLicense);

		// PublicKey lsPublic = keyPairService.getLSKeyPair().getPublic();
		// String lsPublicHexString =
		// CryptoUtils.toHexString(lsPublic.getEncoded());
		// StringAsset lsPublicAsset = new StringAsset(lsPublicHexString);

		KeyPair keyPair = keyPairService.getFSKeyPair(server.getId());

		String fsPublicHexString = keyPairService.getEncryptedHexString(keyPair.getPublic());
		StringAsset fsPublicAsset = new StringAsset(fsPublicHexString);

		String fsPrivateHexString = keyPairService.getEncryptedHexString(keyPair.getPrivate());
		StringAsset fsPrivateAsset = new StringAsset(fsPrivateHexString);

		archive.add(licenseAsset, "license/license");
		// archive.add(lsPublicAsset, "license/.keys/ls/public.key");
		archive.add(fsPrivateAsset, "license/.keys/fs/private.key");
		archive.add(fsPublicAsset, "license/.keys/fs/public.key");
		return archive;
	}

	private void addFormServerProperties(FormServerLicenseDTO dto, FormServer server) {
		Map<String, String> properties = dto.getProperties();
		if (properties == null) {
			properties = new HashMap<String, String>();
			dto.setProperties(properties);
		}
		properties.put("server.hddSerial", server.getHddSerial());
		properties.put("server.macAddress", server.getMacAddress());
		properties.put("server.owner", server.getOwner());
	}
}
