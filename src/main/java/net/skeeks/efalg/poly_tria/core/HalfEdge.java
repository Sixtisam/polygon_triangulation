// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

/**
 * Represents an directed edge connecting a vertex to another vertex. Each
 * HalfEdge has a 'twin', which is the same edge but in the opposite direction.
 * Each HalfEdge is exactly associated with 1 face. The twin of an edge is in
 * most cases not the same face (except a polygon with multiple holes)
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class HalfEdge {
	public final static double EPSILON = Math.pow(10, -3);
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
	 * The next edge (counter-clockwise). Same Face
	 */
	public HalfEdge next;

	/**
	 * The previous edge (clockwise) Same face.
	 */
	public HalfEdge prev;

	/**
	 * Is set to true once this edge has completed 2.phase
	 */
	public boolean triangulated = false;

	protected HalfEdge() {
		// for subclass 'search-edge'
	}

	/**
	 * Returns the vertex this edge is leading to
	 */
	public Vertex to() {
		return next.from;
	}

	protected double cachedXSlope = Double.NaN;
	
	double calcXSlope() {
		// X Steigung
		Vertex end = to();
		Vertex start = from;
		int my = (end.y - start.y);
		if (my == 0) {
			// not covered by code coverage because an HalfEdge will never join the EdgeSearchTree and therefore the slope will never be calculated
			return Double.POSITIVE_INFINITY; // horizontal edge
		}
		return (end.x - start.x) / (double) my;
	}

	/**
	 * Calculates the y-coordinate for a given x-coordinate (assuming the HalfEdge
	 * is infinitly long in both directions).
	 */
	public double calcIntersectionX(int y) {
		if(Double.isNaN(cachedXSlope)) {
			cachedXSlope = calcXSlope();
		}
		if (cachedXSlope == Double.POSITIVE_INFINITY) {
			// not covered by code coverage because an HalfEdge will never join the EdgeSearchTree and therefore the slope will never be calculated
			return from.x;
		} else {
			return from.x + (y - from.y) * cachedXSlope;
		}
	}

	@Override
	public String toString() {
		return from.toString() + " -> " + to().toString();
	}

	/**
	 * This edge is used as a SearchEdge when localizing a edge left to a specific
	 * x-coordinate.
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
