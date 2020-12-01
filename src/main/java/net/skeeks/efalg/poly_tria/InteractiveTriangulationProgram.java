package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<Vertex> vertices = new ArrayList<>();

		Runnable refresh = () -> {
			Polygon p = new Polygon(vertices.toArray(new Vertex[0]));
			try {
				List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(p),
						Collections.emptyList());
				painter.setTriangels(triangles);
				painter.setPolygons(new Polygon[0]);
				painter.setHelpText("Success");
				
				System.out.print("Polygon polygon = ");
				printJavaCode(p);
				printTriangles(triangles);
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(new Polygon[] { p });
				painter.setTriangels(Collections.emptyList());
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
		System.out.println("new Polygon(new Vertex[]{");
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
