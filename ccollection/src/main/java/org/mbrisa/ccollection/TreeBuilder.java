package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.List;


public class TreeBuilder<E> {

	private TreeNode<E> root;
	private final ArrayList<TreeNode<E>> scrap = new ArrayList<>();
	private final BuildingCondition<E> condition;
	
	public TreeBuilder(BuildingCondition<E> condition) {
		this.condition = condition;
	}
	
	public void add(E e){
		if(toAdd(new TreeNode<>(e, this.condition))){
			relinkFormScrap();
		}
	}
	
	private boolean toAdd(TreeNode<E> addition){
		assert(addition.getParent() == null);
		if(this.root == null){
			if(this.condition.headable(addition.entity())){
				this.root = addition;
				return true;
			}
			this.scrap.add(addition);
			return false;
		}
		for(TreeNode<E> node : this.root.retrieveAllNode()){
			if(node.add(addition)){ // addition is child
				return true;
			}
		}
		if(this.condition.headable(addition.entity()) && addition.add(this.root)){
			this.root = addition;
			return true;
		}
		return false;
	}
	
	public TreeNode<E> getRoot(){
		return this.root;
	}
	
	public TreeNode<E> retrieveRoot(){
		validateScrap();
		return this.getRoot();
	}
	
	public List<E> getScrap(){
		ArrayList<E> result = new ArrayList<>();
		for(TreeNode<E> st : this.scrap){
			result.add(st.entity());
		}
		return result;
	}
	
	private void validateScrap(){
		if(this.scrap.size() > 0){
			throw new NoCompleteException();
		}
	}

	public Object nodeSize() {
		return this.root == null ? 0 : this.root.size();
	}
	
	private void relinkFormScrap(){
		for(TreeNode<E> node : this.scrap){
			if(this.toAdd(node)){
				this.scrap.remove(node);
				relinkFormScrap();
			}
		}
	}
}
