package org.mbrisa.ccollection;

import static org.junit.Assert.*;


import org.junit.Test;

public class TreeBuilderTest {
	
	@Test
	public void simple(){
		int n_0 = 0;
		int n_1 = 1;
		int n__1 = -1;
		int n_3 = 3;
		int rn__1 = -1;
		int rn_0 = 0;
		int n__2 = -2;
		int n__3 = -3;
		
		TreeBuilder<Integer> builder = new TreeBuilder<>(TestUtil.serialCondition);
		
		/*
		 0
		 */
		builder.add(n_0); 
		assertEquals(1,builder.size()); 
		assertDirectChildrenWithParent(builder,n_0,0);
		iteratorTest(builder,n_0,0);
		scrapTest(builder);
		
		/*
		 0
		 1
		 */
		builder.add(n_1);
		assertEquals(2,builder.size()); 
		assertDirectChildrenWithParent(builder,n_0,0,n_1);
		iteratorTest(builder,n_0,0,n_1);
		scrapTest(builder);
		
		/*
		 -1
		 0
		 1
		 */
		builder.add(n__1);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder);
		
		/*
		 -1
		 0
		 1
		 
		 scrap:
		 -3
		 */
		builder.add(n__3);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n__3);
		
		/*
		 -1
		 0
		 1
		 
		 scrap:
		 -3,3
		 */
		builder.add(n_3);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n__3,n_3);
		
		try{
			builder.add(rn__1);
			assertTrue(false);
		}catch(NodeRepeatException e){
			assertTrue(true);
		}
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n__3,n_3);
		
		try{
			builder.add(rn_0);
			assertTrue(false);
		}catch(NodeRepeatException e){
			assertTrue(true);
		}
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n__3,n_3);
		
		/*
		 -3
		 -2
		 -1
		 0
		 1
		 */
		builder.add(n__2);
		assertEquals(5,builder.size()); 
		assertDirectChildrenWithParent(builder,n__3,0, n__2);
		assertDirectChildrenWithParent(builder,n__2,0, n__1);
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__3,0,n__2,n__1,n_0,n_1);
		scrapTest(builder, n_3);
		
	}
	
	@Test
	public void rootStrict(){
		TreeBuilder<Integer> builder = new TreeBuilder<>(TestUtil.raySerialCondition,new LazyRepeatHandler<Integer>());
		
		builder.add(1);
		assertEquals(0,builder.size());
		builder.add(0);
		assertEquals(2,builder.size());
	}
	
	@Test
	public void repeatable(){
//		TreeNode<Integer> n_0 = new TreeNode<>(0);
//		TreeNode<Integer> n_1 = new TreeNode<>(1);
//		TreeNode<Integer> n_2 = new TreeNode<>(2);
//		TreeNode<Integer> rn_1 = new TreeNode<>(1);
//		TreeNode<Integer> n__1 = new TreeNode<>(-1);
//		TreeNode<Integer> n_3 = new TreeNode<>(3);
//		TreeNode<Integer> rn__1 = new TreeNode<>(-1);
//		TreeNode<Integer> rn_0 = new TreeNode<>(0);
//		TreeNode<Integer> n__2 = new TreeNode<>(-2);
//		TreeNode<Integer> n__3 = new TreeNode<>(-3);
		
		int n_0 = 0;
		int n_1 = 1;
		int n_2 = 2;
		int rn_1 = 1;
		int n__1 = -1;
		int n_3 = 3;
		int rn__1 = -1;
		int rn_0 = 0;
		int n__2 = -2;
		int n__3 = -3;
		
		TreeBuilder<Integer> builder = new TreeBuilder<>(TestUtil.serialCondition,new LazyRepeatHandler<Integer>());
		
		/*
		 0
		 */
		builder.add(n_0); 
		assertEquals(1,builder.size()); 
		assertDirectChildrenWithParent(builder,n_0,0);
		iteratorTest(builder,n_0,0);
		scrapTest(builder);
		
		/*
		 0
		 1
		 */
		builder.add(n_1);
		assertEquals(2,builder.size()); 
		assertDirectChildrenWithParent(builder,n_0,0,n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n_0,0,n_1);
		scrapTest(builder);
		
		/*
		 -1
		 0
		 1
		 */
		builder.add(n__1);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder);
		
		/*
		 -1
		 0
		 1
		 
		 scrap : 
		 3
		 */
		builder.add(n_3);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n_3);
		
		/*
		 -1
		 0
		 1
		 
		 scrap : 
		 3,-3
		 */
		builder.add(n__3);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n_3,n__3);
		
		/*
		 -1
		 0
		 1
		 
		 scrap : 
		 3,-3,-1
		 */
		builder.add(rn__1);
		assertEquals(3,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1);
		scrapTest(builder, n_3,n__3,rn__1);
		
		
		/*
		 -1
		 0,0
		 1
		 
		 scrap : 
		 3,-3,-1
		 */
		builder.add(rn_0);
		assertEquals(4,builder.size()); 
		assertDirectChildrenWithParent(builder,n__1,0, n_0,rn_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,rn_0,1);
		assertDirectChildrenWithParent(builder,n_1,0);
		iteratorTest(builder,n__1,0,n_0,n_1,rn_0);
		scrapTest(builder, n_3,n__3,rn__1);
		
		
		/*
		 -3
		 -2
		 -1,    -1
		 0,0
		 1
		 
		 scrap : 
		 3,
		 */
		builder.add(n__2);
		assertEquals(7,builder.size()); 
		assertDirectChildrenWithParent(builder,n__3,0, n__2);
		assertDirectChildrenWithParent(builder,n__2,0, n__1,rn__1);
		assertDirectChildrenWithParent(builder,rn__1,1);
		assertDirectChildrenWithParent(builder,n__1,0, n_0,rn_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		assertDirectChildrenWithParent(builder,rn_0,1);
		iteratorTest(builder,n__3,0,n__2,n__1,n_0,n_1,rn_0,rn__1);
		scrapTest(builder, n_3);
		
		/*
		 -3
		 -2
		 -1,    -1
		0, 0
	   1,1
	   
	   scrap : 
		 3,
		 */
		builder.add(rn_1);
		assertEquals(8,builder.size()); 
		assertDirectChildrenWithParent(builder,n__3,0, n__2);
		assertDirectChildrenWithParent(builder,n__2,0, n__1,rn__1);
		assertDirectChildrenWithParent(builder,rn__1,1);
		assertDirectChildrenWithParent(builder,n__1,0, n_0,rn_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1,rn_1);
		assertDirectChildrenWithParent(builder,n_1,0);
		assertDirectChildrenWithParent(builder,rn_0,1);
		assertDirectChildrenWithParent(builder,rn_1,1);
		iteratorTest(builder,n__3,0,n__2,n__1,n_0,n_1,rn_1,rn_0,rn__1);
		scrapTest(builder, n_3);
		
		/*
		 -3
		 -2
		 -1,    -1
		0,  0
	   1, 1
	   2
	   3
		 */
		builder.add(n_2);
		assertEquals(10,builder.size()); 
		assertDirectChildrenWithParent(builder,n__3,0, n__2);
		assertDirectChildrenWithParent(builder,n__2,0, n__1,rn__1);
		assertDirectChildrenWithParent(builder,rn__1,1);
		assertDirectChildrenWithParent(builder,n__1,0, n_0,rn_0);
		assertDirectChildrenWithParent(builder,n_0,0, n_1,rn_1);
		assertDirectChildrenWithParent(builder,rn_0,1);
		assertDirectChildrenWithParent(builder,n_1,0,n_2);
		assertDirectChildrenWithParent(builder,rn_1,1);
		assertDirectChildrenWithParent(builder,n_2,0,n_3);
		assertDirectChildrenWithParent(builder,n_3,0);
		iteratorTest(builder,n__3,0,n__2,n__1,n_0,n_1,n_2,n_3,rn_1,rn_0,rn__1);
		scrapTest(builder);
		
	}
	
	private <E> void assertDirectChildrenWithParent(TreeBuilder<E> builder,E pe,int index,Object... children){
		TreeNode<E> parent = builder.retrieveSilently(pe, index);
		assertEquals(children.length , parent.children().size());
		int i = 0;
		for(Object child : children){
			assertEquals(child,parent.children().get(i++).entity());
		}
	}
	
	private <E> void iteratorTest(TreeBuilder<E> builder,E pe,int index,Object... children){
		TreeNode<E> parent = builder.retrieveSilently(pe, index);
		int i = 0;
		for(TreeNode<E> child : parent){
			assertEquals(children[i++],child.entity());
		}
	}
	
	private <E> void scrapTest(TreeBuilder<E> builder,Object... children){
		assertEquals(children.length,builder.retrieveScrap().size());
		int i = 0;
		for(Object child : builder.retrieveScrap()){
			assertEquals(children[i++],child);
		}
	}
	
}
