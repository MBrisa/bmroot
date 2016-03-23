package org.mbrisa.ccollection;

public class NoCompleteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5090910986865512650L;

	public NoCompleteException() {
		super();
	}

	public NoCompleteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoCompleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCompleteException(String message) {
		super(message);
	}

	public NoCompleteException(Throwable cause) {
		super(cause);
	}

	
}
