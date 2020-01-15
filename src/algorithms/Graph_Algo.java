package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import MyDataStructure.*;
//import MyDataStructure.myDGraph;
import MyDataStructure.Vertex;
import MyDataStructure.edge_data;
import MyDataStructure.graph;
import MyDataStructure.node_data;
import utils.Point3D;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 720151264462843803L;
	graph graph;

	public Graph_Algo(graph g)
	{
		this.graph = g;
	}
	public Graph_Algo()
	{
		this.graph = new myDGraph();
	}
	@Override
	public void init(graph g) 
	{
		// TODO Auto-generated method stub
		this.graph = g;

	}

	@Override
	public void init(String file_name) 
	{
		// TODO Auto-generated method stub
		try
		{
			FileInputStream f = new FileInputStream(file_name);
			ObjectInputStream o = new ObjectInputStream(f);
			//o.writeObject(this.graph);
			Graph_Algo GA = (Graph_Algo)o.readObject();
			this.graph = GA.graph;
			//this.graph = (graph)o.readObject();
			o.close();
			f.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(String file_name) 
	{
		// TODO Auto-generated method stub
		try
		{
			FileOutputStream f = new FileOutputStream(file_name);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(this);
			o.close();
			f.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isConnected() 
	{
		//graph copy = copy();
		Collection<node_data> vertexes = graph.getV();
		for (node_data node : vertexes) 
		{
			ResetTags(graph);
			if(graph.nodeSize() > count_edges(node))
				return false;	
		}

		return true;
	}

	private int count_edges(node_data v)
	{
		if (v.getTag() == 1) 
			return 0;
		v.setTag(1);
		Collection<edge_data> edgesOfv = graph.getE(v.getKey());
		int count = 1;
		for (edge_data edge : edgesOfv)
		{
			count += count_edges(graph.getNode(edge.getDest()));
		}

		return count;	
	}

	/**
	 * Resets all tags to be 0.
	 * @param graph g
	 */
	private static void ResetTags(graph g) 
	{
		for(node_data v : g.getV()) 
		{
			v.setTag(0);
		}
	}
	private void cleanTags()
	{
		Collection<node_data> Verts = graph.getV();
		for (node_data v : Verts)
		{
			v.setTag(0);
		}
		//PriorityQueue(Collection<E> c)
	}




	private void Dijkstra(int src)
	{

		// Initialize distances of all vertices as infinite, and Set tags to zero.
		setTagsAndWeight();

		//Generate a new Priority Queue with Comparator of weight
		PriorityQueue<node_data> PrioQueue = new PriorityQueue<node_data>(new comperator());

		// Start saving distances using Dikstra From the src vertex
		PrioQueue.add(graph.getNode(src)); 
		// set first node weight 0 zero
		PrioQueue.peek().setWeight(0); 


		while (!PrioQueue.isEmpty()) // while not all vertexes reached
		{
			//Extract minimum distance vertex from Set. 
			node_data currNode = PrioQueue.poll();

			if (currNode.getTag() == 0) // Vertex not stepped yet
			{
				currNode.setTag(1); // mark vertex as visited
				PrioQueue.poll(); // remove from queue.
				Collection<edge_data> edges = graph.getE(currNode.getKey()); // Extract minimum distance vertex from prioQueue
				for (edge_data edge : edges) // Loop through all adjacent of u and do following for every destination vertex.	       
				{
					node_data destNode = graph.getNode(edge.getDest()); // dest vertex
					if(destNode.getWeight() > currNode.getWeight() + edge.getWeight())
					{
						destNode.setWeight(currNode.getWeight() + edge.getWeight());
						destNode.setInfo(currNode.getKey()+"");
						if(destNode.getTag() == 0)
						{
							PrioQueue.add(destNode);
						}
					}
				}
			}
			else
			{
				PrioQueue.poll();
			}
		}
	}

	@Override
	public double shortestPathDist(int src, int dest) 
	{
		// TODO Auto-generated method stub

		//if (!this.isConnected()) // if the graph isn't connected
		//return Double.POSITIVE_INFINITY;

		if (src == dest)
			return 0;
		if (src < 0 || dest < 0)
			return -1;
		if (this.graph.getV().size() == 0 || this.graph == null)
			return -1;

		Dijkstra(src);

		return graph.getNode(dest).getWeight();
	}


	private void setTagsAndWeight()
	{
		Collection<node_data> c_node_data = graph.getV();
		for (node_data node_data : c_node_data) 
		{
			node_data.setTag(0);
			node_data.setWeight(Integer.MAX_VALUE);
			node_data.setInfo("");
		}
	}

	/**
	 * The function starts from the destination vertex, every vertex info is the prev vertex that we came from, that's how we backtrack finding the path.
	 **/
	@Override
	public List<node_data> shortestPath(int src, int dest) 
	{
		// TODO Auto-generated method stub
		Dijkstra(src);
		List<node_data> ans = new ArrayList<node_data>();

		node_data currNode = graph.getNode(dest);
		while(!currNode.getInfo().isEmpty())
		{
			ans.add(0, currNode);
			currNode = graph.getNode(Integer.parseInt(currNode.getInfo()));
		}
		ans.add(0, currNode);
		return ans;
	}
	private boolean isTherePath(int src, int dest) 
	{
		//if(graph.getNode(src).getWeight() == Integer.MAX_VALUE || graph.getNode(dest).getWeight() == Integer.MAX_VALUE)
		//return false;
		if(shortestPath(src, dest) == null || shortestPath(src, dest).size() == 0) 
			return false;


		return true;
	}
	@Override
	public List<node_data> TSP(List<Integer> targets) 
	{
		// TODO Auto-generated method stub
		List <node_data> ans = new ArrayList<>();
		if (targets.size() == 0)
			return ans;

		if (targets.size() == 1)
		{
			ans.add(graph.getNode(targets.get(0)));
			return ans;
		}

		// if will be needed, an option to remove duplicates
		// List<Integer> newList = targets.stream().distinct().collect(Collectors.toList()); 

		if (!isTherePath(targets.get(0), targets.get(1)))
			return null;

		ans.addAll(shortestPath(targets.get(0), targets.get(1)));

		int last_path = 1;

		for(int i = 2; i < targets.size(); i++)
		{
			if(!ans.contains(this.graph.getNode(targets.get(i))))
			{
				if (!isTherePath(targets.get(last_path), targets.get(i)))
					return null;
				ans.addAll(shortestPath(targets.get(last_path), targets.get(i)));

				// Update the last node we checked path from
				last_path = i;
				ans.remove(i);
			}
		}
		return ans;
	}


	@Override
	public graph copy() 
	{
		// TODO Auto-generated method stub
		graph new_graph = new myDGraph();
		Collection<node_data> nodes = graph.getV();
		for (node_data vert : nodes) 
		{
			new_graph.addNode(new Vertex(vert));
			Collection<edge_data> edges = graph.getE(vert.getKey());
			for (edge_data edge : edges) 
			{
				new_graph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
			}
		}
		return new_graph;
	}
}
class comperator implements Comparator<node_data>
{

	@Override
	public int compare(node_data o1, node_data o2) 
	{
		// TODO Auto-generated method stub
		return (int)(o1.getWeight() - o2.getWeight());
	}

}


