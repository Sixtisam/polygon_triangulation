// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.awt.Color;

/**
 * The different types of a vertex
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public enum VertexType {
	START(Color.CYAN), END(Color.GREEN), SPLIT(Color.RED), MERGE(Color.ORANGE), REGULAR(Color.DARK_GRAY);

	public final Color color;

	private VertexType(Color color) {
		this.color = color;
	}
}
