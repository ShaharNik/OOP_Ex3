package MyDataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class myDGraph implements graph, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7198496570353106889L;

	Hashtable<Integer, node_data> Vertexes;

	Hashtable<node_data, Hashtable<Integer,edge_data>> Edges;

	private int edgesCounter=0;
	private int MC=0;

	/**
	 * This Function will initialize a graph from a json string thats represents the graph.
	 * @param JsonString - a graph parameters in json
	 */
	public void initFromJson(String JsonString)
	{
		try 
		{
			JSONObject obj = new JSONObject(JsonString);
			JSONArray nodes = obj.getJSONArray("Nodes");
			JSONArray edges = obj.getJSONArray("Edges");
			for (int i = 0; i < nodes.length(); i++) 
			{
				JSONObject CurrNode = (JSONObject)nodes.get(i);
				String pos = CurrNode.getString("pos");
				int id = CurrNode.getInt("id");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				node_data n = new Vertex(id, new Point3D(x, y, z));
				this.addNode(n);

			}
			for (int i = 0; i < edges.length(); i++) 
			{
				JSONObject CurrNode = (JSONObject)edges.get(i);
				int src = CurrNode.getInt("src");
				double w = CurrNode.getDouble("w");
				int dest = CurrNode.getInt("dest");
				this.connect(src, dest, w);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
	}

	public myDGraph()
	{
		this.Vertexes = new Hashtable<Integer, node_data>();
		this.Edges = new Hashtable<node_data, Hashtable<Integer, edge_data>>();
		this.edgesCounter=0;
		this.MC=0;
	}

	@Override
	public node_data getNode(int key) 
	{
		// TODO Auto-generated method stub
		//table can't contains null values so dont need to check , but should check key?
		MC++;
		return Vertexes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) 
	{
		// TODO Auto-generated method stub
		MC++;
		node_data vert = Vertexes.get(src);
		edge_data Edge = Edges.get(vert).get(dest);
		return Edge;
	}


	@Override
	public void addNode(node_data n) 
	{
		// TODO Auto-generated method stub
		MC++;
		if(!Vertexes.containsKey(n.getKey()))
		{
			Vertexes.put(n.getKey(), n);
			Edges.put(n, new Hashtable<Integer, edge_data>());
		} 

	}

	@Override
	public void connect(int src, int dest, double w) 
	{
		// TODO Auto-generated method stub
		MC++;
		node_data vert = Vertexes.get(src);
		Edges.get(vert).put(dest, new Edge(src, dest, w));
		edgesCounter++;

	}

	@Override
	public Collection<node_data> getV() 
	{
		// TODO Auto-generated method stub
		MC++;
		return Vertexes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) 
	{
		// TODO Auto-generated method stub
		MC++;
		node_data vert = Vertexes.get(node_id);
		return Edges.get(vert).values();
	}

	@Override
	public node_data removeNode(int key) 
	{
		// TODO Auto-generated method stub
		MC++;
		node_data vertex = Vertexes.get(key);
		Set<node_data> sets_of_nodes = Edges.keySet();
		for (node_data node_data : sets_of_nodes)
		{
			edge_data edge = Edges.get(node_data).remove(key);
			if(edge != null)
			{
				edgesCounter--;
			}

		}
		edgesCounter = edgesCounter - Edges.get(vertex).size();

		Edges.remove(vertex);
		return Vertexes.remove(key);
	}

	@Override
	public edge_data removeEdge(int src, int dest) 
	{
		// TODO Auto-generated method stub
		MC++;
		node_data vert = Vertexes.get(src);
		return Edges.get(vert).remove(dest);
	}

	@Override
	public int nodeSize() 
	{
		// TODO Auto-generated method stub
		MC++;
		return Vertexes.size();
	}

	@Override
	public int edgeSize() 
	{
		// TODO Auto-generated method stub
		return this.edgesCounter;
	}

	@Override
	public int getMC() 
	{
		// TODO Auto-generated method stub
		return MC;
	}

}
