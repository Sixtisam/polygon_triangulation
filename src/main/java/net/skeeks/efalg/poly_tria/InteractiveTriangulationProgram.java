package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InteractiveTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();

		List<Vertex> vertices = new ArrayList<>();

		Runnable refresh = () -> {
			Polygon p = new Polygon(vertices.toArray(new Vertex[0]));
			try {
				List<Face> faces = PolygonTriangulation.triangulate(Collections.singletonList(p),
						Collections.emptyList(), new ArrayList<>());
				painter.setFaces(faces.toArray(new Face[0]));
				painter.setPolygons(new Polygon[0]);
				painter.setHelpText("Success");
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(new Polygon[] { p });
				painter.setFaces(new Face[0]);
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
}
