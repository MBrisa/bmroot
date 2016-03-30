package org.mbrisa.ccollection;

import java.util.Collection;

public interface CCBuilder<E> {

	
	/**
	 * 添加元素
	 * @param e
	 */
	public abstract void add(E e);

	/**
	 * 返回 build 的结果，该结果可能是一个 E 的容器，也可能是多个 E 的容器。
	 * @return
	 */
	public abstract Object retrieve();

	/**
	 * 返回通过 add 添加的，但没有融入任何一个集合的元素
	 * @return
	 */
	public abstract Collection<E> retrieveScrap();

	/**
	 * 如果存在没有融入任何集合的元素，该方法返回 false，否则返回 true
	 * @return
	 */
	public abstract boolean isComplete();

	/**
	 * 清除所有在构造过程中产生的数据。
	 */
	public abstract void clear();
	
	/**
	 * 返回有效的，即已经融入任意一个集合的元素个数。
	 * @return
	 */
	public abstract int size();

}