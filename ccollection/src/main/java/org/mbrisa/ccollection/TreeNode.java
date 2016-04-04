package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<E> implements Cloneable, Iterable<TreeNode<E>> {

//	private TreeNode<E> root;
	private TreeNode<E> parent; 
	private int indexInParent = -1; // 该值是当前节点在其父节点 allNodes 中的 index 。当前节点如果没有父节点，该值为 -1 ，如果有父节点该值大于0
	private List<DCIndex> dcis = new ArrayList<>(); // direct children index in allNodex
	private final E e;
	private LinkedList<TreeNode<E>> allNodes = new LinkedList<TreeNode<E>>(); 
	

	public TreeNode(E e) {
		this.e = e;
		this.allNodes.add(this);
//		this.root = this;
	}
	

	public boolean add(TreeNode<E> child){
		if(child== null )
			throw new NullPointerException();
		if(child == this)
			throw new NodeConflictException("can not add self.");
		if(child.getParent() != null) 
			return false;
//			throw new NodeConflictException("target: ["+ child +"] exists parent already.");
//		if(this.allNodes.contains(target) || target.allNodes.contains(this)) //不需做次检查，如果 target 不存在 parent， 其一定不会再当前的 allNodes 列表中
//			throw new NodeConflictException("target: ["+ target +"] is current node, or relation was create already");
		return addToChild(child);
	}
	
	public TreeNode<E> add(E child){
		TreeNode<E> node = new TreeNode<E>(child/*, this.condition*/);
		addToChild(node);
		return node;
	}
	
	private boolean addToChild(TreeNode<E> child){
		child.setParent(this);
//		child.setRoot(this.getRoot());
//		for(TreeNode<E> cc : child.retrieveAllNode()){
//			cc.setRoot(this.getRoot());
//		}
		
		int index = this.size(); //child 在 allNodes 列表中的下标
		this.dcis.add(new DCIndex(index)); //记录 child 为当前 node 的直接子节点，并记录其在 allNodes 列表中的下标
		child.indexInParent = index; //通知 child 在其父节点allNodes 列表中的位置
		this.allNodes.addAll(child.allNodes); //正式添加该 child 到 allNodes 中
		
		this.updateAdditionInfoInParent(index,child.size());
		return true;
	}
	
	
	private void updateAdditionInfoInParent(int addedIndexInChild,int addedSize){
		if(this.getParent() == null)
			return;
		int newAddIndex;
		int childIndex = this.getParent().allNodes.lastIndexOf(this);
		assert(childIndex != -1);
		for(DCIndex dci : this.getParent().dcis){ // update 当前父节点的直接子节点信息。并更新这些子节点的 indexInParent 信息
			if(dci.getIndex() <= childIndex){ 
				continue;
			}
			int ori_dci = dci.getIndex();
			int new_dci = ori_dci+addedSize; 
			dci.setIndex(new_dci);
			this.getParent().allNodes.get(ori_dci).indexInParent = new_dci;
		}
		newAddIndex = childIndex+addedIndexInChild;
		this.getParent().allNodes.addAll(childIndex+addedIndexInChild, this.allNodes.subList(addedIndexInChild, addedIndexInChild + addedSize));
		this.getParent().updateAdditionInfoInParent(newAddIndex,addedSize);
	}
	
	/**
	 * 移除当前节点的任意一个子节点，该子节点可以不是当前节点的直接子节点。
	 * @param child
	 * @return
	 */
	public boolean remove(TreeNode<E> child){
//		TODO do not forget to change the size,set child 'parent','root' property to null, remove node from allNodes
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 返回当前节点及其所有后代节点的个数
	 * @return
	 */
	public int size(){
		return this.allNodes.size();
	}
	
	public E entity(){
		return this.e;
	}
	
	/**
	 * 返回当前节点的直接子节点
	 * @return
	 */
	public List<TreeNode<E>> children(){
		ArrayList<TreeNode<E>> result = new ArrayList<TreeNode<E>>();
		for(DCIndex dci : dcis){
			result.add(this.allNodes.get(dci.getIndex()));
		}
		return result;
	}
	
	public TreeNode<E> getParent() {
		return parent;
	}
	
	public boolean isRoot() {
		return this.getParent() == null;
	}
	
	private void setParent(TreeNode<E> parent) {
		this.parent = parent;
	}
	
//	private void setRoot(TreeNode<E> root){
//		this.root = root;
//	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// TODO
		return super.hashCode();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("indexInParent :["+indexInParent+"] current: ["+this.e+"]  children [");
		for(TreeNode<E> node : this){
			builder.append(node.entity());
			builder.append(",");
		}
		return builder.append("]").toString();
	}
	
	@Override
	public Iterator<TreeNode<E>> iterator() {
		return this.allNodes.iterator();
	}
	
	
	@Override
	public TreeNode<E> clone() {
		int indexInRoot = 0;
		TreeNode<E> node = this;
		while(node.getParent() != null){
			indexInRoot += node.indexInParent;
			node = node.getParent();
		}
		TreeNode<E> root = node;
		TreeNode<E> rootCloned = root.cloneNode();
		assert(root.indexInParent == -1);
		TreeNode<E> currentCloned = rootCloned.allNodes.get(indexInRoot);
		return currentCloned;
	}
	
	@SuppressWarnings("unchecked")
	private TreeNode<E> cloneNode(){
		TreeNode<E> cloned = null;
		try {
			cloned = (TreeNode<E>)super.clone();
		} catch (CloneNotSupportedException e) {
		}
		assert(cloned != null);
		cloned.allNodes = new LinkedList<>();
		cloned.dcis = new ArrayList<>();
		cloned.indexInParent = -1;
		cloned.setParent(null);
//		cloned.setRoot(cloned);
		cloned.allNodes.add(cloned);
		for(TreeNode<E> child : this.children()){
			TreeNode<E> childClone = child.cloneNode();
			cloned.add(childClone);
		}
		return cloned;
	}
	
	
	private static class DCIndex{
		private int index;
		public DCIndex(int index) {
			this.index = index;
		}
		public void setIndex(int index){
			this.index = index;
		}
		public int getIndex(){
			return this.index;
		}
	}
	
}