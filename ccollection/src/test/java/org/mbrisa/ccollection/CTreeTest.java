package org.mbrisa.ccollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CTreeTest {
	
	private final LinkedCondition<Person> nameAndAgeCondition = new LinkedCondition<Person>() {
		@Override
		public boolean equals(Object ob) {
			if(ob == null){
				return false;
			}
			return this.getClass().equals(ob.getClass());
		}
		@Override
		public boolean headable(Person addition) {
			return true;
		}
		@Override
		public boolean appendable(Person target, Person addition) {
			return target.name.equals(addition.name) && target.age >= addition.age;
		}
		@Override
		public boolean rejectNull() {
			return true;
		}
	};
	
//	private final LinkedCondition<Person> ageCondition = new LinkedCondition<Person>() {
//		@Override
//		public boolean equals(Object ob) {
//			if(ob == null){
//				return false;
//			}
//			return this.getClass().equals(ob.getClass());
//		}
//		@Override
//		public boolean headable(Person addition) {
//			return true;
//		}
//		@Override
//		public boolean appendable(Person target, Person addition) {
//			return target.age >= addition.age;
//		}
//		@Override
//		public boolean rejectNull() {
//			return true;
//		}
//	};
	
	@Test
	public void exceptionTest(){
		CTree<Person> cTree = new CTree<Person>(nameAndAgeCondition);
		try{
			cTree.add((Person)null); // reject null
			assertTrue(false);
		}catch(NullPointerException e){
		}
		
	}
	
	@Test
	public void simple(){
		Person may45 = new Person("may", 45);
		Person may35 = new Person("may", 35);
		Person may20 = new Person("may", 20);
		
		
		Person brisa40 = new Person("brisa", 40);
		Person brisa35 = new Person("brisa", 35);
		Person brisa5 = new Person("brisa", 5);
		
		CTree<Person> cTree = new CTree<Person>(nameAndAgeCondition);
		assertTrue(cTree.add(may35));
		iteratorTest(cTree, may35);
		assertTrue(cTree.add(may45));
		iteratorTest(cTree, may45,may35);
		assertTrue(cTree.add(may20));
		iteratorTest(cTree, may45,may35,may20);
		TreeNode<Person> root = cTree.getRoot();
		TestUtil.dcTest(root, may35,may20);
		
		assertFalse(cTree.add(brisa5)); // name is not same
		
		cTree.clear();
		iteratorTest(cTree);
		assertTrue(cTree.add(brisa5));
		iteratorTest(cTree,brisa5);
		assertTrue(cTree.add(brisa35));
		iteratorTest(cTree,brisa35,brisa5);
		assertTrue(cTree.add(brisa40));
		iteratorTest(cTree,brisa40,brisa35,brisa5);
		
		TreeNode<Person> bRoot = cTree.getRoot();
		TestUtil.dcTest(bRoot, brisa35);
	}
	
	@Test
	public void addTree(){
		CTree<Integer> cTree = new CTree<Integer>(TestUtil.serialCondition);
		cTree.add(1);
		iteratorTest(cTree, 1);
		CTree<Integer> pTree = new CTree<>(TestUtil.raySerialConditionToBuild);
		try{
			cTree.add(pTree);
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		iteratorTest(cTree, 1);
		CTree<Integer> cTree2 = new CTree<Integer>(TestUtil.serialCondition);
		cTree2.add(3);
		assertFalse(cTree.add(cTree2));
		iteratorTest(cTree, 1);
		assertFalse(cTree2.isSubtree());
		
		cTree2.add(2);
		assertTrue(cTree.add(cTree2));
		iteratorTest(cTree, 1,2,3);
		assertTrue(cTree2.isSubtree());
		
		cTree2.add(4);
		iteratorTest(cTree, 1,2,3,4);
		iteratorTest(cTree2, 2,3,4);
		
		assertFalse(cTree2.add(0));
		assertTrue(cTree.add(0));
		iteratorTest(cTree, 0,1,2,3,4);
		iteratorTest(cTree2, 2,3,4);
		
		cTree = new CTree<Integer>(TestUtil.serialCondition);
		try{
			cTree.add(cTree2);
			assertTrue(false); // cTree2 was subTree already
		}catch(NodeConflictException e){
		}
		iteratorTest(cTree);
		
		CTree<Integer> cTree3 = new CTree<Integer>(TestUtil.serialCondition);
		assertFalse(cTree.add(cTree3));
		iteratorTest(cTree);
		
		cTree = new CTree<Integer>(TestUtil.serialCondition);
		assertFalse(cTree.add(cTree3));
		iteratorTest(cTree);
	}
	
	private void iteratorTest(CTree<?> cTree,Object... nodes){
		assertEquals(nodes.length,cTree.size());
		int i = 0;
		for(Object n : cTree){
			assertEquals(nodes[i++],n);
		}
	}
	
	private class Person{
		private final String name;
		private final int age;
		public Person(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Person other = (Person) obj;
			if (age != other.age)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}
		
	}

}
