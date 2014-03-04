/**
 * Represents a point given by its x and y coordinates, which are double. Please
 * note that DoublePoint.java and BytePoint.java do not use generics to prevent
 * the necessity to use boxed types.
 * 
 * @author Johannes
 * 
 */
public class DoublePoint {

	double x;
	double y;

	public DoublePoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public DoublePoint(DoublePoint other) {
		this.x = other.getX();
		this.y = other.getY();
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

	public BytePoint toPoint() {
		return new BytePoint((byte) x, (byte) y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoublePoint other = (DoublePoint) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
