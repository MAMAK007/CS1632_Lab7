package main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EdgeTest {

	@Test
	public void gettersReturnConstructorValues() {
		Edge e = new Edge("A", "B", 2.5);
		assertEquals("A", e.getSrc(), "src should round-trip");
		assertEquals("B", e.getDest(), "dest should round-trip");
		assertEquals(2.5, e.getWeight(), 1e-9, "weight should round-trip (delta for doubles)");
	}
}