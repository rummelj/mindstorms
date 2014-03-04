/**
 * Represents a line specified by two double points. Please note that
 * DoubleLine.java and ByteLine.java do not use generics to prevent the necessity to
 * use boxed types.
 * 
 * @author Johannes
 * 
 */
public class DoubleLine {

	DoublePoint p1;
	DoublePoint p2;

	public DoubleLine(DoublePoint p1, DoublePoint p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
	}

	public DoublePoint getP1() {
		return p1;
	}

	public void setP1(DoublePoint p1) {
		this.p1 = p1;
	}

	public DoublePoint getP2() {
		return p2;
	}

	public void setP2(DoublePoint p2) {
		this.p2 = p2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
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
		DoubleLine other = (DoubleLine) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Line [p1=");
		builder.append(p1);
		builder.append(", p2=");
		builder.append(p2);
		builder.append("]");
		return builder.toString();
	}

}
