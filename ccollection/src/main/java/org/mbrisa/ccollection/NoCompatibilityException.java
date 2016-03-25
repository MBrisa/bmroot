package org.mbrisa.ccollection;

public class NoCompatibilityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4865499593697284835L;

	public NoCompatibilityException() {
		super();
	}

	public NoCompatibilityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoCompatibilityException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCompatibilityException(String message) {
		super(message);
	}

	public NoCompatibilityException(Throwable cause) {
		super(cause);
	}
	

}
