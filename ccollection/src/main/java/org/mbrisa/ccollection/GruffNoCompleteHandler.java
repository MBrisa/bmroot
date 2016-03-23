package org.mbrisa.ccollection;

public class GruffNoCompleteHandler implements NoCompleteHandler {

	@Override
	public void handle() {
		throw new NoCompleteException();
	}

}
