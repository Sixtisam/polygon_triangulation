// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.programs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

public class InteractiveHoleTriangulationProgram {

	public static void main(String[] args) {

		GeometryPainter painter = new GeometryPainter();
		AtomicBoolean holemode = new AtomicBoolean(false);
		AtomicInteger currHoleIndex = new AtomicInteger(-1);
		AtomicInteger currPolyIndex = new AtomicInteger(0);

		List<List<Vertex>> vholes = new ArrayList<>();
		List<List<Vertex>> vpolygons = new ArrayList<>();
		vpolygons.add(new ArrayList<>());

		Runnable refresh = () -> {
			List<Polygon> holes = vholes.stream().filter(h -> h.size() > 2)
					.map(l -> new Polygon(l.toArray(new Vertex[0]))).collect(Collectors.toList());
			List<Polygon> polygons = vpolygons.stream().filter(h -> h.size() > 2)
					.map(l -> new Polygon(l.toArray(new Vertex[0]))).collect(Collectors.toList());

			try {

				List<Triangle> triangles = PolygonTriangulation.triangulate(polygons, holes);

				painter.setPolygons(polygons.toArray(new Polygon[0]), holes.toArray(new Polygon[0]));
				painter.setTriangles(triangles);
				System.out.println(
						"----------------------------------------------------------------------------------------");
				printJavaTestcase(polygons, holes, triangles);
			} catch (Throwable t) {
				t.printStackTrace();
				painter.setPolygons(polygons.toArray(new Polygon[0]), holes.toArray(new Polygon[0]));
				painter.setTriangles(Collections.emptyList());
				painter.setHelpText("Errror " + painter.getHelpText());
			}
		};

		refresh.run();

		painter.mouseLeftClickConsumer = (x, y) -> {
			if (holemode.get()) {
				vholes.get(currHoleIndex.get()).add(new Vertex(x, y));
			} else {
				vpolygons.get(currPolyIndex.get()).add(new Vertex(x, y));
			}
			refresh.run();
		};

		painter.mouseShiftMoveConsumer = (x, y) -> {
			if (holemode.get()) {
				vholes.get(currHoleIndex.get()).get(vholes.get(currHoleIndex.get()).size() - 1).x = x;
				vholes.get(currHoleIndex.get()).get(vholes.get(currHoleIndex.get()).size() - 1).y = y;
			} else {
				vpolygons.get(currPolyIndex.get()).get(vpolygons.get(currPolyIndex.get()).size() - 1).x = x;
				vpolygons.get(currPolyIndex.get()).get(vpolygons.get(currPolyIndex.get()).size() - 1).y = y;
			}
			refresh.run();
		};

		painter.mouseRightClickConsumer = (x, y) -> {
			vholes.clear();
			vpolygons.clear();
			currHoleIndex.set(-1);
			currPolyIndex.set(0);
			vpolygons.add(new ArrayList<>());
			holemode.set(false);
			painter.setHelpText("Polygon Mode");
			refresh.run();
		};

		painter.spaceConsumer = () -> {
			if (holemode.get()) {
				currHoleIndex.incrementAndGet();
				vholes.add(new ArrayList<>());
			} else {
				currPolyIndex.incrementAndGet();
				vpolygons.add(new ArrayList<>());
			}
		};

		painter.nConsumer = () -> {
			if (holemode.get()) {
				holemode.set(false);
				currPolyIndex.incrementAndGet();
				vpolygons.add(new ArrayList<>());
				painter.setHelpText("Polygon Mode");
			} else {
				holemode.set(true);
				currHoleIndex.incrementAndGet();
				vholes.add(new ArrayList<>());
				painter.setHelpText("Hole Mode");
			}
		};

		painter.start();
	}

	static void printJavaTestcase(List<Polygon> polygons, List<Polygon> holes, List<Triangle> triangles) {
		System.out.println("void testX(){");
		System.out.println("List<Polygon> polygons = new ArrayList<>();");
		printPolygonList("polygons", polygons);
		System.out.println("List<Polygon> holes = new ArrayList<>();");
		printPolygonList("holes", holes);
		printTriangles(triangles);

		System.out.println("test(polygons, holes, triangles);");
		System.out.println("}");
	}

	static void printPolygonList(String listVar, List<Polygon> polygons) {
		polygons.forEach(p -> {
			System.out.print(listVar + ".add(new Polygon(new Vertex[]{");
			System.out.print(Arrays.stream(p.vertices).map(v -> "new Vertex(" + v.x + "," + v.y + ")")
					.collect(Collectors.joining(",")));
			System.out.println("}));");
		});

	}

	public static void printTriangles(List<Triangle> triangles) {
		System.out.println("List<Triangle> triangles = Arrays.asList(new Triangle[]{");
		System.out.println(triangles.stream()
				.map(triangle -> "new Triangle(new Vertex(" + triangle.p1.x + ", " + triangle.p1.y + "), new Vertex("
						+ triangle.p2.x + ", " + triangle.p2.y + "), new Vertex(" + triangle.p3.x + ", " + triangle.p3.y
						+ "))")
				.collect(Collectors.joining(", ")));
		System.out.println("});");
	}
}
