package org.mbrisa.ccollection;

@SuppressWarnings("rawtypes")
public class NoLimitLinkedCondition implements LinkedCondition {
	private static final NoLimitLinkedCondition INSTANCE = new NoLimitLinkedCondition();

	private NoLimitLinkedCondition() {
	}

	
	public static NoLimitLinkedCondition getInstance(){
		return INSTANCE;
	}
	
	@Override
	public boolean appendable(Object target, Object addition) {
		return true;
	}
	
	@Override
	public boolean rejectNull() {
		return false;
	}
	
	@Override
	public boolean headable(Object addition) {
		return true;
	}
}