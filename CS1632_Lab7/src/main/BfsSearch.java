package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BfsSearch {

	// BFS on the adjacency list. FIFO queue + visited set: each node is
	// added exactly once. Sorted neighbor lists give alphabetical order.
	public List<String> visitOrder(AdjacencyListGraph g, String start) {
		Set<String> visited = new HashSet<>();
		Deque<String> queue = new ArrayDeque<>();
		List<String> order = new ArrayList<>();

		visited.add(start);
		queue.add(start);

		while (!queue.isEmpty()) {
			String current = queue.remove();
			order.add(current);
			for (String neighbor : g.neighbors(current)) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.add(neighbor);
				}
			}
		}
		return order;
	}

	// BFS on the adjacency matrix. Same algorithm; neighbors found by
	// scanning row i for non-NO_EDGE cells, left to right — naturally
	// alphabetical since labels are sorted.
	public List<String> visitOrder(AdjacencyMatrixGraph g, int start) {
		boolean[] visited = new boolean[g.size()];
		Deque<Integer> queue = new ArrayDeque<>();
		List<String> order = new ArrayList<>();

		visited[start] = true;
		queue.add(start);

		while (!queue.isEmpty()) {
			int current = queue.remove();
			order.add(g.label(current));
			for (int neighbor : g.neighbors(current)) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					queue.add(neighbor);
				}
			}
		}
		return order;
	}
}
