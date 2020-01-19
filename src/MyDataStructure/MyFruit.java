package MyDataStructure;
import java.util.Collection;

import org.json.JSONObject;

import utils.Point3D;

public class MyFruit implements Comparable<MyFruit>
{
	/**
	 * This class represents a fruit (object the robots can collect on the graph.
	 * The fruit contains its position(x,y) on the graph, the value the robot get when eating it,
	 * the graph the fruit is on.
	 * the type -1 or 1 represents the direction a fruit can be eaten.
	 * boolean targeted represents if the fruit is targeted by a robot.
	 * edge - the edge on the graph the fruit is on, edge have src and dest keys.
	 */
	static double EPS = 0.00001;
	Point3D pos;
	edge_data edge = null;
	int value;
	int type;
	graph g;
	boolean targeted = false;

	public MyFruit() 
	{
		// TODO Auto-generated constructor stub
		this.edge = null;
		this.pos = null;
		this.value = 0;
		this.type = -1;
		this.g = null;
	}
	public MyFruit(graph g)
	{
		this.g = g;
		this.edge=null;
		this.pos=null;
		this.value = 0;
		this.type = -1;
	}
	public MyFruit(edge_data edge,Point3D pos,int val,int type, graph g)
	{
		this.edge = new Edge(edge);
		this.pos = new Point3D(pos);
		this.value = val;
		this.type = type;
		this.g = g;
	}
	/** Initializing fruit from json file
	 * @param jsonString
	 */
	public void initFromJson(String jsonString)
	{
		if(!jsonString.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(jsonString);
				JSONObject CurrFruit = (JSONObject) obj.get("Fruit");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int value = CurrFruit.getInt("value");
				this.value = value;
				int type = CurrFruit.getInt("type");
				this.type = type;
				findEdge();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	public void setOccupied(boolean targeted)
	{
		this.targeted = targeted;
	}
	public boolean getTargeted()
	{
		return this.targeted;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	public edge_data getEdge() {
		return edge;
	}
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/** find and set the edge the fruit is on.
	 */
	public void findEdge()
	{
		if(g != null)
		{
			Collection<node_data> nd = g.getV();
			for (node_data node_data : nd) 
			{
				Collection<edge_data> ed = g.getE(node_data.getKey());
				for (edge_data edges : ed) 
				{
					int src = edges.getSrc();
					int dest = edges.getDest();
					if( (src-dest) * this.type < 0 )
					{
						node_data srcN = g.getNode(src);
						node_data destN = g.getNode(dest);
						Point3D srcD = srcN.getLocation();
						Point3D destD = destN.getLocation();
						double allDist = srcD.distance2D(destD);
						double srcToPos = srcD.distance2D(pos);
						double posToDest = pos.distance2D(destD);
						if(Math.abs(allDist - (srcToPos + posToDest)) <= EPS)
						{
							this.edge = edges;
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public int compareTo(MyFruit otherFruit) {
		// TODO Auto-generated method stub
		return this.value - otherFruit.value;
	}

}

