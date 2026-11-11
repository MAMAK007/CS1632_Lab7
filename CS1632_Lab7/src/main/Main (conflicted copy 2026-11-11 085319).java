package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

	// Sentinel meaning "no edge" in the adjacency matrix
	private double NO_EDGE = Double.POSITIVE_INFINITY;

	// Make BFS adjacency list for vertex
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

	// Make matrix of adjacency matrices
	public static void bfsMatrix(AdjacencyMatrixGraph g, int start) {
		boolean[] visited = new boolean[g.labels.length];
		Deque<Integer> queue = new ArrayDeque<>();

		visited[start] = true;
		queue.add(start);

		System.out.print("BFS order (adjacency matrix): ");
		while (!queue.isEmpty()) {
			int current = queue.remove();
			System.out.print(g.labels[current] + " ");
			for (int neighbor : g.neighbors(current)) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					queue.add(neighbor);
				}
			}
		}
		System.out.println();
	}

	// File reading
	public List<Edge> readEdges(String filename) throws FileNotFoundException {
		List<Edge> edges = new ArrayList<>();
		Scanner fileScanner = new Scanner(new File(filename));
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine().trim();
			if (line.isEmpty()) {
				continue; // skip blank lines
			}
			String[] parts = line.split("\\s+");
			if (parts.length != 3) {
				System.out.println("Skipping malformed line: " + line);
				continue;
			}
			try {
				double weight = Double.parseDouble(parts[2]);
				edges.add(new Edge(parts[0], parts[1], weight));
			} catch (NumberFormatException nfe) {
				System.out.println("Skipping line with bad weight: " + line);
			}
		}
		fileScanner.close();
		return edges;
	}

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the graph file name: ");
		String filename = keyboard.nextLine().trim();

		List<Edge> edges;
		try {
			edges = readEdges(filename);
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
			listGraph.addEdge(e.src, e.dest, e.weight);
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
		bfsMatrix(matrixGraph, matrixGraph.indexOf.get(start));

		keyboard.close();
	}
}

