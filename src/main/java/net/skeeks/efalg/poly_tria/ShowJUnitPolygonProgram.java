// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.List;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;
import net.skeeks.efalg.poly_tria.programs.GeometryPainter;
import net.skeeks.efalg.poly_tria.programs.InteractiveHoleTriangulationProgram;

/**
 * Used to re-generate triangles result of a testcase. Can also be used to show
 * the polygon of a JUnitTest
 * 
 * @author sixkn_000
 *
 */
public class ShowJUnitPolygonProgram {

	public static void main(String[] args) {

		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(
				new Vertex[] { new Vertex(67, 133), new Vertex(225, 57), new Vertex(528, 94), new Vertex(557, 254),
						new Vertex(377, 314), new Vertex(436, 186), new Vertex(300, 192), new Vertex(257, 299),
						new Vertex(138, 318), new Vertex(175, 215), new Vertex(65, 233), new Vertex(34, 308) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(new Vertex[] { new Vertex(204, 87), new Vertex(140, 134), new Vertex(230, 169),
				new Vertex(317, 114) }));
		holes.add(new Polygon(new Vertex[] { new Vertex(332, 99), new Vertex(345, 118), new Vertex(301, 160),
				new Vertex(380, 173), new Vertex(475, 143), new Vertex(489, 105) }));

		GeometryPainter painter = new GeometryPainter();
		painter.setPolygons(polygons.toArray(new Polygon[0]), holes.toArray(new Polygon[0]));
		List<Triangle> triangles = PolygonTriangulation.triangulate(polygons, holes);
		painter.setTriangles(triangles);
		painter.start();
		InteractiveHoleTriangulationProgram.printTriangles(triangles);
	}
}
