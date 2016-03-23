package org.mbrisa.ccollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TreeBuilder<E> {
	
	private int size = 0;
	private final LinkCondition<E> condition;
	private final NodeRepeatHandler<E> conflictHandler;
	
	private List<TreeNode<E>> rootNodes = new LinkedList<>();
	private HashMap<E,List<TreeNode<E>>> log = new HashMap<>();
	private final List<TreeNode<E>> scrap = new ArrayList<>();
	
	public TreeBuilder(LinkCondition<E> condition) {
		this(condition, new SingleNodeHandler<E>());
	}
	
	public TreeBuilder(LinkCondition<E> condition,NodeRepeatHandler<E> conflictHandler) {
		this.condition = condition;
		this.conflictHandler = conflictHandler;
	}
	
	public void add(E node){
		if(node == null){ // XXX 如果期望支持对 null 的添加，可以考虑增加 AdditionStrategy ，此时需要考虑 linkStrategy 在连接是对 null 的处理，也要考虑 add(Chain<E> subChain) 方法的处理( subChain 的 AdditionStrategy 是否应该与当前类的 AdditionStrategy 一致).
			throw new NullPointerException();
		}
		if(this.log.get(node) != null && !this.conflictHandler.repeatable()){
			throw new NodeRepeatException("node was duplicate");
		}
		TreeNode<E> addition = new TreeNode<>(node,this.condition);
		if(toAdd(addition)){
			reAddOnScrap();
			return;
		}
		addToScrap(addition);
	}
	
	private void addToScrap(TreeNode<E> node){
		this.scrap.add(node);
		logNewNode(node);
	}
	
	private boolean toAdd(TreeNode<E> addition){
		if(this.condition.headable(addition.entity())){
			if(this.rootNodes.size() == 0){
				addRoot(addition);
				return true;
			}
			for(TreeNode<E> rootNode : this.rootNodes){
				if(this.condition.appendable(addition.entity(),rootNode.entity())){
					if(selectParentToLink(addition, rootNode)){
						addRoot(addition);
						return true;
					}
				}
			}
		}
		for(TreeNode<E> rootNode : this.rootNodes){
			if(tryToChild(rootNode, addition)){
				return true;
			}
		}
		return false;//consider to add strategy : root first or child first ? now is child first.
	}
	
	private boolean selectParentToLink(TreeNode<E> parent,TreeNode<E> child){
		TreeNode<E> realParent;
		List<TreeNode<E>> parentNodeList = this.log.get(parent.entity());
		if(this.conflictHandler.repeatable()){
			List<E> parentList = new ArrayList<>();
			if(parentNodeList == null){ //param 'parent' is current addition ,
				parentNodeList = new ArrayList<>();
				parentNodeList.add(parent);
				parentList.add(parent.entity());
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
				realParent = parent;
			}else if(parentNodeList.size() > 1){
				throw new NodeRepeatException();
			}else{
				assert(parentNodeList.size() == 1); //在 log 中创建一个 list 的时候同时必然会添加一个元素
				realParent = parentNodeList.get(0);
				assert(realParent.equals(parent));
			}
		}
		
		if(realParent == null){
			return false;
		}
		
		return realParent.add(child);
	}
	
	private void addRoot(TreeNode<E> addition){
		this.rootNodes.clear();this.rootNodes.add(addition);
		added(addition);
	}
	
	private boolean tryToChild(TreeNode<E> parent,TreeNode<E> addition){
		if(this.condition.appendable(parent.entity(), addition.entity())){
			if(selectParentToLink(parent,addition)){
				added(addition);
				return true;
			}
		}
		for(TreeNode<E> sub : parent.children()){
			if(tryToChild(sub, addition)){
				return true;
			}
		}
		return false;
	}
	
	
	private void added(TreeNode<E> addition){
		logNewNode(addition);
		this.size += 1;
	}
	
	private void logNewNode(TreeNode<E> node){
		List<TreeNode<E>> original = this.log.get(node.entity());
		if(original == null){
			original = new ArrayList<>();
			this.log.put(node.entity(), original);
		}
		original.add(node);
	}
	
	private void reAddOnScrap(/*TreeNode<E> lastAddition*/){ //can not use lastAddition,because need to consider to reset the root node with scrap
		if(this.scrap.size() == 0){
			return;
		}
		for(TreeNode<E> node : this.scrap){
			if(toAdd(node)){
				this.scrap.remove(node);
				reAddOnScrap();
				return ;
			}
		}
		return;
	}
	
	public int size(){
		return this.size;
	}

	public TreeNode<E> retrieveSilently(E e,int index) {
		return this.log.get(e).get(index).clone();
	}
	
	public TreeNode<E> retrieve(E e,int index) {
		if(this.scrap.size() > 0){
			throw new NoCompleteException();
		}
		return retrieveSilently(e, index);
	}
	
	public TreeNode<E> retrieveRootSilently() {
		return this.rootNodes.get(0).clone();
	}
	
	public TreeNode<E> retrieveRoot(){
		if(this.scrap.size() > 0){
			throw new NoCompleteException();
		}
		return retrieveRootSilently();
	}
	
	public List<E> retrieveScrap(){
		ArrayList<E> result = new ArrayList<>();
		for(TreeNode<E> node : this.scrap){
			result.add(node.entity());
		}
		return result;
	}
}
