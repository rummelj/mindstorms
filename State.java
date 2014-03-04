/**
 * Encapsulates a state of a car, given by its position (x and y coordinates)
 * and its heading direction.
 * 
 * @author Johannes
 * 
 */
public class State {

	DoublePoint position;
	Angle heading;

	public State(DoublePoint position, Angle heading) {
		super();
		this.position = position;
		this.heading = heading;
	}

	public State(State other) {
		this.position = new DoublePoint(other.getPosition());
		this.heading = new Angle(other.getHeading());
	}

	public DoublePoint getPosition() {
		return position;
	}

	public void setPosition(DoublePoint position) {
		this.position = position;
	}

	public Angle getHeading() {
		return heading;
	}

	public void setHeading(Angle heading) {
		this.heading = heading;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((heading == null) ? 0 : heading.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
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
		State other = (State) obj;
		if (heading == null) {
			if (other.heading != null)
				return false;
		} else if (!heading.equals(other.heading))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("State [position=");
		builder.append(position);
		builder.append(", heading=");
		builder.append(heading);
		builder.append("]");
		return builder.toString();
	}

}
