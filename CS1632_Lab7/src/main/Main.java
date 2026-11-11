package main;

import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

	// BFS on the adjacency list. FIFO queue + visited set: each node
	// prints exactly once. Sorted neighbor lists give alphabetical order.
	public static void bfsList(AdjacencyListGraph g, String start) {
		Set<String> visited = new HashSet<>();
		Deque<String> queue = new ArrayDeque<>();

		visited.add(start);
		queue.add(start);

		System.out.print("BFS order (adjacency list): ");
		while (!queue.isEmpty()) {
			String current = queue.remove();
			System.out.print(current + " ");
			for (String neighbor : g.neighbors(current)) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.add(neighbor);
				}
			}
		}
		System.out.println();
	}

	// BFS on the adjacency matrix. Same algorithm; neighbors are found by
	// scanning row i for non-NO_EDGE cells, left to right, which is
	// naturally alphabetical since labels are sorted.
	public static void bfsMatrix(AdjacencyMatrixGraph g, int start) {
		boolean[] visited = new boolean[g.size()];
		Deque<Integer> queue = new ArrayDeque<>();

		visited[start] = true;
		queue.add(start);

		System.out.print("BFS order (adjacency matrix): ");
		while (!queue.isEmpty()) {
			int current = queue.remove();
			System.out.print(g.label(current) + " ");
			for (int neighbor : g.neighbors(current)) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					queue.add(neighbor);
				}
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the graph file name: ");
		String filename = keyboard.nextLine().trim();

		GraphFileReader reader = new GraphFileReader();
		List<Edge> edges;
		try {
			edges = reader.readEdges(filename);
		} catch (FileNotFoundException fnfe) {
			System.out.println("Error: could not find file \"" + filename + "\"");
			keyboard.close();
			return;
		}

		if (edges.isEmpty()) {
			System.out.println("No edges found in file — nothing to do.");
			keyboard.close();
			return;
		}

		// Build representation 1: adjacency list
		AdjacencyListGraph listGraph = new AdjacencyListGraph();
		for (Edge e : edges) {
			listGraph.addEdge(e.getSrc(), e.getDest(), e.getWeight());
		}
		listGraph.sortNeighborLists();

		// Build representation 2: adjacency matrix (from the same edge list)
		AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph(edges);

		// Show both structures so you can verify the file was read correctly
		listGraph.print();
		System.out.println();
		matrixGraph.print();
		System.out.println();

		// BFS from the alphabetically-first node (A in the sample file)
		String start = listGraph.nodes().iterator().next();
		System.out.println("Starting BFS from node: " + start);
		bfsList(listGraph, start);
		bfsMatrix(matrixGraph, matrixGraph.indexOf(start));

		keyboard.close();
	}
}
