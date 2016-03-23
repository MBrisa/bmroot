package org.mbrisa.ccollection;

public class NodeRepeatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809410831878815591L;

	public NodeRepeatException() {
		super();
	}

	public NodeRepeatException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NodeRepeatException(String message, Throwable cause) {
		super(message, cause);
	}

	public NodeRepeatException(String message) {
		super(message);
	}

	public NodeRepeatException(Throwable cause) {
		super(cause);
	}

	
}
