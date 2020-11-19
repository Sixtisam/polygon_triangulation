package net.skeeks.efalg.poly_tria;

public class Edge {
	private Vertex start;
	private Vertex end;

	private double xSlope;

	public Edge() {

	}

	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
		this.xSlope = calcXSlope();
	}

	double calcXSlope() {
		// X Steigung
		double my = (end.y - start.y);
		if (my == 0.0) {
			return Double.POSITIVE_INFINITY; // horizontal edge
		}
		return (end.x - start.x) / my;
	}

	public double calcIntersectionX(int y) {
		if (xSlope == Double.POSITIVE_INFINITY) {
			return start.x;
		} else {
			return start.x + (y - start.y) * xSlope;
		}
	}

	@Override
	public String toString() {
		return start.toString() + " -> " + end.toString();
	}

	public static class SearchEdge extends Edge {
		public int x;

		@Override
		public double calcIntersectionX(int y) {
			return x;
		}

		@Override
		public String toString() {
			return "[Search Edge " + x + "]";
		}
	}

}
