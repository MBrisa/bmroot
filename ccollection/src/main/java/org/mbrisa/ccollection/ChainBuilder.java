package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainBuilder<E> implements CCBuilder<E> {
	
	private Chain<E> chain ;
	private final LinkedList<E> scrap = new LinkedList<>();
	private final NoCompleteHandler noCompletion;
	private final BuildingCondition<E> condition;

	public ChainBuilder(BuildingCondition<E> condition, NoCompleteHandler handler) {
		this.condition = condition;
		this.noCompletion = handler;
	}
	
	public ChainBuilder(BuildingCondition<E> condition){
		this(condition,new GruffNoCompleteHandler());
	}
	
	@Override
	public void add(E e){
		if(chain == null && this.condition.headable(e)){
			chain = new Chain<>(e,this.condition);
			this.relinkFromScrap();
			return;
		}
		if(chain != null && this.chain.add(e)){
			this.relinkFromScrap();
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
		return this.chain == null ? 0 : this.chain.size();
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
		this.chain = null;
		this.scrap.clear();
	}
	
}
