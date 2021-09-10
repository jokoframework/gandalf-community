package py.com.sodep.mf.license_server.core.business.exceptions;

public class InvalidDTOException extends LicenseServerRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDTOException() {
		super();
	}

	public InvalidDTOException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDTOException(String message) {
		super(message);
	}

	public InvalidDTOException(Throwable cause) {
		super(cause);
	}

}
