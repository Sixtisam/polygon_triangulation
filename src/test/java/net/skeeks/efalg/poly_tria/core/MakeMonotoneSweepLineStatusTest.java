package net.skeeks.efalg.poly_tria.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MakeMonotoneSweepLineStatusTest {

	@Test
	void testInit() {
		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
		
		Polygon p = new Polygon(new Vertex[] {
				new Vertex(10,10),
				new Vertex(200,59),
				new Vertex(300, 120),
				new Vertex(200, 80),
				new Vertex(10, 120)
		});
		
		int count = sls.init(p);
		assertEquals(1, count);
		
		assertEquals(1, sls.dcel.faces.size());
		assertEquals(5, sls.dcel.vertices.size());
		
		HalfEdge e = sls.dcel.edges.get(0);
		assertEquals(new Vertex(10,10), e.from);
		assertEquals(new Vertex(10,120), e.prev.from);
		assertEquals(new Vertex(10,120), e.prev.twin.next.from);
		assertEquals(new Vertex(10,10), e.prev.next.from);
				
	}

}
