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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + SingleNodeHandler.class.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return this.getClass() == obj.getClass();
	}
	

}
