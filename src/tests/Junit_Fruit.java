package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MyDataStructure.*;

/**
 * Fruit tests.
 * @authors Shahar and Or 

**/


public class Junit_Fruit
{
	
	@Test
	void NewFruit_and_to_string_test()
	{
		
		MyFruit f =new MyFruit();
		MyFruit f2 =new MyFruit();
		if(f.toString().equals(f2.toString())) {
			fail("defalut Constractor problem");
		}
	}
		@Test
		void test_type()
		{
		MyFruit fruit =new MyFruit();
		assertEquals(fruit.getType(), -1);
		}
	
		
		@Test
		void test_value()
		{
		MyFruit fruit =new MyFruit();
		assertEquals(fruit.getValue(), 0);
		}
		
		@Test
		void test_MyFruit_graph()
		{
		myDGraph g = new myDGraph();
		MyFruit fruit =new MyFruit(g);
		assertEquals(fruit.getEdge(), null);
		}
		
		
	
		
		
		
		
}

