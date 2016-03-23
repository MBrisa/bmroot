package org.mbrisa.ccollection;

public class NodeConflictException extends RuntimeException {

	private TreeNode<?> conflict,parent;
	
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
	
	public NodeConflictException(String message,TreeNode<?> conflict,TreeNode<?> parent){
		super(message);
		this.conflict = conflict;
		this.parent = parent;
	}

	/**
	 * @return the conflict
	 */
	public TreeNode<?> getConflict() {
		return conflict;
	}

	/**
	 * @return the parent
	 */
	public TreeNode<?> getConflictParent() {
		return parent;
	}
	
	
}
