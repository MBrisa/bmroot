package org.mbrisa.ccollection;

public interface LinkCondition<E> {

	/**
	 * 如果可以将 addition 添加到 target 的后面返回 true ,否则为 false
	 * @param target
	 * @param addition
	 * @return
	 * 
	 * @throws ClassCastException 
	 */
	boolean appendable(E target,E addition);
	
	/**
	 * 能否将 addition 添加到 collection 的头部
	 * @param addition
	 * @return 如果可以将 addition 添加到 collection 的头部返回 true,否则为 false
	 */
	boolean headable(E addition);
	
//	/** TODO 在添加子集合时，在对比当前结合的 LinkCondition 和 子集合的 LinkCondition 时，考虑使用此方法替换 equals 方法的使用
//	 * 
//	 * @param condition
//	 * @return
//	 * 
//	 * @throws ClassCastException 
//	 */
//	boolean isCompatible(LinkCondition<E> condition);
}
