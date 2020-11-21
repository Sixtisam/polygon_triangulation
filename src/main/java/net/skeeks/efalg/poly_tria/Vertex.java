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
	
	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the next vertex (counter-clockwise order)
	 */
	public Vertex next() {
		return edge.to();
	}
	
	public HalfEdge previousEdge() {
		return prev.edge;
	}



	@Override
	public String toString() {
		return "[" + x + "/" + y + "]";
	}
	
	
	/**
	 * Returns true, if {@code this} lies left to the edge formed by connecting {@code v1} and {@code v2}
	 */
	public boolean isLeft(Vertex v1, Vertex v2) {
		int cross = (v2.x - v1.x) * (this.y - v1.y) -
				  (this.x - v1.x) * (v2.y - v1.y);
		return cross > 0;
	}
	
	/**
	 * Sorts vertices descending y (or ascending x if same y)
	 * @return {@code < 0} if {@code this} vertex is higher than {@code o}
	 */
	@Override
	public int compareTo(Vertex o) {
		if(this == o) return 0;
		int result = Integer.compare(o.y, this.y);
		if (result == 0)
			result = Integer.compare(this.x, o.x);
		return result;
	}
}
