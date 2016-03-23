package org.mbrisa.ccollection;

public interface ChainCondition<E> {

	/**
	 * 如果可以将 addition 添加到 target 的后面，返回 true ,否则为 false
	 * @param target
	 * @param addition
	 * @return
	 * 
	 * @throws ClassCastException 
	 */
	boolean tryAppend(E target,E addition) ;
	
	/**
	 * 
	 * @param condition
	 * @return
	 * 
	 * @throws ClassCastException
	 */
	boolean isCompatible(ChainCondition<E> condition);
}
