package org.mbrisa.wxmessage.corp;

public class InvalidContentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3136873600007417815L;

	public InvalidContentException() {
		super();
	}

	public InvalidContentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidContentException(String message) {
		super(message);
	}

	public InvalidContentException(Throwable cause) {
		super(cause);
	}

}
