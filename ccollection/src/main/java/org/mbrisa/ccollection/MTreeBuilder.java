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
				ArrayList<TreeNode<E>> newAddition = new ArrayList<>();
				newAddition.add(tree.getLastAddition());
				this.reLink(tree,newAddition,true);
				return;
			}
		}
		CTree<E> cTree = new CTree<>(this.condition);
		if(cTree.add(node)){
			this.treeList.add(cTree);
			if(!this.scrap.isEmpty()){ // all chain can not link the new node.so if scrap is empty ,no need relink on old chain
				this.reLink();
			}
			return;
		}
		this.scrap.add(node);
		
	}
	
	//因为可读性较差、数据操作层次不明显，所以可暂不使用 TreeNode 进行连接操作。如果有性能需要可以让 relink 只发生在最后添加的 TreeNode上。 
	private void reLink(CTree<E> lastActionTree,List<TreeNode<E>> newAddition,boolean noScrap) {
		if(this.scrap.size() > 0){
			if(noScrap){// noScrap 为 true 说明没有从 this.scrap 中添加节点，此时因为除了 lastAddition 外，scrap 中的数据不可能与该 builder 中任意tree的任意节点连接，所以只需与 lastAddition 进行匹配。
				for(E e : this.scrap){
					if(this.condition.appendable(newAddition.entity(), e)){
						newAddition.add(e);
						reLink(lastActionTree, newAddition, false);
					}
				}
			}else{
				
			}
			for(E e : this.scrap){
				if(lastActionTree.add(e)){ // 
					this.scrap.remove(e);
					reLink(tree,);
					return;
				}
			}
		}
		
		if(this.treeList.size() > 1){
			for(int i = 0;i < this.treeList.size(); i++){
				CTree<E> treeI = this.treeList.get(i);
				
				for(int j = 0; j < this.treeList.size(); j++){
					if(i == j){
						continue;
					}
					CTree<E> treeJ = this.treeList.get(j);
					try {
						if(treeI.add(treeJ)){
							this.treeList.remove(j);
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
	
//	private RelinkStrategy toAdd(TreeNode<E> addition){
//		assert(addition.getParent() == null);
//		assert(addition.size() ==1);
//		for(TreeNode<E> root : rootNodes){
//			//首先将 addition 作为 root 进行添加，因为这个过程不需要对原有 root 进行迭代。
//			if(addition.add(root)){ // addition is root
//				upsertRoot(addition, root);
//				return RelinkStrategy.RELINK_EACH_OTHER;
//			}
//			for(TreeNode<E> node : root.retrieveAllNode()){
//				if(node.add(addition)){ // addition is child
//					return RelinkStrategy.PULL_OTHER;
//				}
//			}
//		}
//		if(this.condition.headable(addition.entity())){//to here the addition can not link to the existing tree,so try to create a new tree
//			upsertRoot(addition, null);
//			if(this.scrap.size() > 0){
//				return RelinkStrategy.SCRAP_NOLY;
//			}
//			return RelinkStrategy.NO_RELINK;
//		}
//		return RelinkStrategy.NO_ADDED;
//	}
	
	
//	private void upsertRoot(TreeNode<E> addition, TreeNode<E> original){
//		if(original != null){
//			this.removeRoot(original);
//		}
//		this.rootNodes.add(addition);
//	}
	
//	private void removeRoot(TreeNode<E> root){
//		if(!this.rootNodes.remove(root) ){
//			assert(false);// must exist original in rootNodes and must addable
//			throw new RuntimeException("..");
//		}
//	}
	
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
	/*
	private static enum RelinkStrategy{
		NO_ADDED,
		
		NO_RELINK{
			@Override
			public boolean added() {
				return true;
			}
			@Override
			public <E> void handle(MTreeBuilder<E> builder,
					TreeNode<E> lastAddition) {
				// do nothing
			}
		},
		
		SCRAP_NOLY{
			@Override
			public boolean added() {
				return true;
			}
			
			@Override
			public <E> void handle(MTreeBuilder<E> builder,TreeNode<E> lastAddition) {
				for(TreeNode<E> node : builder.scrap){
					RelinkStrategy state = builder.toAdd(node);
					if(state.added()){
						builder.scrap.remove(node);
						state.handle(builder,node);
						return ;
					}
				}
			}
		},
		
		RELINK_EACH_OTHER{
			@Override
			public boolean added() {
				return true;
			}
			
			@Override
			public <E> void handle(MTreeBuilder<E> builder,TreeNode<E> lastAddition) {
				assert(lastAddition.getParent() == null);
				for(TreeNode<E> root : builder.rootNodes){
					if(root == lastAddition){
						continue;
					}
					if(lastAddition.add(root)){
						builder.removeRoot(root);
						this.handle(builder, lastAddition);
						return;
					}
					for(TreeNode<E> node : root.retrieveAllNode()){
						if(node.add(lastAddition)){
							builder.removeRoot(lastAddition);
							PULL_OTHER.handle(builder, lastAddition);
							return;
						}
					}
				}
				SCRAP_NOLY.handle(builder, lastAddition);
			}
			
		},
		
		PULL_OTHER{
			@Override
			public boolean added() {
				return true;
			}
			
			@Override
			public <E> void handle(MTreeBuilder<E> builder, TreeNode<E> lastAddition) {
				assert(lastAddition.getParent() != null);
				for(TreeNode<E> root : builder.rootNodes){
					if(lastAddition.getRoot() == root){
						continue;
					}
					if(lastAddition.add(root)){
						builder.removeRoot(root);
						this.handle(builder, lastAddition);
						return;
					}
				}
				SCRAP_NOLY.handle(builder, lastAddition);
			}
		};
		
		public boolean added(){
			return false;
		}
		
		public <E> void handle(MTreeBuilder<E> builder,TreeNode<E> lastAddition) {
			throw new UnsupportedOperationException();
		}
		
	}*/
}
