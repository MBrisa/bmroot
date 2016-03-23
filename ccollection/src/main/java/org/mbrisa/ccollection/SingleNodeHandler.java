package org.mbrisa.ccollection;

import java.util.List;

public class SingleNodeHandler<E> implements NodeRepeatHandler<E> {

	@Override
	public boolean repeatable() {
		return false;
	}
	
	@Override
	public int selectParent(List<E> parent, E current) {
		throw new NodeRepeatException();
	}

}
