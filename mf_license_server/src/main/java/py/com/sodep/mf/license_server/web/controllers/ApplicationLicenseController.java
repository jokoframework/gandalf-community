package py.com.sodep.mf.license_server.web.controllers;

import java.io.IOException;
import java.util.List;

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

import py.com.sodep.mf.license_server.core.business.services.ApplicationLicenseService;
import py.com.sodep.mf.license_server.core.persistence.entities.ApplicationLicense;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServerLicense;
import py.com.sodep.mf.license_server.core.translators.ApplicationLicenseTranslator;
import py.com.sodep.mf.license_server.dtos.ApplicationLicenseDTO;
import py.com.sodep.mf.license_server.dtos.DeleteResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.annotations.ApiOperation;

//TODO list application licenses by Form Server license id
@Controller
public class ApplicationLicenseController extends LicenseController {

	@Autowired
	private ApplicationLicenseService applicationLicenseService;

	@Autowired
	private ApplicationLicenseTranslator translator;

	/**
	 * Creates a new Application License
	 * 
	 * @param request
	 * @param response
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "Creates a new application within the Form Sever License")
	@RequestMapping(value = "/application/license", method = RequestMethod.POST)
	public @ResponseBody
	ApplicationLicenseDTO post(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ApplicationLicenseDTO dto) {
		// TODO control the total of application licenses granted within the
		// Form Server License first
		ApplicationLicense saved = applicationLicenseService.save(dto);
		return translator.toDTO(saved);
	}

	/**
	 * Get the Application License with the given id
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "Returns the license with the given id")
	@RequestMapping(value = "/application/license", method = RequestMethod.GET)
	public @ResponseBody
	ApplicationLicenseDTO get(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws IOException {
		ApplicationLicense license = applicationLicenseService.findOne(id);
		if (license == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No application license found with id " + id);
			return null;
		}
		return translator.toDTO(license);
	}

	/**
	 * Get the license with the given id, encrypted
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "Returns the license with the given id, encrypted. ")
	@RequestMapping(value = "/application/encrypted", method = RequestMethod.GET)
	public @ResponseBody
	LicenseEnvelope getEncrypted(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") Long id) throws IOException {
		LicenseEnvelope envelope = null;
		ApplicationLicense applicationLicense = applicationLicenseService.findOne(id);

		if (applicationLicense != null) {
			ApplicationLicenseDTO dto = translator.toDTO(applicationLicense);
			FormServerLicense formServerLicense = applicationLicense.getFormServerLicense();
			FormServer formServer = formServerLicense.getFormServer();
			envelope = new LicenseEnvelope();
			String encryptedLicense = getEncrypted(dto, formServer.getId());
			envelope.setLicense(encryptedLicense);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No application license found with id " + id);
		}
		return envelope;
	}

	@ApiOperation(value = "Returns a zip or text file with the application license")
	@RequestMapping(value = "/application/download", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "format", required = false, defaultValue = "zip") String format,
			@RequestParam(value = "id") Long id) throws ArchiveExportException,
			IllegalArgumentException, IOException {
		ApplicationLicense applicationLicense = applicationLicenseService.findOne(id);

		if (applicationLicense != null && applicationLicense.getApplicationId() != null
				&& applicationLicense.getFormServerLicense() != null) {
			FormServerLicense formServerLicense = applicationLicense.getFormServerLicense();
			ApplicationLicenseDTO dto = translator.toDTO(applicationLicense);
			FormServer server = formServerLicense.getFormServer();
			GenericArchive archive = generateApplicationLicenseArchive(server, dto);
			if (format == null || format.equalsIgnoreCase("zip")) {
				String fileName = "application-license-" + id + ".zip";
				respondWithZipArchive(response, fileName, archive);
			} else {
				String fileName = "application-license-" + id + ".txt";
				String encryptedLicense = getEncrypted(dto, server.getId());
				respondWithTextArchive(response, fileName, encryptedLicense);
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No application license found with id " + id);
		}

	}

	private GenericArchive generateApplicationLicenseArchive(FormServer server, ApplicationLicenseDTO dto)
			throws JsonProcessingException {
		GenericArchive archive = ShrinkWrap.create(GenericArchive.class);
		String encryptedLicense = getEncrypted(dto, server.getId());
		StringAsset licenseAsset = new StringAsset(encryptedLicense);
		archive.add(licenseAsset, "license/apps/license-" + dto.getApplicationId());
		return archive;
	}

	@ApiOperation(value = "Returns a list of the application licenses within the Form Server License")
	@RequestMapping(value = "/application/license/list", method = RequestMethod.GET)
	public @ResponseBody
	List<ApplicationLicenseDTO> list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "formServerLicenseId") Long formServerLicenseId) {
		List<ApplicationLicense> list = applicationLicenseService.list(formServerLicenseId);
		return translator.toDTO(list);
	}
	
	
	@ApiOperation(value = "Deletes the Application License with the given Id")
	@RequestMapping(value = "/application/license", method = RequestMethod.DELETE)
	public @ResponseBody
	DeleteResponse delete(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id") Long id)
			throws IOException {
		ApplicationLicense applicationLicense = applicationLicenseService.findOne(id);
		DeleteResponse deleteResponse = new DeleteResponse();
		deleteResponse.setId(id);
		if (applicationLicense == null) {
			deleteResponse.setDeleted(false);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "No application license was found with id " + id);
		} else {
			applicationLicenseService.delete(id);
			deleteResponse.setDeleted(true);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return deleteResponse;
	}
	
	

}
