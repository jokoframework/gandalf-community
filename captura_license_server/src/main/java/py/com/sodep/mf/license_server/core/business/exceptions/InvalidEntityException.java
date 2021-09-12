package py.com.sodep.mf.license_server.core.business.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

public class InvalidEntityException extends LicenseServerRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, String> messages;

	private Map<String, String> invalidProperties;

	public InvalidEntityException() {
		super();
	}

	public InvalidEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEntityException(String message) {
		super(message);
	}

	public InvalidEntityException(Throwable cause) {
		super(cause);
	}

	private Map<String, String> messages() {
		if (messages == null) {
			messages = new LinkedHashMap<String, String>();
		}
		return messages;
	}

	private Map<String, String> invalidProperties() {
		if (invalidProperties == null) {
			invalidProperties = new LinkedHashMap<String, String>();
		}
		return invalidProperties;
	}

	public void addMessage(String key, String message) {
		messages().put(key, message);
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void addInvalidProperty(String propertyName, String message) {
		invalidProperties().put(propertyName, message);
	}

	public Map<String, String> getInvalidProperties() {
		return invalidProperties;
	}
}
