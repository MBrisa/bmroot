package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainBuilder<E> implements CCBuilder<E> {
	
	private Chain<E> chain ;
	private final LinkedList<E> scrap = new LinkedList<>();
	private final NoCompleteHandler noCompletion;
	private final LinkedCondition<E> condition;

	public ChainBuilder(LinkedCondition<E> condition, NoCompleteHandler handler) {
		this.condition = condition;
		this.chain = new Chain<>(condition);
		this.noCompletion = handler;
	}
	
	public ChainBuilder(LinkedCondition<E> condition){
		this(condition,new GruffNoCompleteHandler());
	}
	
	@Override
	public void add(E e){
		if(this.chain.add(e)){
			relinkFromScrap();
			return;
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
	
	@Override
	public int size() {
		return this.chain.size();
	}
	
	@Override
	public Chain<E> retrieve(){
		if(!isComplete()){
			this.noCompletion.handle();
		}
		return this.chain;
	}
	
	@Override
	public List<E> retrieveScrap(){
		return new ArrayList<>(scrap);
	}
	
	@Override
	public boolean isComplete(){
		return this.scrap.isEmpty();
	}
	
	@Override
	public void clear(){
		this.chain = new Chain<>(this.condition);
		this.scrap.clear();
	}
	
}
