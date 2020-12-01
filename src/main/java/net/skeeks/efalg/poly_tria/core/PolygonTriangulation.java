// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.skeeks.efalg.poly_tria.Edge;

/**
 * Implement a y-monotone polygon triangulation algorithm
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
		for (Polygon polygon : polygons) {
			// -----------------------------------------------------
			// 1. Split the polygon up into monotone polygons
			// -----------------------------------------------------
			MakeMonotoneSweepLineStatus sls = new MakeMonotoneSweepLineStatus();
			// Optimization: if the polygon has no split or merge vertices, we can skip
			// making the polgony y-monotone because it is already y-monotone.
			if (sls.init(polygon) > 0) {
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
			}

			// -----------------------------------------------------
			// 2. Triangulate those y-monotone polygons
			// -----------------------------------------------------
			for (int i = 0; i < sls.dcel.faces.size(); i++) {
				triangulateMonotonePolygon(sls.dcel.faces.get(i), triangles);
			}
		}
		return triangles;
	}

	/**
	 * Each hole must be inside a polygon. This method finds that polygon and
	 * assigns that hole to that polygon.
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

	/**
	 * Adds triangle to the triangulation results
	 */
	public static void addTriangulationConnection(Vertex v1, Vertex v2, Vertex v3, List<Triangle> triangles) {
		assert v1 != null;
		assert v2 != null;
		assert v3 != null;
		triangles.add(new Triangle(v1, v2, v3));
		
		// also add new edge to progress list
		PROGRESS_EDGES.add(new Edge(v1, v2));
	}

	/**
	 * Triangulates a y-monotone polygon
	 */
	public static void triangulateMonotonePolygon(Face face, List<Triangle> triangles) {
		Vertex[] vertices = getYMonotoneVertices(face);
		assert vertices.length >= 3;
		if (vertices.length == 3) {
			triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
			return; // triangles are already triangulated
		}
		assert vertices.length > 3; // just to be sure

		Deque<Vertex> stack = new ArrayDeque<>();
		stack.push(vertices[0]);
		stack.push(vertices[1]);
		// Start at 3 vertex as for the first two, there is nothing to triangulate
		for (int i = 2; i < vertices.length - 1; i++) {
			Vertex curr = vertices[i];

			if (stack.peek().triangulationChainType != curr.triangulationChainType) {
				// Different chains -> Connect all vertices on stack (always possible)

				Vertex prevPopped = null;
				do {
					Vertex popped = stack.pop();
					if (prevPopped != null) {
						addTriangulationConnection(curr, popped, prevPopped, triangles);
					}
					prevPopped = popped;
				} while (stack.size() > 1);
				Vertex v1 = stack.pop(); // last vertex on stack must be popped but not connected
				if (prevPopped != null) {
					addTriangulationConnection(curr, v1, prevPopped, triangles);
				}
				assert i - 1 >= 1;
				stack.push(vertices[i - 1]);
				stack.push(curr);
			} else {
				// Same chain -> Connect to vertices on same chain until its not longer possible
				Vertex prevPopped = stack.pop(); // already connected because on same chainw
				Vertex popped = null;
				while (!stack.isEmpty()) {
					popped = stack.pop();
					// only draw connections while they are inside the polygon
					if (connectionLiesInside(curr, prevPopped, popped)) {
						addTriangulationConnection(curr, popped, prevPopped, triangles);
						// dcel.insertEdge(curr, popped);
						prevPopped = popped;
					} else {
						stack.push(popped);
						popped = prevPopped;
						break; // stop on first connection that would be outside the polygon
					}
				}
				stack.push(popped); // last popped vertex must be pushed again
				stack.push(curr); // also push current vertex
			}
		}

		// Pop all remaining vertices from stack, draw diagonal to all of them except
		// last and first one
		Vertex prevPopped = stack.pop();
		while (stack.size() > 1) {
			Vertex popped = stack.pop();
			addTriangulationConnection(vertices[vertices.length - 1], popped, prevPopped, triangles);
			prevPopped = popped;
		}
		Vertex lastPopped = stack.pop();
		addTriangulationConnection(vertices[vertices.length - 1], lastPopped, prevPopped, triangles);
	}

	/**
	 * Return all vertices of the passed face
	 */
	public static Vertex[] getYMonotoneVertices(Face face) {
		ArrayList<Vertex> tmpVertices = new ArrayList<>();

		Vertex max = null; // vertex that will later become ChainType.START
		// First, create a doubly connected list (since the 'twin' half edges now form
		// other faces we cannot use them, but we need them)
		{
			HalfEdge currEdge = face.edge;
			Vertex prev = null;
			do {
				Vertex v = currEdge.from;
				v.triangulationPrev = prev; // must be overwritten
				v.triangulationEdge = currEdge; // must be overwritten
				tmpVertices.add(v);
				if (max == null || v.compareTo(max) < 0) {
					max = v;
				}
				prev = v;
				currEdge = currEdge.next;
			} while (currEdge != face.edge);
			face.edge.from.triangulationPrev = prev; // also set prev vertex of first vertex
			face.edge.from.triangulationEdge = face.edge; // must be overwritten
			// integrity check
			for (Vertex v : tmpVertices) {
				assert v.triangulationPrev != null;
			}
		}

		// list that will be returned
		Vertex[] vertices = new Vertex[tmpVertices.size()];
		// init max vertex
		max.triangulationChainType = ChainType.START;
		vertices[0] = max;

		// traverse all other vertices
		Vertex currNext = max.triangulationNext();
		Vertex currPrev = max.triangulationPrev;
		int i = 1;
		while (currNext != currPrev) {
			if (currNext.compareTo(currPrev) < 0) {
				// currNext is higher
				currNext.triangulationChainType = ChainType.LEFT;
				vertices[i++] = currNext;
				currNext = currNext.triangulationNext();
			} else {
				// currPrev is higher
				currPrev.triangulationChainType = ChainType.RIGHT;
				vertices[i++] = currPrev;
				currPrev = currPrev.triangulationPrev;
			}
			assert vertices[i - 2].compareTo(vertices[i - 1]) < 0;
		}
		assert currNext == currPrev;
		assert i + 1 == vertices.length;
		vertices[i] = currNext;
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
			HalfEdge prevEdge = event.previousEdge();
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
		HalfEdge prevEdge = event.previousEdge();
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
		if (event.edge.face.hole) {
			System.out.println("Detected hole at " + event + " associating with face of edge to theleft: " + toTheLeft);
			// will only be true for the uppermost vertex of any holes.
			// this code will set the face of the hole (outer edges)
			Face newFace = toTheLeft.face; // the edges of the hole will be associated with the face of the edge to the
											// left
			assert !newFace.hole;
			HalfEdge curr = event.edge; // twin edge is in the polygon, normal edge would be inside
			do {
				curr.face = newFace;
				curr = curr.next;
			} while (curr != event.edge);

		}
		sls.dcel.insertEdge(event, toTheLeft.helper);
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
}
