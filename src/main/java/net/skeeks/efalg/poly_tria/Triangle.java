package net.skeeks.efalg.poly_tria;

public class Triangle {
	public Vertex p1;
	public Vertex p2;
	public Vertex p3;
	
	public Triangle(Vertex p1, Vertex p2, Vertex p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	@Override
	public String toString() {
		return p1 + " <-> " + p2 + " <-> " + p3;
	}
}
