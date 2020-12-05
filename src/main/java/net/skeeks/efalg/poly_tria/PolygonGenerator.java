package net.skeeks.efalg.poly_tria;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Vertex;

public class PolygonGenerator {
	public static final Random RANDOM = new Random();

	public final static int X_RIGHT_BOUND = 100;
	public final static int Y_MIDDLE = 400;
	
	public static Polygon generatePolygon(int verticesCount) {
		assert verticesCount % 2 == 0; // must be even
		Vertex[] vertices = new Vertex[verticesCount];
		int v = 0;
	
		int currX = 0;
		for (int i = 0; i < verticesCount / 2; i++) {
			vertices[v++] = new Vertex(currX + RANDOM.nextInt(X_RIGHT_BOUND), RANDOM.nextInt(Y_MIDDLE));
			currX += X_RIGHT_BOUND;
		}
		
		for (int i = 0; i < verticesCount / 2; i++) {
			vertices[v++] = new Vertex(currX - RANDOM.nextInt(X_RIGHT_BOUND), Y_MIDDLE + RANDOM.nextInt(Y_MIDDLE));
			currX -= X_RIGHT_BOUND;
			
		}
		
		return new Polygon(vertices);
	}
	
	public static void main(String[] args) throws IOException {
		Polygon polygon = generatePolygon(100_000);
		System.out.println("Ready for profiling");
		System.in.read();
		PolygonTriangulation.triangulate(Collections.singletonList(polygon), Collections.emptyList());
	}
}
