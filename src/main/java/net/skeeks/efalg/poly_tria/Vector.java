// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria;

public class Vector {
	public int x;
	public int y;

	public Vector(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double distance() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector reverse() {
		return new Vector(-x, -y);
	}
}
