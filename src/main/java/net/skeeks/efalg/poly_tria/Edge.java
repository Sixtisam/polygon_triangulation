package net.skeeks.efalg.poly_tria;

public class Edge {
	public Vertex start;
	public Vertex end;
	public Vertex helper;
	
	/**
	 * The start point of this edge
	 */
	public Vertex from;
	
	/**
	 * End point is omitted for memory reasons
	 */
	
	/**
	 * The edge in the opposite direction (to() -> from)
	 */
	public Edge twin;
	
	/**
	 * The next edge (counter-clockwise)
	 */
	public Edge next;

	private double xSlope;

	public Edge() {

	}
	
	public Edge(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
		this.xSlope = calcXSlope();
	}
	
	/**
	 * Returns the vertex this edge is leading to
	 */
	public Vertex to() {
		return next.from;
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
