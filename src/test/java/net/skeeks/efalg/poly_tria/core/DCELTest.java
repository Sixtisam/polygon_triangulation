package net.skeeks.efalg.poly_tria.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class DCELTest {

//	@Test
//	@Disabled
//	public void testInsertEdgeTrival() {
//		Polygon p = new Polygon(
//				new Vertex[] { new Vertex(0, 5), new Vertex(5, 0), new Vertex(5, 5), new Vertex(5, 10) });
//
//		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
//		sls.init(p);
//
//		sls.dcel.insertEdge(p.vertices[1], p.vertices[3]);
//		System.out.println(sls.dcel.faces.get(0));
//		System.out.println(sls.dcel.faces.get(1));
//	}
	
//	@Test
//	public void testInsertEdge_TwoEdgesToSameVertex() {
//		Polygon p = new Polygon(
//				new Vertex[] { new Vertex(0, 5), new Vertex(3, 0), new Vertex(4,0), new Vertex(5, 5), new Vertex(5, 10) });
//
//		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
//		sls.init(p);
//
//		sls.dcel.insertEdge(p.vertices[1], p.vertices[4]);
//		System.out.println("Calcualted Prev edges " + Arrays.toString(sls.dcel.getPreviousVerticesWithCommonFace(p.vertices[2], p.vertices[4])));
//		sls.dcel.insertEdge(p.vertices[2], p.vertices[4]);
//		System.out.println("Results: -------- " + sls.dcel.faces.size());
//		System.out.println(sls.dcel.faces.get(0));
//		System.out.println(sls.dcel.faces.get(1));
//		System.out.println(sls.dcel.faces.get(2));
//	}
//	
	
	@Test
	public void testInsertEdge() {
		Vertex v2 = new Vertex(10, 1);
		Vertex v3 = new Vertex(10,10);
		Vertex v4 = new Vertex(0,9);
		Vertex v1 = new Vertex(0, 0);
		Polygon p = new Polygon(
				new Vertex[] { v1, v2, v3, v4});
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
