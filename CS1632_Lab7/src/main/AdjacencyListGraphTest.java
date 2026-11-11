package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AdjacencyListGraphTest {

	@Test
	public void addEdgeRecordsBothDirections() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 2.0);
		// Undirected: B must appear in A's list AND vice versa
		assertEquals(List.of("B"), g.neighbors("A"));
		assertEquals(List.of("A"), g.neighbors("B"));
	}

	@Test
	public void neighborsAreSortedAfterSortCall() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		// Deliberately added out of order to prove sorting happens
		g.addEdge("A", "D", 5.5);
		g.addEdge("A", "B", 2.0);
		g.addEdge("A", "C", 3.0);
		g.sortNeighborLists();
		assertEquals(List.of("B", "C", "D"), g.neighbors("A"),
				"sorted neighbor list makes BFS order deterministic");
	}

	@Test
	public void nodesReturnsAllVertexNamesSorted() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("C", "A", 1.0);
		g.addEdge("A", "B", 1.0);
		assertEquals(List.of("A", "B", "C"), List.copyOf(g.nodes()),
				"a node counts as present even if it only appears as a dest");
	}

	@Test
	public void neighborsOfUnknownNodeIsEmptyNotCrash() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 1.0);
		assertTrue(g.neighbors("Z").isEmpty(),
				"asking about a missing node should be safe (BFS guards with a visited check)");
	}

	@Test
	public void edgesOfIsUnmodifiable() {
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 1.0);
		// The defensive copy we added: callers must not be able to reach
		// inside the graph and mutate its edge lists
		assertThrows(UnsupportedOperationException.class, () -> g.edgesOf("A").add(new Edge("X", "Y", 1.0)));
	}

	@Test
	public void parallelEdgesAppearTwiceInNeighborList() {
		// Duplicate lines in a file are legal input: the edge "A-B" twice
		// means two parallel edges. Worth pinning down the behavior.
		AdjacencyListGraph g = new AdjacencyListGraph();
		g.addEdge("A", "B", 1.0);
		g.addEdge("A", "B", 9.0);
		assertEquals(2, g.edgesOf("A").size(), "parallel edges preserved, not merged");
	}
}
