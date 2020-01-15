package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import algorithms.Graph_Algo;
import MyDataStructure.myDGraph;
import MyDataStructure.Vertex;
import MyDataStructure.edge_data;
import MyDataStructure.graph;
import MyDataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class MyGraphGUI extends JFrame implements ActionListener
{
	/**
	 * This Gui can save and init graphs from deserializable files, You can run algorithms on the graph and see it visual
	 * The gui is very friendly, but more used to test the graph algorithms and creations.
	 */
	private static final long serialVersionUID = -3074823522732093831L;

	graph _graph;

	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;

	public MyGraphGUI()
	{
		this._graph = new myDGraph();
		initGUI();
	}

	public MyGraphGUI(graph g) 
	{
		// TODO Auto-generated constructor stub
		this._graph = g;
		initGUI();
	}

	private void initGUI() 
	{
		StdDraw.setCanvasSize(800, 800);
		if(_graph != null)
		{
			Collection<node_data> nd = _graph.getV();
			for (node_data node_data : nd) 
			{
				Point3D p = node_data.getLocation();
				if(p.ix() < minx)
					minx = p.ix();
				if(p.ix() > maxx)
					maxx = p.ix();
				if(p.iy() > maxy)
					maxy = p.iy();
				if(p.iy() < miny)
					miny = p.iy();
			}
			StdDraw.setXscale(minx-(minx/10), maxx+(maxx/10));
			StdDraw.setYscale(miny-(miny/10),maxy+(maxy/10));
			//StdDraw.setG_GUI(this);
			paint();
		}
		

	}


	public void paint()
	{
		if (_graph != null)
		{
			Collection<node_data> points = _graph.getV();
			for(node_data v : points)
			{
				StdDraw.setPenColor(Color.BLUE);
				StdDraw.filledCircle(v.getLocation().x(), v.getLocation().y(), 10);
				//g.fillOval((int)v.getLocation().x(), (int)v.getLocation().y(), 10, 10);
				StdDraw.text(v.getLocation().x(), v.getLocation().y(), String.valueOf(v.getKey()));
				Collection<edge_data> edgesOfVertex = _graph.getE(v.getKey());
				for (edge_data edge : edgesOfVertex) 
				{
					if(edge.getTag() == 555)
					{
						edge.setTag(0);
						StdDraw.setPenColor(Color.GREEN);
					}
					else
					{
						StdDraw.setPenColor(Color.RED);
					}
					StdDraw.line(v.getLocation().x(), v.getLocation().y(), _graph.getNode(edge.getDest()).getLocation().x(), _graph.getNode(edge.getDest()).getLocation().y());
					StdDraw.setPenColor(Color.YELLOW);
					double xHalf = (v.getLocation().x() + _graph.getNode(edge.getDest()).getLocation().x()) /2;
					double yHalf = (v.getLocation().y() + _graph.getNode(edge.getDest()).getLocation().y()) /2;
					double halfOfHalf_X = (xHalf + _graph.getNode(edge.getDest()).getLocation().x()) / 2;
					double halfOfHalf_Y = (yHalf + _graph.getNode(edge.getDest()).getLocation().y()) / 2;
					StdDraw.filledCircle(halfOfHalf_X, halfOfHalf_Y, 8);
					StdDraw.setPenColor(Color.MAGENTA);
					StdDraw.text(xHalf, yHalf, edge.getWeight()+"");
				}
			}
		}
		StdDraw.show();
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String Option = e.getActionCommand();
		switch (Option)
		{
		case "Init Graph From File": initGraph(); 
		break;
		case "Save Graph As textFile": saveToFile();
		break;
		case "isConnected": isConnected();
		break;
		case "Shortest Path Dist": shortestPathDist();
		break;
		case "Shortest Path": shortestPath();
		break;
		case "TSP": TSP();
		break;
		}

	}
	/**
	 * Prints a message box that tells if the graph is strongly connected or not.
	 */
	private void isConnected()
	{
		JFrame jinput = new JFrame();
		if (_graph.getV().size() == 0 || this._graph == null)
			JOptionPane.showMessageDialog(jinput, "Init a graph first");
		else
		{
			Graph_Algo ga = new Graph_Algo();
			ga.init(_graph);

			if (ga.isConnected())
				JOptionPane.showMessageDialog(jinput, "The Graph is Strongly Connected");
			else
				JOptionPane.showMessageDialog(jinput, "The Graph is NOT Strongly Connected");
		}
	}
	/**
	 * Save the current graph into a deserializable file.
	 */
	private void saveToFile()
	{
		JFrame jinput = new JFrame();
		if (_graph.getV().size() == 0 || this._graph == null)
			JOptionPane.showMessageDialog(jinput, "The graph is empty, there is nothing to save");
		else
		{
			Graph_Algo ga = new Graph_Algo();
			ga.init(this._graph);
			JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jf.setDialogTitle("Choose a directory to save your file: ");

			jf.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
			jf.addChoosableFileFilter(filter);

			int returnV = jf.showOpenDialog(null);
			if (returnV == JFileChooser.APPROVE_OPTION) 
			{
				try 
				{
					ga.save(jf.getSelectedFile()+".txt");
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
		}
	}
	/**
	 * Init a graph from serializable file
	 */
	private void initGraph()
	{
		Graph_Algo ga = new Graph_Algo();
		JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		jf.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		jf.addChoosableFileFilter(filter);

		int returnV = jf.showOpenDialog(null);
		if (returnV == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jf.getSelectedFile();
			ga.init(selectedFile.getAbsolutePath());
			this._graph = ga.copy();
			repaint();
		}
	}
	private void makeGraph()
	{
		this._graph.addNode(new Vertex(1,new Point3D(100,100), 8.4));
		this._graph.addNode(new Vertex(2,new Point3D(50,300), 20));
		this._graph.addNode(new Vertex(3,new Point3D(400,150), 30));
		this._graph.addNode(new Vertex(4,new Point3D(88,76), 55));
		this._graph.addNode(new Vertex(5,new Point3D(566,444), 7.8));
		this._graph.addNode(new Vertex(6,new Point3D(45,78), 60));
		this._graph.addNode(new Vertex(7,new Point3D(203,567), 35));
		_graph.connect(1, 2, 5);
		_graph.connect(3, 1, 6);
		_graph.connect(1, 3, 3);
		_graph.connect(2, 4, 4);
		_graph.connect(2, 5, 1);
		_graph.connect(3, 2, 2);
		_graph.connect(4, 6, 8);
		_graph.connect(4, 5, 9);
		_graph.connect(5, 6, 4);
		_graph.connect(5, 7, 5);
		_graph.connect(6, 7, 5);
		_graph.connect(7, 2, 5);
		repaint();
	}
	private void shortestPathDist()
	{
		JFrame jinput = new JFrame();
		if (_graph.getV().size() == 0 || this._graph == null)
		{
			JOptionPane.showMessageDialog(jinput, "The graph is empty");
			return;
		}

		String src_key = JOptionPane.showInputDialog(jinput,"Enter Src Vertex Key");		
		String dest_key = JOptionPane.showInputDialog(jinput,"Enter Destination Vertex Key");
		try
		{
			int src = Integer.parseInt(src_key);
			int dest = Integer.parseInt(dest_key);
			Graph_Algo ga = new Graph_Algo();
			ga.init(this._graph);
			double dis = ga.shortestPathDist(src, dest);
			if (dis == -1 || dis == Integer.MAX_VALUE)
				JOptionPane.showMessageDialog(jinput, "There isn't a path between src to dest");
			else
				JOptionPane.showMessageDialog(jinput, "The shortest distance between them is:" + dis);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	/**
	 * this function will paint green the shortest path (if exist) between src and dest vertexes.
	 */
	private void shortestPath()
	{
		JFrame jinput = new JFrame();
		if (_graph.getV().size() == 0 || this._graph == null)
		{
			JOptionPane.showMessageDialog(jinput, "The graph is empty");
			return;
		}

		String src_key = JOptionPane.showInputDialog(jinput,"Enter From");		
		String dest_key = JOptionPane.showInputDialog(jinput,"Enter To");
		try
		{
			int src = Integer.parseInt(src_key);
			int dest = Integer.parseInt(dest_key);
			Graph_Algo ga = new Graph_Algo();
			ga.init(_graph);
			List<node_data> ans = ga.shortestPath(src, dest);
			//System.out.println(ans.toString());
			if (ans == null || ans.size() == 0)
				JOptionPane.showMessageDialog(jinput, "The isn't a valid path between src to dest!!");
			else
			{
				for (int j = 0; j < ans.size()-1; j++) 
				{
					_graph.getEdge(ans.get(j).getKey(), ans.get(j+1).getKey()).setTag(555);
				}
				repaint();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This will paint green the shortest path between all vertexes entered, if at least 1 target isn't reachable a messagebox will shown
	 */
	private void TSP()
	{
		JFrame jinput = new JFrame();
		if (_graph.getV().size() == 0 || this._graph == null)
			JOptionPane.showMessageDialog(jinput, "The graph is empty");
		else
		{
			String amount = JOptionPane.showInputDialog(jinput, "How many targets?");
			ArrayList<Integer> arrayTSP = new ArrayList<Integer>();
			for (int i = 1; i <= Integer.parseInt(amount); i++) 
			{
				try
				{
					String x = JOptionPane.showInputDialog(jinput, "Enter The "+i+"'th Target Node");
					arrayTSP.add(Integer.parseInt(x));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			Graph_Algo ga = new Graph_Algo();
			ga.init(this._graph);
			List<node_data> ansTSP = ga.TSP(arrayTSP); 
			if (ansTSP == null || ansTSP.size() == 0 || ansTSP.size() == 1)
				JOptionPane.showMessageDialog(jinput, "There isn't a valid path between 1 or more vertexes");
			else
			{
				for (int j = 0;j < ansTSP.size()-1; j++) 
				{
					//JOptionPane.showMessageDialog(jinput, "Node index: "+ansTSP.get(j).getKey());
					this._graph.getEdge(ansTSP.get(j).getKey(), ansTSP.get(j+1).getKey()).setTag(555);
				}
				repaint();
			}

		}
	}

}
