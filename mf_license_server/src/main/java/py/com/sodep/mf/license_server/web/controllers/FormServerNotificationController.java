package py.com.sodep.mf.license_server.web.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import py.com.sodep.mf.license_server.core.business.services.FormServerNotificationService;
import py.com.sodep.mf.license_server.core.business.services.FormServerService;
import py.com.sodep.mf.license_server.core.persistence.entities.FormServer;
import py.com.sodep.mf.license_server.dtos.FormServerNotificationDTO;
import py.com.sodep.mobileforms.license.crypto.CryptoUtils;
import py.com.sodep.mobileforms.license.json.notification.MFFormServerNotification;
import py.com.sodep.mobileforms.license.json.notification.MFFormServerNotificationResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class FormServerNotificationController {

	private static Logger logger = LoggerFactory.getLogger(FormServerController.class);

	@Autowired
	private FormServerService service;

	@Autowired
	private FormServerNotificationService formServerNotificationService;

	@ApiOperation(value = "A Form Server must notify the license server on startup")
	@RequestMapping(value = "/notification", method = RequestMethod.POST)
	public @ResponseBody
	MFFormServerNotificationResponse receiveNotification(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") Long id) throws IOException {
		FormServer fs = service.findOne(id);
		MFFormServerNotificationResponse notificationResponse = new MFFormServerNotificationResponse();
		if (fs != null) {
			try {
				MFFormServerNotification notification = parseMFFormServerInfo(request.getInputStream(), fs);
				String remoteAddress = request.getRemoteAddr();
				log(notification);
				// TODO check if server info (mac, hdd) is valid
				formServerNotificationService.save(fs, remoteAddress, notification);
				notificationResponse.setSuccess(true);
				notificationResponse.setMessage("OK");
			} catch (Exception e) {
				notificationResponse.setSuccess(false);
				notificationResponse.setMessage(e.getMessage());
				logger.error(e.getMessage(), e);
			}
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			String message = "Received notification from a Form Server with an invalid id " + id;
			logger.warn(message);
			notificationResponse.setSuccess(false);
			notificationResponse.setMessage(message);
		}

		return notificationResponse;
	}

	@ApiOperation(value = "List notifications received from the Form Servers")
	@RequestMapping(value = "/notification/list", method = RequestMethod.GET)
	public @ResponseBody
	List<FormServerNotificationDTO> listNotifications(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "serverId", required = false) Long serverId
	/*
	 * ,@RequestParam(value = "notificationType", required = false) Integer
	 * notificationType
	 */) throws IOException {
		if (serverId == null) {
			return formServerNotificationService.listAllChronologically();
		} else {
			return formServerNotificationService.listAllChronologically(serverId);
		}
	}

	private MFFormServerNotification parseMFFormServerInfo(InputStream in, FormServer fs) throws IOException,
			InvalidKeySpecException, UnsupportedEncodingException, JsonParseException, JsonMappingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String encryptedInfo = reader.readLine();

		PublicKey publicKey = getPublicKey(fs);
		byte[] decryptedBytes = CryptoUtils.decrypt(CryptoUtils.fromHexString(encryptedInfo), publicKey);
		String serializedInfo = new String(decryptedBytes, "UTF-8");

		ObjectMapper objectMapper = getObjectMapper();
		return objectMapper.readValue(serializedInfo, MFFormServerNotification.class);
	}

	private PublicKey getPublicKey(FormServer fs) throws InvalidKeySpecException {
		String publicKeyStr = fs.getPublicKey();
		byte[] publicKeyBytes = CryptoUtils.fromHexString(publicKeyStr);
		return CryptoUtils.getPublicKey(publicKeyBytes);
	}

	private void log(MFFormServerNotification notification) {
		logger.info("------ Server started -------");
		logger.info("# Notfification Type : " + notification.getNotificationType());
		logger.info("# Server id : " + notification.getServerId());
		logger.info("# hdd serial : " + notification.getHddSerial());
		logger.info("# mac address : " + notification.getMacAddress());
	}

	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new MrBeanModule());
		return mapper;
	}
}
