package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MyDataStructure.*;
public class Junit_Edge    // change shemi 

/**
 * test class for Edge
 */
{


		Edge e1 = new Edge(6,7,25);
		Edge e2 = new Edge(2,9,30);

		@Test
		void getSrc() {
			assertEquals(6,e1.getSrc());
			assertEquals(2,e2.getSrc());
		}

		@Test
		void getDest() {
			assertEquals(7,e1.getDest());
			assertEquals(9,e2.getDest());
		}

		@Test
		void getWeight() {
			assertEquals(25,e1.getWeight());
			assertEquals(30,e2.getWeight());
		}

		@Test
		void getInfo() {
			assertEquals(null,e1.getInfo());
			assertEquals(null,e2.getInfo());
		}

		@Test
		void setInfo() {
			Edge e = new Edge(1,2,10);
			e.setInfo("edge for test");
			assertEquals("edge for test",e.getInfo());
		}

		@Test
		void getTag() {
			assertEquals(0,e1.getTag());
			assertEquals(0,e2.getTag());
		}

		@Test
		void setTag() {
			Edge e3 = new Edge(5,7,18);
			Edge e4 = new Edge(7,4,50);
			e3.setTag(5);
			e4.setTag(5);
			assertEquals(5,e3.getTag());
			assertEquals(5,e4.getTag());


		}

	}


