package org.mbrisa.ccollection;

import java.util.List;


public interface NodeRepeatHandler<E> {
	
	/**
	 * 如果可以出现重复的节点返回 true ，否则为 false。
	 * WARN: 对于 condition collection 构造过程中使用的 NodeRepeatHandler ，其 repeatable 方法的返回值应该始终一致。最简单的做法是返回一个常量。如果在构造过程中 repeatable 返回了不同的值，那么构造行为不确定。 
	 * @return
	 */
	public boolean repeatable();
	
	/**
	 * 如果 {@link #repeatable()} 返回 false，那么在向 {@link TreeBuilder} 中添加节点时将忽略该方法的调用。
	 * 如果 {@link #repeatable()} 返回 true, 那么该方法需要返回参数 parent 的一个下标，该下标所指的对象将作为参数 current 的父节点。如果返回 -1 则 {@link TreeBuilder} 将不会将不会对 current 进行添加
	 * @param parent
	 * @param child
	 * @return
	 * @throws NullPointerException 如果 parent 或 child 为 null
	 * @throws InvalidRelationException 如果 parent 为 empty, 或 child 与 parent 列表中的任意一个元素不是父子关系。 
	 */
	public int selectParent(List<E> parent,E child);

}
