/**
 * Represents a point given by its x and y coordinates, which are byte. Please
 * note that DoublePoint.java and Point.java do not use generics to prevent the
 * necessity to use boxed types.
 * 
 * @author Johannes
 * 
 */
public class BytePoint {

	byte x;
	byte y;

	public BytePoint(byte x, byte y) {
		super();
		this.x = x;
		this.y = y;
	}

	public BytePoint(BytePoint other) {
		this.x = other.getX();
		this.y = other.getY();
	}

	public byte getX() {
		return x;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public byte getY() {
		return y;
	}

	public void setY(byte y) {
		this.y = y;
	}

	public DoublePoint toDoublePoint() {
		return new DoublePoint(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		BytePoint other = (BytePoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
