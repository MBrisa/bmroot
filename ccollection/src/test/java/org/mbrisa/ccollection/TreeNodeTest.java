package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void simple(){
		
		TreeNode<Integer> _1 = new TreeNode<>(-1);
		TreeNode<Integer> _2 = new TreeNode<>(-2);
		TreeNode<Integer> _3 = new TreeNode<>(-3);
		
		TreeNode<Integer> one = new TreeNode<>(1);
		TreeNode<Integer> two = new TreeNode<>(2);
		TreeNode<Integer> three = new TreeNode<>(3);
		TreeNode<Integer> four = new TreeNode<>(4);
		TreeNode<Integer> five = new TreeNode<>(5);
		
		
		try{
			one.link(one);
			assertTrue(false);
		}catch(NodeConflictException e){
//			assertEquals("can not add self.",e.getMessage());
		}
		assertEquals(0,one.size());
		
		assertTrue(one == one.link(two));
		assertEquals(1,one.size());
		try{
			one.link(two);
			assertTrue(false);
		}catch(NodeConflictException e){
//			assertEquals("relation was create already",e.getMessage());
		}
		assertEquals(1,one.size());
		
		
		assertTrue(two == two.link(three));
		assertEquals(1,two.size());
		assertEquals(2,one.size());
		
		try{
			one.link(three);
			assertTrue(false);
		}catch(NodeConflictException e){
//			assertEquals("relation was create already",e.getMessage());
		}
		assertEquals(1,two.size());
		assertEquals(2,one.size());
		
		assertTrue(one == one.link(five));
		assertEquals(0,five.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		
		assertTrue(four == four.link(five));
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		
		try{
			one.link(four);
			assertTrue(false);
		}catch(NodeConflictException e){
//			assertEquals("relation was create already",e.getMessage());
		}
		
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		
		assertTrue(_1 == _1.link(one));
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		assertEquals(4,_1.size());
		
		assertTrue(_3 == _3.link(_2));
		assertEquals(0,_2.size());
		assertEquals(1,_3.size());
		
		assertTrue(_3 == _3.link(_1));
		assertEquals(0,five.size());
		assertEquals(1,four.size());
		assertEquals(0,three.size());
		assertEquals(1,two.size());
		assertEquals(3,one.size());
		assertEquals(4,_1.size());
		assertEquals(0,_2.size());
		assertEquals(6,_3.size());
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
		
		
		root.link(root_c1);
		assertEquals(1,root.size());
		root.link(root_c2);
		assertEquals(2,root.size());
		root.link(root_c3);
		assertEquals(3,root.size());
		
		
		root_c1.link(root_c1_c1);
		assertEquals(1,root_c1.size());
		assertEquals(4,root.size());
		root_c1.link(root_c1_c2);
		assertEquals(2,root_c1.size());
		assertEquals(5,root.size());
		assertEquals(0,root_c1_c1.size());
		assertEquals(0,root_c1_c2.size());
		
		superNode.link(root);
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
		
		
		assertTrue(may45 == may35.link(may45));
		assertTrue(null == brisa40.link(may20));
		try{
			brisa35.link(may35);
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		try{
			may35.link(brisa35);
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		assertTrue(brisa35 == brisa35.link(brisa5));
		assertEquals(1,may45.size());
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
