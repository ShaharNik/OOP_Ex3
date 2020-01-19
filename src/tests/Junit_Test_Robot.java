package tests;

import static org.junit.jupiter.api.Assertions.*;

import algorithms.*;

import org.junit.jupiter.api.Test;

import MyDataStructure.*;

/**
 * Robot test class.
 * @authors Shahar and Or 

**/



public class Junit_Test_Robot
{
	
	@Test
	void NewRobot_and_to_string_test()
	{
		
		Robot r =new Robot();
		Robot r2 =new Robot();
		assertEquals(r.toString(),r2.toString());
		
		}
	
		@Test
		void test_id()
		{
		Robot Rob =new Robot();
		assertEquals(Rob.getId(), -1);
		}
	
		
		@Test
		void test_speed()
		{
		Robot Rob =new Robot();
		assertEquals(Rob.getSpeed(), -1);
		}
		
		@Test
		void test_pos()
		{
			Robot Rob =new Robot();
			assertEquals(Rob.getPos(), null);
		}
		
			
		
}

