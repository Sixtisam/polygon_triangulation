package net.skeeks.efalg.poly_tria;

import java.awt.Color;

public enum VertexType {
	START(Color.CYAN), 
	END(Color.GREEN), 
	SPLIT(Color.RED), 
	MERGE(Color.ORANGE), 
	REGULAR(Color.DARK_GRAY);
	
	
	public final Color color;
	private VertexType(Color color) {
		this.color = color;
	}
}
