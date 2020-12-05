// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.Vertex;

class PolygonTest {

	@Test
	void testCheckIntersection() {
		assertEquals(1, Polygon.checkIntersection(2, 1, 8, 7, 8, 1, -10, 10));
		assertEquals(1, Polygon.checkIntersection(2, 1, 8, 7, 10, 1, -1, 1));

		assertEquals(0, Polygon.checkIntersection(2, 1, 8, 7, 8, 1, 10, 10));
		assertEquals(0, Polygon.checkIntersection(2, 1, 8, 7, 8, 1, 10, -10));
	}

	@Test
	void testContains() {
		Polygon polygon = new Polygon(
				new Vertex[] { new Vertex(5, 0), new Vertex(5, 5), new Vertex(5, 10), new Vertex(0, 5) });
		Polygon hole = new Polygon(new Vertex[] { new Vertex(2, 5), new Vertex(4, 8), new Vertex(4, 3) });
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
		assertTrue(polygon.contains(hole));
	}

	@Test
	void testNotContains() {
		Polygon polygon = new Polygon(
				new Vertex[] { new Vertex(5, 0), new Vertex(5, 5), new Vertex(5, 10), new Vertex(0, 5) });
		Polygon hole = new Polygon(new Vertex[] { new Vertex(12, 5), new Vertex(15, 9), new Vertex(16, 3) });
		assertFalse(polygon.contains(hole));
	}

	@Test
	void testIsClockwise() {
		assertFalse(new Polygon(new Vertex[] { new Vertex(855, 100), new Vertex(1337, 100), new Vertex(1703, 614),
				new Vertex(1251, 873), new Vertex(1269, 483), new Vertex(734, 768), new Vertex(408, 511),
				new Vertex(508, 100) }).isClockwise());
		assertTrue(new Polygon(new Vertex[] { new Vertex(810, 196), new Vertex(827, 320), new Vertex(1107, 318),
				new Vertex(1087, 201), new Vertex(924, 171) }).isClockwise());

	}

}
