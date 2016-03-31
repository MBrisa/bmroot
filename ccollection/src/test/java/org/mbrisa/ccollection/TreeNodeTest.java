package org.mbrisa.ccollection;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeNodeTest {
	
	@Test
	public void testException(){
//		Exception
		TreeNode<Integer> n1 = new TreeNode<>(0);
		try{
			n1.add(n1); // can not add self
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		TreeNode<Integer> n2 = new TreeNode<>(0);
		assertTrue(n1.add(n2));
		try{
			n1.add(n2); // can not add again
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		TreeNode<Integer> n3 = new TreeNode<>(0);
		try{
			n3.add(n2); // n2 exists parent
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		assertTrue(n2.add(n3));
		try{
			n1.add(n3); // n3 exists parent
			assertTrue(false);
		}catch(NodeConflictException e){
		}
		assertTrue(n1.add(0));
		iteratorTest(n1, 0,0,0,0);
		
		TreeNode<Integer> node = new TreeNode<>(0,TestUtil.serialCondition);
		iteratorTest(node, 0);
		assertTrue(node.add(1));
		assertFalse(node.add(2));
		
		LinkedCondition<Integer> rejectNullCondition = new LinkedCondition<Integer>() {
			@Override
			public boolean appendable(Integer target, Integer addition) {
				return true;
			}
			@Override
			public boolean rejectNull() {
				return true;
			}
			
			@Override
			public boolean headable(Integer addition) {
				return false;
			}
		};
		
		try{
			new TreeNode<Integer>(null,rejectNullCondition);
			assertTrue(false);
		}catch(NullPointerException e){
		}
		
		TreeNode<Integer> treeNode = new TreeNode<Integer>(0,rejectNullCondition);
		try{
			treeNode.add((Integer)null);
			assertTrue(false);
		}catch(NullPointerException e){
		}
	}
	
	@Test
	public void nomalTest(){
//		Normal
		TreeNode<Integer> root = new TreeNode<>(0);
		TreeNode<Integer> c1 = new TreeNode<Integer>(1);
		TreeNode<Integer> c2 = new TreeNode<Integer>(2);
		
		rootTest(root, root);
		rootTest(c1, c1);
		rootTest(c2, c2);
		
		iteratorTest(root, 0);
		assertTrue(root.add(c1));
		rootTest(root, c1);
		iteratorTest(root, 0,1);
		assertTrue(root.add(c2));
		rootTest(root, c2);
		iteratorTest(root, 0,1,2);
		iteratorTest(c1, 1);
		iteratorTest(c2, 2);
		dcTest(root, c1,c2);
		dcTest(c1);
		dcTest(c2);
		cloneTest(root);
		cloneTest(c1);
		cloneTest(c2);
		
		rootTest(root, root);
	}
	
	@Test
	public void subToAddTest(){
//		Sub to add
		TreeNode<Integer> root = new TreeNode<Integer>(0);
		TreeNode<Integer> c1 = new TreeNode<Integer>(1);
		TreeNode<Integer> c2 = new TreeNode<Integer>(2);
		
		
		root.add(c1);
		rootTest(root, c1);
		c1.add(c2);
		rootTest(root, c2);
		iteratorTest(root, 0,1,2);
		iteratorTest(c1, 1,2);
		iteratorTest(c2, 2);
		dcTest(root, c1);
		dcTest(c1,c2);
		dcTest(c2);
		cloneTest(root);
		cloneTest(c1);
		cloneTest(c2);
		
		rootTest(root, root);
	}
	
	@Test
	public void addSubTree(){
//		add a sub tree
		TreeNode<Integer> root = new TreeNode<>(0);
		TreeNode<Integer> subRoot = new TreeNode<>(1);
		TreeNode<Integer> subChild = new TreeNode<>(2);
		
		
		subRoot.add(subChild);
		rootTest(subRoot, subChild);
		root.add(subRoot);
		rootTest(root,subRoot);
		rootTest(root,subChild);
		iteratorTest(root, 0,1,2);
		iteratorTest(subRoot, 1,2);
		iteratorTest(subChild, 2);
		dcTest(root, subRoot);
		dcTest(subRoot, subChild);
		dcTest(subChild);
		cloneTest(root);
		cloneTest(subRoot);
		cloneTest(subChild);
		
		rootTest(root,root);
	}
	
	@Test
	public void addNull(){
//		add null
		TreeNode<Integer> root = new TreeNode<>(0);
		try{
			root.add((TreeNode<Integer>)null);
			assertTrue(false);
		}catch(NullPointerException e){
		}
		iteratorTest(root, 0);
		TreeNode<Integer> intNull = new TreeNode<>((Integer)null);
		assertTrue(root.add(intNull));
		iteratorTest(root, 0,null);
		dcTest(root, intNull);
		cloneTest(root);
		cloneTest(intNull);
		
		rootTest(root,intNull);
		rootTest(root,root);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void addMultinode(){
//		add multi-node
		TreeNode<Object> root0 = new TreeNode<>(null);
		TreeNode<Object> numberRoot = new TreeNode<>((Object)(-1));
		TreeNode<Object> stringRoot = new TreeNode<>((Object)"");
		TreeNode<Object> classRoot = new TreeNode<>((Object)Object.class);
		
		TreeNode<Object> intRoot = new TreeNode((Object)0);
		TreeNode<Object> int1 = new TreeNode((Object)1);
		TreeNode<Object> int50 = new TreeNode((Object)50);
		TreeNode<Object> longRoot = new TreeNode((Object)100L);
		TreeNode<Object> long1 = new TreeNode((Object)101L);
		TreeNode<Object> long2 = new TreeNode((Object)102L);
		
		
		TreeNode<Object> oneCharRoot = new TreeNode((Object)"a");
		TreeNode<Object> sb = new TreeNode((Object)"b");
		TreeNode<Object> sc = new TreeNode((Object)"c");
		TreeNode<Object> twoCharRoot = new TreeNode((Object)"aa");
		TreeNode<Object> sbb = new TreeNode((Object)"bb");
		TreeNode<Object> scc = new TreeNode((Object)"cc");
		
		TreeNode<Object> stringClass = new TreeNode<>((Object)String.class);
		TreeNode<Object> intgerClass = new TreeNode<>((Object)Integer.class);
		
//		顺序添加
		assertTrue(root0.add(numberRoot));
		assertTrue(numberRoot.add(intRoot));
		assertTrue(numberRoot.add(longRoot));
		assertTrue(intRoot.add(int1));
		assertTrue(intRoot.add(int50));
		assertTrue(longRoot.add(long1));
		assertTrue(long1.add(long2));
		
//		逆序添加
		assertTrue(sbb.add(scc));
		rootTest(sbb,scc);
		assertTrue(twoCharRoot.add(sbb));
		rootTest(twoCharRoot,sbb);
		rootTest(twoCharRoot,scc);
		assertTrue(oneCharRoot.add(sb));
		rootTest(oneCharRoot,sb);
		assertTrue(oneCharRoot.add(sc));
		rootTest(oneCharRoot,sc);
		assertTrue(stringRoot.add(oneCharRoot));
		rootTest(stringRoot,oneCharRoot);
		rootTest(stringRoot,sb);
		rootTest(stringRoot,sc);
		assertTrue(stringRoot.add(twoCharRoot));
		rootTest(stringRoot,twoCharRoot);
		rootTest(stringRoot,sbb);
		rootTest(stringRoot,scc);
		assertTrue(root0.add(stringRoot));
		
//		无序添加
		assertTrue(classRoot.add(stringClass));
		assertTrue(root0.add(classRoot));
		assertTrue(classRoot.add(intgerClass));
		
		
		iteratorTest(long2, 102L);
		dcTest(long2);
		iteratorTest(long1, 101L,102L);
		dcTest(long1,long2);
		iteratorTest(longRoot, 100L,101L,102L);
		dcTest(longRoot,long1);
		iteratorTest(int1, 1);
		dcTest(int1);
		iteratorTest(int50, 50);
		dcTest(int50);
		iteratorTest(intRoot, 0,1,50);
		dcTest(intRoot,int1,int50);
		iteratorTest(numberRoot, -1,0,1,50,100L,101L,102L);
		dcTest(numberRoot,intRoot,longRoot);
		
		iteratorTest(scc, "cc");
		dcTest(scc);
		iteratorTest(sbb, "bb","cc");
		dcTest(sbb,scc);
		iteratorTest(twoCharRoot, "aa","bb","cc");
		dcTest(twoCharRoot,sbb);
		iteratorTest(sb, "b");
		dcTest(sb);
		iteratorTest(sc, "c");
		dcTest(sc);
		iteratorTest(oneCharRoot, "a","b","c");
		dcTest(oneCharRoot,sb,sc);
		iteratorTest(stringRoot, "","a","b","c","aa","bb","cc");
		dcTest(stringRoot,oneCharRoot,twoCharRoot);
		
		iteratorTest(stringClass, String.class);
		dcTest(stringClass);
		iteratorTest(intgerClass, Integer.class);
		dcTest(intgerClass);
		iteratorTest(classRoot, Object.class,String.class,Integer.class);
		dcTest(classRoot,stringClass,intgerClass);
		
		iteratorTest(root0, (Object)null,-1,0,1,50,100L,101L,102L,"","a","b","c","aa","bb","cc",Object.class,String.class,Integer.class);
		dcTest(root0,numberRoot,stringRoot,classRoot);
		
		cloneTest(root0);
		cloneTest(numberRoot);
		cloneTest(stringRoot);
		cloneTest(classRoot);
		cloneTest(intRoot);
		cloneTest(int1);
		cloneTest(int50);
		cloneTest(longRoot);
		cloneTest(long1);
		cloneTest(long2);
		cloneTest(oneCharRoot);
		cloneTest(sb);
		cloneTest(sc);
		cloneTest(twoCharRoot);
		cloneTest(sbb);
		cloneTest(sbb);
		cloneTest(stringClass);
		cloneTest(intgerClass);
		
		rootTest(root0,root0);
		rootTest(root0,numberRoot);
		rootTest(root0,stringRoot);
		rootTest(root0,classRoot);
		rootTest(root0,intRoot);
		rootTest(root0,int1);
		rootTest(root0,int50);
		rootTest(root0,longRoot);
		rootTest(root0,long1);
		rootTest(root0,long2);
		rootTest(root0,oneCharRoot);
		rootTest(root0,sb);
		rootTest(root0,sc);
		rootTest(root0,twoCharRoot);
		rootTest(root0,sbb);
		rootTest(root0,scc);
		rootTest(root0,stringClass);
		rootTest(root0,intgerClass);
		
	}
	
	
	@Test
	public void go(){
		
		final BuildingCondition<Person> nameAndAgeCondition = new BuildingCondition<Person>() {
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
		
		final BuildingCondition<Person> ageCondition = new BuildingCondition<Person>() {
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
			
			@Override
			public boolean rejectNull() {
				return true;
			}
		};
		
		TreeNode<Person> may45 = new TreeNode<Person>(new Person("may", 45),ageCondition);
		TreeNode<Person> may35 = new TreeNode<>(new Person("may", 35),ageCondition);
		TreeNode<Person> may20 = new TreeNode<Person>(new Person("may", 20),nameAndAgeCondition);
		TreeNode<Person> brisa40 = new TreeNode<Person>(new Person("brisa", 40),nameAndAgeCondition);
		TreeNode<Person> brisa35 = new TreeNode<Person>(new Person("brisa", 35),nameAndAgeCondition);
		TreeNode<Person> brisa5 = new TreeNode<Person>(new Person("brisa", 5),nameAndAgeCondition);
		
		
		assertFalse(may35.add(may45)); //不满足 condition
		assertFalse(brisa40.add(may20)); // 不满足 condition
		try{
			brisa35.add(may35); // condition 不一致
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		try{
			may35.add(brisa35); // condition 不一致
			assertTrue(false);
		}catch(NoCompatibilityException e){
		}
		assertTrue(brisa35.add(brisa5));
		assertEquals(1,may45.size());
		assertEquals(1,may35.size());
		assertEquals(2,brisa35.size());
		
	}
	
	private void iteratorTest(TreeNode<?> treeNode,Object... nodes){
		assertEquals(nodes.length,treeNode.size());
		int i = 0;
		for(Object n : treeNode){
			assertEquals(nodes[i++],n);
		}
	}
	
	private void rootTest(TreeNode<?> expect,TreeNode<?> target){
		assertTrue(expect == target.getRoot());
	}
	
	/**
	 * direct children test
	 */
	private void dcTest(TreeNode<?> parent,TreeNode<?>... children){
		assertEquals(children.length,parent.children().size());
		int i = 0;
		for(TreeNode<?> child : parent.children()){
			assertEquals(children[i++],child);
		}
	}
	
	private void cloneTest(TreeNode<?> original){
		TreeNode<?> cloned = original.clone();
		cloneTest(original, cloned);
		TreeNode<?> op = original.getParent();
		TreeNode<?> cp = cloned.getParent();
		while(op != null){
			cloneTest(op, cp);
			op = op.getParent();
			cp = cp.getParent();
		}
	}
	
	private void cloneTest(TreeNode<?> original,TreeNode<?> cloned){
		assertFalse(original.getRoot() == cloned.getRoot());
		assertTrue(original.getRoot().entity() == cloned.getRoot().entity());
		assertEquals(original.size(),cloned.size());
		int i = 0;
		for(Object entry : original){
			int j = 0;
			boolean compared = false;
			for(Object clonedEntry : original){
				if(j++ == i){
					assertEquals(entry, clonedEntry);
					compared = true;
					break;
				}
			}
			assertTrue(compared);
			i++;
		}
		assertEquals(original.children().size(),cloned.children().size());
		i = 0;
		for(TreeNode<?> child : original.children()){
			TreeNode<?> clonedChild = cloned.children().get(i++);
			assertEquals(child.size(),clonedChild.size());
			assertEquals(child.entity(),clonedChild.entity());
			cloneTest(child, clonedChild);
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
