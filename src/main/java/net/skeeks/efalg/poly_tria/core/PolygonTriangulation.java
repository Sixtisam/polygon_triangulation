// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.skeeks.efalg.poly_tria.Edge;

/**
 * Implementation of a y-monotone polygon triangulation algorithm
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class PolygonTriangulation {

	/**
	 * only used to preserve the edges order in which they were added.
	 */
	public static List<Edge> PROGRESS_EDGES = new ArrayList<>();

	/**
	 * Triangulates a list of polygons with holes
	 * 
	 * @param polygons - polygons (counter-clockwise)
	 * @param holes    - the holes (clockwise)
	 * @return A list of triangles resulting in triangulation of all the polygons
	 */
	public static List<Triangle> triangulate(List<Polygon> polygons, List<Polygon> holes) {
		PROGRESS_EDGES.clear();
		mapHolesToPolygons(polygons, holes);

		ArrayList<Triangle> triangles = new ArrayList<>();
		// each polygon from the input is handled separately
		for (int i = 0; i < polygons.size(); i++) {
			Polygon polygon = polygons.get(i);
			// -----------------------------------------------------
			// 1. Split the polygon up into monotone polygons
			// -----------------------------------------------------
			MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
			// Optimization: if the polygon has no split or merge vertices, we can skip
			// making the polgony y-monotone because it is already y-monotone.
			int splitOrMergeVerticeCount = sls.init(polygon);
			if (splitOrMergeVerticeCount > 0) {
				for (Vertex currentEvent : sls.events) {
					switch (currentEvent.type) {
					case START:
						handleStartVertex(currentEvent, sls);
						break;
					case REGULAR:
						handleRegularVertex(currentEvent, sls);
						break;
					case MERGE:
						handleMergeVertex(currentEvent, sls);
						break;
					case SPLIT:
						handleSplitVertex(currentEvent, sls);
						break;
					case END:
						handleEndVertex(currentEvent, sls);
						break;
					default:
						throw new RuntimeException("should not happen");
					}
				}
			} else {
				// only 1 y-monotone polygon to triangulate
				sls.dcel.newConnections.add(sls.events[0].edge);
			}

			// -----------------------------------------------------
			// 2. Triangulate those y-monotone polygons
			// -----------------------------------------------------
			for (int j = 0; j < sls.dcel.newConnections.size(); j++) {
				HalfEdge startEdge = sls.dcel.newConnections.get(j);
				// it may happen that the edge is already triangulated. this is for example the case when a polygon has a hole. 
				// The hole creates at least one new connection, but the polygon may be still only one single "area"
				if(!startEdge.triangulated) {
					triangulateMonotonePolygon(startEdge, triangles);
				}
			}
		}
		return triangles;
	}

	/**
	 * Each hole must be inside a polygon. This method finds that polygon and
	 * assigns that hole to that polygon.
	 * 
	 */
	public static void mapHolesToPolygons(List<Polygon> polygons, List<Polygon> holes) {
		for (Polygon hole : holes) {
			for (Polygon polygon : polygons) {
				if (polygon.contains(hole)) {
					polygon.addHole(hole);
					break;
				}
			}
		}
	}

	// --------------------------------------
	// 1. Splitting the polygon up into y-monotone polygons
	// --------------------------------------

	public static void handleStartVertex(Vertex event, MakeMonotoneSweepLineStatus sls) {
		// add edge to next vertex into tree
		sls.edgeTree.currentY = event.y;
		event.edge.helper = event;
		sls.edgeTree.insert(event.edge);
	}

	public static void handleRegularVertex(Vertex event, MakeMonotoneSweepLineStatus sls) {
		if (!isAreaLeftOfVertex(event)) {
			// connect if its a merge vertex
			HalfEdge prevEdge = event.prev.edge;
			assert prevEdge.helper != null;
			assert prevEdge.helper.type != null;
			if (prevEdge.helper.type == VertexType.MERGE) {
				insertConnection(event, prevEdge, sls);
			}
			// remove edge from tree as it is finished now
			sls.edgeTree.currentY = event.y;
			sls.edgeTree.remove(prevEdge);
			// add the new edge to the tree
			event.edge.helper = event;
			sls.edgeTree.insert(event.edge);
		} else {
			sls.edgeTree.currentY = event.y;
			HalfEdge toTheLeft = sls.edgeTree.findToLeft(event);
			assert toTheLeft.helper != null;
			assert toTheLeft.helper.type != null;
			if (toTheLeft.helper.type == VertexType.MERGE) {
				insertConnection(event, toTheLeft, sls);
			}
			toTheLeft.helper = event;
		}
	}

	public static void handleSplitVertex(Vertex event, MakeMonotoneSweepLineStatus sls) {
		// Draw diagonal to helper of edge to the left
		sls.edgeTree.currentY = event.y;
		HalfEdge edgeToTheLeft = sls.edgeTree.findToLeft(event);
		insertConnection(event, edgeToTheLeft, sls);
		// set event as new helper of edge to the left
		edgeToTheLeft.helper = event;
		// add new edge
		event.edge.helper = event;
		sls.edgeTree.insert(event.edge);

	}

	public static void handleMergeVertex(Vertex event, MakeMonotoneSweepLineStatus sls) {
		// make diagonal to helper of prev edge if its a merge vertex
		HalfEdge prevEdge = event.prev.edge;
		if (prevEdge.helper.type == VertexType.MERGE) {
			insertConnection(event, prevEdge, sls);
		}
		// remove that edge from the tree because that edge is now finished
		sls.edgeTree.currentY = event.y;
		sls.edgeTree.remove(prevEdge);
		HalfEdge toTheLeft = sls.edgeTree.findToLeft(event);
		if (toTheLeft.helper.type == VertexType.MERGE) {
			insertConnection(event, toTheLeft, sls);
		}
		toTheLeft.helper = event;
	}

	public static void handleEndVertex(Vertex event, MakeMonotoneSweepLineStatus sls) {
		// make diagonal if helper of prev edge is merge vertex
		HalfEdge prevEdge = event.previousEdge();
		assert prevEdge.helper != null;
		assert prevEdge.helper.type != null;
		if (prevEdge.helper.type == VertexType.MERGE) {
			insertConnection(event, prevEdge, sls);
		}
		sls.edgeTree.currentY = event.y;
		// remove edge from tree as this edge is now finished
		sls.edgeTree.remove(prevEdge);
	}

	public static void insertConnection(Vertex event, HalfEdge toTheLeft, MakeMonotoneSweepLineStatus sls) {
		// TODO ske check there is some special handling needed for the first hole vertex.
		sls.dcel.insertEdge(event, toTheLeft);
		// progress visualization
		PROGRESS_EDGES.add(new Edge(event, toTheLeft.helper));
	}

	/**
	 * Returns true if the area of the polygon lies to the left of currV. <br>
	 * Caution: Can only be used for regular vertices
	 */
	public static boolean isAreaLeftOfVertex(Vertex currV) {
		assert currV.type == VertexType.REGULAR;
		Vertex prevV = currV.prev;
		if (prevV.y < currV.y || (prevV.y == currV.y && prevV.x > currV.x)) {
			return true;
		} else {
			return false;
		}
	}
	
	// -----------------------------------------------------
	// 2. Triangulate those y-monotone polygons
	// -----------------------------------------------------
	
	/**
	 * Stack instance of 2. phase
	 */
	protected static final Deque<Vertex> TRIANGULATION_STACK = new ArrayDeque<>();

	/**
	 * Triangulates a y-monotone polygon
	 */
	public static void triangulateMonotonePolygon(HalfEdge startEdge, List<Triangle> triangles) {
		Vertex[] vertices = getYMonotoneVertices(startEdge);
		assert vertices.length >= 3;
		if (vertices.length == 3) {
			triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
			return; // triangles are already triangulated
		}
		assert vertices.length > 3; // just to be sure

		assert TRIANGULATION_STACK.isEmpty(); // stack is reused 
		TRIANGULATION_STACK.push(vertices[0]);
		TRIANGULATION_STACK.push(vertices[1]);
		// Start at 3 vertex as for the first two, there is nothing to triangulate
		for (int i = 2; i < vertices.length - 1; i++) {
			Vertex curr = vertices[i];

			if (TRIANGULATION_STACK.peek().triangulationChainType != curr.triangulationChainType) {
				// Different chains -> Connect all vertices on stack (always possible)

				Vertex prevPopped = null;
				do {
					Vertex popped = TRIANGULATION_STACK.pop();
					if (prevPopped != null) {
						addTriangulationConnection(curr, popped, prevPopped, triangles);
					}
					prevPopped = popped;
				} while (TRIANGULATION_STACK.size() > 1);
				Vertex v1 = TRIANGULATION_STACK.pop(); // last vertex on stack must be popped but not connected
				if (prevPopped != null) {
					addTriangulationConnection(curr, v1, prevPopped, triangles);
				}
				assert i - 1 >= 1;
				TRIANGULATION_STACK.push(vertices[i - 1]);
				TRIANGULATION_STACK.push(curr);
			} else {
				// Same chain -> Connect to vertices on same chain until its not longer possible
				Vertex prevPopped = TRIANGULATION_STACK.pop(); // already connected because on same chainw
				Vertex popped = null;
				while (!TRIANGULATION_STACK.isEmpty()) {
					popped = TRIANGULATION_STACK.pop();
					// only draw connections while they are inside the polygon
					if (connectionLiesInside(curr, prevPopped, popped)) {
						addTriangulationConnection(curr, popped, prevPopped, triangles);
						// dcel.insertEdge(curr, popped);
						prevPopped = popped;
					} else {
						TRIANGULATION_STACK.push(popped);
						popped = prevPopped;
						break; // stop on first connection that would be outside the polygon
					}
				}
				TRIANGULATION_STACK.push(popped); // last popped vertex must be pushed again
				TRIANGULATION_STACK.push(curr); // also push current vertex
			}
		}

		// Pop all remaining vertices from stack, draw diagonal to all of them except
		// last and first one
		Vertex prevPopped = TRIANGULATION_STACK.pop();
		while (TRIANGULATION_STACK.size() > 1) {
			Vertex popped = TRIANGULATION_STACK.pop();
			addTriangulationConnection(vertices[vertices.length - 1], popped, prevPopped, triangles);
			prevPopped = popped;
		}
		Vertex lastPopped = TRIANGULATION_STACK.pop();
		addTriangulationConnection(vertices[vertices.length - 1], lastPopped, prevPopped, triangles);
		assert TRIANGULATION_STACK.isEmpty();
	}

	/**
	 * Returns an array of vertices of the passed face.
	 * Also prepares the vertex for the 2. algorithm phase
	 */
	public static Vertex[] getYMonotoneVertices(HalfEdge startEdge) {
		ArrayList<Vertex> tmpVertices = new ArrayList<>();

		// First, search the highest vertex
		Vertex max = null; // vertex that will later become ChainType.START
		HalfEdge maxEdge = null;
		// First, create a doubly connected list (since the 'twin' half edges now form
		// other faces we cannot use them, but we need them)
		{
			HalfEdge currEdge = startEdge;
			do {
				Vertex v = currEdge.from;
				tmpVertices.add(v);
				if (max == null || v.compareTo(max) < 0) {
					max = v;
					maxEdge = currEdge;
				}
				// its not triangulated yet but it will be for sure triangulated in the following code
				currEdge.triangulated = true;
				currEdge = currEdge.next;
			} while (currEdge != startEdge);
		}

		// list that will be returned
		Vertex[] vertices = new Vertex[tmpVertices.size()];
		// init max vertex
		max.triangulationChainType = ChainType.START;
		vertices[0] = max;

		// traverse all other vertices
		HalfEdge currNext = maxEdge.next;
		HalfEdge currPrev = maxEdge.prev;
		int i = 1;
		while (currNext != currPrev) {
			if (currNext.from.compareTo(currPrev.from) < 0) {
				// currNext is higher
				currNext.from.triangulationChainType = ChainType.LEFT;
				vertices[i++] = currNext.from;
				currNext = currNext.next;
			} else {
				// currPrev is higher
				currPrev.from.triangulationChainType = ChainType.RIGHT;
				vertices[i++] = currPrev.from;
				currPrev = currPrev.prev;
			}
			assert vertices[i - 2].compareTo(vertices[i - 1]) < 0;
		}
		assert currNext == currPrev;
		assert i + 1 == vertices.length;
		vertices[i] = currNext.from;
		assert vertices[i - 1].compareTo(vertices[i]) < 0;
		return vertices;
	}

	public static boolean connectionLiesInside(Vertex from, Vertex between, Vertex to) {
		// Kreuzprodukt zwischen ziel und vertex dazwischen
		int v1x = from.x - between.x;
		int v1y = from.y - between.y;
		int v2x = to.x - between.x;
		int v2y = to.y - between.y;
		long cross = (v1x * v2y) - (v1y * v2x);
		if (from.triangulationChainType == ChainType.LEFT) {
			return cross > 0;
		} else {
			return cross < 0;
		}
	}
	
	/**
	 * Adds triangle to the triangulation results
	 */
	public static void addTriangulationConnection(Vertex v1, Vertex v2, Vertex v3, List<Triangle> triangles) {
		assert v1 != null;
		assert v2 != null;
		assert v3 != null;
		triangles.add(new Triangle(v1, v2, v3));
		
		// dont add yellow edges because it does not work accurately
	}
}
