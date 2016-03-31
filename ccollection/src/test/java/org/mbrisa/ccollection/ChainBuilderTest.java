package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChainBuilderTest {
	
	@Test
	public void go(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.serialCondition,new IgnoreNoCompleteHandler());
		builder.add(0);
		TestUtil.orderTest(builder.retrieve(), 0);
		assertTrue(builder.isComplete());
		
		builder.add(2);
		TestUtil.orderTest(builder.retrieve(), 0);
		assertFalse(builder.isComplete());
		
		builder.add(-2);
		TestUtil.orderTest(builder.retrieve(), 0);
		assertFalse(builder.isComplete());
		
		builder.add(1);
		TestUtil.orderTest(builder.retrieve(), 0,1,2);
		assertFalse(builder.isComplete());
		
		builder.add(-1);
		TestUtil.orderTest(builder.retrieve(), -2,-1,0,1,2);
		assertTrue(builder.isComplete());
		
		cleanTest(builder);
	}
	
	@Test
	public void go2(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.serialCondition,new GruffNoCompleteHandler());
		TestUtil.orderTest(builder.retrieve());
		
		builder.add(1);
		TestUtil.orderTest(builder.retrieve(), 1);
		
		builder.add(3);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(NoCompleteException e){
			assertTrue(true);
		}
		
		cleanTest(builder);
	}
	
	@Test
	public void go3(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.raySerialConditionToBuild,new GruffNoCompleteHandler());
		
		builder.add(1);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(Exception e){
		}
		
		builder.add(0);
		TestUtil.orderTest(builder.retrieve(), 0,1);
		
		builder.add(-1);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(Exception e){
		}
		assertEquals(1,builder.retrieveScrap().size());
		Integer ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		builder.add(0);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(Exception e){
		}
		assertEquals(2,builder.retrieveScrap().size());
		ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		builder.add(1);
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(Exception e){
		}
		assertEquals(3,builder.retrieveScrap().size());
		ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		cleanTest(builder);
	}
	
	private void cleanTest(ChainBuilder<?> builder){
		builder.clear();
		assert(builder.isComplete());
		TestUtil.orderTest(builder.retrieve());
		assertEquals(0,builder.retrieveScrap().size());
	}
	

}
