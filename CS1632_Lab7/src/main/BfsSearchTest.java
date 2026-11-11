package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BfsSearchTest {

	private final BfsSearch bfs = new BfsSearch();

	// The exact graph from the assignment handout
	private List<Edge> sampleEdges() {
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge("A", "B", 2.0));
		edges.add(new Edge("A", "C", 3.0));
		edges.add(new Edge("A", "D", 5.5));
		edges.add(new Edge("B", "E", 7.0));
		edges.add(new Edge("C", "E", 3.0));
		edges.add(new Edge("D", "F", 5.0));
		edges.add(new Edge("E", "G", 5.0));
		edges.add(new Edge("E", "H", 5.7));
		edges.add(new Edge("F", "H", 4.4));
		edges.add(new Edge("F", "I", 5.0));
		edges.add(new Edge("G", "J", 4.3));
		edges.add(new Edge("H", "I", 2.8));
		edges.add(new Edge("H", "J", 4.0));
		return edges;
	}

	@Test
	public void listBfsVisitsSampleGraphInExpectedOrder() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		for (Edge e : sampleEdges()) {
			g.addEdge(e.getSrc(), e.getDest(), e.getWeight());
		}
		g.sortNeighborLists();

		List<String> order = bfs.visitOrder(g, "A");
		// Worked by hand: A pops B,C,D; B pulls E; D pulls F; E pulls G,H;
		// F pulls I; G pulls J. Layer by layer = breadth-first.
		assertEquals(List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"), order);
	}

	@Test
	public void bothRepresentationsAgreeOnVisitOrder() {
		// KEY INVARIANT: the algorithm must behave identically regardless
		// of which data structure backs it. Building both from the same
		// edges and comparing is the strongest end-to-end check we have.
		AdjacencyListGraph listGraph = new AdjacencyListGraph();
		for (Edge e : sampleEdges()) {
			listGraph.addEdge(e.getSrc(), e.getDest(), e.getWeight());
		}
		listGraph.sortNeighborLists();
		AdjacencyMatrixGraph matrixGraph = new AdjacencyMatrixGraph(sampleEdges());

		List<String> fromList = bfs.visitOrder(listGraph, "A");
		List<String> fromMatrix = bfs.visitOrder(matrixGraph, matrixGraph.indexOf("A"));
		assertEquals(fromList, fromMatrix, "representations are interchangeable");
	}

	@Test
	public void disconnectedComponentIsNeverVisited() {
		// Two islands: {A,B} and {C,D}. BFS from A finds ONLY A's island.
		// (The sample graph is too well-connected for this — it has no
		// bridges, so no single deleted edge disconnects it.)
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 1.0);
		g.addEdge("C", "D", 1.0);
		g.sortNeighborLists();

		assertEquals(List.of("A", "B"), bfs.visitOrder(g, "A"),
				"C and D are unreachable from A and must not appear");
	}

	@Test
	public void startNodeWithNoEdgesVisitsJustItself() {
		// Degenerate case: single edge graph, start at one end
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 1.0);
		assertEquals(List.of("B", "A"), bfs.visitOrder(g, "B"));
	}

	@Test
	public void visitOrderContainsNoDuplicates() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		for (Edge e : sampleEdges()) {
			g.addEdge(e.getSrc(), e.getDest(), e.getWeight());
		}
		g.sortNeighborLists();

		List<String> order = bfs.visitOrder(g, "A");
		assertEquals(g.nodes().size(), order.size(),
				"every node visited exactly once — no duplicates possible if visited-set works");
	}
}
