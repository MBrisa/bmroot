package org.mbrisa.ccollection;

public class NotCompatibleConditionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4865499593697284835L;

	public NotCompatibleConditionException() {
		super();
	}

	public NotCompatibleConditionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotCompatibleConditionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotCompatibleConditionException(String message) {
		super(message);
	}

	public NotCompatibleConditionException(Throwable cause) {
		super(cause);
	}
	

}
