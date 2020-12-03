// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.programs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import net.skeeks.efalg.poly_tria.Edge;
import net.skeeks.efalg.poly_tria.GeometryPainter;
import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

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

		// read in all polygons
		ArrayList<Polygon> polygons = new ArrayList<>();
		ArrayList<Polygon> holes = new ArrayList<>();
		for (int i = 0; i < polygonCount; i++) {
			Polygon polygon = readPolygon();
			if (polygon.isClockwise()) {
				holes.add(polygon);
			} else {
				polygons.add(polygon);
			}
		}

		// Run the algorithm
		long start = System.nanoTime();
		List<Triangle> triangles = PolygonTriangulation.triangulate(polygons, holes);
		System.out.println("Triangulation took " + ((System.nanoTime() - start) / 1_000_000) + "ms");
		// Show visualization
		GeometryPainter painter = new GeometryPainter();
		painter.setPolygons(polygons.toArray(new Polygon[0]));
		painter.setTriangels(triangles);
		painter.setPoints(polygons.stream().filter(Objects::nonNull).flatMap(polygon -> Arrays.stream(polygon.vertices))
				.toArray(x -> new Vertex[x]));
		AtomicInteger currProgress = new AtomicInteger(0);

		painter.nextConsumer = () -> {
			painter.setEdges(PolygonTriangulation.PROGRESS_EDGES.subList(0, currProgress.incrementAndGet())
					.toArray(new Edge[0]));
		};

		painter.previousConsumer = () -> {
			painter.setEdges(PolygonTriangulation.PROGRESS_EDGES.subList(0, currProgress.decrementAndGet())
					.toArray(new Edge[0]));
		};

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
