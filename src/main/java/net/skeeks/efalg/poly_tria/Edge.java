package net.skeeks.efalg.poly_tria;

public class Edge {
	public Vertex from;
	public Vertex to;
	
	public Edge(Vertex from, Vertex to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString() {
		return from.toString() +  " -> " + to.toString();
	}
}
