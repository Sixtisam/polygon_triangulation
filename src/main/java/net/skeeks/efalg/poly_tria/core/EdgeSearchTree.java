// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.TreeSet;

public class EdgeSearchTree {
	/**
	 * The edges sorted by x-coordinates.
	 */
	private TreeSet<HalfEdge> sortedEdges = new TreeSet<HalfEdge>(this::edgeComparator);

	/**
	 * Whenever a new edge is inserted
	 */
	public int currentY;

	public void insert(HalfEdge edge) {
		sortedEdges.add(edge);
	}

	public void remove(HalfEdge edge) {
		sortedEdges.remove(edge);
	}

	public static final HalfEdge.SearchEdge SEARCH_EDGE = new HalfEdge.SearchEdge();

	public HalfEdge findToLeft(Vertex vertex) {
		SEARCH_EDGE.x = vertex.x;

		return sortedEdges.floor(SEARCH_EDGE);
	}

	public int edgeComparator(HalfEdge a, HalfEdge b) {
		if (a == b)
			return 0;

		double aX = a.calcIntersectionX(currentY);
		double bX = b.calcIntersectionX(currentY);

		return aX < bX ? -1 : 1;
	}
}
