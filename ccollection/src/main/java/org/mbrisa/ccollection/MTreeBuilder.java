package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MTreeBuilder<E> {
	
	private final BuildingCondition<E> condition;
	private final List<TreeNode<E>> rootNodes = new LinkedList<>();
	private final List<TreeNode<E>> scrap = new ArrayList<>();
	
	
	public MTreeBuilder(BuildingCondition<E> condition) {
		this.condition = condition;
	}
	
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
		for(TreeNode<E> root : rootNodes){
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
		if(this.condition.headable(addition.entity())){
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
	
	public int nodeSize(){
		int nodeSize = 0;
		for(TreeNode<E> root : rootNodes){
			nodeSize += root.size();
		}
		return nodeSize;
	}

	public List<TreeNode<E>> getRoots(){
		ArrayList<TreeNode<E>> result = new ArrayList<>();
		for(TreeNode<E> node : this.rootNodes){
			result.add(node.clone());
		}
		return result;
	}
	
	public List<TreeNode<E>> retrieveRoots(){
		validateScrap();
		return getRoots();
	}
	
	public TreeNode<E> getFirstRoot() {
		if(this.rootNodes.size() == 0){
			return null;
		}
		return this.rootNodes.get(0).clone();
	}
	
	public TreeNode<E> retrieveFirstRoot(){
		validateScrap();
		return getFirstRoot();
	}
	
	private void validateScrap(){
		if(this.scrap.size() > 0){
			throw new NoCompleteException();
		}
	}
	
	public List<E> getScrap(){
		ArrayList<E> result = new ArrayList<>();
		for(TreeNode<E> node : this.scrap){
			result.add(node.entity());
		}
		return result;
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
