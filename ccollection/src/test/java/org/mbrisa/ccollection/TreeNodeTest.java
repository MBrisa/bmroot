package org.mbrisa.ccollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void testException(){
//		Exception
		TreeNode<Integer> n1 = new TreeNode<>(0);
		try{
			n1.add(n1); // can not add self
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		TreeNode<Integer> n2 = new TreeNode<>(0);
		assertTrue(n1.add(n2));
		assertFalse(n1.add(n2));
		TreeNode<Integer> n3 = new TreeNode<>(0);
		assertFalse(n3.add(n2)); //n2 exists parent
		assertTrue(n2.add(n3));
		assertFalse(n1.add(n3)); // n3 exists parent
		TreeNode<Integer> added0 = n1.add(0);
		iteratorTest(added0,added0);
		iteratorTest(n1, n1,n2,n3,added0);
		
		TreeNode<Integer> node = new TreeNode<>(0);
		iteratorTest(node, node);
		TreeNode<Integer> added1 = node.add(1);
		TreeNode<Integer> added2 = node.add(2);
		iteratorTest(node, node,added1,added2);
		
	}
	
	@Test
	public void nomalTest(){
//		Normal
		TreeNode<Integer> root = new TreeNode<>(0);
		TreeNode<Integer> c1 = new TreeNode<Integer>(1);
		TreeNode<Integer> c2 = new TreeNode<Integer>(2);
		
		rootTest(root, root);
		rootTest(c1, c1);
		rootTest(c2, c2);
		
		iteratorTest(root, root);
		assertTrue(root.add(c1));
		rootTest(root, c1);
		iteratorTest(root, root,c1);
		assertTrue(root.add(c2));
		rootTest(root, c2);
		iteratorTest(root, root,c1,c2);
		iteratorTest(c1, c1);
		iteratorTest(c2, c2);
		TestUtil.dcTest(root, c1,c2);
		TestUtil.dcTest(c1);
		TestUtil.dcTest(c2);
		cloneTest(root);
		cloneTest(c1);
		cloneTest(c2);
		
		rootTest(root, root);
	}
	
	@Test
	public void subToAddTest(){
//		Sub to add
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> c1 = new TreeNode<Integer>(1);
		TreeNode<Integer> c2 = new TreeNode<Integer>(2);
		
		
		root.add(c1);
		rootTest(root, c1);
		c1.add(c2);
		rootTest(root, c2);
		iteratorTest(root, root,c1,c2);
		iteratorTest(c1, c1,c2);
		iteratorTest(c2, c2);
		TestUtil.dcTest(root, c1);
		TestUtil.dcTest(c1,c2);
		TestUtil.dcTest(c2);
		cloneTest(root);
		cloneTest(c1);
		cloneTest(c2);
		
		rootTest(root, root);
	}
	
	@Test
	public void addSubTree(){
//		add a sub tree
		TreeNode<Integer> root = new TreeNode<>(0);
		TreeNode<Integer> subRoot = new TreeNode<>(1);
		TreeNode<Integer> subChild = new TreeNode<>(2);
		
		
		subRoot.add(subChild);
		rootTest(subRoot, subChild);
		root.add(subRoot);
		rootTest(root,subRoot);
		rootTest(root,subChild);
		iteratorTest(root, root,subRoot,subChild);
		iteratorTest(subRoot, subRoot,subChild);
		iteratorTest(subChild, subChild);
		TestUtil.dcTest(root, subRoot);
		TestUtil.dcTest(subRoot, subChild);
		TestUtil.dcTest(subChild);
		cloneTest(root);
		cloneTest(subRoot);
		cloneTest(subChild);
		
		rootTest(root,root);
	}
	
	@Test
	public void addNull(){
//		add null
		TreeNode<Integer> root = new TreeNode<>(0);
		try{
			root.add((TreeNode<Integer>)null);
			assertTrue(false);
		}catch(NullPointerException e){
		}
		iteratorTest(root, root);
		TreeNode<Integer> intNull = new TreeNode<>((Integer)null);
		assertTrue(root.add(intNull));
		iteratorTest(root, root,intNull);
		TestUtil.dcTest(root, intNull);
		cloneTest(root);
		cloneTest(intNull);
		
		rootTest(root,intNull);
		rootTest(root,root);
		
		
		TreeNode<Integer> nullRoot = new TreeNode<Integer>(null);
		iteratorTest(nullRoot, nullRoot);
		try{
			nullRoot.add((TreeNode<Integer>)null); //add null
			assertTrue(false);
		}catch(NullPointerException e){
		}
	
		TestUtil.dcTest(nullRoot);
		cloneTest(nullRoot);
		rootTest(nullRoot,nullRoot);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void addMultinode(){
//		add multi-node
		TreeNode<Object> root0 = new TreeNode<>(null);
		TreeNode<Object> numberRoot = new TreeNode<>((Object)(-1));
		TreeNode<Object> stringRoot = new TreeNode<>((Object)"");
		TreeNode<Object> classRoot = new TreeNode<>((Object)Object.class);
		
		TreeNode<Object> intRoot = new TreeNode((Object)0);
		TreeNode<Object> int1 = new TreeNode((Object)1);
		TreeNode<Object> int50 = new TreeNode((Object)50);
		TreeNode<Object> longRoot = new TreeNode((Object)100L);
		TreeNode<Object> long1 = new TreeNode((Object)101L);
		TreeNode<Object> long2 = new TreeNode((Object)102L);
		
		
		TreeNode<Object> oneCharRoot = new TreeNode((Object)"a");
		TreeNode<Object> sb = new TreeNode((Object)"b");
		TreeNode<Object> sc = new TreeNode((Object)"c");
		TreeNode<Object> twoCharRoot = new TreeNode((Object)"aa");
		TreeNode<Object> sbb = new TreeNode((Object)"bb");
		TreeNode<Object> scc = new TreeNode((Object)"cc");
		
		TreeNode<Object> stringClass = new TreeNode<>((Object)String.class);
		TreeNode<Object> intgerClass = new TreeNode<>((Object)Integer.class);
		
//		顺序添加
		assertTrue(root0.add(numberRoot));
		assertTrue(numberRoot.add(intRoot));
		assertTrue(numberRoot.add(longRoot));
		assertTrue(intRoot.add(int1));
		assertTrue(intRoot.add(int50));
		assertTrue(longRoot.add(long1));
		assertTrue(long1.add(long2));
		
//		逆序添加
		assertTrue(sbb.add(scc));
		rootTest(sbb,scc);
		assertTrue(twoCharRoot.add(sbb));
		rootTest(twoCharRoot,sbb);
		rootTest(twoCharRoot,scc);
		assertTrue(oneCharRoot.add(sb));
		rootTest(oneCharRoot,sb);
		assertTrue(oneCharRoot.add(sc));
		rootTest(oneCharRoot,sc);
		assertTrue(stringRoot.add(oneCharRoot));
		rootTest(stringRoot,oneCharRoot);
		rootTest(stringRoot,sb);
		rootTest(stringRoot,sc);
		assertTrue(stringRoot.add(twoCharRoot));
		rootTest(stringRoot,twoCharRoot);
		rootTest(stringRoot,sbb);
		rootTest(stringRoot,scc);
		assertTrue(root0.add(stringRoot));
		
//		无序添加
		assertTrue(classRoot.add(stringClass));
		assertTrue(root0.add(classRoot));
		assertTrue(classRoot.add(intgerClass));
		
		
		iteratorTest(long2, long2);
		TestUtil.dcTest(long2);
		iteratorTest(long1, long1,long2);
		TestUtil.dcTest(long1,long2);
		iteratorTest(longRoot, longRoot,long1,long2);
		TestUtil.dcTest(longRoot,long1);
		iteratorTest(int1, int1);
		TestUtil.dcTest(int1);
		iteratorTest(int50, int50);
		TestUtil.dcTest(int50);
		iteratorTest(intRoot, intRoot,int1,int50);
		TestUtil.dcTest(intRoot,int1,int50);
		iteratorTest(numberRoot, numberRoot,intRoot,int1,int50,longRoot,long1,long2);
		TestUtil.dcTest(numberRoot,intRoot,longRoot);
		
		iteratorTest(scc, scc);
		TestUtil.dcTest(scc);
		iteratorTest(sbb, sbb,scc);
		TestUtil.dcTest(sbb,scc);
		iteratorTest(twoCharRoot, twoCharRoot,sbb,scc);
		TestUtil.dcTest(twoCharRoot,sbb);
		iteratorTest(sb, sb);
		TestUtil.dcTest(sb);
		iteratorTest(sc, sc);
		TestUtil.dcTest(sc);
		iteratorTest(oneCharRoot, oneCharRoot,sb,sc);
		TestUtil.dcTest(oneCharRoot,sb,sc);
		iteratorTest(stringRoot, stringRoot,oneCharRoot,sb,sc,twoCharRoot,sbb,scc);
		TestUtil.dcTest(stringRoot,oneCharRoot,twoCharRoot);
		
		iteratorTest(stringClass, stringClass);
		TestUtil.dcTest(stringClass);
		iteratorTest(intgerClass, intgerClass);
		TestUtil.dcTest(intgerClass);
		iteratorTest(classRoot, classRoot,stringClass,intgerClass);
		TestUtil.dcTest(classRoot,stringClass,intgerClass);
		
		iteratorTest(root0, root0,numberRoot,intRoot,int1,int50,longRoot,long1,long2,stringRoot,oneCharRoot,sb,sc,twoCharRoot,sbb,scc,classRoot,stringClass,intgerClass);
		TestUtil.dcTest(root0,numberRoot,stringRoot,classRoot);
		
		cloneTest(root0);
		cloneTest(numberRoot);
		cloneTest(stringRoot);
		cloneTest(classRoot);
		cloneTest(intRoot);
		cloneTest(int1);
		cloneTest(int50);
		cloneTest(longRoot);
		cloneTest(long1);
		cloneTest(long2);
		cloneTest(oneCharRoot);
		cloneTest(sb);
		cloneTest(sc);
		cloneTest(twoCharRoot);
		cloneTest(sbb);
		cloneTest(sbb);
		cloneTest(stringClass);
		cloneTest(intgerClass);
		
		rootTest(root0,root0);
		rootTest(root0,numberRoot);
		rootTest(root0,stringRoot);
		rootTest(root0,classRoot);
		rootTest(root0,intRoot);
		rootTest(root0,int1);
		rootTest(root0,int50);
		rootTest(root0,longRoot);
		rootTest(root0,long1);
		rootTest(root0,long2);
		rootTest(root0,oneCharRoot);
		rootTest(root0,sb);
		rootTest(root0,sc);
		rootTest(root0,twoCharRoot);
		rootTest(root0,sbb);
		rootTest(root0,scc);
		rootTest(root0,stringClass);
		rootTest(root0,intgerClass);
		
	}
	
	
	private void iteratorTest(TreeNode<?> treeNode,TreeNode<?>... nodes){
		assertEquals(nodes.length,treeNode.size());
		int i = 0;
		for(Object n : treeNode){
			assertEquals(nodes[i++],n);
		}
	}
	
	private void rootTest(TreeNode<?> expect,TreeNode<?> target){
		assertTrue(expect == getRoot(target));
	}
	
	private <E> TreeNode<E> getRoot(TreeNode<E> target){
		TreeNode<E> tRoot = target;
		while(tRoot.getParent() != null){
			tRoot = tRoot.getParent();
		}
		return tRoot;
	}
	
	private void cloneTest(TreeNode<?> original){
		TreeNode<?> cloned = original.clone();
		cloneTest(original, cloned);
		TreeNode<?> op = original.getParent();
		TreeNode<?> cp = cloned.getParent();
		while(op != null){
			cloneTest(op, cp);
			op = op.getParent();
			cp = cp.getParent();
		}
	}
	
	private void cloneTest(TreeNode<?> original,TreeNode<?> cloned){
		TreeNode<?> oriRoot = getRoot(original);
		TreeNode<?> cloRoot = getRoot(cloned);
		
		assertFalse(oriRoot == cloRoot);
		assertTrue(oriRoot.entity() == cloRoot.entity());
		assertEquals(original.size(),cloned.size());
		int i = 0;
		for(Object entry : original){
			int j = 0;
			boolean compared = false;
			for(Object clonedEntry : original){
				if(j++ == i){
					assertEquals(entry, clonedEntry);
					compared = true;
					break;
				}
			}
			assertTrue(compared);
			i++;
		}
		assertEquals(original.children().size(),cloned.children().size());
		i = 0;
		for(TreeNode<?> child : original.children()){
			TreeNode<?> clonedChild = cloned.children().get(i++);
			assertEquals(child.size(),clonedChild.size());
			assertEquals(child.entity(),clonedChild.entity());
			cloneTest(child, clonedChild);
		}
	}
	
}
