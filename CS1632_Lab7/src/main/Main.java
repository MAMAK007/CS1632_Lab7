package main;

import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the graph CSV file name: ");
		String filename = keyboard.nextLine().trim();

		GraphFileReader reader = new GraphFileReader();
		List<Edge> edges;
		try {
			edges = reader.readEdges(filename);
		} catch (java.io.FileNotFoundException fnfe) {
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

		// Main's job is now just I/O: get the order, then print it
		BfsSearch bfs = new BfsSearch();
		List<String> listOrder = bfs.visitOrder(listGraph, start);
		List<String> matrixOrder = bfs.visitOrder(matrixGraph, matrixGraph.indexOf(start));

		System.out.println("BFS order (adjacency list): " + String.join(" ", listOrder));
		System.out.println("BFS order (adjacency matrix): " + String.join(" ", matrixOrder));

		keyboard.close();
	}
}
