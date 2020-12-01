package net.skeeks.efalg.poly_tria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PolygonWithHoleTest {

	void test(Polygon polygon, Polygon hole, List<Triangle> expectedTriangles) {
		List<Triangle> triangles = PolygonTriangulation.triangulate(Collections.singletonList(polygon),
				Collections.singletonList(hole));
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
				.asList(new Triangle[] { new Triangle(new Vertex(220, 110), new Vertex(400, 102), new Vertex(189, 85)),
						new Triangle(new Vertex(189, 85), new Vertex(10, 150), new Vertex(200, 150)),
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
						new Triangle(new Vertex(300, 10), new Vertex(50, 50), new Vertex(600, 50)) });
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
				.asList(new Triangle[] { new Triangle(new Vertex(388, 235), new Vertex(329, 245), new Vertex(277, 240)),
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
						new Triangle(new Vertex(365, 81), new Vertex(343, 115), new Vertex(291, 91)),
						new Triangle(new Vertex(600, 300), new Vertex(300, 320), new Vertex(50, 300)),
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
						new Triangle(new Vertex(300, 10), new Vertex(50, 50), new Vertex(600, 50)) });

		test(polygon, hole, triangles);
	}

	void testWithMultipleHoles() {
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

}
