package gameClient;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import MyDataStructure.*;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import MyDataStructure.node_data;
import utils.Point3D;
import utils.StdDraw_gameGUI;

/**
 * 
 * @author Shahar and Or
 *
 */
public class MyGameGUI 
{

	graph _graph;
	ArrayList<MyFruit> _fruits = new ArrayList<MyFruit>();
	ArrayList<Robot> _bots;

	double minx=Integer.MAX_VALUE;
	double maxx=Integer.MIN_VALUE;
	double miny=Integer.MAX_VALUE;
	double maxy=Integer.MIN_VALUE;


	public MyGameGUI(graph g) 
	{
		// TODO Auto-generated constructor stub
		this._graph = g;
		this._fruits = new ArrayList<MyFruit>();
		this._bots = new ArrayList<Robot>();
		initGUI();
	}
	public MyGameGUI() 
	{
		// TODO Auto-generated constructor stub
		_graph = new myDGraph();
		this._fruits = new ArrayList<MyFruit>();
		this._bots = new ArrayList<Robot>();
		initGUI();

	}

	public static void main(String[] args) 
	{

		MyGameGUI ggnew = new MyGameGUI();
	}

	private void initGUI()
	{

		if(StdDraw_gameGUI.getDrawed() == false)
		{
			StdDraw_gameGUI.setCanvasSize(800, 800);
			StdDraw_gameGUI.enableDoubleBuffering();
			StdDraw_gameGUI.setDrawed();
		}

		if(_graph != null)
		{
			Collection<node_data> nd = _graph.getV();
			for (node_data node_data : nd) 
			{
				Point3D s = node_data.getLocation();
				if(s.x() < minx)
					minx = s.x();
				if(s.x() > maxx)
					maxx = s.x();
				if(s.y() > maxy)
					maxy = s.y();
				if(s.y() < miny)
					miny = s.y();
			}
			StdDraw_gameGUI.setXscale(minx, maxx);
			StdDraw_gameGUI.setYscale(miny,maxy);
			StdDraw_gameGUI.setG_GUI(this);
			paint();
			StdDraw_gameGUI.show();
		}
	}


	public void paint()
	{
		StdDraw_gameGUI.clear();
		if (_graph != null)
		{
			Collection<node_data> points = _graph.getV();
			for(node_data v : points)
			{
				StdDraw_gameGUI.setPenColor(Color.BLUE);
				StdDraw_gameGUI.filledCircle(v.getLocation().x(), v.getLocation().y(), 0.0001);
				//g.fillOval((int)v.getLocation().x(), (int)v.getLocation().y(), 10, 10);
				StdDraw_gameGUI.text(v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.03, String.valueOf(v.getKey()));
				Collection<edge_data> edgesOfVertex = _graph.getE(v.getKey());
				for (edge_data edge : edgesOfVertex) 
				{
					if(edge.getTag() == 555)
					{
						edge.setTag(0);
						StdDraw_gameGUI.setPenColor(Color.GREEN);
					}
					else
					{
						StdDraw_gameGUI.setPenColor(Color.RED);
					}
					StdDraw_gameGUI.line(v.getLocation().x(), v.getLocation().y(), _graph.getNode(edge.getDest()).getLocation().x(), _graph.getNode(edge.getDest()).getLocation().y());
					StdDraw_gameGUI.setPenColor(Color.YELLOW);
					double xHalf = (v.getLocation().x() + _graph.getNode(edge.getDest()).getLocation().x()) /2;
					double yHalf = (v.getLocation().y() + _graph.getNode(edge.getDest()).getLocation().y()) /2;
					double halfOfHalf_X = (xHalf + _graph.getNode(edge.getDest()).getLocation().x()) / 2;
					double halfOfHalf_Y = (yHalf + _graph.getNode(edge.getDest()).getLocation().y()) / 2;
					StdDraw_gameGUI.filledCircle(halfOfHalf_X, halfOfHalf_Y, (maxx-minx)*0.004);
				}
			}
		}
		if(!_fruits.isEmpty())
		{
			for (int i = 0; i < _fruits.size(); i++) {
				MyFruit currF = _fruits.get(i);
				Point3D p = currF.getPos();
				if(currF.getType() == 1)
				{
					StdDraw_gameGUI.setPenColor(Color.GREEN);
				}
				else // -1
				{
					StdDraw_gameGUI.setPenColor(Color.CYAN);
				}
				StdDraw_gameGUI.filledCircle(p.x(), p.y(),(maxx-minx)*0.006	);
			}
		}
		if(!_bots.isEmpty())
		{
			for (int i = 0; i < _bots.size(); i++) {
				Robot currB = _bots.get(i);
				Point3D p = currB.getPos();
				StdDraw_gameGUI.setPenColor(Color.BLACK);
				StdDraw_gameGUI.filledCircle(p.x(), p.y(), 0.0002);
			}
		}
		StdDraw_gameGUI.show();
		StdDraw_gameGUI.pause(30);
	}

	public void Play_manual(String scenario)
	{
		try
		{
			int number = Integer.parseInt(scenario);
			if(number>=0 && number<=23)
			{

				//init graph by scenario
				game_service game = Game_Server.getServer(number);
				String g = game.getGraph();
				myDGraph gg = new myDGraph();
				gg.initFromJson(g);
				this._graph = gg;

				//init fruits
				Iterator<String> f_iter = game.getFruits().iterator();
				_fruits = new ArrayList<MyFruit>();
				if(f_iter.hasNext())
				{
					while(f_iter.hasNext())
					{
						String json = f_iter.next();
						MyFruit f = new MyFruit(this._graph);
						//System.out.println(json);
						f.initFromJson(json);
						//System.out.println("x = " +f.getPos().ix() +" y = " + f.getPos().iy());
						//System.out.println("The edge is: " +f.getEdge());
						_fruits.add(f);
					}

				}

				// init robots
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject)obj.get("GameServer");
				int amountRob = CurrGame.getInt("robots");
				int robotsPlaced = 0;
				_bots = new ArrayList<Robot>();
				Collections.sort(_fruits); // compare by fruit value
				int j = _fruits.size()-1;
				while(robotsPlaced < amountRob && j > 0)
				{
					game.addRobot(_fruits.get(j).getEdge().getSrc()); //edge is null
					robotsPlaced++;
					j--;
				}
				List<String> Robots = game.getRobots();
				for (int i = 0; i < Robots.size(); i++) 
				{
					Robot b = new Robot();
					b.botFromJSON(Robots.get(i));
					_bots.add(b);
				}
				initGUI();
				StdDraw_gameGUI.pause(30);
				moveManual(game,gg);				

			}
			else
			{
				JFrame jinput = new JFrame();
				JOptionPane.showMessageDialog(jinput,"Not a number between 0 - 23");
				jinput.dispose();
			}

		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}
	/**
	 * This Functions get graph and robot src position,
	 * and returns the next destination according to user's mouse click
	 */
	private int ManualNode(graph g, int src)
	{
		int ans = -1;

		//Iterator<edge_data> itr = edgesOfsrc.iterator();
		double mouseX = 0;
		double mouseY = 0;
		while (true)
		{
			if (StdDraw_gameGUI.isMousePressed())
			{
				//get mouse click coordinates
				mouseX = StdDraw_gameGUI.mouseX();
				mouseY = StdDraw_gameGUI.mouseY();

				// make point from mouse coordinates
				Point3D mouseP = new Point3D(mouseX, mouseY);

				//make  point for src node (robot current place)
				Point3D srcP =  g.getNode(src).getLocation();

				// get neighboors robot can go to
				Collection<edge_data> edgesOfsrc = g.getE(src);

				for (edge_data edge : edgesOfsrc) 
				{
					node_data dest = g.getNode(edge.getDest());
					Point3D destP = g.getNode(dest.getKey()).getLocation();
					if (mouseP.distance2D(destP) < (maxx-minx)*0.005) 
					{
						ans = dest.getKey();
						return dest.getKey();
					}
				}
			}
		}
	}

	private int AutoNextNode(graph g, int RoboSrc)
	{
		int ans = -1;
		Graph_Algo ga = new Graph_Algo();
		Collection<edge_data> edgesOfRobot = g.getE(RoboSrc);
		double minDistance = Double.POSITIVE_INFINITY;
		int shortestRobot_src;
		int shortestFruit_src;
		for (int i = 0; i < this._fruits.size(); i++) 
		{
			MyFruit cur_fruit = _fruits.get(i);
			if (ga.shortestPathDist(RoboSrc, cur_fruit.getEdge().getSrc()) < minDistance && cur_fruit.getTargeted() == false)
			{
				//shortestRobot_src = 
			}
			/*
			for (edge_data edge : edgesOfRobot) 
			{
				node_data nd = edge.getDest();
				if (ga.shortestPathDist(RoboSrc, nd.getKey()))
				{
					
				}
			}
			*/	
		}
		return ans;
	}

	public void Play_Automaticly()
	{

	}
	 //random walk
	private int nextNode(graph g, int src) 
	{
		int ans = -1;
		Collection<edge_data> edgesOfsrc = g.getE(src);
		Iterator<edge_data> itr = edgesOfsrc.iterator();
		int s = edgesOfsrc.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	//The automatic movement
	public void moveAuto(game_service game, myDGraph gg)
	{
		paint();
		game.startGame();
		int i=0;
		while(game.isRunning()) 
		{
			initGUI();

			long t = game.timeToEnd();
			//System.out.println("roung: "+i+"  seconds to end:"+(t/1000));
			List<String> log = game.move();
			//for (int j = 0; j < log.size(); j++) {
			//	System.out.println(log.get(j));
			//}
			if(log!=null) 
			{

				String robot_json = log.get(0);
				//	System.out.println(robot_json);
				JSONObject line;
				try 
				{ // robo move
					line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int robo_id = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1) // need to move robo
					{	
						dest = AutoNextNode(gg, src); // the function to chose next node to move
						
						
						game.chooseNextEdge(robo_id, dest); // the game moves the robot
						Robot b = _bots.get(0);
						b.setPos(_graph.getNode(dest).getLocation());
						//System.out.println("Turn to node: "+dest);
						//System.out.println(ttt);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
			i++;
		}
	}
	//the manual movement
	public void moveManual(game_service game, myDGraph gg)
	{
		paint();
		game.startGame();
		int i=0;
		while(game.isRunning()) 
		{
			initGUI();

			long t = game.timeToEnd();
			//System.out.println("roung: "+i+"  seconds to end:"+(t/1000));
			List<String> log = game.move();
			for (int j = 0; j < log.size(); j++) {
				System.out.println(log.get(j));
			}
			if(log!=null) 
			{

				String robot_json = log.get(0);
				//	System.out.println(robot_json);
				JSONObject line;
				try 
				{ // robo move
					line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1) // need to move robo
					{	
						dest = ManualNode(gg, src); // the function to chose next node to move
						
						
						game.chooseNextEdge(rid, dest); // the game moves the robot
						Robot b = _bots.get(0);
						b.setPos(_graph.getNode(dest).getLocation());
						//System.out.println("Turn to node: "+dest);
						//System.out.println(ttt);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
			i++;
		}
	}
	public void stop() {
		// TODO Auto-generated method stub

	}
}