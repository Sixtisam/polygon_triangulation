package net.skeeks.efalg.poly_tria;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class DCELTest {

	@Test
	@Disabled
	public void testInsertEdgeTrival() {
		Polygon p = new Polygon(
				new Vertex[] { new Vertex(0, 5), new Vertex(5, 0), new Vertex(5, 5), new Vertex(5, 10) });

		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
		sls.init(p);

		sls.dcel.insertEdge(p.vertices[1], p.vertices[3]);
		System.out.println(sls.dcel.faces.get(0));
		System.out.println(sls.dcel.faces.get(1));
	}
	
	@Test
	public void testInsertEdge_TwoEdgesToSameVertex() {
		Polygon p = new Polygon(
				new Vertex[] { new Vertex(0, 5), new Vertex(3, 0), new Vertex(4,0), new Vertex(5, 5), new Vertex(5, 10) });

		MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
		sls.init(p);

		sls.dcel.insertEdge(p.vertices[1], p.vertices[4]);
		System.out.println("Calcualted Prev edges " + Arrays.toString(sls.dcel.getPreviousVerticesWithCommonFace(p.vertices[2], p.vertices[4])));
		sls.dcel.insertEdge(p.vertices[2], p.vertices[4]);
		System.out.println("Results: -------- " + sls.dcel.faces.size());
		System.out.println(sls.dcel.faces.get(0));
		System.out.println(sls.dcel.faces.get(1));
		System.out.println(sls.dcel.faces.get(2));
	}
}
