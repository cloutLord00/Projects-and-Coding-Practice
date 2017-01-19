package graphs;

import java.util.*;

/**
 * Implements a graph. We use two maps: one map for adjacency properties 
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated 
 * with a vertex. 
 * 
 * @author cmsc132
 * 
 * @param <E>
 */
public class Graph<E> {
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;

	/**
	 * Initialize adjacency map and data map
	 */
	public Graph()
	{
		this.adjacencyMap = new HashMap<String, HashMap<String,Integer>>();
		this.dataMap = new HashMap<String,E>();
	}
	
	
	/**
	 * Adds or updates a directed edge with the specified cost.
	 * @param startVertex
	 * @param endVertex
	 * @param cost
	 */
	public void addDirectedEdge(String startVertex, String endVertex, int cost)
	{
		if(!this.dataMap.containsKey(startVertex) || !this.dataMap.containsKey(endVertex))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		this.adjacencyMap.get(startVertex).put(endVertex, cost);
	}
	
	/**
	 * Adds a vertex to the graph by adding to the adjacency map an entry for the vertex. 
	 * This entry will be an empty map. 
	 * An entry in the dataMap will store the provided data.
	 * @param vertexName
	 * @param data
	 */
	public void addVertex(String vertexName, E data)
	{
		if(this.adjacencyMap.containsKey(vertexName))
		{
			throw new IllegalArgumentException("Vertex already exists in the graph!");
		}
		
		this.dataMap.put(vertexName, data);
		this.adjacencyMap.put(vertexName, new HashMap<String,Integer>());
	}

	/**
	 * Computes Breadth-First Search of the specified graph.
	 * @param startVertex
	 * @param callback
	 */
	public void doBreadthFirstSearch(String startVertexName, CallBack<E> callback)
	{
		if(!this.dataMap.containsKey(startVertexName))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		Set<String> visited = new HashSet<String>();
		Queue<String> discovered = new PriorityQueue<String>();
		
		discovered.add(startVertexName);
		
		while(!discovered.isEmpty())
		{
			String V = discovered.poll();
			
			if(!visited.contains(V))
			{
				callback.processVertex(V, this.dataMap.get(V));
				visited.add(V);
				SortedSet<String> neighbors = new TreeSet<String>(this.adjacencyMap.get(V).keySet());
				
				for(String node : neighbors)
				{
					discovered.add(node);
				}
			}

		}
		
	}
	
	/**
	 * Computes Depth-First Search of the specified graph.
	 * @param startVertexName
	 * @param callback
	 */
	public void doDepthFirstSearch(String startVertexName, CallBack<E> callback)
	{
		if(!this.dataMap.containsKey(startVertexName))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		Stack<String> discovered = new Stack<String>();
		Set<String> visited = new HashSet<String>();
		
		discovered.push(startVertexName);
		
		while(!discovered.isEmpty()){
			String V = discovered.pop();
			
			if(!visited.contains(V))
			{
				callback.processVertex(V, this.dataMap.get(V));
				visited.add(V);
				
				SortedSet<String> nextV = new TreeSet<String>(this.adjacencyMap.get(V).keySet());
				
				for(String next : nextV)
				{
					if(!visited.contains(next))
					{
						discovered.push(next);
					}
				}
			}
		}
	
	}
	
	/**
	 * Computes the shortest path and shortest path cost using Dijkstras's algorithm.
	 * @param startVertex
	 * @param endVertex
	 * @param shortestPath
	 */
	public int doDijkstras(String startVertex, String endVertex, ArrayList<String> shortestPath)
	{
		if(!this.dataMap.containsKey(startVertex) || !this.dataMap.containsKey(endVertex))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		
		Set<String> visited = new HashSet<String>();
		
		PriorityQueue<Vertex> minDist = new PriorityQueue<Vertex>();
		
		Vertex firstV = new Vertex(startVertex,startVertex,0);
		minDist.add(firstV);
		
		for(String V : this.adjacencyMap.get(startVertex).keySet())
		{
			minDist.add(new Vertex(V,startVertex,this.getCost(startVertex, V)));
		}
		
		
		//map of vertexName --> VertexObject
		HashMap<String, Vertex> mapV = new HashMap<String,Vertex>();
		mapV.put(startVertex, firstV);	
		
		/*
		 * Init keys-->costs
		 */
		for(String key : this.getVertices())
		{
			if(key.equals(startVertex))
			{
				mapV.put(key, new Vertex(key,null,0));
			}
			else
			{
				mapV.put(key, new Vertex(key,null,Integer.MAX_VALUE));
			}
		}
		
	
		/*
		 * Init List for shortest path
		 */
		LinkedList<String> list = new LinkedList<String>();
		list.add(startVertex);

		HashMap<String,List<String>> path = new HashMap<String,List<String>>();
		path.put(startVertex, list);
		
		while(!minDist.isEmpty())
		{
			Vertex node = minDist.poll();
			String V = node.current;
			int minimum = node.cost;
			System.out.println(minDist.toString());
			
				visited.add(V);
				
				TreeSet<String> adj = new TreeSet<String>(this.adjacencyMap.get(V).keySet());
				
				for(String successor : adj)
				{
					if(!visited.contains(successor) && !V.equals(successor))
					{
							int newCost = this.getCost(V, successor)+minimum;
							
							if(newCost < mapV.get(successor).cost)
							{
								minDist.remove(mapV.get(successor));
								minDist.add(new Vertex(successor,V,newCost));
								mapV.put(successor, new Vertex(successor,V,newCost));

								LinkedList<String> newList = new LinkedList<String>(path.get(V));
								newList.add(successor);
								path.put(successor, newList);
							}
						
					}
				}
				
		}
		
		int smallestPath = mapV.get(endVertex).cost == Integer.MAX_VALUE ? -1 : mapV.get(endVertex).cost;
		
		if(smallestPath == -1)
		{
			shortestPath.add("None");
		}
		else
		{
			for(String node : path.get(endVertex))
			{
				shortestPath.add(node);
			}
		}
		
		return smallestPath;
	}
	
	class Vertex implements Comparable<Vertex>{
		private String current,previous;
		private int cost;
		
		public Vertex(String curr, String prev, int c)
		{
			this.current = curr;
			this.previous = prev;
			this.cost = c;
		}
		
		public boolean equals(Object other)
		{
			if(other == null)
			{
				return false;
			}
			
			if(!(other.getClass() != Vertex.class))
			{
				return false;
			}
			
			Vertex oth = (Vertex) other;
			return (this.current).equals(oth.current);
		}

		@Override
		public int compareTo(Vertex arg0) {
			// TODO Auto-generated method stub
			Integer current = new Integer(this.cost);
			Integer other = new Integer(arg0.cost);
			return current.compareTo(other);
		}
		
		public int hashCode(){
			return (this.current.hashCode()+this.previous.hashCode())*57;
		}
		
		public String toString()
		{
			return this.current;
		}

	}
	
	/**
	 * Returns a map with information about vertices adjacent to vertexName.
	 * @param vertexName
	 */
	public Map<String,Integer> getAdjacentVertices(String vertexName)
	{
		return this.adjacencyMap.get(vertexName);
	}
	
	/**
	 * Returns the cost associated with the specified edge.
	 * @param startVertex
	 * @param endVertex
	 * @return int - cost from startvertex to endvertex
	 */
	public int getCost(String startVertex, String endVertex)
	{
		if(!this.dataMap.containsKey(startVertex) || !this.dataMap.containsKey(endVertex))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		
		return startVertex.equals(endVertex) ? 0 : this.adjacencyMap.get(startVertex).get(endVertex);
	}
	
	/**
	 * Returns the data component associated with the specified vertex.
	 * @param vertex
	 * @return <E> data of vertex
	 */
	public E getData(String vertex)
	{
		if(!this.dataMap.containsKey(vertex))
		{
			throw new IllegalArgumentException("Vertex does not exist in the graph!");
		}
		
		return this.dataMap.get(vertex);
	}
	
	/**
	 * Returns a Set with all the graph vertices.
	 * @return Set<String> of all vertices 
	 */
	public Set<String> getVertices()
	{
		return this.dataMap.keySet();
	}
	
	/**
	 * Returns a string with information about the Graph.
	 * @return String of information about the graph
	 */
	public String toString()
	{
		String str = "";
		
		SortedSet<String> sortedVertices = new TreeSet<String>(this.getVertices());
		
		str += "Vertices: " + sortedVertices.toString() + "\nEdges:\n";
		
		for(String vertex : sortedVertices)
		{
			if(this.dataMap.containsKey(vertex))
			{
				HashMap<String,Integer> adjMap = this.adjacencyMap.get(vertex);
				
				SortedSet<String> sortedKeys = new TreeSet<String>(adjMap.keySet());
				
				str += "Vertex(" + vertex + ")--->{";
				for(String adj : sortedKeys)
				{
					str += adj +"="+ adjMap.get(adj) +", ";
				}
				str = str.trim();
				
				if(!sortedKeys.isEmpty())
				{
					StringBuilder s = new StringBuilder(str);
					s.deleteCharAt(str.length()-1);
					str = s.toString();
				}
				
				str+="}\n";
			}
		}
		
		return str;
	}
}