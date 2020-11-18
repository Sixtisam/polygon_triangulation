package net.skeeks.efalg.poly_tria;

public class Vertex {
	public int x;
	public int y;
	public VertexType type;

	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + "/" + y + "]";
	}
}
