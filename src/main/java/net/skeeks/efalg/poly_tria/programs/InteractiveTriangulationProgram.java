// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.programs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

public class InteractiveTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<Vertex> vertices = new ArrayList<>();

		Runnable refresh = () -> {
			Polygon p = new Polygon(vertices.toArray(new Vertex[0]));
			try {
				List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(p),
						Collections.emptyList());
				painter.setTriangles(triangles);
				painter.setPolygons(new Polygon[0], new Polygon[0]);
				painter.setHelpText("Success");
				
//				printJavaCode(p);
//				printTriangles(triangles);
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(new Polygon[] { p }, new Polygon[0]);
				painter.setTriangles(Collections.emptyList());
				painter.setHelpText("Error occurred");
			}
		};

		painter.mouseLeftClickConsumer = (x, y) -> {
			vertices.add(new Vertex(x, y));
			refresh.run();
		};

		painter.mouseShiftMoveConsumer = (x, y) -> {
			vertices.get(vertices.size() - 1).x = x;
			vertices.get(vertices.size() - 1).y = y;
			refresh.run();
		};

		painter.mouseRightClickConsumer = (x, y) -> {
			vertices.clear();
			refresh.run();
		};

		painter.start();
	}
	
	static void printJavaCode(Polygon polygon) {
		System.out.println("Polygon = new Polygon(new Vertex[]{");
		for(Vertex v : polygon.vertices) {
			System.out.println("new Vertex(" + v.x + ", " + v.y + "), ");
		}
		System.out.println("});");
	}
	
	static void printTriangles(List<Triangle> triangles) {
		System.out.println("List<Triangle> triangles = Arrays.asList(new Triangle[]{");
		System.out.println(triangles.stream().map(triangle -> "new Triangle(new Vertex("+triangle.p1.x+", "+triangle.p1.y+"), new Vertex("+ triangle.p2.x + ", " + triangle.p2.y + "), new Vertex(" + triangle.p3.x + ", " + triangle.p3.y + "))").collect(Collectors.joining(", ")));
		System.out.println("});");
	}
}
