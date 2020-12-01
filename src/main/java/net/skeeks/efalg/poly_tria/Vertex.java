package net.skeeks.efalg.poly_tria;

public class Vertex implements Comparable<Vertex> {
	public int x;
	public int y;
	/**
	 * Event type of this vertex
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
	public ChainType chainType;

	/**
	 * prev vertex, used only for the triangulation process. Cannot used 'prev'
	 * because if the vertices are reused (like in InteractiveTriangulationProgram)
	 * the triangulation and make-monotone will interfere
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

	public HalfEdge previousEdge() {
		return prev.edge;
	}

	@Override
	public String toString() {
		return "[" + x + "/" + y + "] " + type;
	}

	/**
	 * Returns true, if {@code this} lies left to the edge formed by connecting
	 * {@code v1} and {@code v2}
	 */
	public boolean isLeft(Vertex v1, Vertex v2) {
		int cross = (v2.x - v1.x) * (this.y - v1.y) - (this.x - v1.x) * (v2.y - v1.y);
		return cross > 0;
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
