package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AdjacencyMatrixGraph {
	// Sentinel meaning "no edge" — a constant belongs on the class, not instances
	public static final double NO_EDGE = Double.POSITIVE_INFINITY;

	private String[] labels;              // sorted node names; index i = row/col i
	private Map<String, Integer> indexOf; // node name -> matrix index
	private double[][] matrix;            // matrix[i][j] = weight, or NO_EDGE

	// Builds the matrix from a list of parsed edges.
	public AdjacencyMatrixGraph(List<Edge> edges) {
		// Collect all node names into a TreeSet to get them sorted and de-duplicated
		Set<String> names = new TreeSet<>();
		for (Edge e : edges) {
			names.add(e.getSrc());
			names.add(e.getDest());
		}

		labels = names.toArray(new String[0]);
		indexOf = new TreeMap<>();
		for (int i = 0; i < labels.length; i++) {
			indexOf.put(labels[i], i);
		}

		// Fill with NO_EDGE first, then stamp in the weights
		matrix = new double[labels.length][labels.length];
		for (double[] row : matrix) {
			Arrays.fill(row, NO_EDGE);
		}
		for (Edge e : edges) {
			int i = indexOf.get(e.getSrc());
			int j = indexOf.get(e.getDest());
			// Undirected: set both [i][j] and [j][i]
			matrix[i][j] = e.getWeight();
			matrix[j][i] = e.getWeight();
		}
	}

	// Number of vertices in the graph
	public int size() {
		return labels.length;
	}

	// Name of the node at row/column i
	public String label(int i) {
		return labels[i];
	}

	// Matrix index for a node name, or -1 if the name is not in the graph
	public int indexOf(String name) {
		Integer idx = indexOf.get(name);
		return (idx == null) ? -1 : idx;
	}

	// Returns the column indices of all neighbors of row i
	public List<Integer> neighbors(int i) {
		List<Integer> result = new ArrayList<>();
		for (int j = 0; j < labels.length; j++) {
			if (matrix[i][j] != NO_EDGE) {
				result.add(j);
			}
		}
		return result;
	}

	// Weight of the edge between two node names, or NO_EDGE if none
	public double weight(String src, String dest) {
		Integer i = indexOf.get(src);
		Integer j = indexOf.get(dest);
		if (i == null || j == null) {
			return NO_EDGE;
		}
		return matrix[i][j];
	}

	public void print() {
		System.out.println("Adjacency Matrix:");
		// Header row
		System.out.print("      ");
		for (String label : labels) {
			System.out.printf("%-6s", label);
		}
		System.out.println();
		// One row per node
		for (int i = 0; i < labels.length; i++) {
			System.out.printf("%-6s", labels[i]);
			for (int j = 0; j < labels.length; j++) {
				if (matrix[i][j] == NO_EDGE) {
					System.out.printf("%-6s", "-");
				} else {
					System.out.printf("%-6.1f", matrix[i][j]);
				}
			}
			System.out.println();
		}
	}
}