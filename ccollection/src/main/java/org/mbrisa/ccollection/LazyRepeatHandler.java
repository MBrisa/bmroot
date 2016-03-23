package org.mbrisa.ccollection;

import java.util.List;

public class LazyRepeatHandler<E> implements NodeRepeatHandler<E> {
	
	@Override
	public boolean repeatable() {
		return true;
	}
	
	@Override
	public int selectParent(List<E> parent, E current) {
		return 0;
	}

}
