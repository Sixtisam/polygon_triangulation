package net.skeeks.efalg.poly_tria;

import java.awt.Color;
import java.util.ArrayList;

/**
 * A face represents an area enclosed by edges
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 */
public class Face {

	/**
	 * An arbitrary edge which borders this face. Can be used to find all edge enclosing this edge.
	 */
	public HalfEdge edge;
	
	public boolean hole = false;
	
	public Color color;
	
	public static int i = 0;
	public static final Color[] COLORS = {
		Color.GREEN,
		Color.RED,
		Color.YELLOW,
		Color.BLACK,
		Color.ORANGE,
		Color.DARK_GRAY,
		Color.PINK,
		Color.LIGHT_GRAY,
		Color.MAGENTA,
		Color.BLUE,
		Color.CYAN,
	};
	
	public Face(boolean hole){
		this.hole = hole;
		this.color = COLORS[Math.floorMod(i++, COLORS.length)];
	}
	
	@Override
	public String toString() {
		ArrayList<String> vertices = new ArrayList<>();
		HalfEdge curr = edge;
		do {
			vertices.add(curr.from.toString());
			curr = curr.next;
		} while(curr != edge);
		vertices.add(curr.from.toString());
		return String.join(" -> ", vertices.toArray(new String[0])) + " Color: " + color;
	}
}
