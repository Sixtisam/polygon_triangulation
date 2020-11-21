package net.skeeks.efalg.poly_tria;

public class Vertex {
	public int x;
	public int y;
	public VertexType type;

	public Vertex prevVertex;
	public Vertex nextVertex;
	
	public Edge edge;
	
	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the previous vertex of the polygon
	 * @return
	 */
	public Vertex previous() {
		return edge.twin.next.from;
	}
	
	/**
	 * Returns the next vertex (counter-clockwise order)
	 */
	public Vertex next() {
		return edge.to();
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
}
