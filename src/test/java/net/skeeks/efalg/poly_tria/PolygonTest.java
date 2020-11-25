package net.skeeks.efalg.poly_tria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		Polygon polygon = new Polygon(new Vertex[] {
				new Vertex(5,0),
				new Vertex(5,5),
				new Vertex(5,10),
				new Vertex(0,5)
		});
		Polygon hole = new Polygon(new Vertex[] {
			new Vertex(2,5),
			new Vertex(5,9),
			new Vertex(6,3)
		});
		assertTrue(polygon.contains(hole));
	}
	
	@Test
	void testNotContains() {
		Polygon polygon = new Polygon(new Vertex[] {
				new Vertex(5,0),
				new Vertex(5,5),
				new Vertex(5,10),
				new Vertex(0,5)
		});
		Polygon hole = new Polygon(new Vertex[] {
			new Vertex(12,5),
			new Vertex(15,9),
			new Vertex(16,3)
		});
		assertFalse(polygon.contains(hole));
	}

}
