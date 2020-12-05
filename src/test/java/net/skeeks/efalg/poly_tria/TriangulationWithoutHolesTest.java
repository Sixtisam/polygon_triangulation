// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

class TriangulationWithoutHolesTest {

	/**
	 * Test helper
	 */
	void test(Polygon polygon, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(polygon),
				Collections.emptyList());
		assertEquals(expectedTriangles, triangles);
	}

	void test(List<Polygon> polygon, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(polygon, Collections.emptyList());
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

	@Test
	void test4() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(292, 197), new Vertex(296, 23), new Vertex(490, 41),
				new Vertex(595, 173), new Vertex(457, 83), new Vertex(576, 294), new Vertex(422, 171),
				new Vertex(411, 315), new Vertex(361, 182), new Vertex(302, 307), new Vertex(37, 312),
				new Vertex(293, 287), new Vertex(34, 278), new Vertex(280, 247), new Vertex(49, 234),
				new Vertex(271, 205), new Vertex(50, 188), new Vertex(273, 155), new Vertex(59, 142),
				new Vertex(254, 112), new Vertex(50, 83), new Vertex(270, 55) }));
		polygons.add(new Polygon(new Vertex[] { new Vertex(464, 288), new Vertex(518, 286), new Vertex(541, 315),
				new Vertex(474, 324) }));
		polygons.add(new Polygon(new Vertex[] { new Vertex(43, 23), new Vertex(165, 35), new Vertex(100, 67),
				new Vertex(15, 61), new Vertex(83, 42) }));
		polygons.add(new Polygon(new Vertex[] { new Vertex(200, 13), new Vertex(262, 30), new Vertex(276, 11),
				new Vertex(283, 42), new Vertex(194, 50), new Vertex(234, 31) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(50, 188), new Vertex(271, 205), new Vertex(292, 197)),
						new Triangle(new Vertex(273, 155), new Vertex(292, 197), new Vertex(50, 188)),
						new Triangle(new Vertex(254, 112), new Vertex(273, 155), new Vertex(59, 142)),
						new Triangle(new Vertex(254, 112), new Vertex(292, 197), new Vertex(273, 155)),
						new Triangle(new Vertex(270, 55), new Vertex(254, 112), new Vertex(50, 83)),
						new Triangle(new Vertex(270, 55), new Vertex(292, 197), new Vertex(254, 112)),
						new Triangle(new Vertex(293, 287), new Vertex(37, 312), new Vertex(302, 307)),
						new Triangle(new Vertex(280, 247), new Vertex(293, 287), new Vertex(34, 278)),
						new Triangle(new Vertex(280, 247), new Vertex(302, 307), new Vertex(293, 287)),
						new Triangle(new Vertex(271, 205), new Vertex(280, 247), new Vertex(49, 234)),
						new Triangle(new Vertex(271, 205), new Vertex(302, 307), new Vertex(280, 247)),
						new Triangle(new Vertex(292, 197), new Vertex(302, 307), new Vertex(271, 205)),
						new Triangle(new Vertex(361, 182), new Vertex(302, 307), new Vertex(292, 197)),
						new Triangle(new Vertex(457, 83), new Vertex(361, 182), new Vertex(422, 171)),
						new Triangle(new Vertex(457, 83), new Vertex(292, 197), new Vertex(361, 182)),
						new Triangle(new Vertex(490, 41), new Vertex(292, 197), new Vertex(457, 83)),
						new Triangle(new Vertex(296, 23), new Vertex(292, 197), new Vertex(490, 41)),
						new Triangle(new Vertex(411, 315), new Vertex(361, 182), new Vertex(422, 171)),
						new Triangle(new Vertex(576, 294), new Vertex(422, 171), new Vertex(457, 83)),
						new Triangle(new Vertex(595, 173), new Vertex(457, 83), new Vertex(490, 41)),
						new Triangle(new Vertex(464, 288), new Vertex(474, 324), new Vertex(541, 315)),
						new Triangle(new Vertex(518, 286), new Vertex(541, 315), new Vertex(464, 288)),
						new Triangle(new Vertex(83, 42), new Vertex(100, 67), new Vertex(15, 61)),
						new Triangle(new Vertex(165, 35), new Vertex(100, 67), new Vertex(83, 42)),
						new Triangle(new Vertex(43, 23), new Vertex(83, 42), new Vertex(165, 35)),
						new Triangle(new Vertex(234, 31), new Vertex(262, 30), new Vertex(200, 13)),
						new Triangle(new Vertex(234, 31), new Vertex(194, 50), new Vertex(283, 42)),
						new Triangle(new Vertex(262, 30), new Vertex(283, 42), new Vertex(234, 31)),
						new Triangle(new Vertex(276, 11), new Vertex(283, 42), new Vertex(262, 30)) });
		test(polygons, triangles);
	}

	@Test
	void test5() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(273, 152), new Vertex(445, 22), new Vertex(547, 303),
				new Vertex(167, 303), new Vertex(75, 246) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(75, 246), new Vertex(167, 303), new Vertex(547, 303)),
						new Triangle(new Vertex(273, 152), new Vertex(547, 303), new Vertex(75, 246)),
						new Triangle(new Vertex(445, 22), new Vertex(547, 303), new Vertex(273, 152)) });
		test(polygons, triangles);
	}

	@Test
	void test6() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(327, 197), new Vertex(223, 71), new Vertex(454, 29),
				new Vertex(617, 193), new Vertex(523, 158), new Vertex(424, 113), new Vertex(483, 282),
				new Vertex(403, 186), new Vertex(383, 299), new Vertex(317, 241), new Vertex(253, 321),
				new Vertex(237, 213), new Vertex(138, 291), new Vertex(141, 190), new Vertex(48, 263),
				new Vertex(98, 152), new Vertex(243, 173), new Vertex(252, 200), new Vertex(282, 182),
				new Vertex(239, 140), new Vertex(160, 104), new Vertex(93, 112), new Vertex(88, 56),
				new Vertex(117, 31), new Vertex(205, 76), new Vertex(214, 107) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(253, 321), new Vertex(317, 241), new Vertex(237, 213)),
						new Triangle(new Vertex(98, 152), new Vertex(141, 190), new Vertex(243, 173)),
						new Triangle(new Vertex(98, 152), new Vertex(48, 263), new Vertex(141, 190)),
						new Triangle(new Vertex(205, 76), new Vertex(93, 112), new Vertex(160, 104)),
						new Triangle(new Vertex(88, 56), new Vertex(93, 112), new Vertex(205, 76)),
						new Triangle(new Vertex(117, 31), new Vertex(205, 76), new Vertex(88, 56)),
						new Triangle(new Vertex(252, 200), new Vertex(317, 241), new Vertex(237, 213)),
						new Triangle(new Vertex(327, 197), new Vertex(317, 241), new Vertex(252, 200)),
						new Triangle(new Vertex(327, 197), new Vertex(383, 299), new Vertex(317, 241)),
						new Triangle(new Vertex(403, 186), new Vertex(383, 299), new Vertex(327, 197)),
						new Triangle(new Vertex(424, 113), new Vertex(327, 197), new Vertex(403, 186)),
						new Triangle(new Vertex(223, 71), new Vertex(327, 197), new Vertex(424, 113)),
						new Triangle(new Vertex(252, 200), new Vertex(138, 291), new Vertex(237, 213)),
						new Triangle(new Vertex(141, 190), new Vertex(138, 291), new Vertex(252, 200)),
						new Triangle(new Vertex(243, 173), new Vertex(252, 200), new Vertex(141, 190)),
						new Triangle(new Vertex(483, 282), new Vertex(403, 186), new Vertex(424, 113)),
						new Triangle(new Vertex(282, 182), new Vertex(252, 200), new Vertex(327, 197)),
						new Triangle(new Vertex(239, 140), new Vertex(327, 197), new Vertex(282, 182)),
						new Triangle(new Vertex(214, 107), new Vertex(327, 197), new Vertex(239, 140)),
						new Triangle(new Vertex(160, 104), new Vertex(239, 140), new Vertex(214, 107)),
						new Triangle(new Vertex(205, 76), new Vertex(214, 107), new Vertex(160, 104)),
						new Triangle(new Vertex(424, 113), new Vertex(617, 193), new Vertex(523, 158)),
						new Triangle(new Vertex(454, 29), new Vertex(424, 113), new Vertex(223, 71)),
						new Triangle(new Vertex(454, 29), new Vertex(617, 193), new Vertex(424, 113)) });
		test(polygons, triangles);
	}

	@Test
	void testX() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(249, 97), new Vertex(434, 82), new Vertex(590, 348),
				new Vertex(681, 72), new Vertex(918, 548), new Vertex(1070, 103), new Vertex(1274, 698),
				new Vertex(1519, 104), new Vertex(1554, 936), new Vertex(50, 82) }));
		List<Triangle> triangles = Arrays.asList(
				new Triangle[] { new Triangle(new Vertex(918, 548), new Vertex(1554, 936), new Vertex(1274, 698)),
						new Triangle(new Vertex(590, 348), new Vertex(1554, 936), new Vertex(918, 548)),
						new Triangle(new Vertex(50, 82), new Vertex(590, 348), new Vertex(249, 97)),
						new Triangle(new Vertex(50, 82), new Vertex(1554, 936), new Vertex(590, 348)),
						new Triangle(new Vertex(1554, 936), new Vertex(1274, 698), new Vertex(1519, 104)),
						new Triangle(new Vertex(1274, 698), new Vertex(918, 548), new Vertex(1070, 103)),
						new Triangle(new Vertex(918, 548), new Vertex(590, 348), new Vertex(681, 72)),
						new Triangle(new Vertex(590, 348), new Vertex(249, 97), new Vertex(434, 82)) });
		test(polygons, triangles);
	}
}
