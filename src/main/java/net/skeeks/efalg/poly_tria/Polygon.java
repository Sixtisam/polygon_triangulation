package net.skeeks.efalg.poly_tria;

import java.util.Arrays;

public class Polygon {
	public Vertex[] points;

	public Polygon(Vertex[] points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return String.join(" -> ", Arrays.stream(points).map(Vertex::toString).toArray(l -> new String[l]));
	}

	public int length() {
		return points.length;
	}
}
