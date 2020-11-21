package net.skeeks.efalg.poly_tria;

public class HalfEdge {
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
	public HalfEdge twin;

	/**
	 * The next edge (counter-clockwise)
	 */
	public HalfEdge next;
	

	/**
	 * The face which lays to the left (counter-clockwise order) of this edge.
	 */
	public Face face;
	
	private double xSlope = Double.NaN;

	protected HalfEdge() {
		// for subclass 'search-edge'
	}
	
	/**
	 * Returns the vertex this edge is leading to
	 */
	public Vertex to() {
		return next.from;
	}

	double calcXSlope() {
		// X Steigung
		Vertex end = to();
		Vertex start = from;
		double my = (end.y - start.y);
		if (my == 0.0) {
			return Double.POSITIVE_INFINITY; // horizontal edge
		}
		return (end.x - start.x) / my;
	}

	public double calcIntersectionX(int y) {
		if(Double.isNaN(xSlope)) {
			xSlope = calcXSlope();
		}
		if (xSlope == Double.POSITIVE_INFINITY) {
			return from.x;
		} else {
			return from.x + (y - from.y) * xSlope;
		}
	}

	@Override
	public String toString() {
		return from.toString() + " -> " + to().toString();
	}

	public static class SearchEdge extends HalfEdge {
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
