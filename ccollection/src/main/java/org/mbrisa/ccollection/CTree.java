package org.mbrisa.ccollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CTree<E> implements Collection<E> {
	
	private TreeNode<E> root;
	private final LinkedCondition<E> condition;
	
	public CTree(LinkedCondition<E> condition) {
		if(condition == null){
			throw new NullPointerException();
		}
		this.condition = condition;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
//		if(this.condition != addition.getCondition()){
//			throw new RuntimeException("去除 treeNode 中的 condition");
//		}
		if(e == null && this.condition.rejectNull()){
			throw new NullPointerException();
		}
		TreeNode<E> addition = new TreeNode<>(e); 
		if(this.root == null){
			if(this.condition.headable(e)){
				this.root = addition;
				return true;
			}
			return false;
		}
		//首先将 addition 作为 root 进行添加，因为这个过程不需要对原有 root 进行迭代。
		if(this.condition.headable(addition.entity()) && this.condition.appendable(addition.entity(), this.root.entity()) && addition.add(this.root)){ // first try to become a root
			this.root = addition;
			return true;
		}
		for(TreeNode<E> node : this.root.retrieveAllNode()){
			if(this.condition.appendable(node.entity(), addition.entity())){ // addition is child
				return node.add(addition);
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.root == null ? 0 : this.root.allNodes.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.root == null;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		if(this.root == null)
			return false;
		if(o == null){
			for(E e : this){
				if(e == null){
					return true;
				}
			}
			return false;
		}else{
			for(E e : this){
				if(o.equals(e)){
					return true;
				}
			}
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return this.root == null ? new EmptyIterator() : new EIterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[this.size()];
		if(this.root == null)
			return result;
		
        int i = 0;
        for(TreeNode<E> node : this.root.allNodes)
        	 result[i++] = node.entity();
        return result;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		
		if(this.root == null)
			return a;
		
		if (a.length < this.size())
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), this.size());
        int i = 0;
        Object[] result = a;
        for(TreeNode<E> node : this.root.allNodes)
            result[i++] = node.entity();

        if (a.length > this.size())
            a[this.size()] = null;

        return a;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		if(this.root == null)
			return c.isEmpty();
		for(Object o : c)
			if(!this.contains(o))
				return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.root = null;
	}
	
	 /**
     * An optimized version of AbstractList.Itr
     */
    private class EmptyIterator implements Iterator<E> {
        public boolean hasNext() {
            return false;
        }
        public E next() {
        	throw new NoSuchElementException();
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private class EIterator implements Iterator<E>{
		private Iterator<TreeNode<E>> iterator = CTree.this.root.allNodes.iterator();
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}
		@Override
		public E next() {
			return iterator.next().entity();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
}
