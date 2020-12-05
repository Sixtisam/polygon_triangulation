// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DCELTest {

	@Test
	public void testInsertEdge() {
		Vertex v2 = new Vertex(10, 1);
		Vertex v3 = new Vertex(10, 10);
		Vertex v4 = new Vertex(0, 9);
		Vertex v1 = new Vertex(0, 0);
		Polygon p = new Polygon(new Vertex[] { v1, v2, v3, v4 });
		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
		sls.init(p);
		v4.edge.helper = v4;
		sls.dcel.insertEdge(v2, v4.edge);
		assertEquals(v2, v4.edge.prev.from);
		assertEquals(v4, v4.edge.prev.twin.from);
		assertEquals(v2, v4.edge.prev.twin.next.from);
		assertEquals(v3, v4.edge.prev.twin.prev.from);
		assertEquals(v2, v4.edge.prev.twin.prev.prev.from);
		assertEquals(v1, v4.edge.prev.prev.from);
		assertEquals(v1, v4.edge.next.from);
		assertEquals(v2, v4.edge.next.next.from);
	}
}
