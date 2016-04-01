package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mbrisa.ccollection.MChainBuilder;

public class MChainBuilderTest{
	
	@Test
	public void fromPro(){
		LinkedCondition<Integer> condition = new LinkedCondition<Integer>() {
			@Override
			public boolean rejectNull() {
				return false;
			}
			@Override
			public boolean headable(Integer addition) {
				return addition != null && addition < 5;
			}
			@Override
			public boolean appendable(Integer target, Integer addition) {
				return target != null && addition != null && target - addition == -1;
			}
		};
		MChainBuilder<Integer> builder = new MChainBuilder<Integer>(condition);
		builder.add(0);
		builder.add(2);
		assertEquals(2,builder.chainCount());
		assertTrue(builder.isComplete());
		builder.add(5);
		assertEquals(2,builder.chainCount());
		assertFalse(builder.isComplete());
		builder.add(1);
		assertEquals(1,builder.chainCount());
	}
	
	@Test
	public void simpleTest(){
		MChainBuilder<Integer> builder = new MChainBuilder<Integer>(TestUtil.serialCondition);
		
		builder.add(0);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0);
		assertTrue(builder.isComplete());
		
		builder.add(1);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		assertTrue(builder.isComplete());
		
		builder.add(3);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3);
		assertTrue(builder.isComplete());
		
		builder.add(4);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		assertTrue(builder.isComplete());
		
		builder.add(6);
		assertEquals(3,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		TestUtil.orderTest(builder.retrieve().get(2), 6);
		assertTrue(builder.isComplete());
		
		builder.add(1);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		TestUtil.orderTest(builder.retrieve().get(2), 6);
		TestUtil.orderTest(builder.retrieve().get(3), 1);
		assertTrue(builder.isComplete());
		
		builder.add(0);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		TestUtil.orderTest(builder.retrieve().get(2), 6);
		TestUtil.orderTest(builder.retrieve().get(3), 0,1);
		assertTrue(builder.isComplete());
		
		builder.add(3);
		assertEquals(5,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		TestUtil.orderTest(builder.retrieve().get(2), 6);
		TestUtil.orderTest(builder.retrieve().get(3), 0,1);
		TestUtil.orderTest(builder.retrieve().get(4), 3);
		assertTrue(builder.isComplete());
		
		builder.add(-1);
		assertEquals(5,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), -1,0,1);
		TestUtil.orderTest(builder.retrieve().get(1), 3,4);
		TestUtil.orderTest(builder.retrieve().get(2), 6);
		TestUtil.orderTest(builder.retrieve().get(3), 0,1);
		TestUtil.orderTest(builder.retrieve().get(4), 3);
		assertTrue(builder.isComplete());
		
		builder.add(2);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), -1,0,1,2,3,4);
		TestUtil.orderTest(builder.retrieve().get(1), 6);
		TestUtil.orderTest(builder.retrieve().get(2), 0,1);
		TestUtil.orderTest(builder.retrieve().get(3), 3);
		assertTrue(builder.isComplete());
		
		builder.add(7);
		assertEquals(4,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), -1,0,1,2,3,4);
		TestUtil.orderTest(builder.retrieve().get(1), 6,7);
		TestUtil.orderTest(builder.retrieve().get(2), 0,1);
		TestUtil.orderTest(builder.retrieve().get(3), 3);
		assertTrue(builder.isComplete());
		
		builder.add(5);
		assertEquals(3,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), -1,0,1,2,3,4,5,6,7);
		TestUtil.orderTest(builder.retrieve().get(1), 0,1);
		TestUtil.orderTest(builder.retrieve().get(2), 3);
		assertTrue(builder.isComplete());
		
		builder.add(2);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), -1,0,1,2,3,4,5,6,7);
		TestUtil.orderTest(builder.retrieve().get(1), 0,1,2,3);
		assertTrue(builder.isComplete());
		
		cleanTest(builder);
	}
	
	@Test
	public void go(){
		MChainBuilder<Integer> builder = new MChainBuilder<Integer>(TestUtil.raySerialConditionToBuild);
		
		builder.add(1);
		assertEquals(0,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)1,builder.retrieveScrap().get(0));
		
		builder.add(0);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1);
		
		builder.add(2);
		assertEquals(1,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1,2);
		
		builder.add(4);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		
		builder.add(1);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(2,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		assertEquals((Integer)1,builder.retrieveScrap().get(1));
		
		
		builder.add(2);
		assertEquals(1,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(3,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		assertEquals((Integer)1,builder.retrieveScrap().get(1));
		assertEquals((Integer)2,builder.retrieveScrap().get(2));
		
		builder.add(0);
		assertEquals(2,builder.chainCount());
		canNotRetrieve(builder);
		assertEquals(1,builder.retrieveScrap().size());
		assertEquals((Integer)4,builder.retrieveScrap().get(0));
		
		builder.add(3);
		assertEquals(2,builder.chainCount());
		TestUtil.orderTest(builder.retrieve().get(0), 0,1,2,3,4);
		TestUtil.orderTest(builder.retrieve().get(1), 0,1,2);
		assertEquals(0,builder.retrieveScrap().size());
		
		cleanTest(builder);
	}
	
	private void canNotRetrieve(MChainBuilder<Integer> builder){
		assertFalse(builder.isComplete());
		try{
			builder.retrieve();
			assertTrue(false);
		}catch(NoCompleteException e){
		}
	}
	
	private void cleanTest(MChainBuilder<?> builder){
		builder.clear();
		assert(builder.isComplete());
		assertEquals(0,builder.retrieve().size());
		assertEquals(0,builder.retrieveScrap().size());
	}

}
