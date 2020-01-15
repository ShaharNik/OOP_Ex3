package gui;

import MyDataStructure.Vertex;
import MyDataStructure.graph;
import MyDataStructure.myDGraph;
import utils.Point3D;

public class WindowMyGraphGUI {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
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
	}

}
