package org.mbrisa.ccollection;

public class NodeConflictException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809410831878815591L;

	public NodeConflictException() {
		super();
	}

	public NodeConflictException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NodeConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public NodeConflictException(String message) {
		super(message);
	}

	public NodeConflictException(Throwable cause) {
		super(cause);
	}
	
}
