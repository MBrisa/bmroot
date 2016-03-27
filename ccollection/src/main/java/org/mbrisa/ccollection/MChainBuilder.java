package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MChainBuilder<E> {
	
	private final LinkedList<Chain<E>> chainList = new LinkedList<>();
	private final BuildingCondition<E> chainCondition;
	private final NoCompleteHandler noCompletion;
	private final LinkedList<E> scrap = new LinkedList<>();
	
	public MChainBuilder(BuildingCondition<E> chainCondition,NoCompleteHandler handler) {
		this.chainCondition = chainCondition;
		this.noCompletion = handler;
	}
	
	public MChainBuilder(BuildingCondition<E> chainCondition) {
		this(chainCondition, new GruffNoCompleteHandler());
	}
	
	public void addNode(E node) {
		for(Chain<E> chain : this.chainList){
			if(chain.add(node)){
				reLink();
				return;
			}
		}
		if(chainCondition.headable(node)){
			this.chainList.add(new Chain<E>(node,chainCondition));
			if(!scrap.isEmpty()){ // all chain can not link the new node.so if scrap is empty ,no need relink on old chain
				reLink();
			}
		}else{
			scrap.add(node);
		}
	}
	
	private void reLink() {
		
		if(this.scrap.size() > 0){
			for(E e : scrap){
				for(Chain<E> chain : this.chainList){
					if(chain.add(e)){
						this.scrap.remove(e);
						reLink();
						return;
					}
				}
			}
		}else if(this.chainList.size() > 1){
			for(int i = 0;i < this.chainList.size(); i++){
				Chain<E> chainI = this.chainList.get(i);
				
				for(int j = 0; j < this.chainList.size(); j++){
					if(i == j){
						continue;
					}
					Chain<E> chainJ = this.chainList.get(j);
					try {
						if(chainI.add(chainJ)){
							this.chainList.remove(j);
							reLink();
							return ;
						}
					} catch (NoCompatibilityException e) {
						assert(false);// must compatible with same chainCondition
					}
				}
			}
		}
	}
	
	
	public List<Chain<E>> retrieveChains(){
		if(!this.scrap.isEmpty()){
			this.noCompletion.handle();
		}
		return new ArrayList<>(this.chainList);
	}
	
	public boolean remove(Chain<E> chain){
		return this.chainList.remove(chain);
	}
	
	public int chainCount() {
		return this.chainList.size();
	}
	
	public List<E> retrieveScrap(){
		return new ArrayList<>(this.scrap);
	}
	
}
