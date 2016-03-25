package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class TreeNode<E> implements Cloneable,Iterable<TreeNode<E>> {

	private int size;
	private TreeNode<E> parent;
	private List<TreeNode<E>> children = new ArrayList<>();
	private final E e;
	private final LinkCondition<E> condition;
	private final HashMap<E,List<TreeNode<E>>> log = new HashMap<>();
	private final Set<TreeNode<E>> allChildrenNode = new HashSet<TreeNode<E>>();
	private final NodeRepeatHandler<E> rHandler;
	
	@SuppressWarnings("unchecked")
	public TreeNode(E e) {
		this(e, NoLimitLinkCondition.INSTANCE, new SingleNodeHandler<E>());
	}
	
	public TreeNode(E e,LinkCondition<E> condition) {
		this(e, condition, new SingleNodeHandler<E>());
	}
	
	public TreeNode(E e,LinkCondition<E> condition,NodeRepeatHandler<E> nodeRepeatHandler) {
		if(e == null){
			throw new NullPointerException();
		}
		this.e = e;
		this.condition = condition;
		this.rHandler = nodeRepeatHandler;
	}
	
	/**
	 * @return the condition
	 */
	public LinkCondition<E> getCondition() {
		return condition;
	}
	
	public TreeNode<E> link(E e){
		return link(new TreeNode<E>(e, this.condition,this.rHandler));
	}

	public TreeNode<E> link(TreeNode<E> target){
		validate(target);
		if(!this.condition.equals(target.condition)){
			throw new NoCompatibilityException("LinkCondition is not equals");
		}
		if(!this.rHandler.equals(target.rHandler)){
			throw new NoCompatibilityException("NodeRepeatHandler is not equals");
		}
		if(searchParentToLink(this, target)){
			return this;
		}
		if(searchParentToLink(target, this)){
			return target;
		}
		return null;
	}
	
	private boolean searchParentToLink(TreeNode<E> parent,TreeNode<E> target){
		if(this.condition.appendable(parent.entity(), target.entity())){
			TreeNode<E> realParent = this.selectParent(parent,target);
			if(realParent != null){
				realParent.children.add(target);
				target.parent = realParent;
				realParent.added(target);
				return true;
			}
		}
		for(TreeNode<E> sub : parent.children()){
			if(searchParentToLink(sub, target)){
				return true;
			}
		}
		return false;
	}
	
	private TreeNode<E> selectParent(TreeNode<E> parentArchetype,TreeNode<E> child){
		TreeNode<E> realParent;
		List<TreeNode<E>> parentNodeList = this.log.get(parentArchetype.entity());
		if(this.rHandler.repeatable()){
			List<E> parentList = new ArrayList<>();
			if(parentNodeList == null){ //param 'parent' is current addition ,
				parentNodeList = new ArrayList<>();
				parentNodeList.add(parentArchetype);
				parentList.add(parentArchetype.entity());
			}else{
				for(TreeNode<E> p : parentNodeList){
					parentList.add(p.entity());
				}
			}
			int parentIndex = this.rHandler.selectParent(parentList, child.entity());
			if(parentIndex == -1){
				realParent = null;
			}else{
				realParent = parentNodeList.get(parentIndex);
			}
		}else{
			if(parentNodeList == null){ //param 'parent' is current addition ,
				realParent = parentArchetype;
			}else if(parentNodeList.size() > 1){
				throw new NodeRepeatException();
			}else{
				assert(parentNodeList.size() == 1); //在 log 中创建一个 list 的时候同时必然会添加一个元素
				realParent = parentNodeList.get(0);
				assert(realParent.equals(parentArchetype));
			}
		}
		
		return realParent;
		
	}
	
	private void validate(TreeNode<E> target){
		if(target== null /*|| this.entity().equals(child.entity())*/){
			throw new NullPointerException();
		}
		if(this == target){
			throw new NodeConflictException("can not add self.");
		}
		if(this.contains(target) || target.contains(this)){
			throw new NodeConflictException("relation was create already");
		}
		this.repeatCheck(target);
		target.repeatCheck(this);
	}
	
	private boolean contains(TreeNode<E> node){
		if(this.allChildrenNode.contains(node)){
			return true;
		}
		for(TreeNode<E> cn : node){
			if(this.allChildrenNode.contains(cn)){
				return true;
			}
		}
		return false;
	}
	
	private void repeatCheck(TreeNode<E> target){
		if(rHandler.repeatable()){
			return;
		}
		if(this.log.get(target.e) != null){
			throw new NodeRepeatException("entity repeat.");
		}
		for(TreeNode<E> targetChild : target){
			if(this.log.get(targetChild.e) != null){
				throw new NodeRepeatException("entity repeat..");
			}
		}
	}
	
	private void added(TreeNode<E> child){
		this.size += (child.size() + 1);
		for(Entry<E, List<TreeNode<E>>> entry : child.log.entrySet()){
			E e = entry.getKey();
			List<TreeNode<E>> list =  this.log.get(e);
			if(list == null){
				list = new ArrayList<>();
				this.log.put(e, list);
			}
			list.addAll(entry.getValue());
		}
		List<TreeNode<E>> list =  this.log.get(child.entity());
		if(list == null){
			list = new ArrayList<>();
			this.log.put(child.entity(), list);
		}
		list.add(child);
		this.allChildrenNode.addAll(child.allChildrenNode);
		this.allChildrenNode.add(child);
		if(this.parent != null){
			this.parent.added(child);
		}
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
		return allChildrenNode.iterator();
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
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + NoLimitLinkCondition.class.hashCode();
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("TreeNode [");
		for(TreeNode<E> node : this){
			builder.append(node.e.toString());
			builder.append(",");
		}
		return builder.append("]").toString();
	}
	
}