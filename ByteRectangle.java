/**
 * Represents a rectangle with two BytePoints (corners)
 * 
 * @author Johannes
 * 
 */
public class ByteRectangle {

	/**
	 * Corner
	 */
	BytePoint a;

	/**
	 * Corner
	 */
	BytePoint b;

	public ByteRectangle(BytePoint a, BytePoint b) {
		super();
		this.a = a;
		this.b = b;
	}

	/**
	 * Checks if the given point is in the rectangle.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isIn(BytePoint p) {
		return isIn(p.toDoublePoint());
	}

	/**
	 * Checks if the given point is in the rectangle.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isIn(DoublePoint p) {
		return (p.getX() >= a.getX() && p.getX() <= b.getX() || p.getX() <= a
				.getX() && p.getX() >= b.getX())
				&& (p.getY() >= a.getY() && p.getY() <= b.getY() || p.getY() <= a
						.getY() && p.getY() >= b.getY());
	}

	/**
	 * Returns the corner that has the smallest x and y coordinates.
	 * 
	 * @return
	 */
	public BytePoint getLowerLeftCorner() {
		if (a.getX() < b.getX() && a.getY() < b.getY()) {
			return a;
		} else if (a.getX() < b.getX() && a.getY() >= b.getY()) {
			return new BytePoint(a.getX(), b.getY());
		} else if (a.getX() >= b.getX() && a.getY() < b.getY()) {
			return new BytePoint(b.getX(), a.getY());
		} else {
			return b;
		}
	}

	/**
	 * Returns the corner that has the biggest x and y coordinates
	 * 
	 * @return
	 */
	public BytePoint getUpperRightCorner() {
		if (b.getX() < a.getX() && b.getY() < a.getY()) {
			return a;
		} else if (b.getX() < a.getX() && b.getY() >= a.getY()) {
			return new BytePoint(a.getX(), b.getY());
		} else if (b.getX() >= a.getX() && b.getY() < a.getY()) {
			return new BytePoint(b.getX(), a.getY());
		} else {
			return b;
		}
	}

	public double getWidth() {
		return Math.abs(a.getX() - b.getX());
	}

	public double getHeight() {
		return Math.abs(a.getY() - b.getY());
	}

	public BytePoint getA() {
		return a;
	}

	public void setA(BytePoint a) {
		this.a = a;
	}

	public BytePoint getB() {
		return b;
	}

	public void setB(BytePoint b) {
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		ByteRectangle other = (ByteRectangle) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

}
