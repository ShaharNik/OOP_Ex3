package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MyDataStructure.*;

import utils.Point3D;


/**
 * Fruit test class.
 * @authors Shahar and Or 

**/


public class Junit_FruitTest
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
		
	
	
		
		
		  @Test
		    void set_Pos_test() 
		  {
			  	
		        myDGraph Dgraph = new myDGraph();
		        MyFruit fruit = new MyFruit(Dgraph);
		        MyFruit f2 =new MyFruit();
		        Point3D p = new Point3D(2.0,4.0,6.0);
		        f2.setPos(p);
		      
		        assertNotEquals(fruit.getPos(),f2.getPos());
		      

		    }
	
		
		
		
		
}

