package MyDataStructure;

import java.util.List;
import java.util.StringJoiner;

import org.json.JSONObject;

import utils.Point3D;
/**This class represents the "robots" that supposed to collect the prizes during the game
 * 
 * @author Shahar and Or
 *
 */
public class Robot 
{

	int id;
	node_data currNode; 
	edge_data currEdge;
	int money;
	Point3D pos;
	double speed;
	graph gg;
	int dest;
	List<node_data> path;
	int pathIndex;
	
	
	public Robot(int id, node_data currNode, edge_data currEdge, int money, Point3D pos, double speed, graph gg) 
	{
		if (gg != null)
			this.gg = gg;
		
		this.id = id;
		if(currNode != null)
		{
		this.currNode = new Vertex(currNode);
		}
		if(currEdge != null)
		{
		this.currEdge = new Edge(currEdge);
		}
		this.money = money;
		this.pos = new Point3D(pos);
		this.speed = speed;
	}

	public int getDest() {
		return dest;
	}
	public Robot() 
	{
		this.id = -1;
		this.currEdge = null;
		this.currNode = null;
		this.money = -1;
		this.pos = null;
		this.speed = -1;
		this.gg = null;
	}
	
	public void setPath(List<node_data> path)
	{
		this.path = path;
	}
	public List<node_data> getPath()
	{
		return this.path;
	}
	public void setGrap(graph g)
	{
		this.gg = g;
	}
	public int getId() {
		return id;
	}
	public node_data getCurrNode() {
		return currNode;
	}
	public void setCurrNode(node_data currNode) {
		this.currNode = currNode;
	}
	public edge_data getCurrEdge() {
		return currEdge;
	}
	public void setCurrEdge(edge_data currEdge) {
		this.currEdge = currEdge;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/** Initializing bot from json file
	 * @param json
	 */
	public void botFromJSON(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
				JSONObject CurrBot = (JSONObject) obj.get("Robot");
				String pos = CurrBot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int id = CurrBot.getInt("id");
				this.id = id;
				int value = CurrBot.getInt("value");
				this.money = value;
				int speed = CurrBot.getInt("speed");
				this.speed = speed;
				this.path = null;
				this.pathIndex=-1;

				this.dest = CurrBot.getInt("dest");
				
				if(this.gg != null)
				{
					int src = CurrBot.getInt("src");
					this.currNode = gg.getNode(src);
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void setDest(int dest2) {
			this.dest=dest2;
		
	}
	
	@Override
	public String toString() 
	{
		StringJoiner joiner = new StringJoiner("->");
		String repr = "Path: ";
		if (path != null) 
		{
			for (int i = 0; i < path.size(); i++) 
			{
				joiner.add(String.valueOf(path.get(i).getKey()));
			}
			repr += joiner.toString();
		}

		repr += " Current Edge: ";
		if (currEdge != null) 
		{
			repr += currEdge.getSrc() + "->" + currEdge.getDest();
		}

		repr += " Path Index: " + pathIndex;
		return repr;
	}
	public int getPathIndex() {
		return pathIndex;
	}
	public void setPathIndex(int indexInPath) {
		this.pathIndex=indexInPath;
	}
	
}
