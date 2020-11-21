package net.skeeks.efalg.poly_tria;

import java.util.TreeSet;

public class EdgeSearchTree {
	/**
	 * The edges sorted by x-coordinates.
	 */
	private TreeSet<Edge> sortedEdges = new TreeSet<Edge>(this::edgeComparator);

	/**
	 * Whenever a new edge is inserted
	 */
	public int currentY;

	public void insert(Edge edge) {
		sortedEdges.add(edge);
	}

	public void remove(Edge edge) {
		sortedEdges.remove(edge);
	}

	public static final Edge.SearchEdge SEARCH_EDGE = new Edge.SearchEdge();

	public Edge findToLeft(Vertex vertex) {
		SEARCH_EDGE.x = vertex.x;

		return sortedEdges.floor(SEARCH_EDGE);
	}

	public int edgeComparator(Edge a, Edge b) {
		if (a == b)
			return 0;

		double aX = a.calcIntersectionX(currentY);
		double bX = b.calcIntersectionX(currentY);
//		System.out.println(a + " intersecty y=" + currentY +" at x=" + aX);
//		System.out.println(b + " intersecty y=" + currentY +" at x=" + bX);

		return aX < bX ? -1 : 1;
	}

	public static void main(String[] args) {
		EdgeSearchTree tree = new EdgeSearchTree();
		tree.currentY = 11;
		tree.insert(new Edge(new Vertex(7, 11), new Vertex(1, 2)));
		tree.currentY = 8;
		tree.insert(new Edge(new Vertex(7, 8), new Vertex(3, 2)));
		tree.currentY = 5;
		tree.insert(new Edge(new Vertex(9, 5), new Vertex(5, 1)));
		tree.currentY = 4;
		System.out.println(tree.findToLeft(new Vertex(10, 4)));
	}
}
