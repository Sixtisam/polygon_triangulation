package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//This is a personal academic project. Dear PVS-Studio, please check it.

//PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

/**
 * Represents a planar polygon
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class Polygon {
	private static final int UPPER_BOUND_RND_VECTOR = 50_000;
	private static final int LOWER_BOUND_RND_VECTOR = 1_000;
	public Vertex[] vertices;
	/**
	 * All holes associated that are detected to be inside this polygon.
	 */
	public ArrayList<Polygon> holes = new ArrayList<>();

	public Polygon(Vertex[] points) {
		this.vertices = points;
	}

	public void addHole(Polygon hole) {
		holes.add(hole);
	}

	public static final Random RAND = new Random();

	/**
	 * @return true if the Polygon {@code other} is inside {@code this} polygon.
	 */
	public boolean contains(Polygon other) {
		// random vector
		int CDx = RAND.nextInt(UPPER_BOUND_RND_VECTOR) + LOWER_BOUND_RND_VECTOR;
		int CDy = RAND.nextInt(UPPER_BOUND_RND_VECTOR) + LOWER_BOUND_RND_VECTOR;

		// choose random vertices of the 'hole' to add more randomness (and thus
		// reducing likehood of error)
		Vertex otherVertex = other.vertices[RAND.nextInt(other.vertices.length)];
		int intersections = 0;
		Vertex prev = vertices[vertices.length - 1];
		// count with how many edges the random vector intersects
		for (int i = 0; i < vertices.length; i++) {
			Vertex current = vertices[i];
			intersections += checkIntersection(prev.x, prev.y, current.x, current.y, otherVertex.x, otherVertex.y, CDx,
					CDy);
			prev = current;
		}

		return intersections % 2 == 1;
	}

	/**
	 * 
	 * @return true if the vertices of this polygon are in clockwise-order
	 */
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

	public int length() {
		return vertices.length;
	}

	public static final double EPSILON = Math.pow(10, -7);

	/**
	 * @return 1 if (A->B) intersects with C + Beta(CD), 0 if not
	 */
	public static int checkIntersection(int Ax, int Ay, // A
			int Bx, int By, // B
			int Cx, int Cy, // C (the point to check)
			int CDx, int CDy) /* D */ {

		/// A->B
		int ABx = Bx - Ax;
		int ABy = By - Ay;
		// C->D already given

		int det = ABx * (-CDy) - ABy * (-CDx);

		if (det == 0) {
			return 0;
		}

		// matrix multiplication
		int alphaBeforeD = (-CDy * (Cx - Ax)) + (CDx * (Cy - Ay));
		int betaBeforeD = (-ABy * (Cx - Ax)) + (ABx * (Cy - Ay));

		// apply the det
		double alpha = alphaBeforeD / ((double) det);
		double beta = betaBeforeD / ((double) det);

		if (alpha >= -EPSILON && alpha <= 1.0 + EPSILON && beta >= -EPSILON) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return String.join(" -> ", Arrays.stream(vertices).map(Vertex::toString).toArray(l -> new String[l]));
	}
}
