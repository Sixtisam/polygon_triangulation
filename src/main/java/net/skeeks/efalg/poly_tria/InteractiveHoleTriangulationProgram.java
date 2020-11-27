package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveHoleTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<Vertex> vertices = new ArrayList<>();
		vertices.add(new Vertex(189, 85));
		vertices.add(new Vertex(200, 150));
		vertices.add(new Vertex(300, 120));
		vertices.add(new Vertex(400, 102));
		vertices.add(new Vertex(220, 110));

		Runnable refresh = () -> {
			Polygon p = new Polygon(
					new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
							new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150) });
			Polygon hole = new Polygon(vertices.toArray(new Vertex[0]));
			List<Polygon> holes = vertices.size() > 0 ? Collections.singletonList(hole) : Collections.emptyList();
			try {
				List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(p), holes);
				painter.setPolygons(new Polygon[] { p, hole });
				painter.setTriangels(triangles);
				painter.setHelpText("Success");
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(new Polygon[] { p, hole });
				painter.setTriangels(Collections.emptyList());
				painter.setFaces(new Face[0]);
				painter.setHelpText("Error occurred");
			}
		};

		refresh.run();

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
}
