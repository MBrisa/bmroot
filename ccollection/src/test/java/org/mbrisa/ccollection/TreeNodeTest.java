package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void simple(){
		TreeNode<Integer> one = new TreeNode<>(1);
		TreeNode<Integer> two = new TreeNode<>(2);
		TreeNode<Integer> three = new TreeNode<>(3);
		TreeNode<Integer> four = new TreeNode<>(3);
		TreeNode<Integer> five = new TreeNode<>(3);
		
		
		try{
			one.add(one);
			assertTrue(false);
		}catch(NodeConflictException e){
			assertEquals("can not add self.",e.getMessage());
		}
		assertEquals(0,one.size());
		
		assertTrue(one.add(two));
		assertEquals(1,one.size());
		try{
			one.add(two);
			assertTrue(false);
		}catch(NodeConflictException e){
			assertEquals("child added already",e.getMessage());
			assertEquals(two,e.getConflict());
			assertEquals(one,e.getConflictParent());
		}
		assertEquals(1,one.size());
		
		
		assertTrue(two.add(three));
		assertEquals(1,two.size());
		assertEquals(2,one.size());
		
		try{
			one.add(three);
			assertTrue(false);
		}catch(NodeConflictException e){
			assertEquals("child added already",e.getMessage());
			assertEquals(three,e.getConflict());
			assertEquals(two,e.getConflictParent());
		}
		assertEquals(1,two.size());
		assertEquals(2,one.size());
		
		assertTrue(one.add(five));
		assertEquals(0,five.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		
		assertTrue(four.add(five));
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		
		try{
			one.add(four);
			assertTrue(false);
		}catch(NodeConflictException e){
			assertEquals("child added already",e.getMessage());
			assertEquals(five,e.getConflict());
			assertEquals(four,e.getConflictParent());
		}
		
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
	}
	
	@Test
	public void go(){
		Person sp = new Person("may", 55);
		Person one = new Person("may", 55);
		Person one_c1 = new Person("may", 35);
		Person one_c2 = new Person("may", 36);
		Person one_c3 = new Person("may", 37);
		
		Person one_c1_c1 = new Person("may",5);
		Person one_c1_c2 = new Person("may",6);
		
		TreeNode<Person> superNode = new TreeNode<>(sp);
		TreeNode<Person> root = new TreeNode<Person>(one);
		TreeNode<Person> root_c1 = new TreeNode<Person>(one_c1);
		TreeNode<Person> root_c2 = new TreeNode<Person>(one_c2);
		TreeNode<Person> root_c3 = new TreeNode<Person>(one_c3);
		
		TreeNode<Person> root_c1_c1 = new TreeNode<Person>(one_c1_c1);
		TreeNode<Person> root_c1_c2 = new TreeNode<Person>(one_c1_c2);
		
		
		root.add(root_c1);
		assertEquals(1,root.size());
		root.add(root_c2);
		assertEquals(2,root.size());
		root.add(root_c3);
		assertEquals(3,root.size());
		
		
		root_c1.add(root_c1_c1);
		assertEquals(1,root_c1.size());
		assertEquals(4,root.size());
		root_c1.add(root_c1_c2);
		assertEquals(2,root_c1.size());
		assertEquals(5,root.size());
		assertEquals(0,root_c1_c1.size());
		assertEquals(0,root_c1_c2.size());
		
		superNode.add(root);
		assertEquals(2,root_c1.size());
		assertEquals(5,root.size());
		assertEquals(0,root_c1_c1.size());
		assertEquals(0,root_c1_c2.size());
		assertEquals(6,superNode.size());
		
		assertEquals(3,root.children().size());
		assertEquals(root_c1,root.children().get(0));
		assertEquals(root_c2,root.children().get(1));
		assertEquals(root_c3,root.children().get(2));
		
		assertEquals(2,root_c1.children().size());
		assertEquals(root_c1_c1,root_c1.children().get(0));
		assertEquals(root_c1_c2,root_c1.children().get(1));
		
		assertEquals(0,root_c2.children().size());
		assertEquals(0,root_c3.children().size());
		assertEquals(0,root_c1_c1.children().size());
		assertEquals(0,root_c1_c2.children().size());
		
		
	}
	
	@Test
	public void go2(){
		
		final LinkCondition<Person> nameAndAgeCondition = new LinkCondition<Person>() {
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
		};
		
		final LinkCondition<Person> ageCondition = new LinkCondition<Person>() {
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
				return target.age >= addition.age;
			}
		};
		
		TreeNode<Person> may45 = new TreeNode<Person>(new Person("may", 45),ageCondition);
		TreeNode<Person> may35 = new TreeNode<>(new Person("may", 35),ageCondition);
		TreeNode<Person> may20 = new TreeNode<Person>(new Person("may", 20),nameAndAgeCondition);
		TreeNode<Person> brisa40 = new TreeNode<Person>(new Person("brisa", 40),nameAndAgeCondition);
		TreeNode<Person> brisa35 = new TreeNode<Person>(new Person("brisa", 35),nameAndAgeCondition);
		TreeNode<Person> brisa5 = new TreeNode<Person>(new Person("brisa", 5),nameAndAgeCondition);
		
		
		assertFalse(may35.add(may45));
		assertFalse(brisa40.add(may20));
		assertFalse(brisa35.add(may35));
		assertFalse(may35.add(brisa35));
		assertTrue(brisa35.add(brisa5));
		assertEquals(0,may35.size());
		assertEquals(1,brisa35.size());
		
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
