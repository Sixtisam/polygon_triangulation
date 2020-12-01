// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria;

import net.skeeks.efalg.poly_tria.core.Vertex;

/**
 * This class is only not directly used by the algorithm, instead its the data structure to visualize the progress
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class Edge {
	public Vertex from;
	public Vertex to;
	
	public Edge(Vertex from, Vertex to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public String toString() {
		return from.toString() +  " -> " + to.toString();
	}
}
