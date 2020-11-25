package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InteractiveHoleTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<Vertex> vertices = new ArrayList<>();
		vertices.add(new Vertex(189, 85));
		vertices.add(new Vertex(200, 150));
		vertices.add(new Vertex(300, 120));
		vertices.add(new Vertex(400, 102));
		vertices.add(new Vertex(220, 110));

		Polygon p = new Polygon(
				new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
						new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150) });

		Runnable refresh = () -> {
			Vertex[] copied = new Vertex[vertices.size()];
			int i = 0;
			for (Vertex v : vertices) {
				copied[i] = new Vertex(v.x, v.y);
				i++;
			}
			Polygon hole = new Polygon(copied);
			try {
				List<Face> faces = PolygonTriangulation.triangulate(Collections.singletonList(p),
						Collections.singletonList(hole));
				painter.setFaces(faces.toArray(new Face[0]));
				painter.setPolygons(new Polygon[] { p, hole });
//				painter.setPolygons(new Polygon[] {p});
				painter.setHelpText("Success");
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(new Polygon[] { p, hole });
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
