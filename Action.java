/**
 * Action that is performed by the robot. It is defined by its duration and
 * steering angle
 * 
 * @author Johannes
 * 
 */
public class Action {

	double duration;
	Angle steering;

	public Action(double duration, Angle steering) {
		this.duration = duration;
		this.steering = steering;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public Angle getSteering() {
		return steering;
	}

	public void setSteering(Angle steering) {
		this.steering = steering;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((steering == null) ? 0 : steering.hashCode());
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
		Action other = (Action) obj;
		if (Double.doubleToLongBits(duration) != Double
				.doubleToLongBits(other.duration))
			return false;
		if (steering == null) {
			if (other.steering != null)
				return false;
		} else if (!steering.equals(other.steering))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Action [duration=");
		builder.append(duration);
		builder.append(", steering=");
		builder.append(steering);
		builder.append("]");
		return builder.toString();
	}

}
