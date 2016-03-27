package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainBuilder<E> {
	
	private final Chain<E> chain ;
	private final LinkedList<E> scrap = new LinkedList<>();
	private final NoCompleteHandler noCompletion;
	private final BuildingCondition<E> condition;

	public ChainBuilder(BuildingCondition<E> condition, NoCompleteHandler handler) {
		this.chain = new Chain<>(condition);
		this.condition = condition;
		this.noCompletion = handler;
	}
	
	public ChainBuilder(BuildingCondition<E> condition){
		this(condition,new GruffNoCompleteHandler());
	}
	
	public void addNode(E e){
		if(this.chain.size() > 0 || this.condition.headable(e)){
			if(this.chain.add(e)){
				relinkFromScrap();
				return;
			}
		}
		this.scrap.add(e);
	}
	
	
	private void relinkFromScrap(){
		for(E e : this.scrap){
			if(this.chain.add(e)){
				this.scrap.remove(e);
				relinkFromScrap();
				return;
			}
		}
		return;
	}
	
	public Chain<E> retrieveChain(){
		if(this.scrap.size() > 0){
			noCompletion.handle();
		}
		return this.chain;
	}
	
	public List<E> retrieveScrap(){
		return new ArrayList<>(scrap);
	}
}
