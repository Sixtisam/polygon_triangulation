package net.skeeks.efalg.poly_tria;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class PolygonTriangulation {

	public static List<Face> triangulate(List<Polygon> polygons, List<Polygon> holes, List<Edge> edges) {

		mapHolesToPolygons(polygons, holes);

		ArrayList<Face> faces = new ArrayList<>();
		// each polygon from the input is handled separately
		for (Polygon polygon : polygons) {
			// -----------------------------------------------------
			// 1. Split the polygon up into monotone polygons
			// -----------------------------------------------------
			System.out.println("--------------------------");
			System.out.println("Handling following polygon: " + polygon);
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
						handleRegularVertex(currentEvent, sls, edges);
						break;
					case MERGE:
						handleMergeVertex(currentEvent, sls, edges);
						break;
					case SPLIT:
						handleSplitVertex(currentEvent, sls, edges);
						break;
					case END:
						handleEndVertex(currentEvent, sls, edges);
						break;
					default:
						throw new RuntimeException("should not happen");
					}
				}
			}

			// -----------------------------------------------------
			// 2. Triangulate those y-monotone polygons
			// -----------------------------------------------------
			System.out.println("Faces");
			System.out.println(sls.dcel.faces.stream().map(Face::toString).collect(Collectors.joining(System.lineSeparator())));
			int yMonotoneFaces = sls.dcel.faces.size();
			System.out.println("Size is "  + yMonotoneFaces);
			for (int i = 0; i < yMonotoneFaces; i++) {
				triangulateMonotonePolygon(sls.dcel.faces.get(i), sls.dcel, edges);
			}
			
			faces.addAll(sls.dcel.faces);
		}
		return faces;
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

	public static void addTriangulationConnection(Vertex v1, Vertex v2, DCEL dcel, List<Edge> edges) {
		edges.add(new Edge(v1, v2)); // dcel.insertEdge(curr, popped);
		try {
			dcel.insertEdge(v1, v2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void triangulateMonotonePolygon(Face face, DCEL dcel, List<Edge> edges) {
		Vertex[] vertices = getYMonotoneVertices(face);
		assert vertices.length >= 3;
		if (vertices.length == 3) {
			return; // triangles are already triangulated
		}
		assert vertices.length > 3; // just to be sure

		Deque<Vertex> stack = new ArrayDeque<>();
		stack.push(vertices[0]);
		stack.push(vertices[1]);
		// Start at 3 vertex as for the first two, there is nothing to triangulate
		for (int i = 2; i < vertices.length - 1; i++) {
			Vertex curr = vertices[i];
			
			if (stack.peek().chainType != curr.chainType) {
				// Different chains -> Connect all vertices on stack (always possible)

				do {
					Vertex popped = stack.pop();
					addTriangulationConnection(curr, popped, dcel, edges);
				} while (stack.size() > 1);
				stack.pop(); // last vertex on stack must be popped but not connected
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
						addTriangulationConnection(curr, popped, dcel, edges);
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
		stack.pop();
		while (stack.size() > 1) {
			Vertex popped = stack.pop();
			addTriangulationConnection(vertices[vertices.length - 1], popped, dcel, edges);
		}
		stack.pop();
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
			System.out.println("ymonotone: face edge " + face.edge);
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
		max.chainType = ChainType.START;
		vertices[0] = max;

		// traverse all other vertices
		Vertex currNext = max.triangulationNext();
		Vertex currPrev = max.triangulationPrev;
		int i = 1;
		while (currNext != currPrev) {
			if (currNext.compareTo(currPrev) < 0) {
				// currNext is higher
				currNext.chainType = ChainType.LEFT;
				vertices[i++] = currNext;
				currNext = currNext.triangulationNext();
			} else {
				// currPrev is higher
				currPrev.chainType = ChainType.RIGHT;
				vertices[i++] = currPrev;
				currPrev = currPrev.triangulationPrev;
			}
			assert vertices[i - 2].compareTo(vertices[i - 1]) < 0;
		}
		assert currNext == currPrev;
		assert i + 1 == vertices.length;
		vertices[i] = currNext;
		assert vertices[i - 1].compareTo(vertices[i]) < 0;
		System.out.println("List of vertices for face " + face);
		System.out.println(Arrays.stream(vertices).map(Vertex::toString).collect(Collectors.joining(System.lineSeparator())));
		return vertices;
	}

	public static boolean connectionLiesInside(Vertex from, Vertex between, Vertex to) {
		// Kreuzprodukt zwischen ziel und vertex dazwischen
		int v1x = from.x - between.x;
		int v1y = from.y - between.y;
		int v2x = to.x - between.x;
		int v2y = to.y - between.y;
		long cross = (v1x * v2y) - (v1y * v2x);
		if (from.chainType == ChainType.LEFT) {
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

	public static void handleRegularVertex(Vertex event, MakeMonotoneSweepLineStatus sls, List<Edge> edges) {
		if (!isAreaLeftOfVertex(event)) {
			// connect if its a merge vertex
			HalfEdge prevEdge = event.previousEdge();
			assert prevEdge.helper != null;
			assert prevEdge.helper.type != null;
			if (prevEdge.helper.type == VertexType.MERGE) {
				edges.add(new Edge(event, prevEdge.helper));
				sls.dcel.insertEdge(event, prevEdge.helper);
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
				edges.add(new Edge(event, toTheLeft.helper));
				sls.dcel.insertEdge(event, toTheLeft.helper);
			}
			toTheLeft.helper = event;
		}
	}

	public static void handleSplitVertex(Vertex event, MakeMonotoneSweepLineStatus sls, List<Edge> edges) {
		// Draw diagonal to helper of edge to the left
		sls.edgeTree.currentY = event.y;
		HalfEdge edgeToTheLeft = sls.edgeTree.findToLeft(event);
		sls.dcel.insertEdge(event, edgeToTheLeft.helper);
		edges.add(new Edge(event, edgeToTheLeft.helper));
		// set event as new helper of edge to the left
		edgeToTheLeft.helper = event;
		// add new edge
		event.edge.helper = event;
		sls.edgeTree.insert(event.edge);

	}

	public static void handleMergeVertex(Vertex event, MakeMonotoneSweepLineStatus sls, List<Edge> edges) {
		// make diagonal to helper of prev edge if its a merge vertex
		HalfEdge prevEdge = event.previousEdge();
		if (prevEdge.helper.type == VertexType.MERGE) {
			sls.dcel.insertEdge(event, prevEdge.helper);
			edges.add(new Edge(event, prevEdge.helper));
		}
		// remove that edge from the tree because that edge is now finished
		sls.edgeTree.currentY = event.y;
		sls.edgeTree.remove(prevEdge);
		HalfEdge toTheLeft = sls.edgeTree.findToLeft(event);
		if (toTheLeft.helper.type == VertexType.MERGE) {
			sls.dcel.insertEdge(event, toTheLeft.helper);
			edges.add(new Edge(event, toTheLeft.helper));
		}
		toTheLeft.helper = event;
	}

	public static void handleEndVertex(Vertex event, MakeMonotoneSweepLineStatus sls, List<Edge> edges) {
		// make diagonal if helper of prev edge is merge vertex
		HalfEdge prevEdge = event.previousEdge();
		assert prevEdge.helper != null;
		assert prevEdge.helper.type != null;
		if (prevEdge.helper.type == VertexType.MERGE) {
			sls.dcel.insertEdge(event, prevEdge.helper);
			edges.add(new Edge(event,  prevEdge.helper));
		}
		sls.edgeTree.currentY = event.y;
		// remove edge from tree as this edge is now finished
		sls.edgeTree.remove(prevEdge);
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
