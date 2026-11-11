package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AdjacencyMatrixGraphTest {

	// Local fixture builder — three edges forming a triangle
	private AdjacencyMatrixGraph triangle() {
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge("A", "B", 2.0));
		edges.add(new Edge("A", "C", 3.0));
		edges.add(new Edge("B", "C", 5.0));
		return new AdjacencyMatrixGraph(edges);
	}

	@Test
	public void sizeMatchesDistinctVertexCount() {
		// 3 edges but only 3 distinct names across them
		assertEquals(3, triangle().size());
	}

	@Test
	public void labelsAreSortedIndicesAreStable() {
		AdjacencyMatrixGraph g = triangle();
		assertEquals("A", g.label(0));
		assertEquals("B", g.label(1));
		assertEquals("C", g.label(2));
		assertEquals(0, g.indexOf("A"), "index lookup agrees with label order");
	}

	@Test
	public void indexOfUnknownNameIsMinusOne() {
		assertEquals(-1, triangle().indexOf("Z"),
				"-1 sentinel instead of an exception or a bogus index");
	}

	@Test
	public void matrixIsSymmetric() {
		AdjacencyMatrixGraph g = triangle();
		// Undirected guarantee: weight(A,B) must equal weight(B,A)
		assertEquals(g.weight("A", "B"), g.weight("B", "A"), 1e-9,
				"undirected graph => symmetric matrix");
	}

	@Test
	public void neighborsComeBackSortedByIndex() {
		AdjacencyMatrixGraph g = triangle();
		// Row 0 is A: neighbors B (1) and C (2) in ascending order
		assertEquals(List.of(1, 2), g.neighbors(0));
	}

	@Test
	public void missingEdgeIsNoEdgeSentinel() {
		AdjacencyMatrixGraph g = triangle();
		assertEquals(AdjacencyMatrixGraph.NO_EDGE, g.weight("A", "Z"), 0.0,
				"unknown node names report no edge, never throw");
		assertTrue(g.weight("A", "A") == AdjacencyMatrixGraph.NO_EDGE,
				"no self-loops: diagonal stays NO_EDGE");
	}

	@Test
	public void parallelEdgesKeepLastWeight() {
		// Unlike the list (which preserves duplicates), the matrix has ONE
		// cell per pair — a duplicate line overwrites. Pinning this
		// behavior down so a future change doesn't surprise anyone.
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge("A", "B", 1.0));
		edges.add(new Edge("A", "B", 9.0));
		AdjacencyMatrixGraph g = new AdjacencyMatrixGraph(edges);
		assertEquals(9.0, g.weight("A", "B"), 1e-9, "last write wins in the matrix");
	}
}