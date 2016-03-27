package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mbrisa.ccollection.MChainBuilder;

public class MChainBuilderTest{
	
	@Test
	public void simpleTest(){
		MChainBuilder<Integer> builder = new MChainBuilder<Integer>(TestUtil.serialConditionToBuild);
		
		builder.addNode(0);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0);
		
		builder.addNode(1);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		
		builder.addNode(3);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3);
		
		builder.addNode(4);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		
		builder.addNode(6);
		assertEquals(3,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		TestUtil.orderTest(builder.retrieveChains().get(2), 6);
		
		builder.addNode(1);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		TestUtil.orderTest(builder.retrieveChains().get(2), 6);
		TestUtil.orderTest(builder.retrieveChains().get(3), 1);
		
		builder.addNode(0);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		TestUtil.orderTest(builder.retrieveChains().get(2), 6);
		TestUtil.orderTest(builder.retrieveChains().get(3), 0,1);
		
		builder.addNode(3);
		assertEquals(5,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		TestUtil.orderTest(builder.retrieveChains().get(2), 6);
		TestUtil.orderTest(builder.retrieveChains().get(3), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(4), 3);
		
		builder.addNode(-1);
		assertEquals(5,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), -1,0,1);
		TestUtil.orderTest(builder.retrieveChains().get(1), 3,4);
		TestUtil.orderTest(builder.retrieveChains().get(2), 6);
		TestUtil.orderTest(builder.retrieveChains().get(3), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(4), 3);
		
		builder.addNode(2);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), -1,0,1,2,3,4);
		TestUtil.orderTest(builder.retrieveChains().get(1), 6);
		TestUtil.orderTest(builder.retrieveChains().get(2), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(3), 3);
		
		builder.addNode(7);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), -1,0,1,2,3,4);
		TestUtil.orderTest(builder.retrieveChains().get(1), 6,7);
		TestUtil.orderTest(builder.retrieveChains().get(2), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(3), 3);
		
		builder.addNode(5);
		assertEquals(3,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), -1,0,1,2,3,4,5,6,7);
		TestUtil.orderTest(builder.retrieveChains().get(1), 0,1);
		TestUtil.orderTest(builder.retrieveChains().get(2), 3);
		
		builder.addNode(2);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), -1,0,1,2,3,4,5,6,7);
		TestUtil.orderTest(builder.retrieveChains().get(1), 0,1,2,3);
	}
	
	@Test
	public void go(){
		MChainBuilder<Integer> builder = new MChainBuilder<Integer>(TestUtil.raySerialConditionToBuild);
		
		builder.addNode(1);
		assertEquals(0,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)1,builder.retrieveScrap().get(0));
		
		builder.addNode(0);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1);
		
		builder.addNode(2);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1,2);
		
		builder.addNode(4);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		
		builder.addNode(1);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(2,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		assertEquals((Integer)1,builder.retrieveScrap().get(1));
		
		
		builder.addNode(2);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(3,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		assertEquals((Integer)1,builder.retrieveScrap().get(1));
		assertEquals((Integer)2,builder.retrieveScrap().get(2));
		
		builder.addNode(0);
		assertEquals(2,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		
		builder.addNode(3);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieveChains().get(0), 0,1,2,3,4);
		TestUtil.orderTest(builder.retrieveChains().get(1), 0,1,2);
		assertEquals(0,builder.retrieveScrap().size());
		
	}
	
	private void canNotRetrieve(MChainBuilder<Integer> builder){
		try{
			builder.retrieveChains();
			assertTrue(false);
		}catch(NoCompleteException e){
			assertTrue(true);
		}
	}

}
