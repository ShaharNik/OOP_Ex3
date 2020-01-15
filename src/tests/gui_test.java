package tests;

import java.util.Collection;
import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import MyDataStructure.*;
import algorithms.*;
import gameClient.MyGameGUI;
import MyDataStructure.myDGraph;
import MyDataStructure.graph;
import MyDataStructure.node_data;
import gui.MyGraphGUI;

import utils.Point3D;

class g_GUITest 
{

	public static void main(String[] args) 
	{
		testNewGUI();
	}
	
	
	@Test
	static void testDraw() {
		//fail("Not yet implemented");
		graph g = new myDGraph();
		g.addNode(new Vertex(1, new Point3D(130, 130)));
		g.addNode(new Vertex(2, new Point3D(180, 160)));
		g.addNode(new Vertex(3, new Point3D(210, 180)));
		g.addNode(new Vertex(4, new Point3D(240, 190)));
		g.addNode(new Vertex(5, new Point3D(160, 200)));
		g.addNode(new Vertex(6, new Point3D(160, 230)));
		g.addNode(new Vertex(7, new Point3D(160, 210)));
		g.addNode(new Vertex(8, new Point3D(210, 140)));
		g.addNode(new Vertex(9, new Point3D(240, 250)));
		g.addNode(new Vertex(10, new Point3D(210, 210)));
		g.addNode(new Vertex(11, new Point3D(340, 300)));
		g.connect(1, 2, 30);
		g.connect(1, 5, 40);
		g.connect(2, 3, 50);
		g.connect(4, 7, 60);
		g.connect(2, 11, 70);
		g.connect(3, 4, 80);
		g.connect(9, 1, 30);
		g.connect(4, 2, 40);
		g.connect(3, 5, 50);
		g.connect(10, 11, 60);
		g.connect(5, 6, 10);
		g.connect(6, 1, 50);
		MyGraphGUI gui = new MyGraphGUI(g);
		Graph_Algo gg = new Graph_Algo();
		gg.init(g);
		double ee = gg.shortestPathDist(1, 6);
		System.out.println(ee);
	}


	@Test
	static void testNewGUI()
	{
		System.out.println();
		graph g = new myDGraph();
		g.addNode(new Vertex(1, new Point3D(130, 130)));
		g.addNode(new Vertex(2, new Point3D(180, 160)));
		g.addNode(new Vertex(3, new Point3D(210, 180)));
		g.addNode(new Vertex(4, new Point3D(240, 190)));
		g.addNode(new Vertex(5, new Point3D(160, 200)));
		g.addNode(new Vertex(6, new Point3D(160, 230)));
		g.addNode(new Vertex(7, new Point3D(160, 210)));
		g.addNode(new Vertex(8, new Point3D(210, 140)));
		g.addNode(new Vertex(9, new Point3D(240, 250)));
		g.addNode(new Vertex(10, new Point3D(210, 210)));
		g.addNode(new Vertex(11, new Point3D(340, 300)));
		g.connect(1, 2, 30);
		g.connect(1, 5, 40);
		g.connect(2, 3, 50);
		g.connect(4, 7, 60);
		g.connect(2, 11, 70);
		g.connect(3, 4, 80);
		g.connect(9, 1, 30);
		g.connect(4, 2, 40);
		g.connect(3, 5, 50);
		g.connect(10, 11, 60);
		g.connect(5, 6, 10);
		g.connect(6, 1, 50);
		MyGraphGUI ggnew = new MyGraphGUI();
		MyGraphGUI ggnew2 = new MyGraphGUI(g);
		System.out.println("asd");
	}
}
