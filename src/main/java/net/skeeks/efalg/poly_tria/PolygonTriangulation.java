package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class PolygonTriangulation {

	public static void triangulate(Polygon[] polygons) {
		List<Vertex> eventQueue = init(polygons);
		
		for(int i = 0; i < eventQueue.size(); i++) {
			Vertex curr = eventQueue.get(i);
		}
	}

	public static List<Vertex> init(Polygon[] polygons) {
		ArrayList<Vertex> events = new ArrayList<>();
		for (int i = 0; i < polygons.length; i++) {
			Polygon currP = polygons[i];
			for (int v = 0; v < currP.length(); v++) {
				Vertex currV = currP.points[v];
				Vertex nextV = currP.points[(v + 1) % currP.points.length];
				Vertex prevV = currP.points[Math.floorMod((v - 1), currP.points.length)];
				categorizePoint(currV, nextV, prevV);
				events.add(currV);
			}
		}
		Collections.sort(events, (a,b) -> {
			int result = Integer.compare(b.y, a.y);
			if(result == 0) result = Integer.compare(a.x, b.x);
			return result;
		});
		return events;
	}

	public static void categorizePoint(Vertex currV, Vertex nextV, Vertex prevV) {
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

	public static boolean isClockwise(Polygon polygon) {
		int sum = 0;
		Vertex currP, nextP;
		for (int i = 0; i < polygon.points.length; i++) {
			currP = polygon.points[i];
			// vector product
			nextP = polygon.points[(i + 1) % polygon.points.length];
			sum += (nextP.x - currP.x) * (nextP.y + currP.y);
		}
		// last
		currP = polygon.points[polygon.points.length - 1];
		;
		nextP = polygon.points[0];
		// vector product
		sum += (nextP.x - currP.x) * (nextP.y - currP.y);
		return sum > 0;

	}

	public static void sweepLinePhase() {

	}
	
	
	/**
	 * Returns true if the area of the polygon lies to the left of currV.
	 * Caution: Can only be used for regular vertices
	 */
	public static boolean isAreaLeftOfVertex(Vertex currV, Vertex nextV, Vertex prevV) {
		assert currV.type == VertexType.REGULAR;
		if(prevV.y < currV.y || (prevV.y == currV.y && prevV.x > currV.x)) {
			return true;
		} else {
			return false;
		}
	}
}
