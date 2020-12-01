package net.skeeks.efalg.poly_tria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

public class PolygonWithoutHolesTest {

	/**
	 * Test helper
	 */
	void test(Polygon polygon, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(polygon),
				Collections.emptyList());
		assertEquals(expectedTriangles, triangles);
	}

	@Test
	public void test1() {
		Polygon polygon = new Polygon(new Vertex[] { new Vertex(112, 37), new Vertex(573, 35), new Vertex(504, 93),
				new Vertex(477, 189), new Vertex(494, 270), new Vertex(591, 303), new Vertex(560, 318), });

		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(494, 270), new Vertex(560, 318), new Vertex(591, 303)),
						new Triangle(new Vertex(112, 37), new Vertex(477, 189), new Vertex(504, 93)),
						new Triangle(new Vertex(112, 37), new Vertex(494, 270), new Vertex(477, 189)),
						new Triangle(new Vertex(112, 37), new Vertex(560, 318), new Vertex(494, 270)),
						new Triangle(new Vertex(573, 35), new Vertex(504, 93), new Vertex(112, 37)) });

		test(polygon, triangles);
	}

	@Test
	public void test2() {
		Polygon polygon = new Polygon(new Vertex[] { new Vertex(290, 21), new Vertex(474, 81), new Vertex(485, 250),
				new Vertex(404, 137), new Vertex(319, 133), new Vertex(272, 267), new Vertex(192, 275),
				new Vertex(224, 101), new Vertex(365, 78), new Vertex(414, 70), });
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(319, 133), new Vertex(192, 275), new Vertex(272, 267)),
						new Triangle(new Vertex(224, 101), new Vertex(192, 275), new Vertex(319, 133)),
						new Triangle(new Vertex(224, 101), new Vertex(404, 137), new Vertex(319, 133)),
						new Triangle(new Vertex(474, 81), new Vertex(404, 137), new Vertex(224, 101)),
						new Triangle(new Vertex(474, 81), new Vertex(485, 250), new Vertex(404, 137)),
						new Triangle(new Vertex(365, 78), new Vertex(224, 101), new Vertex(474, 81)),
						new Triangle(new Vertex(414, 70), new Vertex(474, 81), new Vertex(365, 78)),
						new Triangle(new Vertex(290, 21), new Vertex(474, 81), new Vertex(414, 70)) });
		test(polygon, triangles);
	}

	@Test
	public void test3() {
		Polygon polygon = new Polygon(new Vertex[] { new Vertex(213, 32), new Vertex(533, 38), new Vertex(465, 62),
				new Vertex(413, 108), new Vertex(397, 168), new Vertex(401, 229), new Vertex(328, 281),
				new Vertex(222, 214), new Vertex(277, 116), new Vertex(327, 65), });
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(222, 214), new Vertex(328, 281), new Vertex(401, 229)),
						new Triangle(new Vertex(397, 168), new Vertex(401, 229), new Vertex(222, 214)),
						new Triangle(new Vertex(277, 116), new Vertex(222, 214), new Vertex(397, 168)),
						new Triangle(new Vertex(413, 108), new Vertex(397, 168), new Vertex(277, 116)),
						new Triangle(new Vertex(327, 65), new Vertex(277, 116), new Vertex(413, 108)),
						new Triangle(new Vertex(465, 62), new Vertex(413, 108), new Vertex(327, 65)),
						new Triangle(new Vertex(533, 38), new Vertex(327, 65), new Vertex(465, 62)),
						new Triangle(new Vertex(213, 32), new Vertex(327, 65), new Vertex(533, 38)) });
		test(polygon, triangles);
	}
}
