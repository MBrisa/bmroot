package org.mbrisa.ccollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mbrisa.ccollection.Chain;
import org.mbrisa.ccollection.NoCompatibilityException;
import org.mbrisa.ccollection.TestUtil.Node;

public class ChainTest{
	
	@Test
	public void exceptionTest(){
		Chain<Integer> chain = new Chain<Integer>();
		assertTrue(chain.add((Integer)null));
		try{
			chain.add(chain); // add self
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		assertFalse(chain.add(new Chain<Integer>())); //add empty
		Chain<Integer> chain2 = new Chain<Integer>(TestUtil.serialCondition);
		chain.add(2);
		try{
			chain.add(chain2);// condition is not same
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		try{
			chain2.add(chain);// condition is not same
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		
		Chain<Node> nChain = new Chain<>();
		Node root = new Node(null, 0);
		assertTrue(nChain.add(root));
		assertTrue(nChain.add(root)); // add the same ob
		TestUtil.orderTest(nChain, root,root);
		
	}
	
	@Test
	public void simpleTest() {
		Chain<Integer> chain = new Chain<Integer>(TestUtil.serialCondition);
		
		assertTrue(chain.add(1));
		assertTrue(chain.add(0));
		assertFalse(chain.add(3));
		assertTrue(chain.add(-1));
		assertFalse(chain.add(-3));
		
		TestUtil.orderTest(chain, -1,0,1);
		try{
			chain.add((Integer)null);
			assertTrue(false);
		}catch(NullPointerException e){
			assertTrue(true);
		}
		
		TestUtil.orderTest(chain, -1,0,1);
	}
	
	@Test
	public void conditionTest1(){
		Node root = new Node(null, 0);
		Node level1 = new Node(0, 1);
		Node level2 = new Node(1, 2);
		Node level2_no = new Node(1, 2);
		Node level3 = new Node(2,3);
		
		Chain<Node> chain = new Chain<Node>(TestUtil.parentChildCondition);
		
		assertTrue(chain.add(level1));
		TestUtil.orderTest(chain, level1);
		
		assertTrue(chain.add(level2));
		TestUtil.orderTest(chain, level1,level2);
		
		assertFalse(chain.add(level2_no));
		TestUtil.orderTest(chain, level1,level2);
		
		assertTrue(chain.add(level3));
		TestUtil.orderTest(chain, level1,level2,level3);
		
		assertTrue(chain.add(root));
		TestUtil.orderTest(chain, root,level1,level2,level3);
	}
	
	@Test
	public void conditionTest2(){
		Node root = new Node(null, 0);
		Node level1 = new Node(0, 1);
		
		Chain<Node> chain = new Chain<Node>(TestUtil.parentChildCondition);
		
		assertTrue(chain.add(level1));
		assertEquals(1,chain.size());
		
		assertTrue(chain.add(root));
		TestUtil.orderTest(chain, root,level1);
		
		assertFalse(chain.add(level1));
		TestUtil.orderTest(chain, root,level1);
		
	}
	
	@Test
	public void addSubChain() throws NoCompatibilityException{
		
		Chain<Integer> chain = new Chain<Integer>(TestUtil.serialCondition);
		
		chain.add(0);
		TestUtil.orderTest(chain, 0);
		
		Chain<Integer> subChain_no = new Chain<Integer>(TestUtil.serialCondition);
		subChain_no.add(2);
		subChain_no.add(3);
		
		TestUtil.orderTest(subChain_no, 2,3);
		
		assertFalse(chain.add(subChain_no));
		TestUtil.orderTest(chain, 0);
		assertFalse(subChain_no.add(chain));
		TestUtil.orderTest(subChain_no, 2,3);
		
		Chain<Integer> chain2 = new Chain<Integer>(TestUtil.serialCondition);
		chain2.add(2);
		chain2.add(1);
		
		TestUtil.orderTest(chain2, 1,2);
		
		assertTrue(chain.add(chain2));
		TestUtil.orderTest(chain, 0,1,2);
		TestUtil.orderTest(chain2, 1,2);
		
		Chain<Integer> chain3 = new Chain<Integer>(TestUtil.serialCondition);
		chain3.add(0);
		TestUtil.orderTest(chain3, 0);
		assertTrue(chain2.add(chain3));
		TestUtil.orderTest(chain2, 0,1,2);
		
		
		Chain<Integer> chain4 = new Chain<Integer>(TestUtil.serialCondition);
		chain4.add(0);
		
		Chain<Integer> chain5 = new Chain<Integer>(TestUtil.serialCondition);
		chain5.add(1);
		chain5.add(2);
		
		Chain<Integer> chain6 = new Chain<Integer>(TestUtil.serialCondition);
		chain6.add(3);
		chain6.add(2);
		
		assertTrue(chain4.add(chain5));
		assertFalse(chain4.add(chain6));
		TestUtil.orderTest(chain4, 0,1,2);
	}
	
	@Test
	public void toDel(){
		Chain<Integer> chain = new Chain<Integer>(TestUtil.serialCondition);
		chain.add(-1);
		chain.add(0);
		chain.add(1);
		chain.add(2);
		chain.add(3);
		chain.add(4);
		chain.add(5);
		TestUtil.orderTest(chain, -1,0,1,2,3,4,5);
		chain.remove(5);
		TestUtil.orderTest(chain, -1,0,1,2,3,4);
		chain.remove(3);
		TestUtil.orderTest(chain, -1,0,1,2);
		chain.remove(-1);
		assertEquals(0,chain.size());
	}
	
	@Test
	public void collectionTest(){
		Node root = new Node(null, 0);
		Node level1 = new Node(0, 1);
		Node level2 = new Node(1, 2);
		Node level2_no = new Node(1, 2);
		Node level3 = new Node(2,3);
		Node level4 = new Node(3, 4);
		
		Chain<Node> chain = new Chain<Node>(TestUtil.parentChildCondition);
		chain.add(level1);
		chain.add(level2);
		chain.add(level3);
		chain.add(root);
		
		assertTrue(chain.contains(root));
		assertTrue(chain.contains(level1));
		assertTrue(chain.contains(level2));
		assertTrue(chain.contains(level3));
		
		assertFalse(chain.contains(level2_no));
		assertFalse(chain.contains(level4));
		
		List<Node> list = new ArrayList<>();
		list.add(level1);
		list.add(level3);
		list.add(root);
		assertTrue(chain.containsAll(list));
		
		List<Node> list2 = new ArrayList<>();
		list2.add(level1);
		list2.add(level4);
		list2.add(root);
		assertFalse(chain.containsAll(list2));
		
		Chain<Node> toTest1 = new Chain<>(TestUtil.parentChildCondition);
		toTest1.add(root);
		toTest1.add(level1);
		toTest1.add(level2);
		toTest1.add(level3);
		assertTrue(chain.containsAll(toTest1));
		
		Chain<Node> toTest2 = toTest1.clone();
		assertEquals(toTest2,toTest1);
		assertEquals(4,toTest2.size());
		toTest2.clear();
		assertTrue(toTest2.isEmpty());
		assertEquals(0,toTest2.size());
		
		
		toTest2.add(root);
		toTest2.add(level1);
		toTest2.add(level2_no);
		toTest2.add(level3);
		assertFalse(chain.containsAll(toTest2));
		
		assertEquals(root,chain.getFirst());
		assertEquals(level3,chain.getLast());
		
		Object[] ob = chain.toArray();
		assertEquals(4,ob.length);
		assertEquals(root,ob[0]);
		assertEquals(level1,ob[1]);
		assertEquals(level2,ob[2]);
		assertEquals(level3,ob[3]);
		
		Node[] noa = chain.toArray(new Node[0]);
		assertEquals(4,noa.length);
		assertEquals(root,noa[0]);
		assertEquals(level1,noa[1]);
		assertEquals(level2,noa[2]);
		assertEquals(level3,noa[3]);
	}

}
