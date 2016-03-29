package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeBuilderTest {
	
	@Test
	public void exceptionTest(){
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>(TestUtil.raySerialConditionToBuild); 
		iteratorTest(builder);
		assertNull(builder.getRoot());
		scrapTest(builder);
		
		builder.add(1);
		
		iteratorTest(builder);
		assertNull(builder.getRoot());
		scrapTest(builder,1);
		try{
			builder.retrieveRoot();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
	}
	
	@Test
	public void simple(){
		
	}
	
	private <E> void iteratorTest(TreeBuilder<E> builder,Object... eachNode){
		assertEquals(eachNode.length,builder.nodeSize());
		TreeNode<E> root =  builder.getRoot();
		if(root != null){
			int i = 0;
			for(E e : root){
				assertEquals(eachNode[i++] , e);
			}
		}
	}
	
	private void scrapTest(TreeBuilder<?> builder,Object... nodes){
		assertEquals(nodes.length,builder.getScrap().size());
		int i = 0;
		for(Object o : builder.getScrap()){
			assertEquals(nodes[i++],o);
		}
	}

}
