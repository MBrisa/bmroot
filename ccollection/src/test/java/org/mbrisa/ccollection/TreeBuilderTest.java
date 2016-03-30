package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mbrisa.ccollection.TestUtil.Node;

public class TreeBuilderTest {
	
	@Test
	public void exceptionTest(){
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>(TestUtil.raySerialConditionToBuild); 
		iteratorTest(builder);
		assertNull(builder.retrieve());
		scrapTest(builder);
		
		builder.add(1);
		
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
		scrapTest(builder,1);
		
		cleanTest(builder);
		
		builder = new TreeBuilder<Integer>(TestUtil.raySerialConditionToBuild,new IgnoreNoCompleteHandler()); 
		builder.clear(); 
		assertNull(builder.retrieve());
		scrapTest(builder);
		
		builder.add(0);
		builder.add(0);
		assertFalse(builder.isComplete());
		iteratorTest(builder, 0);
		scrapTest(builder,0);
		
		builder.add(1);
		assertFalse(builder.isComplete());
		iteratorTest(builder, 0,1);
		scrapTest(builder,0);
		
		builder.add(1);
		assertFalse(builder.isComplete());
		iteratorTest(builder,0,1,1);
		scrapTest(builder,0);
		
		cleanTest(builder);
		
		BuildingCondition<Integer> noLimitCondition = new BuildingCondition<Integer>() {
			@Override
			public boolean rejectNull() {
				return false;
			}
			
			@Override
			public boolean appendable(Integer target, Integer addition) {
				return true;
			}
			
			@Override
			public boolean headable(Integer addition) {
				return true;
			}
		};
		
		TreeBuilder<Integer> noLimitBuilder = new TreeBuilder<Integer>(noLimitCondition);
		noLimitBuilder.add(1);
		iteratorTest(noLimitBuilder, 1);
		noLimitBuilder.add(2);
		iteratorTest(noLimitBuilder, 2,1);
		
		cleanTest(noLimitBuilder);
	}
	
	@Test
	public void simple(){
		TreeBuilder<Integer> builder = new TreeBuilder<Integer>(TestUtil.raySerialConditionToBuild); 
		builder.add(3);
		builder.add(2);
		builder.add(1);
		scrapTest(builder,3,2,1);
		builder.add(0);
		iteratorTest(builder, 0,1,2,3);
		scrapTest(builder);
		builder.add(1);
		builder.add(2);
		iteratorTest(builder, 0,1,2,3,2,1);
		scrapTest(builder);
		
		cleanTest(builder);
		
		TreeBuilder<Node> nodeBuilder = new TreeBuilder<>(TestUtil.rayParentChildCondition,new IgnoreNoCompleteHandler());
		Node root = new Node(null, 0);
		Node node1 = new Node(0, 1);
		Node node2 = new Node(0, 2);
		nodeBuilder.add(node1);
		nodeBuilder.add(node2);
		
		iteratorTest(nodeBuilder);
		scrapTest(nodeBuilder,node1,node2);
		
		nodeBuilder.add(root);
		iteratorTest(nodeBuilder,root,node1,node2);
		scrapTest(nodeBuilder);
		
		cleanTest(nodeBuilder);
	}
	
	private <E> void iteratorTest(TreeBuilder<E> builder,Object... eachNode){
		assertEquals(eachNode.length,builder.size());
		TreeNode<E> root =  builder.retrieve();
		if(root != null){
			int i = 0;
			for(E e : root){
				assertEquals(eachNode[i++] , e);
			}
		}
	}
	
	private void scrapTest(TreeBuilder<?> builder,Object... nodes){
		assertEquals(nodes.length,builder.retrieveScrap().size());
		int i = 0;
		for(Object o : builder.retrieveScrap()){
			assertEquals(nodes[i++],o);
		}
	}
	
	private void cleanTest(TreeBuilder<?> builder){
		builder.clear();
		scrapTest(builder);
		iteratorTest(builder);
		assertTrue(builder.isComplete());
	}

}
