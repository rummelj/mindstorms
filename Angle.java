/**
 * Angle with easy conversion between degrees and radians.
 * 
 * @author Johannes
 * 
 */
public class Angle {

	/**
	 * Internall the angle is only stored as degrees and converted to radians if
	 * necessary.
	 */
	double degree;

	/**
	 * Shortcut factory method.
	 * 
	 * @param value
	 * @return
	 */
	public static Angle newDeg(double value) {
		return new Angle(value, AngleType.DEGREE);
	}

	/**
	 * Shortcut factory method.
	 * 
	 * @param value
	 * @return
	 */
	public static Angle newRad(double value) {
		return new Angle(value, AngleType.RADIANS);
	}

	/**
	 * Initialises angle with 0 degrees.
	 */
	public Angle() {
		this.degree = .0;
	}

	public Angle(double value, AngleType type) {
		set(value, type);
	}

	public Angle(Angle other) {
		degree = other.degree;
	}

	public double get(AngleType type) {
		switch (type) {
		case DEGREE:
			return degree;
		case RADIANS:
			return Math.toRadians(degree);
		default:
			throw new IllegalArgumentException("type");
		}
	}

	public void set(double value, AngleType type) {
		switch (type) {
		case DEGREE:
			this.degree = value;
			break;
		case RADIANS:
			this.degree = Math.toDegrees(value);
			break;
		default:
			throw new IllegalArgumentException("type");
		}
	}

	/**
	 * Adds the other angle to this angle
	 * 
	 * @param other
	 */
	public void add(Angle other) {
		this.degree += other.get(AngleType.DEGREE);
		normalize();
	}

	/**
	 * Subtracts the other angle from this angle
	 * 
	 * @param other
	 */
	public void sub(Angle other) {
		this.degree -= other.get(AngleType.DEGREE);
		normalize();
	}

	void normalize() {
		while (this.degree < -180) {
			this.degree += 360;
		}
		while (this.degree > 180) {
			this.degree -= 360;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(degree);
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
		Angle other = (Angle) obj;
		if (Double.doubleToLongBits(degree) != Double
				.doubleToLongBits(other.degree))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Angle [degree=");
		builder.append(degree);
		builder.append("]");
		return builder.toString();
	}

}
