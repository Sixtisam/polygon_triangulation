package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class PolygonTriangulation {

	public static List<Edge> triangulate(Polygon[] polygons) {
		List<Edge> connections = new ArrayList<>();
		for (Polygon polygon : polygons) {
			SweepLineStatus sls = new SweepLineStatus(polygon);
			sls.init();
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
				}
			}
			connections.addAll(sls.connections);
		}
		return connections;
	}

	public static void addConnection(Vertex p1, Vertex p2, SweepLineStatus sls) {
		sls.connections.add(new Edge(p1, p2));
	}

	public static void handleStartVertex(Vertex event, SweepLineStatus sls) {
		// add edge to next vertex into tree
		sls.edgeTree.currentY = event.y;
		event.edge = new Edge(event, event.nextVertex);
		;
		event.edge.helper = event;
		sls.edgeTree.insert(event.edge);
	}

	public static void handleRegularVertex(Vertex event, SweepLineStatus sls) {
		if (!isAreaLeftOfVertex(event)) {
			// connect if its a merge vertex
			if (event.prevVertex.edge.helper.type == VertexType.MERGE) {
				addConnection(event, event.prevVertex.edge.helper, sls);
			}
			// remove edge from tree as it is finished now
			sls.edgeTree.currentY = event.y;
			sls.edgeTree.remove(event.prevVertex.edge);
			// add the new edge to the tree
			event.edge = new Edge(event, event.nextVertex);
			event.edge.helper = event;
			sls.edgeTree.insert(event.edge);
		} else {
			sls.edgeTree.currentY = event.y;
			Edge toTheLeft = sls.edgeTree.findToLeft(event);
			if (toTheLeft.helper.type == VertexType.MERGE) {
				addConnection(event, toTheLeft.helper, sls);
			}
			toTheLeft.helper = event;
		}
	}

	public static void handleSplitVertex(Vertex event, SweepLineStatus sls) {
		// Draw diagonal to helper of edge to the left
		sls.edgeTree.currentY = event.y;
		Edge edgeToTheLeft = sls.edgeTree.findToLeft(event);
		addConnection(event, edgeToTheLeft.helper, sls);
		// set event as new helper of edge to the left
		edgeToTheLeft.helper = event;
		// add new edge
		event.edge = new Edge(event, event.nextVertex);
		event.edge.helper = event;
		sls.edgeTree.insert(event.edge);

	}

	public static void handleMergeVertex(Vertex event, SweepLineStatus sls) {
		// make diagonal to helper of prev edge if its a merge vertex
		if (event.prevVertex.edge.helper.type == VertexType.MERGE) {
			addConnection(event, event.prevVertex.edge.helper, sls);
		}
		// remove that edge from the tree because that edge is now finished
		sls.edgeTree.currentY = event.y;
		sls.edgeTree.remove(event.prevVertex.edge);
		Edge toTheLeft = sls.edgeTree.findToLeft(event);
		if (toTheLeft.helper.type == VertexType.MERGE) {
			addConnection(event, toTheLeft.helper, sls);
		}
		toTheLeft.helper = event;
	}

	public static void handleEndVertex(Vertex event, SweepLineStatus sls) {
		// make diagonal if helper of prev edge is merge vertex
		if (event.prevVertex.edge.helper.type == VertexType.MERGE) {
			addConnection(event, event.prevVertex.edge.helper, sls);
		}
		sls.edgeTree.currentY = event.y;
		// remove edge from tree as this edge is now finished
		sls.edgeTree.remove(event.prevVertex.edge);
	}

	// TODO ske this function does not work
	public static boolean isClockwise(Polygon polygon) {
		long sum = 0;
		Vertex currP, nextP;
		for (int i = 0; i < polygon.points.length; i++) {
			currP = polygon.points[i];
			// vector product
			nextP = polygon.points[(i + 1) % polygon.points.length];
			sum += (nextP.x - currP.x) * (nextP.y + currP.y);
		}

		System.out.println(polygon.toString() + " is clockwise: " + sum + " " + (sum > 0));
		return sum > 0;

	}

	/**
	 * Returns true if the area of the polygon lies to the left of currV. Caution:
	 * Can only be used for regular vertices
	 */
	public static boolean isAreaLeftOfVertex(Vertex currV) {
		Vertex prevV = currV.prevVertex;
		assert currV.type == VertexType.REGULAR;
		if (prevV.y < currV.y || (prevV.y == currV.y && prevV.x > currV.x)) {
			return true;
		} else {
			return false;
		}
	}
}
