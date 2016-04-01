package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;


public class TreeBuilder<E> implements CCBuilder<E> {

	private CTree<E> cTree;
	private final LinkedList<E> scrap = new LinkedList<>();
	private final NoCompleteHandler noCompletion;
	
	public TreeBuilder(LinkedCondition<E> condition, NoCompleteHandler handler) {
		this.cTree = new CTree<>(condition);
		this.noCompletion = handler;
	}
	
	public TreeBuilder(LinkedCondition<E> condition) {
		this(condition, new GruffNoCompleteHandler());
	}
	
	@Override
	public void add(E e){
		if(cTree.add(e)){
			relinkFromScrap();
			return;
		}
		this.scrap.add(e);
	}
	
	@Override
	public CTree<E> retrieve(){
		validateScrap();
		return this.cTree;
	}
	
	@Override
	public Collection<E> retrieveScrap(){
		return new ArrayList<>(this.scrap);
	}
	
	private void validateScrap(){
		if(!this.isComplete()){
			noCompletion.handle();
		}
	}

	@Override
	public int size() {
		return this.cTree.size();
	}
	
	
	private void relinkFromScrap(){
		for(E e : this.scrap){
			if(this.cTree.add(e)){
				this.scrap.remove(e);
				relinkFromScrap();
				return;
			}
		}
		return;
	}
	
	@Override
	public boolean isComplete(){
		return this.scrap.size() == 0;
	}
	
	@Override
	public void clear(){
		this.cTree.clear();
		this.scrap.clear();
	}
}
