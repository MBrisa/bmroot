package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MTreeBuilder<E> implements CCBuilder<E> {
	
	private final LinkedCondition<E> condition;
	private final List<TreeNode<E>> rootNodes = new LinkedList<>();
	private final List<TreeNode<E>> scrap = new ArrayList<>();
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
		TreeNode<E> addition = new TreeNode<>(node,this.condition);
		
		RelinkStrategy additionState = toAdd(addition);
		if(additionState.added()){
			additionState.handle(this,addition);
			return;
		}
		this.scrap.add(addition);
	}
	
	private RelinkStrategy toAdd(TreeNode<E> addition){
		assert(addition.getParent() == null);
		assert(addition.size() ==1);
		for(TreeNode<E> root : rootNodes){
			//首先将 addition 作为 root 进行添加，因为这个过程不需要对原有 root 进行迭代。
			if(addition.add(root)){ // addition is root
				upsertRoot(addition, root);
				return RelinkStrategy.RELINK_EACH_OTHER;
			}
			for(TreeNode<E> node : root.retrieveAllNode()){
				if(node.add(addition)){ // addition is child
					return RelinkStrategy.PULL_OTHER;
				}
			}
		}
		if(this.condition.headable(addition.entity())){//to here the addition can not link to the existing tree,so try to create a new tree
			upsertRoot(addition, null);
			if(this.scrap.size() > 0){
				return RelinkStrategy.SCRAP_NOLY;
			}
			return RelinkStrategy.NO_RELINK;
		}
		return RelinkStrategy.NO_ADDED;
	}
	
	
	private void upsertRoot(TreeNode<E> addition, TreeNode<E> original){
		if(original != null){
			this.removeRoot(original);
		}
		this.rootNodes.add(addition);
	}
	
	private void removeRoot(TreeNode<E> root){
		if(!this.rootNodes.remove(root) ){
			assert(false);// must exist original in rootNodes and must addable
			throw new RuntimeException("..");
		}
	}
	
	public int treeSize(){
		return this.rootNodes.size();
	}
	
	@Override
	public int size(){
		int nodeSize = 0;
		for(TreeNode<E> root : rootNodes){
			nodeSize += root.size();
		}
		return nodeSize;
	}

	@Override
	public List<TreeNode<E>> retrieve(){
		validateScrap();
		ArrayList<TreeNode<E>> result = new ArrayList<>();
		for(TreeNode<E> node : this.rootNodes){
			result.add(node.clone());
		}
		return result;
	}
	
	public TreeNode<E> retrieveFirstRoot(){
		validateScrap();
		if(this.rootNodes.size() == 0){
			return null;
		}
		return this.rootNodes.get(0).clone();
	}
	
	@Override
	public void clear(){
		this.rootNodes.clear();
		this.scrap.clear();
	}
	
	private void validateScrap(){
		if(!this.isComplete()){
			noCompletion.handle();
		}
	}
	
	@Override
	public List<E> retrieveScrap(){
		ArrayList<E> result = new ArrayList<>();
		for(TreeNode<E> node : this.scrap){
			result.add(node.entity());
		}
		return result;
	}
	
	@Override
	public boolean isComplete(){
		return this.scrap.size() == 0;
	}
	
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
		
	}
}
