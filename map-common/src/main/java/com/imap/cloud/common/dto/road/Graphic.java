package com.imap.cloud.common.dto.road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class Graphic {
	
	public static void main(String[] args) {
		Graphic g = new Graphic();
		g.addVertex("0dc7088833004d73b9194df8985f4f78",new Vertex("38ff3cf81f494379b3bfa069c57ceda9", 710.3844699866875));
		g.addVertex("2e297a6f07854e6094855f111357c151",new Vertex("38ff3cf81f494379b3bfa069c57ceda9", 271.84726091257517));
		g.addVertex("38ff3cf81f494379b3bfa069c57ceda9",new Vertex("0dc7088833004d73b9194df8985f4f78", 710.3844699866875));
		g.addVertex("38ff3cf81f494379b3bfa069c57ceda9",new Vertex("2e297a6f07854e6094855f111357c151", 271.84726091257517));
		g.addVertex("38ff3cf81f494379b3bfa069c57ceda9",new Vertex("9877644aaff44c9a8b4bf02092af0cd2", 352.46015503251806));
		g.addVertex("38ff3cf81f494379b3bfa069c57ceda9",new Vertex("b8bd103fb5b34061aa624e3932f71e4d", 201.47511317324285));
		g.addVertex("9877644aaff44c9a8b4bf02092af0cd2",new Vertex("38ff3cf81f494379b3bfa069c57ceda9", 352.46015503251806));
		g.addVertex("b8bd103fb5b34061aa624e3932f71e4d",new Vertex("38ff3cf81f494379b3bfa069c57ceda9", 201.47511317324285));
		//[b8bd103fb5b34061aa624e3932f71e4d, 0dc7088833004d73b9194df8985f4f78, 2e297a6f07854e6094855f111357c151, 9877644aaff44c9a8b4bf02092af0cd2, 38ff3cf81f494379b3bfa069c57ceda9, b8bd103fb5b34061aa624e3932f71e4d]
		System.out.println(g.getShortestPath("b8bd103fb5b34061aa624e3932f71e4d", "0dc7088833004d73b9194df8985f4f78"));
		
	}

	private Map<String, List<Vertex>> vertices;
	
	public Graphic() {
		this.vertices = new HashMap<String, List<Vertex>>();
	}
	
	public void addVertex(String key, List<Vertex> vertexs) {
		List<Vertex> list = this.vertices.get(key);
		if(list==null) list = new ArrayList<Vertex>();
		if(vertexs!=null)list.addAll(vertexs);
		this.vertices.put(key, list);
	}
	
	public void addVertex(String key, Vertex vertex) {
		List<Vertex> list = this.vertices.get(key);
		if(list==null) list = new ArrayList<Vertex>();
		if(vertex!=null)list.add(vertex);
		this.vertices.put(key, list);
	}
	public Map<String, List<Vertex>> getVertexs() {	
		return this.vertices;
	}
	
	private List<String> calculate(String start, String finish) {
		final Map<String, Double> distances = new HashMap<String, Double>();
		final Map<String, Vertex> previous = new HashMap<String, Vertex>();
		PriorityQueue<Vertex> nodes = new PriorityQueue<Vertex>();
		
		for(String vertex : vertices.keySet()) {
			if (vertex.equals(start)) {
				distances.put(vertex, 0d);
				nodes.add(new Vertex(vertex, 0d));
			} else {
				distances.put(vertex, Double.MAX_VALUE);
				nodes.add(new Vertex(vertex, Double.MAX_VALUE));
			}
			previous.put(vertex, null);
		}
		
		while (!nodes.isEmpty()) {
			Vertex smallest = nodes.poll();
			if (smallest.getId().equals(finish)) {
				final List<String> path = new ArrayList<String>();
				while (previous.get(smallest.getId()) != null) {
					path.add(smallest.getId());
					smallest = previous.get(smallest.getId());
				}
				return path;
			}

			if (distances.get(smallest.getId()) == Double.MAX_VALUE) {
				break;
			}
						
			for (Vertex neighbor : vertices.get(smallest.getId())) {
				Double alt = distances.get(smallest.getId()) + neighbor.getDistance();
				if(distances.get(neighbor.getId()) == null)
					continue;
				if (alt < distances.get(neighbor.getId())) {
					distances.put(neighbor.getId(), alt);
					previous.put(neighbor.getId(), smallest);
					
					forloop:
					for(Vertex n : nodes) {
						if (n.getId().equals(neighbor.getId())) {
							nodes.remove(n);
							n.setDistance(alt);
							nodes.add(n);
							break forloop;
						}
					}
				}
			}
		}
		
		return new ArrayList<String>(distances.keySet());
	}
	
	public List<String> getShortestPath(String start, String finish) {
		List<String> list = this.calculate(start, finish);
		list.add(start);
		java.util.Collections.reverse(list);
		return list;
	}
}
