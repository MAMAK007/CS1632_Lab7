package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AdjacencyListGraph {
	// TreeMap keeps node names sorted, so iteration (and BFS discovery
	// order) is alphabetical: A, B, C, ...
	private Map<String, List<Edge>> adj = new TreeMap<>();

	// Adds an undirected edge: dest becomes a neighbor of src AND
	// src becomes a neighbor of dest, each carrying the same weight.
	public void addEdge(String src, String dest, double weight) {
		adj.computeIfAbsent(src, k -> new ArrayList<>()).add(new Edge(src, dest, weight));
		adj.computeIfAbsent(dest, k -> new ArrayList<>()).add(new Edge(dest, src, weight));
	}

	// Sorts every neighbor list by destination name so BFS is deterministic
	public void sortNeighborLists() {
		for (List<Edge> list : adj.values()) {
			list.sort((e1, e2) -> e1.getDest().compareTo(e2.getDest()));
		}
	}

	// Returns the destination names of all neighbors of `node`, in sorted order
	public List<String> neighbors(String node) {
		List<String> result = new ArrayList<>();
		List<Edge> list = adj.get(node);
		if (list != null) {
			for (Edge e : list) {
				result.add(e.getDest());
			}
		}
		return result;
	}

	// All node names, sorted
	public Set<String> nodes() {
		return adj.keySet();
	}

	// Defensive copy of the edge list for a node, or an empty list
	// if the node has no recorded edges (e.g. an isolated vertex)
	public List<Edge> edgesOf(String node) {
		return Collections.unmodifiableList(adj.getOrDefault(node, new ArrayList<>()));
	}

	public void print() {
		System.out.println("Adjacency List:");
		for (Map.Entry<String, List<Edge>> entry : adj.entrySet()) {
			StringBuilder sb = new StringBuilder("  " + entry.getKey() + " -> ");
			for (Edge e : entry.getValue()) {
				sb.append(e.getDest()).append("(").append(e.getWeight()).append(") ");
			}
			System.out.println(sb);
		}
	}
}