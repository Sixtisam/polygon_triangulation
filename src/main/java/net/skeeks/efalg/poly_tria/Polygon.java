package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.skeeks.efalg.poly_tria.Common.DPoint;
import net.skeeks.efalg.poly_tria.Common.Line;

public class Polygon {
	public Vertex[] vertices;
	public ArrayList<Polygon> holes = new ArrayList<>();

	public Polygon(Vertex[] points) {
		this.vertices = points;
	}
	
	public void addHole(Polygon hole) {
		holes.add(hole);
	}
	
	public static final Random RAND = new Random();
	
	public boolean contains(Polygon other) {
		int CDx = RAND.nextInt(1000) + 1_000;
		int CDy = RAND.nextInt(1000) + 50_000;
		
		Vertex otherVertex = other.vertices[RAND.nextInt(other.vertices.length)];
		int intersections = 0;
		Vertex prev = vertices[vertices.length - 1];
		for(int i = 0; i < vertices.length; i++){
			Vertex current = vertices[i];
			intersections += checkIntersection(prev.x, prev.y, current.x, current.y, otherVertex.x, otherVertex.y, CDx, CDy);
			prev = current;
		}
		
		
		return intersections % 2 == 1;
	}
	
	public boolean isClockwise() {
		long sum = 0;
		Vertex currP, nextP;
		for (int i = 0; i < vertices.length; i++) {
			currP = vertices[i];
			// vector product
			nextP = vertices[(i + 1) % vertices.length];
			sum += (nextP.x - currP.x) * (nextP.y + currP.y);
		}

		return sum > 0;

	}

	@Override
	public String toString() {
		return String.join(" -> ", Arrays.stream(vertices).map(Vertex::toString).toArray(l -> new String[l]));
	}

	public int length() {
		return vertices.length;
	}
	
	public static final double EPSILON = Math.pow(10, -7);
	
	/**
	 * Returns one if (A->B) intersects with C + Beta(CD)
	 */
    public static int checkIntersection(
    		int Ax, int Ay, // A
    		int Bx, int By, // B
    		int Cx, int Cy, // C (the point to check)
    		int CDx, int CDy) /* D */ {
    	
    	/// A->B
    	int ABx = Bx - Ax;
    	int ABy = By - Ay;
    	// C->D already given
    	
        int det = ABx*(-CDy) - ABy*(-CDx);

        if (det == 0) {
            return 0;
        }

        // matrix multiplication
        int alphaBeforeD = (-CDy*(Cx - Ax)) + (CDx*(Cy-Ay));
        int betaBeforeD = (-ABy*(Cx-Ax)) + (ABx*(Cy-Ay));

        // apply the det
        double alpha = alphaBeforeD / ((double) det);
        double beta = betaBeforeD / ((double) det);
        
        if(alpha >= -EPSILON && alpha <= 1.0 + EPSILON && beta >= -EPSILON) {
        	return 1;
        } else {
        	return 0;
        }
    }
}
