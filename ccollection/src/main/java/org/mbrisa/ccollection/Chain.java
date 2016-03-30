package org.mbrisa.ccollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Chain<E> implements Collection<E>,Cloneable{

	private LinkedList<E> container = new LinkedList<>();
	private final LinkedCondition<E> chainCondition ;

	@SuppressWarnings("unchecked")
	public Chain() {
		this(NoLimitLinkedCondition.getInstance());
	}
	
	public Chain(LinkedCondition<E> chainCondition) {
		this.chainCondition = chainCondition;
	}
	
	public Chain(E e,LinkedCondition<E> chainCondition) throws NotToHeaderException {
		this.chainCondition = chainCondition;
		this.getContainer().add(e);
	}
	
	/**
	 * @return the chainCondition
	 */
	public LinkedCondition<E> getChainCondition() {
		return chainCondition;
	}
	
	/**
	 * 如果 e 为 null 返回 false
	 * @param e
	 * @return
	 */
	@Override
	public boolean add(E e) {
		if(this.getContainer().isEmpty()){
			this.getContainer().add(e);
			return true;
		}
		return addToHead(e) || addToTail(e);
	}
	
	boolean addToHead(E e){
		E header = this.getContainer().getFirst();
		if(chainCondition.appendable(e, header)){
			this.getContainer().addFirst(e);
			return true;
		}
		return false;
	}
	
	boolean addToTail(E e){
		E tail = this.getContainer().getLast();
		if(chainCondition.appendable(tail,e)){
			this.getContainer().addLast(e);
			return true;
		}
		return false;
	}
	
	public boolean add(Chain<E> subChain){
		if(!this.chainCondition.equals(subChain.chainCondition)){
			throw new NoCompatibilityException("current ChainCondition:"+this.chainCondition+" is not compatible with "+subChain.chainCondition+" of param chain");
		}
		if(subChain == this){
			throw new NodeConflictException("can not add self.");
		}
		if(subChain.size() == 0){
			return false;
		}
		Chain<E> cloned = this.clone();
		for(E e : subChain){
			if(!cloned.add(e)){
				return false;
			}
		}
		this.setContainer(cloned.getContainer());
		return true;
		
	}
	
	@Override
	public void clear() {
		this.getContainer().clear();
	}
	
	@Override
	public boolean contains(Object o) {
		return this.getContainer().contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.getContainer().containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.getContainer().isEmpty();
	}
	
	
	/**
	 * 
	 * 移除指定的对象，及该 chain 中位于指定对象之后的所有对象。
	 * @param o
	 * @return 如果成功移除返回 true ，否则为 false
	 */
	@Override
	public boolean remove(Object o) {
		int index = this.getContainer().indexOf(o);
		if(index != -1){
			if(index == 0){
				this.getContainer().clear();
			}else{
				setContainer(new LinkedList<>(this.getContainer().subList(0, index)));
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
//		int originSize = this.size();
//		for(E e : c){
//			this.add(e);
//		}
//		return originSize < this.size();
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Object[] toArray() {
		return this.getContainer().toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		return this.getContainer().toArray(a);
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getContainer() == null) ? 0 : this.getContainer().hashCode());
		result = prime * result
				+ ((chainCondition == null) ? 0 : chainCondition.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Chain other = (Chain) obj;
		if (this.getContainer() == null) {
			if (other.getContainer() != null){
				return false;
			}
		} else if (!this.getContainer().equals(other.getContainer())){
			return false;
		}
		
		try{
			if (chainCondition == null) {
				if (other.chainCondition != null){
					return false;
				}
			} else if (!chainCondition.equals(other.chainCondition)){
				return false;
			}
		}catch(ClassCastException e){
			return false;
		}
		return true;
	}

	public E getFirst(){
		return this.getContainer().getFirst();
	}
	
	public E getLast(){
		return this.getContainer().getLast();
	}
	
	public E get(int index){
		return this.getContainer().get(index);
	}
	
	public int size(){
		return this.getContainer().size();
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return this.getContainer().iterator();
	}
	
	@Override
	public String toString() {
		return this.getContainer().toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Chain<E> clone() {
		Chain<E> cloned = null;
		try {
			cloned = (Chain<E>)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		assert(cloned != null);
		cloned.setContainer((LinkedList<E>)this.getContainer().clone());
		assert(cloned.getContainer() != null);
		return cloned;
	}

	protected LinkedList<E> getContainer() {
		return container;
	}

	protected void setContainer(LinkedList<E> container) {
		if(container == null){
			throw new NullPointerException();
		}
		this.container = container;
	}

}
