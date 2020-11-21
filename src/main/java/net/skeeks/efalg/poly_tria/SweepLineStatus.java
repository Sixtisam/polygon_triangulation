package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SweepLineStatus {
	private Polygon polygon;
	public List<Vertex> events;
	public final EdgeSearchTree edgeTree  = new EdgeSearchTree();
	public List<Edge> connections = new ArrayList<>();

	public SweepLineStatus(Polygon polygon) {
		this.polygon = polygon;
	}

	public void init() {
		events = new ArrayList<>();
		for (int v = 0; v < polygon.length(); v++) {
			Vertex currV = polygon.points[v];
			Vertex nextV = polygon.points[(v + 1) % polygon.points.length];
			Vertex prevV = polygon.points[Math.floorMod((v - 1), polygon.points.length)];
			currV.prevVertex = prevV;
			currV.nextVertex = nextV;
			// TODO ske optimize: if no vertex is split or merge, we already have a monotone vertex
			categorizeVertex(currV, nextV, prevV);
			events.add(currV); 
		}
		Collections.sort(events, (a, b) -> {
			int result = Integer.compare(b.y, a.y);
			if (result == 0)
				result = Integer.compare(a.x, b.x);
			return result;
		});
	}

	public static void categorizeVertex(Vertex currV, Vertex nextV, Vertex prevV) {
		boolean isNextVBelow = currV.y > nextV.y || (currV.y == nextV.y && currV.x < nextV.x);
		boolean isPrevVBelow = currV.y > prevV.y || (currV.y == prevV.y && currV.x < prevV.x);

		if (isNextVBelow && isPrevVBelow) { // start oder split
			double angle = interiorAngle(currV, prevV, nextV);
			if (angle < Math.PI) {
				currV.type = VertexType.START;
			} else {
				currV.type = VertexType.SPLIT;
			}
		} else if (!isNextVBelow && !isPrevVBelow) { // end oder merge
			double angle = interiorAngle(currV, prevV, nextV);
			if (angle < Math.PI) {
				currV.type = VertexType.END;
			} else {
				currV.type = VertexType.MERGE;
			}
		} else { // regular
			currV.type = VertexType.REGULAR;
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
