package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class MTreeBuilderTest {
	
	@Test
	public void exceptionTest(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild);
		assertNull(builder.retrieveFirstRoot());
		builder.add(1);
		assertEquals(0,builder.nodeSize());
		assertEquals(0,builder.treeSize());
		scrapTest(builder, 1);
		try{
			builder.retrieveRoots();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
		try{
			builder.retrieveFirstRoot();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
		assertNull(builder.getFirstRoot());
		iteratorTest(builder);
	}
	
	@Test
	public void simple(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.serialConditionToBuild);
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
	}
	
	@Test
	public void rootStrict(){
		MTreeBuilder<Integer> builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild);
		
		builder.add(1);
		iteratorTest(builder);
		scrapTest(builder,1);
		
		
		builder.add(0);
		iteratorTest(builder,new Object[]{0,1});
		scrapTest(builder);
		
		builder = new MTreeBuilder<>(TestUtil.raySerialConditionToBuild);
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
	}
	
	
	private <E> void iteratorTest(MTreeBuilder<E> builder,Object[]... eachTree){
		assertEquals(eachTree.length,builder.treeSize());
		int rootCount = 0;
		for(TreeNode<E> root : builder.getRoots()){
			Object[] atree = eachTree[rootCount];
			assertEquals(atree.length , root.size());
			int i = 0;
			for(E e : root){
				assertEquals(atree[i++],e);
			}
			rootCount ++;
		}
		assertEquals(eachTree.length,rootCount);
		
		int totalSize = 0;
		for(Object[] tree : eachTree){
			totalSize += tree.length;
		}
		assertEquals(totalSize,builder.nodeSize());
	}
	
	
	private <E> void scrapTest(MTreeBuilder<E> builder,Object... children){
		assertEquals(children.length,builder.getScrap().size());
		int i = 0;
		for(Object child : builder.getScrap()){
			assertEquals(children[i++],child);
		}
	}
	
	
}
