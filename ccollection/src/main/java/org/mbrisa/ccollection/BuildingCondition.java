package org.mbrisa.ccollection;

public interface BuildingCondition<E> extends LinkedCondition<E> {

	/**
	 * 能否将 addition 添加到 collection 的头部
	 * @param addition
	 * @return 如果可以将 addition 添加到 collection 的头部返回 true,否则为 false
	 */
	boolean headable(E addition);
	
}
