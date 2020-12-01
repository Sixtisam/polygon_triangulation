package net.skeeks.efalg.poly_tria.core;
//This is a personal academic project. Dear PVS-Studio, please check it.

//PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

/**
 * Represents an directed edge connecting a vertex to another vertex.
 * Each HalfEdge has a 'twin', which is the same edge but in the opposite direction.
 * Each HalfEdge is exactly associated with 1 face. The twin of an edge is in most cases not the same face (except a polygon with multiple holes)
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
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
	
	public HalfEdge prev;
	

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

	/**
	 * Calculates the y-coordinate for a given x-coordinate (assuming the HalfEdge is infinitly long in both directions).
	 */
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

	
	/**
	 * This edge is used as a SearchEdge when localizing a edge left to a specific x-coordinate.
	 *
	 */
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
