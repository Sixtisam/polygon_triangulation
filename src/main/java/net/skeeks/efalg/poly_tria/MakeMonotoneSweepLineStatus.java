package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Sweep line status for the 'make polygon y-monotone' algorithm
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class MakeMonotoneSweepLineStatus {
	public List<Vertex> events;
	public DCEL dcel = new DCEL();
	public final EdgeSearchTree edgeTree  = new EdgeSearchTree();

	/**
	 * 
	 * @return the number of split or merge vertices in the polygon
	 */
	public int init(Polygon polygon) {
		events = new ArrayList<>();
		int splitOrMergeVerticesCount = 0;
		
		// A polygon from the input file has initially only 1 face.
		Face polygonFace = new Face();
		dcel.faces.add(polygonFace);
		
		HalfEdge prevEdge = null;
		HalfEdge prevTwinEdge = null;
		for (int v = 0; v < polygon.length(); v++) {
			Vertex currV = polygon.points[v];
			Vertex nextV = polygon.points[(v + 1) % polygon.points.length];
			Vertex prevV = polygon.points[Math.floorMod((v - 1), polygon.points.length)];
			
			HalfEdge edge = new HalfEdge(); // edge in counter-clockwise-direction
			HalfEdge twinEdge = new HalfEdge(); // edge in clockwise-direction
			
			// init edge 
			edge.from = currV;
			edge.face = polygonFace;
			edge.next = null; // not known yet
			edge.twin = twinEdge;
			
			
			// init twin edge
			twinEdge.next = prevTwinEdge;
			twinEdge.face = null; // outer face which can be ignored for the triangulation algorithm
			twinEdge.twin = edge;
			
			if(prevEdge != null) {
				prevEdge.next = edge;
			}
			if(prevTwinEdge != null) {
				prevTwinEdge.from = currV;
			}
			
			// init vertex
			splitOrMergeVerticesCount += categorizeVertex(currV, nextV, prevV);
			currV.edge = edge;
			currV.prev = prevV;
			
			// add everything to the DCEL
			dcel.edges.add(edge);
			dcel.edges.add(twinEdge);
			dcel.vertices.add(currV);
			
			// add to sweep line event list
			events.add(currV); 
			
			// update prev edges
			prevEdge = edge;
			prevTwinEdge = twinEdge;
		}
		
		// connect first and last vertex
		Vertex firstVertex = polygon.points[0];
		Vertex lastVertex = polygon.points[polygon.points.length - 1];
		lastVertex.edge.next = firstVertex.edge;
		lastVertex.edge.twin.from = firstVertex;
		firstVertex.edge.twin.next = lastVertex.edge;
		
		// set edge of polygonFace to an arbitrary edge 
		polygonFace.edge = firstVertex.edge;
		
		dcel.integrityCheck();
				
		Collections.sort(events);
		return splitOrMergeVerticesCount;
	}

	public static int categorizeVertex(Vertex currV, Vertex nextV, Vertex prevV) {
		boolean isNextVBelow = currV.y > nextV.y || (currV.y == nextV.y && currV.x < nextV.x);
		boolean isPrevVBelow = currV.y > prevV.y || (currV.y == prevV.y && currV.x < prevV.x);

		if (isNextVBelow && isPrevVBelow) { // start oder split
			double angle = interiorAngle(currV, prevV, nextV);
			if (angle < Math.PI) {
				currV.type = VertexType.START;
				return 0;
			} else {
				currV.type = VertexType.SPLIT;
				return 1;
			}
		} else if (!isNextVBelow && !isPrevVBelow) { // end oder merge
			double angle = interiorAngle(currV, prevV, nextV);
			if (angle < Math.PI) {
				currV.type = VertexType.END;
				return 0;
			} else {
				currV.type = VertexType.MERGE;
				return 1;
			}
		} else { // regular
			currV.type = VertexType.REGULAR;
			return 0;
		}
	}

	public static final double FULL_360_RAD = 2 * Math.PI;

	public static double interiorAngle(Vertex curr, Vertex prev, Vertex next) {
		double intermediate = Math.atan2(curr.y - prev.y, curr.x - prev.x)
				- Math.atan2(next.y - curr.y, next.x - curr.x) + Math.PI + FULL_360_RAD;
		while (intermediate > FULL_360_RAD)
			intermediate -= FULL_360_RAD;
		assert intermediate > 0.0 && intermediate <= FULL_360_RAD;
		return intermediate;
	}

}
