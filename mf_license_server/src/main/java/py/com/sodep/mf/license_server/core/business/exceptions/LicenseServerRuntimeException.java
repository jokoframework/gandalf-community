package py.com.sodep.mf.license_server.core.business.exceptions;

public class LicenseServerRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LicenseServerRuntimeException() {
		super();
	}

	public LicenseServerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseServerRuntimeException(String message) {
		super(message);
	}

	public LicenseServerRuntimeException(Throwable cause) {
		super(cause);
	}

}
