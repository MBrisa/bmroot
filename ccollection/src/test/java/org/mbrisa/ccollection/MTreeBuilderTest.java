package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class MTreeBuilderTest {
	
	@Test
	public void exceptionTest(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild);
		assertNull(builder.retrieveFirst());
		builder.add(1);
		assertEquals(0,builder.size());
		assertEquals(0,builder.treeSize());
		scrapTest(builder, 1);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
		try{
			builder.retrieveFirst();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
		cleanTest(builder);
		
		builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild,new IgnoreNoCompleteHandler());
		assertNull(builder.retrieveFirst());
		builder.add(1);
		assertEquals(0,builder.size());
		assertEquals(0,builder.treeSize());
		scrapTest(builder, 1);
		assertNull(builder.retrieveFirst());
		iteratorTest(builder);
		
		cleanTest(builder);
		
	}
	
	@Test
	public void simple(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.serialCondition);
		builder.add(0);
		scrapTest(builder);
		iteratorTest(builder, new Object[]{0});
		builder.add(0);
		scrapTest(builder);
		iteratorTest(builder, new Object[]{0},new Object[]{0});
		
		
		builder.add(1);
		scrapTest(builder);
		iteratorTest(builder, new Object[]{0,1},new Object[]{0});
		
		builder.add(3);
		scrapTest(builder);
		iteratorTest(builder, new Object[]{0,1},new Object[]{0},new Object[]{3});
		
		builder.add(2);
		scrapTest(builder);
		iteratorTest(builder, new Object[]{0,1,2,3},new Object[]{0});
		
		cleanTest(builder);
	}
	
	@Test
	public void rootStrict(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild,new IgnoreNoCompleteHandler());
		
		builder.add(1);
		iteratorTest(builder);
		scrapTest(builder,1);
		
		
		builder.add(0);
		iteratorTest(builder,new Object[]{0,1});
		scrapTest(builder);
		
		builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild,new IgnoreNoCompleteHandler());
		builder.add(1);
		builder.add(2);
		builder.add(3);
		builder.add(2);
		builder.add(5);
		
		iteratorTest(builder);
		scrapTest(builder,1,2,3,2,5);
		
		builder.add(0);
		iteratorTest(builder,new Object[]{0,1,2,3,2});
		scrapTest(builder,5);
		
		cleanTest(builder);
	}
	
	
	private <E> void iteratorTest(MTreeBuilder<E> builder,Object[]... eachTree){
		assertEquals(eachTree.length,builder.treeSize());
		int rootCount = 0;
		for(CTree<E> tree : builder.retrieve()){
			Object[] atree = eachTree[rootCount];
			assertEquals(atree.length , tree.size());
			int i = 0;
			for(E e : tree){
				assertEquals(atree[i++],e);
			}
			rootCount ++;
		}
		assertEquals(eachTree.length,rootCount);
		
		int totalSize = 0;
		for(Object[] tree : eachTree){
			totalSize += tree.length;
		}
		assertEquals(totalSize,builder.size());
	}
	
	
	private <E> void scrapTest(MTreeBuilder<E> builder,Object... children){
		assertEquals(children.length,builder.retrieveScrap().size());
		int i = 0;
		for(Object child : builder.retrieveScrap()){
			assertEquals(children[i++],child);
		}
	}
	
	private void cleanTest(MTreeBuilder<?> builder){
		builder.clear();
		iteratorTest(builder);
		scrapTest(builder);
		assertNull(builder.retrieveFirst());
	}
	
}
