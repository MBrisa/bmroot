package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChainBuilderTest {
	
	@Test
	public void go(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.serialCondition,new IgnoreNoCompleteHandler());
		builder.addNode(0);
		TestUtil.orderTest(builder.retrieveChain(), 0);
		
		builder.addNode(2);
		TestUtil.orderTest(builder.retrieveChain(), 0);
		
		builder.addNode(-2);
		TestUtil.orderTest(builder.retrieveChain(), 0);
		
		builder.addNode(1);
		TestUtil.orderTest(builder.retrieveChain(), 0,1,2);
		
		builder.addNode(-1);
		TestUtil.orderTest(builder.retrieveChain(), -2,-1,0,1,2);
	}
	
	@Test
	public void go2(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.serialCondition,new GruffNoCompleteHandler());
		assertEquals(0,builder.retrieveChain().size());
		
		builder.addNode(1);
		TestUtil.orderTest(builder.retrieveChain(), 1);
		
		builder.addNode(3);
		try{
			builder.retrieveChain();
			assertTrue(false);
		}catch(NoCompleteException e){
			assertTrue(true);
		}
	}
	
	@Test
	public void go3(){
		ChainBuilder<Integer> builder = new ChainBuilder<Integer>(TestUtil.raySerialCondition,new GruffNoCompleteHandler());
		
		builder.addNode(1);
		try{
			builder.retrieveChain();
			assertTrue(false);
		}catch(Exception e){
			assertTrue(true);
		}
		
		builder.addNode(0);
		TestUtil.orderTest(builder.retrieveChain(), 0,1);
		
		builder.addNode(-1);
		try{
			builder.retrieveChain();
			assertTrue(false);
		}catch(Exception e){
			assertTrue(true);
		}
		assertEquals(1,builder.retrieveScrap().size());
		Integer ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		builder.addNode(0);
		try{
			builder.retrieveChain();
			assertTrue(false);
		}catch(Exception e){
			assertTrue(true);
		}
		assertEquals(2,builder.retrieveScrap().size());
		ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		builder.addNode(1);
		try{
			builder.retrieveChain();
			assertTrue(false);
		}catch(Exception e){
			assertTrue(true);
		}
		assertEquals(3,builder.retrieveScrap().size());
		ci = -1;
		for(Integer item : builder.retrieveScrap()){
			assertEquals(ci++,item);
		}
		
		
	}
	

}
