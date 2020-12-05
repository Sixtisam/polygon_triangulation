// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.Arrays;

/**
 * 
 * Sweep line status for the 'make polygon y-monotone' algorithm.
 * 
 * - all vertices/events<br>
 * - a doubly connected edge list representing the polygon<br>
 * - active set of edges, ordered by x-coordinate (used to find edge left of a
 * vertex)
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class MakeMonotoneSweepLineStatus {
	public Vertex[] events;
	public final DCEL dcel = new DCEL();
	public final EdgeSearchTree edgeTree = new EdgeSearchTree();

	/**
	 * Current event index used in initialization
	 */
	protected int eventIndex = 0;
	/**
	 * Initializes the sweep line status for triangulation:
	 * <li>Build a DCEL for all vertices/edges
	 * <li>Categorize vertex (VertexType)
	 * <li>Sort vertices (events)
	 * 
	 * @return the number of split or merge vertices in the polygon
	 */
	public int init(Polygon polygon) {
		int splitOrMergeVerticesCount = 0;

		// A polygon from the input file has initially only 1 face.
		Face polygonFace = new Face(false);
		dcel.addFace(polygonFace);

		// Memory Optimization: Use array instead of List because Collections.sort copies its contents and is slow
		int vertexCount = polygon.length();
		for(Polygon hole : polygon.holes) vertexCount += hole.length();
		events = new Vertex[vertexCount];

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
			edge.prev = prevEdge;
			edge.twin = twinEdge;

			// init twin edge
			twinEdge.next = prevTwinEdge;
			twinEdge.face = null; // outer face which can be ignored for the triangulation algorithm
			twinEdge.prev = prevTwinEdge;
			twinEdge.twin = edge;

			if (prevEdge != null) {
				prevEdge.next = edge;
			}
			if (prevTwinEdge != null) {
				prevTwinEdge.prev = twinEdge;
				prevTwinEdge.from = currV;
			}

			// init vertex
			splitOrMergeVerticesCount += categorizeVertex(currV, nextV, prevV);
			currV.edge = edge;
			currV.prev = prevV;

			// add to sweep line event list
			events[eventIndex++] = currV;

			// update prev edges
			prevEdge = edge;
			prevTwinEdge = twinEdge;
		}

		// connect first and last vertex
		Vertex firstVertex = polygon.vertices[0];
		Vertex lastVertex = polygon.vertices[polygon.vertices.length - 1];
		lastVertex.edge.next = firstVertex.edge;
		lastVertex.edge.twin.from = firstVertex;
		lastVertex.edge.twin.prev = firstVertex.edge.twin;
		firstVertex.edge.prev = lastVertex.edge;
		firstVertex.edge.twin.next = lastVertex.edge;

		// set edge of polygonFace to an arbitrary edge
		polygonFace.edge = firstVertex.edge;

		for (int i = 0; i < polygon.holes.size(); i++) {
			splitOrMergeVerticesCount += initHole(polygon.holes.get(i));
		}
		// sort descending-y (if same, ascending x)
		Arrays.sort(events);
		return splitOrMergeVerticesCount;
	}

	public int initHole(Polygon holePolygon) {
		Face holeFace = null;
		Face holePolygonFace = new Face(true); // face of polygon but has to be specially marked
		// explicitly do not ad the hole polygon face to the faces list in dcel (that is
		// done later)

		HalfEdge prevEdge = null;
		HalfEdge prevTwinEdge = null;
		int splitOrMergeVerticesCount = 0;
		for (int v = 0; v < holePolygon.vertices.length; v++) {
			Vertex currV = holePolygon.vertices[v];
			Vertex nextV = holePolygon.vertices[(v + 1) % holePolygon.vertices.length];
			Vertex prevV = holePolygon.vertices[Math.floorMod((v - 1), holePolygon.vertices.length)];
			HalfEdge edge = new HalfEdge(); // edge in counter-clockwise-direction
			HalfEdge twinEdge = new HalfEdge(); // edge in clockwise-direction

			// init edge
			edge.from = currV;
			edge.face = holePolygonFace;
			edge.prev = prevEdge;
			edge.next = null; // not known yet
			edge.twin = twinEdge;

			// init twin edge
			twinEdge.next = prevTwinEdge;
			twinEdge.face = holeFace; // outer face is the polygon face (hole)
			twinEdge.prev = prevTwinEdge;
			twinEdge.twin = edge;

			if (prevEdge != null) {
				prevEdge.next = edge;
			}
			if (prevTwinEdge != null) {
				prevTwinEdge.prev = twinEdge;
				prevTwinEdge.from = currV;
			}

			// init vertex
			// arguments MUST NOT be switched as they are already "switched"
			splitOrMergeVerticesCount += categorizeVertex(currV, nextV, prevV);

			currV.edge = edge;
			currV.prev = prevV;

			// add to sweep line event list
			events[eventIndex++] = currV;

			// update prev edges
			prevEdge = edge;
			prevTwinEdge = twinEdge;
		}

		// connect first and last vertex
		Vertex firstVertex = holePolygon.vertices[0];
		Vertex lastVertex = holePolygon.vertices[holePolygon.vertices.length - 1];
		lastVertex.edge.twin.prev = firstVertex.edge.twin;
		lastVertex.edge.next = firstVertex.edge;
		lastVertex.edge.twin.from = firstVertex;
		firstVertex.edge.twin.next = lastVertex.edge;
		firstVertex.edge.prev = lastVertex.edge;

		return splitOrMergeVerticesCount;
	}

	/**
	 * Set the vertex type depending on position of previous and next vertex as well
	 * as the interior angle
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
