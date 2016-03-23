package org.mbrisa.ccollection;

public class NotCompatibleCondition extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4865499593697284835L;

	public NotCompatibleCondition() {
		super();
	}

	public NotCompatibleCondition(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotCompatibleCondition(String message, Throwable cause) {
		super(message, cause);
	}

	public NotCompatibleCondition(String message) {
		super(message);
	}

	public NotCompatibleCondition(Throwable cause) {
		super(cause);
	}
	

}
