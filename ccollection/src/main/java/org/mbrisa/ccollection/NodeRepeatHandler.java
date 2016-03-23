package org.mbrisa.ccollection;

import java.util.List;


public interface NodeRepeatHandler<E> {
	
	/**
	 * 如果可以出现重复的节点返回 true ，否则为 false
	 * @return
	 */
	public boolean repeatable();
	
	/**
	 * 如果 {@link #repeatable()} 返回 false，那么在向 {@link TreeBuilder} 中添加节点时将忽略该方法的调用。
	 * 如果 {@link #repeatable()} 返回 true, 那么该方法需要返回参数 parent 的一个下标，该下标所指的对象将作为参数 current 的父节点。如果返回 -1 则 {@link TreeBuilder} 将不会将不会对 current 进行添加
	 * @param parent
	 * @param child
	 * @return
	 */
	public int selectParent(List<E> parent,E child);

}
