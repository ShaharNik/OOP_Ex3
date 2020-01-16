package Test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;



class Junit_DGraph
{

	private DGraph graph;

	@BeforeEach
	void setup()
	{
		graph = createGraphBefore();
	}

	@Test
	void testRemoveNode_1()    
	{

		graph.connect(2, 3, 1);
		graph.connect(3, 2, 3);
		graph.connect(4, 5, 8);
		graph.connect(1, 6,9);

		graph.removeNode(1);
		assertEquals(7, graph.edgeSize());
	}


	@Test

	void test_1_AddNode()               
	{

		Vertex v1 = new Vertex(6);
		Vertex v2 = new Vertex(4);
		Vertex v3 = new Vertex(2);
		Vertex v4 = new Vertex(8);

		graph g = new DGraph();
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		if (g.nodeSize()!=4)
		{ 
			fail("need to be 4");
		}
	}

	@Test
	void removeEdge_1()				
	{
		Graph_Algo graph_one = new Graph_Algo();
		graph_one.init(graph);
		double shortest = graph_one.shortestPathDist(1, 6);
		assertNotEquals(shortest, Integer.MAX_VALUE);

		graph.removeEdge(1, 2);
		shortest = graph_one.shortestPathDist(1, 6);
		assertEquals(shortest, Integer.MAX_VALUE);

	}


	@Test

	void test_3_AddNode()               
	{

		Vertex v1 = new Vertex(9);
		Vertex v2 = new Vertex(7);
		Vertex v3 = new Vertex(1);
		Vertex v4 = new Vertex(11);
		Vertex v5 = new Vertex(9);
		Vertex v6 = new Vertex(13);
		Vertex v7 = new Vertex(1);
		Vertex v8 = new Vertex(15);

		graph g = new DGraph();
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		g.addNode(v5);
		g.addNode(v6);
		g.addNode(v7);
		g.addNode(v8);

		assertEquals(6, g.nodeSize());

	}
	@Test
	void testNode_size()
	{
		DGraph Dg = new DGraph();
		Vertex v1 = new Vertex(4);
		Vertex v2 = new Vertex(9);
		Vertex v3 = new Vertex(1);

		Dg.addNode(v1);
		Dg.addNode(v2);
		Dg.addNode(v3);
		int size = Dg.nodeSize();
		assertTrue(size == 3);
	}

	@Test
	void testgetNode() 
	{            
		node_data one = graph.getNode(1);
		node_data two = graph.getNode(3);
		node_data three = graph.getNode(4);
		node_data four = graph.getNode(7);
		assertEquals(1, one.getKey());
		assertEquals(3, two.getKey());
		assertEquals(4, three.getKey());
		if(four!=null)
		{
			fail("four need to be null");
		}
	}
	@Test
	void testRemoveNode()    
	{
		assertEquals(5, graph.edgeSize());
		graph.connect(1, 3, 6);
		graph.connect(1, 4, 36);
		graph.connect(1, 5, 76);
		graph.connect(1, 6,11);
		graph.connect(5, 1, 13);
		graph.connect(6, 1, 4);
		graph.connect(4, 1, 54);
		graph.connect(3, 1, 2);
		graph.connect(2, 1,99);

		assertEquals(14, graph.edgeSize());
		graph.removeNode(1);
		assertEquals(4, graph.edgeSize());
		graph.removeNode(4);
		assertEquals(2, graph.edgeSize());
	}
	@Test
	void test_2_AddNode()      
	{
		assertEquals(6, graph.getV().size());
		graph.addNode(new Vertex(7,new Point3D(12,6)));
		assertEquals(7, graph.getV().size());
		assertEquals(7, graph.getNode(7).getKey());
	}
	@Test
	void removeEdge()				
	{
		Graph_Algo graph_one = new Graph_Algo();
		graph_one.init(graph);
		double ans = graph_one.shortestPathDist(1, 6);
		if(ans == Integer.MAX_VALUE)
		{
			fail("not need to be MAX Value");
		}
		graph.removeEdge(3, 4);
		ans = graph_one.shortestPathDist(1, 6);
		if(ans != Integer.MAX_VALUE)
		{
			fail("need to be MAX_VALUE "); 
		}
	}
	@Test
	void testMC()
	{
		assertEquals(11, graph.getMC());
		graph.addNode(new Vertex(7));
		graph.addNode(new Vertex(8));
		graph.addNode(new Vertex(9));
		graph.connect(7, 8, 10);
		assertEquals(15, graph.getMC());
		graph.removeEdge(7, 8);
		assertEquals(16, graph.getMC());
	}
	DGraph createGraphBefore()
	{
		DGraph graph = new DGraph();
		graph.addNode(new Vertex(1, new Point3D(225 ,66)));
		graph.addNode(new Vertex(2, new Point3D(20,111)));
		graph.addNode(new Vertex(3,new Point3D(19,46)));
		graph.addNode(new Vertex(4,new Point3D(130,210)));
		graph.addNode(new Vertex(5,new Point3D(387,50)));
		graph.addNode(new Vertex(6,new Point3D(100,300)));
		graph.connect(1, 2, 3);
		graph.connect(2, 3, 20);
		graph.connect(3, 4, 5);
		graph.connect(4, 5, 8);
		graph.connect(5, 6,13);
		return graph;
	}
}