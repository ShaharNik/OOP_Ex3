package gameClient;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import MyDataStructure.Edge;
import MyDataStructure.MyFruit;
import MyDataStructure.Robot;
import MyDataStructure.Vertex;
import MyDataStructure.edge_data;
import MyDataStructure.graph;
import MyDataStructure.myDGraph;
import MyDataStructure.node_data;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import utils.Point3D;
import utils.StdDraw_gameGUI;

/**
 * This class represent a GUI game which Robots need to eat as many fruits they can,
 * The game have manual place by the mouse,
 * and automatic gameplay based on dijkstra algorithm 
 * @author Shahar and Or
 *
 */
public class MyGameGUI 
{

	graph _graph;
	ArrayList<MyFruit> _fruits;
	ArrayList<Robot> _bots;
	game_service game;
	KML_Logger myKML;

	double minx = Integer.MAX_VALUE;
	double maxx = Integer.MIN_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxy = Integer.MIN_VALUE;

	// -==========     Constructors =============----
	public MyGameGUI(graph g) 
	{
		this._graph = g;
		this._fruits = new ArrayList<MyFruit>();
		this._bots = new ArrayList<Robot>();
		this.myKML = new KML_Logger();
		initGUI();
	}

	public MyGameGUI() {
		// TODO Auto-generated constructor stub
		_graph = new myDGraph();
		this._fruits = new ArrayList<MyFruit>();
		this._bots = new ArrayList<Robot>();
		this.myKML = new KML_Logger();
		initGUI();
	}

	//// ------============= Painting ============---------
	private void initGUI() {

		if (StdDraw_gameGUI.getDrawed() == false) {
			StdDraw_gameGUI.setCanvasSize(800, 800);
			StdDraw_gameGUI.enableDoubleBuffering();
			StdDraw_gameGUI.setDrawed();
		}

		if (_graph != null) {
			Collection<node_data> nd = _graph.getV();
			for (node_data node_data : nd) {
				Point3D s = node_data.getLocation();
				if (s.x() < minx)
					minx = s.x();
				if (s.x() > maxx)
					maxx = s.x();
				if (s.y() > maxy)
					maxy = s.y();
				if (s.y() < miny)
					miny = s.y();
			}
			StdDraw_gameGUI.setXscale(minx, maxx);
			StdDraw_gameGUI.setYscale(miny, maxy);
			StdDraw_gameGUI.setG_GUI(this);
			paint();
			StdDraw_gameGUI.show();
		}
	}

	private void paint() {
		StdDraw_gameGUI.clear();
		if (_graph != null) 
		{
			if (game != null)
				StdDraw_gameGUI.text(maxx, maxy, "TimeLeft:"+game.timeToEnd()/1000);
			Collection<node_data> points = _graph.getV();
			for (node_data v : points) {
				StdDraw_gameGUI.setPenColor(Color.BLUE);
				StdDraw_gameGUI.filledCircle(v.getLocation().x(), v.getLocation().y(), 0.0001);
				// g.fillOval((int)v.getLocation().x(), (int)v.getLocation().y(), 10, 10);
				StdDraw_gameGUI.text(v.getLocation().x(), v.getLocation().y() + (maxy - miny) * 0.03,
						String.valueOf(v.getKey()));
				Collection<edge_data> edgesOfVertex = _graph.getE(v.getKey());
				for (edge_data edge : edgesOfVertex) {
					if (edge.getTag() == 555) {
						edge.setTag(0);
						StdDraw_gameGUI.setPenColor(Color.GREEN);
					} else {
						StdDraw_gameGUI.setPenColor(Color.RED);
					}
					StdDraw_gameGUI.line(v.getLocation().x(), v.getLocation().y(),
							_graph.getNode(edge.getDest()).getLocation().x(),
							_graph.getNode(edge.getDest()).getLocation().y());
				}
			}
		}
		if (!_fruits.isEmpty()) {
			for (int i = 0; i < _fruits.size(); i++) {
				MyFruit currF = _fruits.get(i);
				Point3D p = currF.getPos();
				if (currF.getType() == 1) 
				{ //Apple
					//StdDraw_gameGUI.setPenColor(Color.GREEN);
					StdDraw_gameGUI.picture(p.x(), p.y(),"cup.jpeg",(maxx-minx)*0.02,(maxx-minx)*0.02);
				} else // -1 banana
				{
					//StdDraw_gameGUI.setPenColor(Color.CYAN);
					StdDraw_gameGUI.picture(p.x(), p.y(),"ball.jpeg",(maxx-minx)*0.02,(maxx-minx)*0.02);
				}
				//StdDraw_gameGUI.filledCircle(p.x(), p.y(), (maxx - minx) * 0.006);
			}
		}
		if (!_bots.isEmpty()) {
			for (int i = 0; i < _bots.size(); i++) {
				Robot currB = _bots.get(i);
				Point3D p = currB.getPos();
				//StdDraw_gameGUI.setPenColor(Color.BLACK);
				//StdDraw_gameGUI.filledCircle(p.x(), p.y(), 0.0002);
				StdDraw_gameGUI.picture(p.x(), p.y(),"ronaldo.jpeg",(maxx-minx)*0.02,(maxx-minx)*0.02);
			}
		}
		StdDraw_gameGUI.show();
		StdDraw_gameGUI.pause(30);
	}


	///  --------======= Game Play ========-----------

	boolean algo = true;
	boolean gui = true;
	public void setAlgo(boolean isAlgo)
	{
		this.algo = isAlgo;
	}
	public void setgui(boolean isGUI)
	{
		this.gui = isGUI;
	}

	public static void main(String[] args) {
		MyGameGUI ggnew = new MyGameGUI();
		//SimpleDB db = new SimpleDB();
		//db.allUsers();
		//db.printLog();
	}

	public void Play(String scenario) 
	{
		// login to server
		//int id = 316416668;
		//Game_Server.login(id);
		//System.out.println("Login to server with id: "+id);

		try 
		{
			int number = Integer.parseInt(scenario);
			if (number >= 0 && number <= 23) 
			{

				game = Game_Server.getServer(number);

				myKML.setGame(game);
				myKML.createKMLforScenario();

				// init graph by scenario
				initGraph(number);

				System.out.println("Starting thread...");
				ThreadForPlay t = new ThreadForPlay();
				t.start();

			} 
			else 
			{
				JFrame jinput = new JFrame();
				JOptionPane.showMessageDialog(jinput, "Not a number between 0 - 23");
				jinput.dispose();
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public class ThreadForPlay extends Thread 
	{
		long dt = 10;
		public void run() 
		{
			System.out.println("Init fruits");
			initOrUpdateFruits();

			System.out.println("Placing the robots");
			putRobotsFirstTime();

			System.out.println("Init robots");
			initRobots();

			System.out.print("Fruits=" + _fruits.size() + " : ");
			for (MyFruit f : _fruits) 
			{
				System.out.print("(" + f.getEdge().getSrc() + "," + f.getEdge().getDest() + ")");
			}
			System.out.println();

			if (gui) 
			{
				initGUI();
				System.out.println("GUI initialized");
			}


			System.out.println("Starting the game");
			game.startGame();

			long timeToEnd = game.timeToEnd();
			//long test = System.currentTimeMillis();
			while (game.isRunning()) 
			{
				/*
				if (isRobotCloseToFruit())
					dt = 0;
				else
					dt = 100;
				 */
				
				// update fruits
				initOrUpdateFruits();
				
				
				//bot position is close to fruit location
				//if (timeToEnd - game.timeToEnd() > 40)
				//{
					gameMove();
					//timeToEnd = game.timeToEnd();
				//}

				myKML.initFruits();
				myKML.initRobots();
						
				for (Robot r : _bots) 
				{
					if (r.getDest() == -1) 
					{
						if (algo) 
						{
							// algo move
							node_data nextNode = chooseNextEdgeForRobot(r);
							System.out.println("Next node = " + nextNode.getKey());
							game.chooseNextEdge(r.getId(), nextNode.getKey());
						} 
						else 
						{
							// manual move
							System.out.println("Waiting for manual move for robot " + r.getId());
							int dest = chooseNextManualDest(r.getCurrNode().getKey()); // choose next dest for this
																						// robot by
							// mouse
							game.chooseNextEdge(r.getId(), dest); // commit the move to game
							r.setPos(_graph.getNode(dest).getLocation()); // update robot position after movement
						}
					}
				}			
				if (gui) {
					paint();
				}
				
				try {
					Thread.sleep(dt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			myKML.FinishAndClose();
			System.out.println("Finish game");
			System.out.println("RealScore: " + game.toString());
			System.out.println("Score: " + calculateScore());
		}
		private boolean isRobotCloseToFruit()
		{
			for (Robot r : _bots) 
			{
				for (MyFruit f : _fruits)
				{
					Point3D RobotLocation = r.getPos();
					Point3D fruitEdgeSrcPos = _graph.getNode(f.getEdge().getDest()).getLocation();
					Point3D fruitEdgeDestPos = _graph.getNode(f.getEdge().getDest()).getLocation();
					if (RobotLocation.close2equals(fruitEdgeSrcPos) 
						|| RobotLocation.close2equals(f.getPos())
						|| RobotLocation.close2equals(fruitEdgeDestPos))
						{
							return true;
						}
				}
			}
			return false;
		}
		private boolean edgeHasRobot(edge_data e) 
		{
			for (Robot r : _bots) 
			{
				if (r.getCurrEdge() == null)
					continue;

				if (e.getSrc() == r.getCurrEdge().getSrc() && e.getDest() == r.getCurrEdge().getDest())
					return true;
			}
			return false;
		}
		/**
		 * Each robot contains a path to target fruit, if he reached it, the function smartly will calculate next path to a new fruit.
		 * we will check for every fruit the edge its on, and if there is a robot on that edge.
		 * If not - we will check the current direction that we can collect the fruit, there 2 options - the correct node or we will do 2 steps to collect it.
		 * If a path isn't founded, we will do a random step to avoid "stucks".
		 * @param r - gets robot
		 * @return The robots next node destination
		 */
		private node_data chooseNextEdgeForRobot(Robot r) 
		{
			System.out.println(r);
			if (r.getPath() == null || r.getPath().size() - 1 == r.getPathIndex()) 
			{
				boolean found = false;
				r.setPath(null);
				r.setPathIndex(-1);
				r.setCurrEdge(null);

				for (MyFruit f : _fruits) 
				{

					edge_data e = f.getEdge();

					if (edgeHasRobot(e)) //edge has robot
						continue;

					int node = -1;
					int before = -1;
					if (f.getType() == 1) { // apple
						node = Math.max(e.getDest(), e.getSrc());
						before = Math.min(e.getDest(), e.getSrc());
					} else { // banana
						node = Math.min(e.getDest(), e.getSrc());
						before = Math.max(e.getDest(), e.getSrc());
					}

					System.out.println("Targeting edge " + before + "->" + node + " dt: "+dt);

					int currentNode = r.getCurrNode().getKey();
					if (currentNode == node) 
					{
						System.out.println("already in wanted node...");
						r.setPath(
								new ArrayList<>(Arrays.asList(new Vertex(node), new Vertex(before), new Vertex(node))));
						r.setCurrEdge(e);
						r.setPathIndex(0);
						found = true;
						break;
					}

					Graph_Algo algorithms = new Graph_Algo(_graph);

					List<node_data> path = algorithms.shortestPath(currentNode, node);

					if (path.get(path.size() - 2).getKey() != before) 
					{
						System.out.println("not in the current direction so need 2 more moves");
						path.add(new Vertex(before));
						path.add(new Vertex(node));
					}

					r.setPath(path);
					r.setCurrEdge(e);
					r.setPathIndex(0);
					found = true;
					break;
				}

				if (!found) {
					System.out.println("Using random...");
					int currentNode = r.getCurrNode().getKey();
					node_data next = getNextRandom(currentNode);
					r.setPath(new ArrayList<>(Arrays.asList(_graph.getNode(currentNode), next)));
					r.setCurrEdge(new Edge(currentNode, next.getKey(), 0));
					r.setPathIndex(0);
				}
			}

			r.setPathIndex(r.getPathIndex() + 1);
			return r.getPath().get(r.getPathIndex());
		}

		/**
		 * This Functions get robot src position, if the user's mouse click is close to
		 * the robot neighbor: He sets it as the new robot dest and returns it.
		 */
		private int chooseNextManualDest(int src) 
		{
			int ans = -1;
			double mouseX = 0;
			double mouseY = 0;
			while (true)
			{
				if (StdDraw_gameGUI.isMousePressed()) 
				{
					// get mouse click coordinates
					mouseX = StdDraw_gameGUI.mouseX();
					mouseY = StdDraw_gameGUI.mouseY();

					// make point from mouse coordinates
					Point3D mouseP = new Point3D(mouseX, mouseY);

					// make point for src node (robot current place)
					Point3D srcP = MyGameGUI.this._graph.getNode(src).getLocation();

					// get neighbors robot can go to
					Collection<edge_data> edgesOfsrc = MyGameGUI.this._graph.getE(src);

					for (edge_data edge : edgesOfsrc) 
					{
						node_data dest = MyGameGUI.this._graph.getNode(edge.getDest());
						Point3D destP = MyGameGUI.this._graph.getNode(dest.getKey()).getLocation();
						if (mouseP.distance2D(destP) < (maxx - minx) * 0.005) 
						{
							ans = dest.getKey();
							System.out.println("Next is " + ans);
							return ans;
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		private node_data getNextRandom(int currentNode) {
			int ans = -1;
			Collection<edge_data> ee = _graph.getE(currentNode);
			Iterator<edge_data> itr = ee.iterator();
			int s = ee.size();
			int r = (int) (Math.random() * s);
			int i = 0;
			while (i < r) {
				itr.next();
				i++;
			}
			ans = itr.next().getDest();
			return _graph.getNode(ans);
		}
	}

	private void initGraph(int scene_number) {
		String g = game.getGraph();
		myDGraph gg = new myDGraph();
		gg.initFromJson(g);
		this._graph = gg;
	}

	private void putRobotsFirstTime() {
		String gameString = game.toString();
		JSONObject obj;
		try {
			obj = new JSONObject(gameString);
			JSONObject CurrGame = (JSONObject) obj.get("GameServer");
			int amountRob = CurrGame.getInt("robots");
			int robotsPlaced = 0;
			Collections.sort(_fruits); // compare by fruit value
			int j = _fruits.size() - 1;
			while (robotsPlaced < amountRob && j >= 0) {
				int node = _fruits.get(j).getEdge().getSrc();
				System.out.println("Place robot in node=" + node);
				game.addRobot(node); // edge is null
				robotsPlaced++;
				j--;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initOrUpdateFruits() {
		// init fruits
		Iterator<String> f_iter = game.getFruits().iterator();
		_fruits = new ArrayList<MyFruit>();
		if (f_iter.hasNext()) {
			while (f_iter.hasNext()) {
				String json = f_iter.next();
				MyFruit f = new MyFruit(this._graph);
				f.initFromJson(json);
				_fruits.add(f);
			}

		}
	}

	private void initRobots() {
		// init robots
		try 
		{
			_bots = new ArrayList<Robot>();
			List<String> Robots = game.getRobots();
			for (int i = 0; i < Robots.size(); i++) {
				Robot b = new Robot();
				b.setGrap(_graph);
				b.botFromJSON(Robots.get(i));
				_bots.add(b);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void gameMove() {
		List<String> jsons = game.move();
		for (String json : jsons) {
			// System.out.println(json);
			Robot b = new Robot();
			b.setGrap(_graph);
			b.botFromJSON(json);
			updateRobotInfo(b);
		}
	}

	private void updateRobotInfo(Robot b) {
		for (Robot r : _bots) {
			if (r.getId() == b.getId()) {
				r.setCurrNode(b.getCurrNode());
				r.setMoney(b.getMoney());
				r.setPos(b.getPos());
				r.setDest(b.getDest());
			}
		}
	}

	private double calculateScore() {
		double score = 0;
		for (Robot r : _bots)
			score += r.getMoney();
		return score;
	}


}