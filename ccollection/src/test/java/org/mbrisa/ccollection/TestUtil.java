package org.mbrisa.ccollection;

import static org.junit.Assert.assertEquals;

import org.mbrisa.ccollection.Chain;
import org.mbrisa.ccollection.BuildingCondition;

public class TestUtil {
	
	public static SerialCondition serialCondition = new SerialCondition();
	public static SerialConditionToBuild serialConditionToBuild = new SerialConditionToBuild();
	public static RaySerialConditionToBuild raySerialConditionToBuild = new RaySerialConditionToBuild();
	public static ParentChildCondition parentChildCondition = new ParentChildCondition();
	public static RayParentChildCondition rayParentChildCondition = new RayParentChildCondition();
	
	

	public static void orderTest(Chain<?> chain, Object... assertArray) {
		assertEquals(assertArray.length,chain.size());
		int index = 0;
		for(Object item : chain){
			assertEquals(assertArray[index++],item);
		}
	}
	
	public static class SerialCondition implements LinkedCondition<Integer>{
		@Override
		public boolean appendable(Integer target, Integer addition) {
			if(target == null || addition == null){
				throw new NullPointerException("target is "+ target +" addition is "+addition);
			}
			return target - addition == -1;
		}
		
		@Override
		public boolean equals(Object ob) {
			if(ob == null){
				return false;
			}
			return this.getClass().equals(ob.getClass());
		}
		
		@Override
		public boolean rejectNull() {
			return false;
		}
		
	}
	
	public static class SerialConditionToBuild extends SerialCondition implements BuildingCondition<Integer>{
		@Override
		public boolean headable(Integer addition) {
			return true;
		}
	}
	
	public static class RaySerialConditionToBuild extends SerialCondition implements BuildingCondition<Integer>{
		
		@Override
		public boolean appendable(Integer target, Integer addition) {
			if(target < 0 || addition < 0){
				return false;
			}
			return super.appendable(target, addition);
		}
		@Override
		public boolean headable(Integer addition) {
			return addition == 0;
		}
	}
	
	
	public static class ParentChildCondition implements LinkedCondition<Node>{
		@Override
		public boolean equals(Object ob) {
			if(ob == null){
				return false;
			}
			return this.getClass().equals(ob.getClass());
		}
		
		@Override
		public boolean appendable(Node target, Node addition) {
			if(target == null || addition == null){
				throw new NullPointerException("target is "+ target +" addition is "+addition);
			}
			return new Integer(target.getId()).equals(addition.getParentId());
		}
		
		@Override
		public boolean rejectNull() {
			return true;
		}
	}
	
	public static class ParentChildConditionToBuild extends ParentChildCondition implements BuildingCondition<Node>{
		@Override
		public boolean headable(Node addition) {
			return true;
		}
	}
	
	public static class RayParentChildCondition extends ParentChildCondition implements BuildingCondition<Node>{
		@Override
		public boolean headable(Node addition) {
			return addition.getParentId() == null;
		}
	}
	
	public static class Node{
		private final Integer parentId;
		private final int id;
		
		public Node(Integer parentId, int id) {
			this.parentId = parentId;
			this.id = id;
		}
		
		public Integer getParentId(){
			return this.parentId;
		}
		
		public int getId(){
			return this.id;
		}
	}

}
