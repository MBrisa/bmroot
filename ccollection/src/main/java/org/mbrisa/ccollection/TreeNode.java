package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TreeNode<E> implements Cloneable,Iterable<TreeNode<E>> {

	private int size;
	private TreeNode<E> parent;
	private List<TreeNode<E>> children = new ArrayList<>();
	private final E e;
	private final LinkCondition<E> condition;
	private Set<TreeNode<E>> log = new HashSet<TreeNode<E>>();
	
	@SuppressWarnings("unchecked")
	public TreeNode(E e) {
		if(e == null){
			throw new NullPointerException();
		}
		this.e = e;
		this.condition = NoLimitLinkCondition.INSTANCE;
	}
	
	public TreeNode(E e,LinkCondition<E> condition) {
		if(e == null){
			throw new NullPointerException();
		}
		this.e = e;
		this.condition = condition;
	}
	
	/**
	 * @return the condition
	 */
	public LinkCondition<E> getCondition() {
		return condition;
	}
	
	public boolean add(E e){
//		TODO
		return false;
	}

	public boolean add(TreeNode<E> child){
		validate(child);
		if(!this.condition.equals(child.condition) || !this.condition.appendable(this.entity(), child.entity())){
			return false;
		}
		if(this.children.add(child)){
			child.parent = this;
			this.increaseChild(child);
			if(this.parent != null){
				this.parent.increaseChild(child);
			}
			return true;
		}else{
			throw new RuntimeException("unknow exception");
		}
	}
	
	private void validate(TreeNode<E> child){
		if(child== null /*|| this.entity().equals(child.entity())*/){
			throw new NullPointerException();
		}
		if(this == child){
			throw new NodeConflictException("can not add self.");
		}
		for(TreeNode<E> existed : this.log){
			if(existed == child){
				throw new NodeConflictException("child added already",existed,existed.parent);
			}
			for(TreeNode<E> childChild : child.children){
				if(existed == childChild){
					throw new NodeConflictException("child added already",childChild,childChild.parent);
				}
			}
		}
	}
	
	private void increaseChild(TreeNode<E> child){
		int size = child.size() + 1;
		this.size += size;
		log.addAll(child.log); // 做 log 以实现对 child 进行检查
		log.add(child);
	}
	
	/**
	 * 移除当前节点的任意一个子节点，该子节点可以不是当前节点的直接子节点。
	 * @param child
	 * @return
	 */
	public boolean remove(TreeNode<E> child){
//		TODO do not forget to change the size,set child 'parent' property to null, remove node from log
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 返回所有子节点的个数
	 * @return
	 */
	public int size(){
		return this.size;
	}
	
	public E entity(){
		return this.e;
	}
	
	public List<TreeNode<E>> children(){
		return new ArrayList<>(this.children);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected TreeNode<E> clone() {
		TreeNode<E> result;
		try {
			result = (TreeNode<E>)super.clone();
		} catch (CloneNotSupportedException e) {
			assert(false);
			return null;
		}
		result.children = new ArrayList<>();
		for(TreeNode<E> child : this.children){
			result.children.add(child.clone());
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		return result;
	}
	
	/**
	 * 返回对其所有子节点的迭代器
	 */
	@Override
	public Iterator<TreeNode<E>> iterator() {
		List<TreeNode<E>> treeNodes = new ArrayList<>();
		restoreAll(treeNodes, this);
		return treeNodes.iterator();
	}
	
	private void restoreAll(List<TreeNode<E>> treeNodes,TreeNode<E> parent){
		for(TreeNode<E> child : parent.children()){
			treeNodes.add(child);
			restoreAll(treeNodes,child);
		}
		return;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static class NoLimitLinkCondition implements LinkCondition{
		private static final NoLimitLinkCondition INSTANCE = new NoLimitLinkCondition(); 
		
		private NoLimitLinkCondition() {
		}
		
		@Override
		public boolean appendable(Object target, Object addition) {
			return true;
		}
		
		@Override
		public boolean headable(Object addition) {
			return true;
		}
		
	}
	

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		TreeNode other = (TreeNode) obj;
//		if (e == null) {
//			if (other.e != null)
//				return false;
//		} else if (!e.equals(other.e))
//			return false;
//		return true;
//	}
	
	
	
}