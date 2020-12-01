// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Sweep line status for the 'make polygon y-monotone' algorithm.
 * 
 * - all vertices/events<br>
 * - a doubly connected edge list representing the polygon<br>
 * - active set of edges, ordered by x-coordinate (used to find edge left of a vertex)
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class MakeMonotoneSweepLineStatus {
	public final List<Vertex> events = new ArrayList<>();
	public final DCEL dcel = new DCEL();
	public final EdgeSearchTree edgeTree  = new EdgeSearchTree();

	/**
	 * 
	 * @return the number of split or merge vertices in the polygon
	 */
	public int init(Polygon polygon) {
		int splitOrMergeVerticesCount = 0;
		
		// A polygon from the input file has initially only 1 face.
		Face polygonFace = new Face(false);
		dcel.addFace(polygonFace);
		
		HalfEdge prevEdge = null;
		HalfEdge prevTwinEdge = null;
		for (int v = 0; v < polygon.length(); v++) {
			Vertex currV = polygon.vertices[v];
			Vertex nextV = polygon.vertices[(v + 1) % polygon.vertices.length];
			Vertex prevV = polygon.vertices[Math.floorMod((v - 1), polygon.vertices.length)];
			
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
		Vertex firstVertex = polygon.vertices[0];
		Vertex lastVertex = polygon.vertices[polygon.vertices.length - 1];
		lastVertex.edge.next = firstVertex.edge;
		lastVertex.edge.twin.from = firstVertex;
		firstVertex.edge.twin.next = lastVertex.edge;
		
		// set edge of polygonFace to an arbitrary edge 
		polygonFace.edge = firstVertex.edge;
		
		dcel.integrityCheck();

		for(int i = 0; i < polygon.holes.size(); i++) {
			splitOrMergeVerticesCount += initHole(dcel, events, polygon.holes.get(i));
		}
		
		Collections.sort(events);
		return splitOrMergeVerticesCount;
	}
	
	public static int initHole(DCEL dcel, List<Vertex> events, Polygon holePolygon) {
		Face holeFace = null;
		Face holePolygonFace = new Face(true); // face of polygon but has to be specially marked
		// explicitly do not ad the hole polygon face to the faces list in dcel (that is done later)
		
		HalfEdge prevEdge = null;
		HalfEdge prevTwinEdge = null;
		int splitOrMergeVerticesCount = 0;
		for (int v = 0; v < holePolygon.vertices.length; v++){
			Vertex currV = holePolygon.vertices[v];
			Vertex nextV = holePolygon.vertices[(v + 1) % holePolygon.vertices.length];
			Vertex prevV = holePolygon.vertices[Math.floorMod((v - 1), holePolygon.vertices.length)];
			HalfEdge edge = new HalfEdge(); // edge in counter-clockwise-direction
			HalfEdge twinEdge = new HalfEdge(); // edge in clockwise-direction
			
			// init edge 
			edge.from = currV;
			edge.face = holePolygonFace;
			edge.next = null; // not known yet
			edge.twin = twinEdge;
			
			
			// init twin edge
			twinEdge.next = prevTwinEdge;
			twinEdge.face = holeFace; // outer face is the polygon face (hole)
			twinEdge.twin = edge;
			
			if(prevEdge != null) {
				prevEdge.next = edge;
			}
			if(prevTwinEdge != null) {
				prevTwinEdge.from = currV;
			}
			
			// init vertex
			// arguments MUST NOT be switched as they are already "switched"
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
		Vertex firstVertex = holePolygon.vertices[0];
		Vertex lastVertex = holePolygon.vertices[holePolygon.vertices.length - 1];
		lastVertex.edge.next = firstVertex.edge;
		lastVertex.edge.twin.from = firstVertex;
		firstVertex.edge.twin.next = lastVertex.edge;
		
		return splitOrMergeVerticesCount;
	}

	/**
	 * Set the vertex type depending on position of previous and next vertex as well as the interior angle
	 */
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

	/**
	 * Calculates the interior angle, assuming counter-clockwise order
	 */
	public static double interiorAngle(Vertex curr, Vertex prev, Vertex next) {
		double intermediate = Math.atan2(curr.y - prev.y, curr.x - prev.x)
				- Math.atan2(next.y - curr.y, next.x - curr.x) + Math.PI + FULL_360_RAD;
		while (intermediate > FULL_360_RAD)
			intermediate -= FULL_360_RAD;
		assert intermediate > 0.0 && intermediate <= FULL_360_RAD;
		return intermediate;
	}

}