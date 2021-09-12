package py.com.sodep.mf.license_server.web.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import py.com.sodep.mf.license_server.core.business.services.FormServerNotificationService;
import py.com.sodep.mf.license_server.core.business.services.FormServerService;
import py.com.sodep.mf.license_server.core.crypto.services.KeyPairService;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.core.translators.FormServerTranslator;
import py.com.sodep.mf.license_server.dtos.DeleteResponse;
import py.com.sodep.mf.license_server.dtos.FormServerDTO;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class FormServerController {

	private static Logger logger = LoggerFactory.getLogger(FormServerController.class);

	@Autowired
	private FormServerService service;

	@Autowired
	private FormServerTranslator translator;

	@Autowired
	private KeyPairService keyPairService;

	@Autowired
	private FormServerNotificationService formServerNotificationService;

	/**
	 * Creates a new Form Server
	 * 
	 * @param request
	 * @param response
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "Creates a new Form Server")
	@RequestMapping(value = "/formServer", method = RequestMethod.POST)
	public @ResponseBody
	FormServerDTO post(HttpServletRequest request, HttpServletResponse response, @RequestBody FormServerDTO dto) {
		FormServer saved = service.save(dto);
		// create the keypair for the form server
		keyPairService.createFSKeyPair(saved.getId());
		saved = service.findOne(saved.getId());
		FormServerDTO savedDTO = translator.toDTO(saved);
		return savedDTO;
	}

	/**
	 * Updates a Form Server
	 * 
	 * @param request
	 * @param response
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "Updates an existing form server")
	@RequestMapping(value = "/formServer", method = RequestMethod.PUT)
	public @ResponseBody
	FormServerDTO put(HttpServletRequest request, HttpServletResponse response, @RequestBody FormServerDTO dto) {
		FormServer udpated = service.update(dto);
		return translator.toDTO(udpated);
	}

	/**
	 * Get the Form Server with the given id
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "Returns the Form Server with the given Id")
	@RequestMapping(value = "/formServer", method = RequestMethod.GET)
	public @ResponseBody
	FormServerDTO get(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id") Long id)
			throws IOException {
		FormServer formServer = service.findOne(id);
		if (formServer == null) {
			String msg = "No form server was found with id " + id;
			response.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
			logger.warn(msg);
			return null;
		}
		return translator.toDTO(formServer);
	}

	@ApiOperation(value = "Deletes the Form Server with the given Id")
	@RequestMapping(value = "/formServer", method = RequestMethod.DELETE)
	public @ResponseBody
	DeleteResponse delete(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id") Long id)
			throws IOException {
		FormServer formServer = service.findOne(id);
		DeleteResponse deleteResponse = new DeleteResponse();
		deleteResponse.setId(id);
		if (formServer == null) {
			deleteResponse.setDeleted(false);
			String msg = "Not Deleted. No form server was found with id " + id;
			response.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
			logger.warn(msg);
		} else {
			service.delete(id);
			deleteResponse.setDeleted(true);
			response.setStatus(HttpServletResponse.SC_OK);
			logger.info("DELETED Form Server #" + id);
		}
		return deleteResponse;
	}

	@ApiOperation(value = "Returns a list of all Form Servers")
	@RequestMapping(value = "/formServer/list", method = RequestMethod.GET)
	public @ResponseBody
	List<FormServerDTO> list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<FormServer> findAll = service.findAll();
		return translator.toDTO(findAll);
	}

}
