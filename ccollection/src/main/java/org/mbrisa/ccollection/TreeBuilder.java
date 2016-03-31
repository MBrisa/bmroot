package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.Collection;


public class TreeBuilder<E> implements CCBuilder<E> {

	private TreeNode<E> root;
	private final ArrayList<TreeNode<E>> scrap = new ArrayList<>();
	private final LinkedCondition<E> condition;
	private final NoCompleteHandler noCompletion;
	
	public TreeBuilder(LinkedCondition<E> condition, NoCompleteHandler handler) {
		this.condition = condition;
		this.noCompletion = handler;
	}
	
	public TreeBuilder(LinkedCondition<E> condition) {
		this(condition, new GruffNoCompleteHandler());
	}
	
	@Override
	public void add(E e){
		TreeNode<E> addition = new TreeNode<>(e, this.condition);
		if(toAdd(addition)){
			relinkFormScrap();
			return;
		}
		this.scrap.add(addition);
	}
	
	private boolean toAdd(TreeNode<E> addition){ 
		assert(addition.getParent() == null);
		assert(addition.size() == 1);
		if(this.root == null){
			if(this.condition.headable(addition.entity())){
				this.root = addition;
				return true;
			}
			return false;
		}
		//首先将 addition 作为 root 进行添加，因为这个过程不需要对原有 root 进行迭代。
		if(this.condition.headable(addition.entity()) && addition.add(this.root)){ // first try to become a root
			this.root = addition;
			return true;
		}
		for(TreeNode<E> node : this.root.retrieveAllNode()){
			if(node.add(addition)){ // addition is child
				return true;
			}
		}
		return false;
	}
	
	@Override
	public TreeNode<E> retrieve(){
		validateScrap();
		return this.root;
	}
	
	@Override
	public Collection<E> retrieveScrap(){
		ArrayList<E> result = new ArrayList<>();
		for(TreeNode<E> st : this.scrap){
			result.add(st.entity());
		}
		return result;
	}
	
	private void validateScrap(){
		if(!this.isComplete()){
			noCompletion.handle();
		}
	}

	@Override
	public int size() {
		return this.root == null ? 0 : this.root.size();
	}
	
	private void relinkFormScrap(){
		for(TreeNode<E> node : this.scrap){
			if(this.toAdd(node)){
				this.scrap.remove(node);
				relinkFormScrap();
				return;
			}
		}
	}
	
	@Override
	public boolean isComplete(){
		return this.scrap.size() == 0;
	}
	
	@Override
	public void clear(){
		this.root = null;
		this.scrap.clear();
	}
}
