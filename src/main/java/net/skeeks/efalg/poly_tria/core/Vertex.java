// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

/**
 * Represents a point in 2D coordinate system. Each vertex nows if prev vertex
 * (counter-clockwise order), it HalfEdge pointing to the next vertex
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class Vertex implements Comparable<Vertex> {
	public int x;
	public int y;

	/**
	 * Event type of this vertex for the make monotone phase
	 */
	public VertexType type;

	/**
	 * Previous vertex according to original polygon
	 */
	public Vertex prev;

	/**
	 * Outgoing edge according to original polygon
	 */
	public HalfEdge edge;

	/**
	 * For the triangulation Algorithm the chain type
	 */
	public ChainType triangulationChainType;

	/**
	 * prev vertex, used only for the triangulation process. Cannot used 'prev'
	 * because if the vertices are reused (like in InteractiveTriangulationProgram)
	 * the triangulation and make-monotone will interfere.
	 * 
	 */
	public Vertex triangulationPrev;
	/**
	 * next edge, used only for the triangulation process. Cannot used 'prev'
	 * because if the vertices are reused (like in InteractiveTriangulationProgram)
	 * the triangulation and make-monotone will interfere
	 */
	public HalfEdge triangulationEdge;

	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the next vertex (counter-clockwise order)
	 */
	public Vertex triangulationNext() {
		return triangulationEdge.to();
	}

	/**
	 * Returns the previous edge of the original polygon.
	 * <b>Caution</b>: If this vertex is connected by multiple edges, it may return an edge which is associated with a different face!
	 */
	public HalfEdge previousEdge() {
		return prev.edge;
	}

	@Override
	public String toString() {
		return "[" + x + "/" + y + "] " + type;
	}

	/**
	 * Sorts vertices descending y (or ascending x if same y)
	 * 
	 * @return {@code < 0} if {@code this} vertex is higher than {@code o}
	 */
	@Override
	public int compareTo(Vertex o) {
		if (this == o)
			return 0;
		int result = Integer.compare(o.y, this.y);
		if (result == 0)
			result = Integer.compare(this.x, o.x);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
