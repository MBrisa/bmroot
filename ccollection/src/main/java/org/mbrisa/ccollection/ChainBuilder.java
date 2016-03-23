package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainBuilder<E> {
	
	private final Chain<E> chain ;
	private final LinkedList<E> scrap = new LinkedList<>();
	private final NoCompleteHandler noCompletion;

	public ChainBuilder(LinkCondition<E> condition,NoCompleteHandler handler) {
		this.chain = new Chain<>(condition);
		this.noCompletion = handler;
	}
	
	public ChainBuilder(LinkCondition<E> condition){
		this(condition, new GruffNoCompleteHandler());
	}
	
	public void addNode(E e){
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
