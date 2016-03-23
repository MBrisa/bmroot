package org.mbrisa.ccollection;

public class NotToHeaderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4865499593697284835L;

	public NotToHeaderException() {
		super();
	}

	public NotToHeaderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotToHeaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotToHeaderException(String message) {
		super(message);
	}

	public NotToHeaderException(Throwable cause) {
		super(cause);
	}
	

}
