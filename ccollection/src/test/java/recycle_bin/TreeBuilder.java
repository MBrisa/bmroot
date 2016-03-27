package recycle_bin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.mbrisa.ccollection.BuildingCondition;
import org.mbrisa.ccollection.NoCompleteException;
import org.mbrisa.ccollection.NodeRepeatException;
import org.mbrisa.ccollection.TreeNode;

public class TreeBuilder<E> {
	
	private int nodeSize = 0;
	private final BuildingCondition<E> condition;
	private final NodeRepeatHandler<E> conflictHandler;
	
	private final List<TreeNode<E>> rootNodes = new LinkedList<>();
	private final HashMap<E,List<TreeNode<E>>> log = new HashMap<>();
	private final List<TreeNode<E>> scrap = new ArrayList<>();
	private final HashMap<E,List<TreeNode<E>>> scrapLog = new HashMap<>();
	
	public TreeBuilder(BuildingCondition<E> condition) {
		this(condition, new SingleNodeHandler<E>());
	}
	
	public TreeBuilder(BuildingCondition<E> condition,NodeRepeatHandler<E> conflictHandler) {
		this.condition = condition;
		this.conflictHandler = conflictHandler;
	}
	
	public void add(E node){
		TreeNode<E> addition = new TreeNode<>(node,this.condition);
		
		if((this.log.get(node) != null || this.scrapLog.get(node) != null) && !this.conflictHandler.repeatable()){
			throw new NodeRepeatException("node was duplicate");
		}
		AdditionState additionState = toAdd(addition);
		if(additionState.added()){
			additionState.rehandle(this,addition);
			return;
		}
		addToScrap(addition);
	}
	
	private void addToScrap(TreeNode<E> node){
		this.scrap.add(node);
		logNewNode(node, this.scrapLog);
	}
	
	private AdditionState toAdd(TreeNode<E> addition){
		boolean headable = this.condition.headable(addition.entity());
		if(headable){
			if(this.rootNodes.size() == 0){
				upsertRoot(addition, null);
				return AdditionState.ADD_ONLY; // nod size 为 0 时，不需要进行 tree relink
			}
			for(TreeNode<E> rootNode : this.rootNodes){
				if(this.condition.appendable(addition.entity(),rootNode.entity())){
					TreeNode<E> realParent = this.selectParent(addition, rootNode);
					if(realParent == addition){
						upsertRoot(realParent, rootNode);
						return AdditionState.TREE_RELINK; //至此，addition 仍就可能成为其它树的根或其它节点的子节点。所以需要进行 tree relink
					}
				}
			}
		}
		for(TreeNode<E> rootNode : this.rootNodes){
			if(tryToChild(rootNode, addition, false)){
				return AdditionState.ADD_ONLY; // 至此,addition 已经明确不能成为根，同时因为一个节点只能由一个父节点，所以其它节点不能在成为 addition 的子，addition 也不能再成为其它节点的子，所以不需要进行 tree relink
			}
		}
		if(headable){
			assert(this.rootNodes.size() > 0);
			upsertRoot(addition, null);// 至此,addition 已经明确不能成为其它节点的父或子，同时其它节点也不能成为 addition 的父或子，故亦不需要进行 tree relink
			return AdditionState.ADD_ONLY;
		}
		return AdditionState.NOT_ADD;
	}
	
	private TreeNode<E> selectParent(TreeNode<E> parentArchetype,TreeNode<E> child){
		TreeNode<E> realParent;
		List<TreeNode<E>> parentNodeList = this.log.get(parentArchetype.entity());
		if(this.conflictHandler.repeatable()){
			List<E> parentList = new ArrayList<>();
			if(parentNodeList == null){ //param 'parent' is current addition ,
				parentNodeList = new ArrayList<>();
				parentNodeList.add(parentArchetype);
				parentList.add(parentArchetype.entity());
			}else{
				for(TreeNode<E> p : parentNodeList){
					parentList.add(p.entity());
				}
			}
			int parentIndex = this.conflictHandler.selectParent(parentList, child.entity());
			if(parentIndex == -1){
				realParent = null;
			}else{
				realParent = parentNodeList.get(parentIndex);
			}
		}else{
			if(parentNodeList == null){ //param 'parent' is current addition ,
				realParent = parentArchetype;
			}else if(parentNodeList.size() > 1){
				throw new NodeRepeatException();
			}else{
				assert(parentNodeList.size() == 1); //在 log 中创建一个 list 的时候同时必然会添加一个元素
				realParent = parentNodeList.get(0);
				assert(realParent.equals(parentArchetype));
			}
		}
		
		return realParent;
		
	}
	
	private void upsertRoot(TreeNode<E> addition, TreeNode<E> original){
		if(original == null){
			this.rootNodes.add(addition);
		}else{
			if(!this.rootNodes.remove(original) || !addition.add(original)){
				assert(false);// must exist original in rootNodes and must addable
				throw new RuntimeException("..");
			}
			this.rootNodes.add(addition);
		}
		added(addition);
	}
	
	private boolean tryToChild(TreeNode<E> parent,TreeNode<E> addition, boolean isTreeLink){
		if(this.condition.appendable(parent.entity(), addition.entity())){
			TreeNode<E> realParent = this.selectParent(parent,addition);
			if(realParent != null && realParent.add(addition)){
				if(!isTreeLink){
					added(addition);
				}
				return true;
			}
		}
		for(TreeNode<E> sub : parent.children()){
			if(tryToChild(sub, addition, isTreeLink)){
				return true;
			}
		}
		return false;
	}
	
	
	private void added(TreeNode<E> addition){
		logNewNode(addition, this.log);
		this.nodeSize += 1;
	}
	
	private void logNewNode(TreeNode<E> node, HashMap<E, List<TreeNode<E>>> continer){
		List<TreeNode<E>> original = continer.get(node.entity());
		if(original == null){
			original = new ArrayList<>();
			continer.put(node.entity(), original);
		}
		original.add(node);
	}
	
//	private void relink(TreeNode<E> lastAddition,AdditionState additionState){
//		if(additionState == AdditionState.TREE_RELINK){
//			treeLink(lastAddition);
//		}
//		for(TreeNode<E> node : this.scrap){
//			AdditionState state = toAdd(node);
//			if(state.added()){
//				this.scrap.remove(node);
//				this.scrapLog.get(node.entity()).remove(node);
//				relink(node,state);
//				return ;
//			}
//		}
//	}
	
	public int treeSize(){
		return this.rootNodes.size();
	}
	
	public int nodeSize(){
		return this.nodeSize;
	}

	/**
	 * for unit test
	 * @param e
	 * @param index
	 * @return
	 */
	TreeNode<E> getNode(E e,int index) {
		List<TreeNode<E>> nodeList = this.log.get(e);
		if(nodeList == null || nodeList.size() <= index){
			return null;
		}
		return nodeList.get(index).clone();
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
	
	private static enum AdditionState{
		NOT_ADD,
		
		ADD_ONLY{
			@Override
			public boolean added() {
				return true;
			}
			
			@Override
			public <E> void rehandle(TreeBuilder<E> builder,TreeNode<E> lastAddition) {
				for(TreeNode<E> node : builder.scrap){
					AdditionState state = builder.toAdd(node);
					if(state.added()){
						builder.scrap.remove(node);
						builder.scrapLog.get(node.entity()).remove(node);
						state.rehandle(builder,node);
						return ;
					}
				}
			}
		},
		
		TREE_RELINK{
			@Override
			public boolean added() {
				return true;
			}
			
			@Override
			public <E> void rehandle(TreeBuilder<E> builder,TreeNode<E> lastAddition) {
				treeLink(builder, lastAddition);
				ADD_ONLY.rehandle(builder, lastAddition);
			}
			
			private <E> void treeLink(TreeBuilder<E> builder,TreeNode<E> lastAddition){
				treeLinkAsChild(builder,lastAddition);
				treeLinkAsParent(builder,lastAddition);
			}
			
			private <E> boolean treeLinkAsChild(TreeBuilder<E> builder,TreeNode<E> targetRoot){
				for(TreeNode<E> rn : builder.rootNodes){ // to make sure the addition is a child.
					if(rn == targetRoot){
						continue;
					}
					if(builder.tryToChild(rn, targetRoot, true)){
						boolean r = builder.rootNodes.remove(targetRoot);
						assert(r);
						return true;
					}
				}
				return false;
			}
			
			private <E> boolean treeLinkAsParent(TreeBuilder<E> builder,TreeNode<E> targetRoot){
				for(TreeNode<E> rn : builder.rootNodes){ 
					if(rn == targetRoot){
						continue;
					}
					if(builder.tryToChild(targetRoot, rn, true)){
						boolean r = builder.rootNodes.remove(rn);
						assert(r);
						this.treeLinkAsParent(builder,targetRoot);
						return true;
					}
					
				}
				return false;
			}
		};
		
		
		public boolean added(){
			return false;
		}
		
		public <E> void rehandle(TreeBuilder<E> builder,TreeNode<E> lastAddition) {
			throw new UnsupportedOperationException();
		}
		
	}
}
