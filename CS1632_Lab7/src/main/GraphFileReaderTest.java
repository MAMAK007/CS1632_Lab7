package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GraphFileReaderTest {

	private final GraphFileReader reader = new GraphFileReader();

	// JUnit creates this folder fresh for EACH test method, then deletes it
	@TempDir
	Path tempDir;

	// Helper: writes content to a temp CSV and returns the path.
	// Keeping this in one place lets each test stay readable.
	private String writeFile(String content) throws IOException {
		Path file = tempDir.resolve("test.csv");
		Files.write(file, content.getBytes());
		return file.toString();
	}

	@Test
	public void parsesValidCsvLines() throws IOException {
		List<Edge> edges = reader.readEdges(writeFile("A,B,2\nC,D,3.5\n"));
		assertEquals(2, edges.size(), "both lines should parse");
		assertEquals("A", edges.get(0).getSrc());
		assertEquals("B", edges.get(0).getDest());
		assertEquals(2.0, edges.get(0).getWeight(), 1e-9);
		assertEquals("C", edges.get(1).getSrc());
		assertEquals(3.5, edges.get(1).getWeight(), 1e-9);
	}

	@Test
	public void trimsSpacesAroundTokens() throws IOException {
		// Files hand-typed or saved from Excel often look like "A, B, 2"
		List<Edge> edges = reader.readEdges(writeFile("A, B , 2\n"));
		assertEquals(1, edges.size(), "spaced tokens should still parse");
		assertEquals("A", edges.get(0).getSrc(), "src should be trimmed");
		assertEquals("B", edges.get(0).getDest(), "dest should be trimmed");
	}

	@Test
	public void skipsMalformedLinesButKeepsGoodOnes() throws IOException {
		// "A,B" has no weight — must be skipped, not crash the whole read
		List<Edge> edges = reader.readEdges(writeFile("A,B\nA,C,3\n"));
		assertEquals(1, edges.size(), "bad line skipped, good line kept");
		assertEquals("C", edges.get(0).getDest());
	}

	@Test
	public void skipsLinesWithBadWeights() throws IOException {
		List<Edge> edges = reader.readEdges(writeFile("A,B,xyz\nA,C,3\n"));
		assertEquals(1, edges.size(), "non-numeric weight skipped");
		assertEquals(3.0, edges.get(0).getWeight(), 1e-9);
	}

	@Test
	public void skipsBlankLines() throws IOException {
		List<Edge> edges = reader.readEdges(writeFile("\nA,B,2\n\n"));
		assertEquals(1, edges.size(), "blank lines ignored");
	}

	@Test
	public void skipsLinesWithEmptyNodeNames() throws IOException {
		// "A,,2" splits into three tokens, but the middle one is empty
		List<Edge> edges = reader.readEdges(writeFile("A,,2\nA,C,3\n"));
		assertEquals(1, edges.size(), "empty node name skipped");
	}

	@Test
	public void missingFileThrowsFileNotFoundException() {
		// assertThrows takes a lambda: the code that SHOULD blow up goes
		// inside. No `throws` clause needed on the test method because
		// the checked exception is consumed by assertThrows itself.
		String missingPath = tempDir.resolve("does_not_exist.csv").toString();
		assertThrows(FileNotFoundException.class, () -> reader.readEdges(missingPath));
	}

	@Test
	public void emptyFileYieldsEmptyEdgeList() throws IOException {
		List<Edge> edges = reader.readEdges(writeFile(""));
		assertTrue(edges.isEmpty(), "no lines means no edges, not an error");
	}
}