package org.mbrisa.ccollection;

public class MultiRootException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8712594642664257587L;

	public MultiRootException() {
		super();
	}

	public MultiRootException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MultiRootException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultiRootException(String message) {
		super(message);
	}

	public MultiRootException(Throwable cause) {
		super(cause);
	}
	
	

}
