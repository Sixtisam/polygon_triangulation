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

class TriangulationWithHolesTest {

	void test(Polygon polygon, Polygon hole, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(polygon),
				Collections.singletonList(hole));
		assertEquals(expectedTriangles, triangles);
	}

	void test(List<Polygon> polygons, List<Polygon> holes, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(polygons, holes);
		assertEquals(expectedTriangles, triangles);
	}

	@Test
	public void test1() {
		Polygon hole = new Polygon(new Vertex[] { new Vertex(189, 85), new Vertex(200, 150), new Vertex(300, 120),
				new Vertex(400, 102), new Vertex(220, 110), });
		Polygon polygon = new Polygon(
				new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
						new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150), });
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(189, 85), new Vertex(10, 150), new Vertex(200, 150)),
						new Triangle(new Vertex(50, 50), new Vertex(10, 150), new Vertex(189, 85)),
						new Triangle(new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300)),
						new Triangle(new Vertex(10, 150), new Vertex(50, 300), new Vertex(600, 300)),
						new Triangle(new Vertex(200, 150), new Vertex(600, 300), new Vertex(10, 150)),
						new Triangle(new Vertex(700, 150), new Vertex(600, 300), new Vertex(200, 150)),
						new Triangle(new Vertex(300, 120), new Vertex(200, 150), new Vertex(700, 150)),
						new Triangle(new Vertex(400, 102), new Vertex(700, 150), new Vertex(300, 120)),
						new Triangle(new Vertex(50, 50), new Vertex(400, 102), new Vertex(189, 85)),
						new Triangle(new Vertex(600, 50), new Vertex(400, 102), new Vertex(50, 50)),
						new Triangle(new Vertex(600, 50), new Vertex(700, 150), new Vertex(400, 102)),
						new Triangle(new Vertex(300, 10), new Vertex(50, 50), new Vertex(600, 50)),
						new Triangle(new Vertex(220, 110), new Vertex(400, 102), new Vertex(189, 85)) });

		test(polygon, hole, triangles);

	}

	@Test
	public void test2() {
		Polygon hole = new Polygon(
				new Vertex[] { new Vertex(172, 135), new Vertex(213, 242), new Vertex(283, 273), new Vertex(397, 264),
						new Vertex(471, 219), new Vertex(483, 123), new Vertex(452, 83), new Vertex(430, 116),
						new Vertex(434, 165), new Vertex(426, 206), new Vertex(388, 235), new Vertex(329, 245),
						new Vertex(277, 240), new Vertex(280, 210), new Vertex(364, 204), new Vertex(391, 159),
						new Vertex(388, 110), new Vertex(365, 81), new Vertex(343, 115), new Vertex(360, 148),
						new Vertex(352, 181), new Vertex(307, 194), new Vertex(295, 158), new Vertex(317, 138),
						new Vertex(321, 105), new Vertex(291, 91), new Vertex(275, 125), new Vertex(277, 173),
						new Vertex(270, 208), new Vertex(246, 215), new Vertex(229, 196), new Vertex(237, 146),
						new Vertex(247, 111), new Vertex(243, 77), new Vertex(200, 73), new Vertex(178, 100), });
		Polygon polygon = new Polygon(
				new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
						new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150), });
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300)),
						new Triangle(new Vertex(283, 273), new Vertex(50, 300), new Vertex(600, 300)),
						new Triangle(new Vertex(213, 242), new Vertex(50, 300), new Vertex(283, 273)),
						new Triangle(new Vertex(10, 150), new Vertex(50, 300), new Vertex(213, 242)),
						new Triangle(new Vertex(172, 135), new Vertex(213, 242), new Vertex(10, 150)),
						new Triangle(new Vertex(178, 100), new Vertex(10, 150), new Vertex(172, 135)),
						new Triangle(new Vertex(200, 73), new Vertex(10, 150), new Vertex(178, 100)),
						new Triangle(new Vertex(50, 50), new Vertex(10, 150), new Vertex(200, 73)),
						new Triangle(new Vertex(397, 264), new Vertex(600, 300), new Vertex(283, 273)),
						new Triangle(new Vertex(471, 219), new Vertex(600, 300), new Vertex(397, 264)),
						new Triangle(new Vertex(700, 150), new Vertex(600, 300), new Vertex(471, 219)),
						new Triangle(new Vertex(483, 123), new Vertex(471, 219), new Vertex(700, 150)),
						new Triangle(new Vertex(452, 83), new Vertex(700, 150), new Vertex(483, 123)),
						new Triangle(new Vertex(243, 77), new Vertex(452, 83), new Vertex(365, 81)),
						new Triangle(new Vertex(200, 73), new Vertex(452, 83), new Vertex(243, 77)),
						new Triangle(new Vertex(50, 50), new Vertex(452, 83), new Vertex(200, 73)),
						new Triangle(new Vertex(600, 50), new Vertex(452, 83), new Vertex(50, 50)),
						new Triangle(new Vertex(600, 50), new Vertex(700, 150), new Vertex(452, 83)),
						new Triangle(new Vertex(300, 10), new Vertex(50, 50), new Vertex(600, 50)),
						new Triangle(new Vertex(388, 235), new Vertex(329, 245), new Vertex(277, 240)),
						new Triangle(new Vertex(280, 210), new Vertex(277, 240), new Vertex(388, 235)),
						new Triangle(new Vertex(426, 206), new Vertex(388, 235), new Vertex(280, 210)),
						new Triangle(new Vertex(364, 204), new Vertex(280, 210), new Vertex(426, 206)),
						new Triangle(new Vertex(434, 165), new Vertex(426, 206), new Vertex(364, 204)),
						new Triangle(new Vertex(391, 159), new Vertex(364, 204), new Vertex(434, 165)),
						new Triangle(new Vertex(430, 116), new Vertex(434, 165), new Vertex(391, 159)),
						new Triangle(new Vertex(388, 110), new Vertex(391, 159), new Vertex(430, 116)),
						new Triangle(new Vertex(452, 83), new Vertex(430, 116), new Vertex(388, 110)),
						new Triangle(new Vertex(365, 81), new Vertex(388, 110), new Vertex(452, 83)),
						new Triangle(new Vertex(229, 196), new Vertex(246, 215), new Vertex(270, 208)),
						new Triangle(new Vertex(277, 173), new Vertex(270, 208), new Vertex(229, 196)),
						new Triangle(new Vertex(237, 146), new Vertex(229, 196), new Vertex(277, 173)),
						new Triangle(new Vertex(275, 125), new Vertex(277, 173), new Vertex(237, 146)),
						new Triangle(new Vertex(247, 111), new Vertex(237, 146), new Vertex(275, 125)),
						new Triangle(new Vertex(291, 91), new Vertex(275, 125), new Vertex(247, 111)),
						new Triangle(new Vertex(243, 77), new Vertex(291, 91), new Vertex(365, 81)),
						new Triangle(new Vertex(243, 77), new Vertex(247, 111), new Vertex(291, 91)),
						new Triangle(new Vertex(295, 158), new Vertex(307, 194), new Vertex(352, 181)),
						new Triangle(new Vertex(360, 148), new Vertex(352, 181), new Vertex(295, 158)),
						new Triangle(new Vertex(317, 138), new Vertex(295, 158), new Vertex(360, 148)),
						new Triangle(new Vertex(343, 115), new Vertex(360, 148), new Vertex(317, 138)),
						new Triangle(new Vertex(321, 105), new Vertex(317, 138), new Vertex(343, 115)),
						new Triangle(new Vertex(291, 91), new Vertex(343, 115), new Vertex(321, 105)),
						new Triangle(new Vertex(365, 81), new Vertex(343, 115), new Vertex(291, 91)) });

		test(polygon, hole, triangles);
	}

	@Test
	void testSingleHole() {
		Polygon polygon = new Polygon(
				new Vertex[] { new Vertex(50, 50), new Vertex(300, 10), new Vertex(600, 50), new Vertex(700, 150),
						new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300), new Vertex(10, 150), });
		Polygon hole = new Polygon(new Vertex[] { new Vertex(49, 169), new Vertex(75, 270), new Vertex(198, 280),
				new Vertex(234, 222), new Vertex(228, 148), new Vertex(134, 115), new Vertex(75, 120), });

		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300)),
						new Triangle(new Vertex(198, 280), new Vertex(50, 300), new Vertex(600, 300)),
						new Triangle(new Vertex(75, 270), new Vertex(50, 300), new Vertex(198, 280)),
						new Triangle(new Vertex(49, 169), new Vertex(50, 300), new Vertex(75, 270)),
						new Triangle(new Vertex(10, 150), new Vertex(50, 300), new Vertex(49, 169)),
						new Triangle(new Vertex(75, 120), new Vertex(49, 169), new Vertex(10, 150)),
						new Triangle(new Vertex(50, 50), new Vertex(75, 120), new Vertex(134, 115)),
						new Triangle(new Vertex(50, 50), new Vertex(10, 150), new Vertex(75, 120)),
						new Triangle(new Vertex(234, 222), new Vertex(600, 300), new Vertex(198, 280)),
						new Triangle(new Vertex(700, 150), new Vertex(600, 300), new Vertex(234, 222)),
						new Triangle(new Vertex(228, 148), new Vertex(234, 222), new Vertex(700, 150)),
						new Triangle(new Vertex(134, 115), new Vertex(700, 150), new Vertex(228, 148)),
						new Triangle(new Vertex(50, 50), new Vertex(700, 150), new Vertex(134, 115)),
						new Triangle(new Vertex(600, 50), new Vertex(700, 150), new Vertex(50, 50)),
						new Triangle(new Vertex(300, 10), new Vertex(50, 50), new Vertex(600, 50)) });

		test(polygon, hole, triangles);
	}

	@Test
	void test2Holes() {
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
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(300, 192), new Vertex(65, 233), new Vertex(175, 215)),
						new Triangle(new Vertex(380, 173), new Vertex(300, 192), new Vertex(436, 186)),
						new Triangle(new Vertex(380, 173), new Vertex(65, 233), new Vertex(300, 192)),
						new Triangle(new Vertex(230, 169), new Vertex(65, 233), new Vertex(380, 173)),
						new Triangle(new Vertex(140, 134), new Vertex(65, 233), new Vertex(230, 169)),
						new Triangle(new Vertex(67, 133), new Vertex(65, 233), new Vertex(140, 134)),
						new Triangle(new Vertex(67, 133), new Vertex(34, 308), new Vertex(65, 233)),
						new Triangle(new Vertex(204, 87), new Vertex(140, 134), new Vertex(67, 133)),
						new Triangle(new Vertex(225, 57), new Vertex(67, 133), new Vertex(204, 87)),
						new Triangle(new Vertex(175, 215), new Vertex(138, 318), new Vertex(257, 299)),
						new Triangle(new Vertex(300, 192), new Vertex(257, 299), new Vertex(175, 215)),
						new Triangle(new Vertex(436, 186), new Vertex(377, 314), new Vertex(557, 254)),
						new Triangle(new Vertex(475, 143), new Vertex(436, 186), new Vertex(380, 173)),
						new Triangle(new Vertex(475, 143), new Vertex(557, 254), new Vertex(436, 186)),
						new Triangle(new Vertex(489, 105), new Vertex(557, 254), new Vertex(475, 143)),
						new Triangle(new Vertex(528, 94), new Vertex(489, 105), new Vertex(332, 99)),
						new Triangle(new Vertex(528, 94), new Vertex(557, 254), new Vertex(489, 105)),
						new Triangle(new Vertex(301, 160), new Vertex(380, 173), new Vertex(230, 169)),
						new Triangle(new Vertex(345, 118), new Vertex(230, 169), new Vertex(301, 160)),
						new Triangle(new Vertex(317, 114), new Vertex(230, 169), new Vertex(345, 118)),
						new Triangle(new Vertex(332, 99), new Vertex(345, 118), new Vertex(317, 114)),
						new Triangle(new Vertex(204, 87), new Vertex(332, 99), new Vertex(528, 94)),
						new Triangle(new Vertex(204, 87), new Vertex(317, 114), new Vertex(332, 99)),
						new Triangle(new Vertex(225, 57), new Vertex(528, 94), new Vertex(204, 87)) });

		test(polygons, holes, triangles);
	}

	@Test
	void test3() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(288, 193), new Vertex(361, 37), new Vertex(532, 50),
				new Vertex(562, 181), new Vertex(486, 115), new Vertex(505, 305), new Vertex(396, 196),
				new Vertex(460, 306), new Vertex(296, 306), new Vertex(354, 221), new Vertex(171, 214),
				new Vertex(47, 132), new Vertex(57, 46), new Vertex(126, 18), new Vertex(136, 83), new Vertex(177, 20),
				new Vertex(228, 82), new Vertex(263, 27), new Vertex(355, 37) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(new Vertex[] { new Vertex(171, 54), new Vertex(142, 95), new Vertex(87, 92),
				new Vertex(128, 156), new Vertex(264, 174) }));
		holes.add(new Polygon(
				new Vertex[] { new Vertex(274, 39), new Vertex(265, 54), new Vertex(297, 78), new Vertex(318, 46) }));
		holes.add(new Polygon(
				new Vertex[] { new Vertex(377, 49), new Vertex(358, 110), new Vertex(435, 118), new Vertex(451, 66) }));
		holes.add(new Polygon(new Vertex[] { new Vertex(385, 274), new Vertex(360, 252), new Vertex(340, 284),
				new Vertex(411, 290), new Vertex(413, 252) }));
		holes.add(new Polygon(new Vertex[] { new Vertex(470, 176), new Vertex(457, 142), new Vertex(390, 174),
				new Vertex(447, 193), new Vertex(477, 247), new Vertex(491, 204) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(411, 290), new Vertex(296, 306), new Vertex(460, 306)),
						new Triangle(new Vertex(340, 284), new Vertex(296, 306), new Vertex(411, 290)),
						new Triangle(new Vertex(360, 252), new Vertex(296, 306), new Vertex(340, 284)),
						new Triangle(new Vertex(354, 221), new Vertex(360, 252), new Vertex(413, 252)),
						new Triangle(new Vertex(354, 221), new Vertex(296, 306), new Vertex(360, 252)),
						new Triangle(new Vertex(413, 252), new Vertex(460, 306), new Vertex(411, 290)),
						new Triangle(new Vertex(396, 196), new Vertex(354, 221), new Vertex(171, 214)),
						new Triangle(new Vertex(396, 196), new Vertex(413, 252), new Vertex(354, 221)),
						new Triangle(new Vertex(396, 196), new Vertex(460, 306), new Vertex(413, 252)),
						new Triangle(new Vertex(288, 193), new Vertex(171, 214), new Vertex(396, 196)),
						new Triangle(new Vertex(264, 174), new Vertex(171, 214), new Vertex(288, 193)),
						new Triangle(new Vertex(128, 156), new Vertex(171, 214), new Vertex(264, 174)),
						new Triangle(new Vertex(47, 132), new Vertex(171, 214), new Vertex(128, 156)),
						new Triangle(new Vertex(87, 92), new Vertex(128, 156), new Vertex(47, 132)),
						new Triangle(new Vertex(57, 46), new Vertex(87, 92), new Vertex(136, 83)),
						new Triangle(new Vertex(57, 46), new Vertex(47, 132), new Vertex(87, 92)),
						new Triangle(new Vertex(126, 18), new Vertex(136, 83), new Vertex(57, 46)),
						new Triangle(new Vertex(385, 274), new Vertex(360, 252), new Vertex(413, 252)),
						new Triangle(new Vertex(396, 196), new Vertex(505, 305), new Vertex(477, 247)),
						new Triangle(new Vertex(447, 193), new Vertex(396, 196), new Vertex(288, 193)),
						new Triangle(new Vertex(447, 193), new Vertex(477, 247), new Vertex(396, 196)),
						new Triangle(new Vertex(390, 174), new Vertex(288, 193), new Vertex(447, 193)),
						new Triangle(new Vertex(457, 142), new Vertex(288, 193), new Vertex(390, 174)),
						new Triangle(new Vertex(435, 118), new Vertex(288, 193), new Vertex(457, 142)),
						new Triangle(new Vertex(358, 110), new Vertex(288, 193), new Vertex(435, 118)),
						new Triangle(new Vertex(377, 49), new Vertex(288, 193), new Vertex(358, 110)),
						new Triangle(new Vertex(361, 37), new Vertex(288, 193), new Vertex(377, 49)),
						new Triangle(new Vertex(491, 204), new Vertex(505, 305), new Vertex(477, 247)),
						new Triangle(new Vertex(457, 142), new Vertex(491, 204), new Vertex(470, 176)),
						new Triangle(new Vertex(486, 115), new Vertex(457, 142), new Vertex(435, 118)),
						new Triangle(new Vertex(486, 115), new Vertex(491, 204), new Vertex(457, 142)),
						new Triangle(new Vertex(486, 115), new Vertex(505, 305), new Vertex(491, 204)),
						new Triangle(new Vertex(451, 66), new Vertex(435, 118), new Vertex(486, 115)),
						new Triangle(new Vertex(228, 82), new Vertex(288, 193), new Vertex(264, 174)),
						new Triangle(new Vertex(297, 78), new Vertex(288, 193), new Vertex(228, 82)),
						new Triangle(new Vertex(318, 46), new Vertex(288, 193), new Vertex(297, 78)),
						new Triangle(new Vertex(355, 37), new Vertex(318, 46), new Vertex(274, 39)),
						new Triangle(new Vertex(355, 37), new Vertex(288, 193), new Vertex(318, 46)),
						new Triangle(new Vertex(136, 83), new Vertex(142, 95), new Vertex(87, 92)),
						new Triangle(new Vertex(171, 54), new Vertex(142, 95), new Vertex(136, 83)),
						new Triangle(new Vertex(177, 20), new Vertex(136, 83), new Vertex(171, 54)),
						new Triangle(new Vertex(171, 54), new Vertex(264, 174), new Vertex(228, 82)),
						new Triangle(new Vertex(177, 20), new Vertex(228, 82), new Vertex(171, 54)),
						new Triangle(new Vertex(265, 54), new Vertex(228, 82), new Vertex(297, 78)),
						new Triangle(new Vertex(274, 39), new Vertex(228, 82), new Vertex(265, 54)),
						new Triangle(new Vertex(263, 27), new Vertex(274, 39), new Vertex(355, 37)),
						new Triangle(new Vertex(263, 27), new Vertex(228, 82), new Vertex(274, 39)),
						new Triangle(new Vertex(451, 66), new Vertex(562, 181), new Vertex(486, 115)),
						new Triangle(new Vertex(532, 50), new Vertex(562, 181), new Vertex(451, 66)),
						new Triangle(new Vertex(377, 49), new Vertex(451, 66), new Vertex(532, 50)),
						new Triangle(new Vertex(361, 37), new Vertex(532, 50), new Vertex(377, 49)) });

		test(polygons, holes, triangles);
	}

	@Test
	void test4() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(267, 63), new Vertex(496, 18), new Vertex(582, 162),
				new Vertex(382, 194), new Vertex(533, 288), new Vertex(354, 314), new Vertex(321, 191),
				new Vertex(228, 295), new Vertex(301, 109) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(new Vertex[] { new Vertex(380, 80), new Vertex(333, 131), new Vertex(414, 164),
				new Vertex(512, 110), new Vertex(377, 146) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(333, 131), new Vertex(321, 191), new Vertex(414, 164)),
						new Triangle(new Vertex(333, 131), new Vertex(228, 295), new Vertex(321, 191)),
						new Triangle(new Vertex(301, 109), new Vertex(228, 295), new Vertex(333, 131)),
						new Triangle(new Vertex(380, 80), new Vertex(333, 131), new Vertex(301, 109)),
						new Triangle(new Vertex(267, 63), new Vertex(301, 109), new Vertex(380, 80)),
						new Triangle(new Vertex(382, 194), new Vertex(354, 314), new Vertex(533, 288)),
						new Triangle(new Vertex(321, 191), new Vertex(354, 314), new Vertex(382, 194)),
						new Triangle(new Vertex(414, 164), new Vertex(382, 194), new Vertex(321, 191)),
						new Triangle(new Vertex(582, 162), new Vertex(382, 194), new Vertex(414, 164)),
						new Triangle(new Vertex(512, 110), new Vertex(414, 164), new Vertex(582, 162)),
						new Triangle(new Vertex(496, 18), new Vertex(380, 80), new Vertex(267, 63)),
						new Triangle(new Vertex(496, 18), new Vertex(512, 110), new Vertex(380, 80)),
						new Triangle(new Vertex(496, 18), new Vertex(582, 162), new Vertex(512, 110)),
						new Triangle(new Vertex(377, 146), new Vertex(512, 110), new Vertex(380, 80)) });

		test(polygons, holes, triangles);
	}

	@Test
	void test5() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(227, 132), new Vertex(321, 29), new Vertex(522, 85),
				new Vertex(517, 223), new Vertex(474, 133), new Vertex(409, 282), new Vertex(422, 149),
				new Vertex(268, 275), new Vertex(301, 188), new Vertex(152, 188) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(
				new Vertex[] { new Vertex(280, 104), new Vertex(233, 156), new Vertex(293, 180), new Vertex(514, 112),
						new Vertex(492, 90), new Vertex(427, 110), new Vertex(425, 74), new Vertex(386, 106),
						new Vertex(379, 64), new Vertex(335, 103), new Vertex(329, 62), new Vertex(314, 129) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(293, 180), new Vertex(152, 188), new Vertex(301, 188)),
						new Triangle(new Vertex(233, 156), new Vertex(152, 188), new Vertex(293, 180)),
						new Triangle(new Vertex(227, 132), new Vertex(152, 188), new Vertex(233, 156)),
						new Triangle(new Vertex(280, 104), new Vertex(233, 156), new Vertex(227, 132)),
						new Triangle(new Vertex(329, 62), new Vertex(227, 132), new Vertex(280, 104)),
						new Triangle(new Vertex(321, 29), new Vertex(227, 132), new Vertex(329, 62)),
						new Triangle(new Vertex(422, 149), new Vertex(301, 188), new Vertex(293, 180)),
						new Triangle(new Vertex(422, 149), new Vertex(268, 275), new Vertex(301, 188)),
						new Triangle(new Vertex(474, 133), new Vertex(293, 180), new Vertex(422, 149)),
						new Triangle(new Vertex(514, 112), new Vertex(293, 180), new Vertex(474, 133)),
						new Triangle(new Vertex(409, 282), new Vertex(422, 149), new Vertex(474, 133)),
						new Triangle(new Vertex(514, 112), new Vertex(517, 223), new Vertex(474, 133)),
						new Triangle(new Vertex(522, 85), new Vertex(514, 112), new Vertex(492, 90)),
						new Triangle(new Vertex(522, 85), new Vertex(517, 223), new Vertex(514, 112)),
						new Triangle(new Vertex(425, 74), new Vertex(492, 90), new Vertex(522, 85)),
						new Triangle(new Vertex(425, 74), new Vertex(427, 110), new Vertex(492, 90)),
						new Triangle(new Vertex(379, 64), new Vertex(522, 85), new Vertex(425, 74)),
						new Triangle(new Vertex(321, 29), new Vertex(379, 64), new Vertex(329, 62)),
						new Triangle(new Vertex(321, 29), new Vertex(522, 85), new Vertex(379, 64)),
						new Triangle(new Vertex(386, 106), new Vertex(425, 74), new Vertex(379, 64)),
						new Triangle(new Vertex(335, 103), new Vertex(379, 64), new Vertex(329, 62)),
						new Triangle(new Vertex(314, 129), new Vertex(280, 104), new Vertex(329, 62)) });

		test(polygons, holes, triangles);
	}

	void test6() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(441, 301), new Vertex(57, 30), new Vertex(385, 194),
				new Vertex(318, 18), new Vertex(596, 62), new Vertex(366, 48), new Vertex(582, 206),
				new Vertex(391, 102), new Vertex(592, 323) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(new Vertex[] { new Vertex(153, 87), new Vertex(421, 278), new Vertex(372, 197) }));
		holes.add(new Polygon(new Vertex[] { new Vertex(412, 188), new Vertex(385, 210), new Vertex(439, 276),
				new Vertex(464, 234) }));
		holes.add(new Polygon(new Vertex[] { new Vertex(511, 287), new Vertex(473, 263), new Vertex(458, 292),
				new Vertex(533, 304) }));
		holes.add(new Polygon(
				new Vertex[] { new Vertex(342, 27), new Vertex(331, 37), new Vertex(391, 182), new Vertex(429, 171) }));
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(439, 276), new Vertex(458, 292), new Vertex(421, 278)),
						new Triangle(new Vertex(473, 263), new Vertex(458, 292), new Vertex(439, 276)),
						new Triangle(new Vertex(464, 234), new Vertex(439, 276), new Vertex(473, 263)),
						new Triangle(new Vertex(441, 301), new Vertex(592, 323), new Vertex(533, 304)),
						new Triangle(new Vertex(458, 292), new Vertex(533, 304), new Vertex(441, 301)),
						new Triangle(new Vertex(421, 278), new Vertex(441, 301), new Vertex(458, 292)),
						new Triangle(new Vertex(153, 87), new Vertex(441, 301), new Vertex(421, 278)),
						new Triangle(new Vertex(57, 30), new Vertex(441, 301), new Vertex(153, 87)),
						new Triangle(new Vertex(511, 287), new Vertex(592, 323), new Vertex(533, 304)),
						new Triangle(new Vertex(473, 263), new Vertex(592, 323), new Vertex(511, 287)),
						new Triangle(new Vertex(464, 234), new Vertex(592, 323), new Vertex(473, 263)),
						new Triangle(new Vertex(412, 188), new Vertex(592, 323), new Vertex(464, 234)),
						new Triangle(new Vertex(429, 171), new Vertex(412, 188), new Vertex(391, 182)),
						new Triangle(new Vertex(429, 171), new Vertex(592, 323), new Vertex(412, 188)),
						new Triangle(new Vertex(391, 102), new Vertex(592, 323), new Vertex(429, 171)),
						new Triangle(new Vertex(342, 27), new Vertex(391, 102), new Vertex(366, 48)),
						new Triangle(new Vertex(342, 27), new Vertex(429, 171), new Vertex(391, 102)),
						new Triangle(new Vertex(582, 206), new Vertex(391, 102), new Vertex(366, 48)),
						new Triangle(new Vertex(153, 87), new Vertex(372, 197), new Vertex(385, 194)),
						new Triangle(new Vertex(57, 30), new Vertex(385, 194), new Vertex(153, 87)),
						new Triangle(new Vertex(385, 210), new Vertex(421, 278), new Vertex(439, 276)),
						new Triangle(new Vertex(372, 197), new Vertex(421, 278), new Vertex(385, 210)),
						new Triangle(new Vertex(385, 194), new Vertex(385, 210), new Vertex(372, 197)),
						new Triangle(new Vertex(412, 188), new Vertex(385, 210), new Vertex(385, 194)),
						new Triangle(new Vertex(391, 182), new Vertex(385, 194), new Vertex(412, 188)),
						new Triangle(new Vertex(331, 37), new Vertex(385, 194), new Vertex(391, 182)),
						new Triangle(new Vertex(318, 18), new Vertex(331, 37), new Vertex(342, 27)),
						new Triangle(new Vertex(318, 18), new Vertex(385, 194), new Vertex(331, 37)),
						new Triangle(new Vertex(342, 27), new Vertex(596, 62), new Vertex(366, 48)),
						new Triangle(new Vertex(318, 18), new Vertex(596, 62), new Vertex(342, 27)) });
		test(polygons, holes, triangles);
	}

	/**
	 * Vertical edge
	 */
	@Test
	void test7() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(200, 287), new Vertex(200, 52), new Vertex(300, 110),
				new Vertex(404, 41), new Vertex(463, 276), new Vertex(317, 226) }));
		List<Polygon> holes = new ArrayList<>();
		List<Triangle> triangles = Arrays
				.asList(new Triangle[] { new Triangle(new Vertex(300, 110), new Vertex(200, 287), new Vertex(317, 226)),
						new Triangle(new Vertex(200, 52), new Vertex(200, 287), new Vertex(300, 110)),
						new Triangle(new Vertex(300, 110), new Vertex(463, 276), new Vertex(317, 226)),
						new Triangle(new Vertex(404, 41), new Vertex(463, 276), new Vertex(300, 110)) });
		test(polygons, holes, triangles);
	}

	/**
	 * has an vertex which neighbours are on same y
	 */
	@Test
	void test8() {
		List<Polygon> polygons = new ArrayList<>();
		polygons.add(new Polygon(new Vertex[] { new Vertex(855, 100), new Vertex(1337, 100), new Vertex(1703, 614),
				new Vertex(1251, 873), new Vertex(1269, 483), new Vertex(734, 768), new Vertex(408, 511),
				new Vertex(508, 100) }));
		List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(new Vertex[] { new Vertex(810, 196), new Vertex(827, 320), new Vertex(1107, 318),
				new Vertex(1087, 201), new Vertex(924, 171) }));
		List<Triangle> triangles = Arrays.asList(
				new Triangle[] { new Triangle(new Vertex(1269, 483), new Vertex(734, 768), new Vertex(408, 511)),
						new Triangle(new Vertex(827, 320), new Vertex(408, 511), new Vertex(1269, 483)),
						new Triangle(new Vertex(810, 196), new Vertex(408, 511), new Vertex(827, 320)),
						new Triangle(new Vertex(508, 100), new Vertex(810, 196), new Vertex(924, 171)),
						new Triangle(new Vertex(508, 100), new Vertex(408, 511), new Vertex(810, 196)),
						new Triangle(new Vertex(1269, 483), new Vertex(1251, 873), new Vertex(1703, 614)),
						new Triangle(new Vertex(827, 320), new Vertex(1703, 614), new Vertex(1269, 483)),
						new Triangle(new Vertex(1107, 318), new Vertex(1703, 614), new Vertex(827, 320)),
						new Triangle(new Vertex(1087, 201), new Vertex(1703, 614), new Vertex(1107, 318)),
						new Triangle(new Vertex(855, 100), new Vertex(924, 171), new Vertex(508, 100)),
						new Triangle(new Vertex(855, 100), new Vertex(1087, 201), new Vertex(924, 171)),
						new Triangle(new Vertex(1337, 100), new Vertex(1087, 201), new Vertex(855, 100)),
						new Triangle(new Vertex(1337, 100), new Vertex(1703, 614), new Vertex(1087, 201)) });
		test(polygons, holes, triangles);
	}

}
