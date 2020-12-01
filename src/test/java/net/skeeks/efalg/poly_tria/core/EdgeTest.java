package net.skeeks.efalg.poly_tria.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EdgeTest {
	public static final double DELTA = 0.0000001;

	@Test
	void testCalcXSlope() {
		HalfEdge edge = createEdge(new Vertex(10, 10), new Vertex(1, 1));
		assertEquals(edge.calcXSlope(), 1.0, DELTA);

		HalfEdge edge2 = createEdge(new Vertex(7, 11), new Vertex(1,2));
		assertEquals(edge2.calcXSlope(), 0.6666666666, DELTA);
		
		HalfEdge edge2Inv = createEdge(new Vertex(1,2), new Vertex(7, 11));
		assertEquals(edge2Inv.calcXSlope(), 0.6666666666, DELTA);
	}
	
	public HalfEdge createEdge(Vertex v1, Vertex v2) {
		HalfEdge edge = new HalfEdge();
		edge.next = new HalfEdge();
		edge.next.from = v2;
		edge.from = v1;
		return edge;
	}
	
	@Test
	void testIntersectionPointX(){
		HalfEdge edge = createEdge(new Vertex(10, 10), new Vertex(1, 1));
		assertEquals(edge.calcIntersectionX(10), 10.0, DELTA);
		assertEquals(edge.calcIntersectionX(1), 1.0, DELTA);
		assertEquals(edge.calcIntersectionX(2), 2.0, DELTA);
		assertEquals(edge.calcIntersectionX(3), 3.0, DELTA);
		assertEquals(edge.calcIntersectionX(4), 4.0, DELTA);
		assertEquals(edge.calcIntersectionX(5), 5.0, DELTA);
		assertEquals(edge.calcIntersectionX(6), 6.0, DELTA);
		assertEquals(edge.calcIntersectionX(7), 7.0, DELTA);
		assertEquals(edge.calcIntersectionX(8), 8.0, DELTA);
		assertEquals(edge.calcIntersectionX(9), 9.0, DELTA);
		assertEquals(edge.calcIntersectionX(10), 10.0, DELTA);
		
		HalfEdge edge2 = createEdge(new Vertex(7, 11), new Vertex(1,2));
		assertEquals(edge2.calcIntersectionX(11), 7, DELTA);
		assertEquals(edge2.calcIntersectionX(8), 5, DELTA);
		assertEquals(edge2.calcIntersectionX(7), 4.333333333333333333, DELTA);
		assertEquals(edge2.calcIntersectionX(2), 1, DELTA);
		
		HalfEdge edge2Inv = createEdge(new Vertex(1,2), new Vertex(7, 11));
		assertEquals(edge2Inv.calcIntersectionX(11), 7, DELTA);
		assertEquals(edge2Inv.calcIntersectionX(8), 5, DELTA);
		assertEquals(edge2Inv.calcIntersectionX(7), 4.333333333333333333, DELTA);
		assertEquals(edge2Inv.calcIntersectionX(2), 1, DELTA);
		
		HalfEdge edge3 = createEdge(new Vertex(17,1), new Vertex(8,3));
		assertEquals(edge3.calcIntersectionX(1), 17.0, DELTA);
		assertEquals(edge3.calcIntersectionX(3), 8, DELTA);
		assertEquals(edge3.calcIntersectionX(2), 12.5, DELTA);
		
		HalfEdge edge3Inv = createEdge(new Vertex(8,3), new Vertex(17,1));
		assertEquals(edge3Inv.calcIntersectionX(1), 17.0, DELTA);
		assertEquals(edge3Inv.calcIntersectionX(3), 8, DELTA);
		assertEquals(edge3Inv.calcIntersectionX(2), 12.5, DELTA);
	}

}
