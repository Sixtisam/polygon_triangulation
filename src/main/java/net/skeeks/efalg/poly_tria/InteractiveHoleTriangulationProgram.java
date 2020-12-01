package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveHoleTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<List<Vertex>> vholes = new ArrayList<>();
		{
			List<Vertex> vertices = new ArrayList<>();
			vertices.add(new Vertex(189, 85));
			vertices.add(new Vertex(200, 150));
			vertices.add(new Vertex(300, 120));
			vertices.add(new Vertex(400, 102));
			vertices.add(new Vertex(220, 110));
			vholes.add(vertices);
		}

		int[] currentIndex = new int[] { 0 };

		Runnable refresh = () -> {
			Polygon p = new Polygon(
					new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
							new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150) });
			List<Polygon> holes = vholes.stream().map(l -> new Polygon(l.toArray(new Vertex[0])))
					.collect(Collectors.toList());
			List<Polygon> polygons = new ArrayList<>();
			polygons.add(p);
			polygons.addAll(holes);
			try {
				List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(p), holes);

				painter.setPolygons(polygons.toArray(new Polygon[0]));
				painter.setTriangels(triangles);
				painter.setEdges(PolygonTriangulation.PROGRESS_EDGES.toArray(new Edge[0]));
				System.out.println(
						"----------------------------------------------------------------------------------------");
				printJavaTestcase(p, holes, triangles);
				painter.setHelpText("Success");
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(polygons.toArray(new Polygon[0]));
				painter.setEdges(PolygonTriangulation.PROGRESS_EDGES.toArray(new Edge[0]));
				painter.setTriangels(Collections.emptyList());
				painter.setFaces(new Face[0]);
				painter.setHelpText("Error occurred");
			}
		};

		refresh.run();

		painter.mouseLeftClickConsumer = (x, y) -> {
			vholes.get(currentIndex[0]).add(new Vertex(x, y));
			refresh.run();
		};

		painter.mouseShiftMoveConsumer = (x, y) -> {
			vholes.get(currentIndex[0]).get(vholes.get(currentIndex[0]).size() - 1).x = x;
			vholes.get(currentIndex[0]).get(vholes.get(currentIndex[0]).size() - 1).y = y;
			refresh.run();
		};

		painter.mouseRightClickConsumer = (x, y) -> {
			vholes.clear();
			currentIndex[0] = 0;
			vholes.add(new ArrayList<>());
			refresh.run();
		};

		painter.spaceConsumer = () -> {
			currentIndex[0]++;
			vholes.add(new ArrayList<>());
		};

		painter.start();
	}

	static void printJavaTestcase(Polygon polygon, List<Polygon> holes, List<Triangle> triangles) {
		System.out.println("void testX(){");
		System.out.print("Polygon polygon = ");
		printJavaCode(polygon);
		System.out.println("List<Polygon> holes = new ArrayList<>();");
		holes.forEach(hole -> {
			System.out.print("holes.add(");
			System.out.println("new Polygon(new Vertex[]{");
			System.out.println(Arrays.stream(polygon.vertices).map(v -> "new Vertex(" + v.x + ", " + v.y + ")")
					.collect(Collectors.joining(", ")));
			System.out.print("));");
		});
		printTriangles(triangles);
		System.out.println("test(polygon, hole, triangles);");
		System.out.println("}");
	}

	static void printTriangles(List<Triangle> triangles) {
		System.out.println("List<Triangle> triangles = Arrays.asList(new Triangle[]{");
		System.out.println(triangles.stream()
				.map(triangle -> "new Triangle(new Vertex(" + triangle.p1.x + ", " + triangle.p1.y + "), new Vertex("
						+ triangle.p2.x + ", " + triangle.p2.y + "), new Vertex(" + triangle.p3.x + ", " + triangle.p3.y
						+ "))")
				.collect(Collectors.joining(", ")));
		System.out.println("});");
	}

	static void printJavaCode(Polygon polygon) {
		System.out.println("new Polygon(new Vertex[]{");
		System.out.println(Arrays.stream(polygon.vertices).map(v -> "new Vertex(" + v.x + ", " + v.y + ")")
				.collect(Collectors.joining(", ")));
		System.out.println("});");
	}
}
