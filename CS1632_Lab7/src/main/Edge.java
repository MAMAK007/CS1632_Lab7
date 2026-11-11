package main;

public class Edge {
	private final String src;
	private final String dest;
	private final double weight;

	public Edge(String src, String dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}

	public String getSrc() {
		return src;
	}

	public String getDest() {
		return dest;
	}

	public double getWeight() {
		return weight;
	}
}