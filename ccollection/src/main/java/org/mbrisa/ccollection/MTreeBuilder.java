package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MTreeBuilder<E> implements CCBuilder<E> {
	
	private final LinkedCondition<E> condition;
	private final List<CTree<E>> treeList = new LinkedList<>();
	private final List<E> scrap = new ArrayList<>();
	private final NoCompleteHandler noCompletion;
	
	
	public MTreeBuilder(LinkedCondition<E> condition, NoCompleteHandler handler) {
		this.condition = condition;
		this.noCompletion = handler;
	}
	public MTreeBuilder(LinkedCondition<E> condition){
		this(condition,new GruffNoCompleteHandler());
	}
	
	@Override
	public void add(E node){
		
		for(CTree<E> tree : this.treeList){
			if(tree.add(node)){
//				ArrayList<TreeNode<E>> newAddition = new ArrayList<>();
//				newAddition.add(tree.getLastAddition());
				this.reLink(tree);
				return;
			}
		}
		CTree<E> cTree = new CTree<>(this.condition);
		if(cTree.add(node)){
			this.treeList.add(cTree);
			if(!this.scrap.isEmpty()){ // all chain can not link the new node.so if scrap is empty ,no need relink on old chain
				this.reLink(cTree);
			}
			return;
		}
		this.scrap.add(node);
		
	}
	
	private void reLink(CTree<E> lastActionTree/*,List<TreeNode<E>> newAddition*/) {
		if(this.scrap.size() > 0){
			for(E e : this.scrap){ 
				if(lastActionTree.add(e)){ //因为可读性较差、数据操作层次不明显，所以可暂不使用 TreeNode 进行连接操作。如果有性能需要可以让 relink 只发生在最后添加的 TreeNode上，此时在对 scrap 进行添加时需要一个 newAddition 列表，且每从 scrap 中添加一个元素，该 newAddition 都要对应的添加此次增加的 TreeNode
											//XXX: 可以考虑在 CTree 上添加一个在指定节点进行添加新元素的方法
					this.scrap.remove(e);
//					newAddition.add(lastActionTree.getLastAddition());
					reLink(lastActionTree);
					return;
				}
			}
		}
		
		if(lastActionTree.isSubtree()){
			for(CTree<E> tree : this.treeList){
				if(tree == lastActionTree){
					continue;
				}
				if(lastActionTree.add(tree)){ 
					this.treeList.remove(tree);
					reLink(lastActionTree);
					return ;
				}
			}
		}else{
			for(CTree<E> tree : this.treeList){
				if(tree == lastActionTree){
					continue;
				}
				CTree<E> toRemoved = tree.add(lastActionTree) ? lastActionTree : (lastActionTree.add(tree) ? tree : null); // 为了避免尝试两个 Tree 的交替添加，首先试图将 lastActionTree 变成一个 subtree
				if(toRemoved != null){
					this.treeList.remove(toRemoved);
					reLink(lastActionTree);
					return;
				}
			}
		}
		
	}
	
	
	public int treeSize(){
		return this.treeList.size();
	}
	
	@Override
	public int size(){
		int nodeSize = 0;
		for(CTree<E> tree : this.treeList){
			nodeSize += tree.size();
		}
		return nodeSize;
	}

	@Override
	public List<CTree<E>> retrieve(){
		validateScrap();
		return new ArrayList<>(this.treeList);
	}
	
	public CTree<E> retrieveFirst(){
		validateScrap();
		if(this.treeList.size() == 0){
			return null;
		}
		return this.treeList.get(0);
	}
	
	@Override
	public void clear(){
		this.treeList.clear();
		this.scrap.clear();
	}
	
	private void validateScrap(){
		if(!this.isComplete()){
			noCompletion.handle();
		}
	}
	
	@Override
	public List<E> retrieveScrap(){
		return new ArrayList<>(this.scrap);
	}
	
	@Override
	public boolean isComplete(){
		return this.scrap.size() == 0;
	}
}
