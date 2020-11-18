package net.skeeks.efalg.poly_tria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class PolygonTriangulationProgram {

	static final BufferedReader inp;
	private static int inpPos = 0;
	private static String inpLine;
	static {
		try {
			inp = new BufferedReader(new FileReader("polygon.in"));
			inpLine = inp.readLine();
		} catch (IOException _e) {
			throw new RuntimeException(_e);
		}
	}

	static String next() throws IOException {
		int nextPos = inpLine.indexOf(' ', inpPos + 1);
		String token = inpLine.substring(inpPos, nextPos == -1 ? inpLine.length() : nextPos);
		if (nextPos == -1)
			inpLine = inp.readLine();
		inpPos = nextPos + 1;
		return token;
	}

	public static void main(String[] args) throws IOException {
		int polygonCount = Integer.parseInt(next());

		Polygon[] polygons = new Polygon[polygonCount];
		for (int i = 0; i < polygonCount; i++) {
			polygons[i] = readPolygon();
		}


		GeometryPainter painter = new GeometryPainter();

		Polygon[] withoutHoles = Arrays.stream(polygons).filter(p -> !PolygonTriangulation.isClockwise(p)).toArray(x -> new Polygon[x]);
		PolygonTriangulation.triangulate(withoutHoles);
		painter.setPolygons(withoutHoles);
		painter.setPoints(
				Arrays.stream(withoutHoles).flatMap(polygon -> Arrays.stream(polygon.points)).toArray(x -> new Vertex[x]));
		painter.start();
	}

	public static Polygon readPolygon() throws IOException {
		int pointCount = Integer.parseInt(next());

		Vertex[] points = new Vertex[pointCount];
		for (int i = 0; i < pointCount; i++) {
			String[] splitted = next().split(",");
			points[i] = new Vertex(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
		}
		return new Polygon(points);
	}
}