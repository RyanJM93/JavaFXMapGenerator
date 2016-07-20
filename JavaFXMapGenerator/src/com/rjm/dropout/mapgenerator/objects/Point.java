package com.rjm.dropout.mapgenerator.objects;

public class Point {
	public int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		Point p = (Point) o;
		return p.x == x && p.y == y;
	}

	@Override
	public int hashCode() {
		int hashCode = 19;
		int hc = x;
		hashCode = hashCode * 31 + hc;
		hc = y;
		hashCode = hashCode * 31 + hc;
		return hc;
	}
}
