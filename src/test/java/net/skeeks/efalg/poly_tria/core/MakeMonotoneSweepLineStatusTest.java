// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MakeMonotoneSweepLineStatusTest {

	@Test
	void testInit() {
		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();

		Polygon p = new Polygon(new Vertex[] { new Vertex(10, 10), new Vertex(200, 59), new Vertex(300, 120),
				new Vertex(200, 80), new Vertex(10, 120) });

		int count = sls.init(p);
		assertEquals(1, count);

		assertEquals(1, sls.dcel.faces.size());
		assertEquals(5, sls.events.length);

		HalfEdge e = sls.events[0].edge;
		assertEquals(new Vertex(10, 120), e.from);
		assertEquals(new Vertex(200, 80), e.prev.from);
		assertEquals(new Vertex(10, 10), e.next.from);
		assertEquals(new Vertex(200, 80), e.prev.twin.next.from);
		assertEquals(new Vertex(200, 59), e.next.twin.from);
		assertEquals(new Vertex(300, 120), e.next.twin.prev.from);
		assertEquals(new Vertex(200, 80), e.prev.from);

	}

}
