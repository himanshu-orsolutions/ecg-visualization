package application.models;

/**
 * The model Coordinate. It holds a co-ordinate information.
 */
public class Coordinate {

	private double x;
	private double y;

	public Coordinate(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
