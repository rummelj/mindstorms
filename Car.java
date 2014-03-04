/**
 * Represents the internal representation of the robot, which is used as a
 * particle.
 * 
 * @author Johannes
 * 
 */
public class Car {

	/**
	 * Position and Heading direction of the car.
	 */
	private State state;

	public Car(State state) {
		this.state = state;
	}

	public Car(Car car) {
		this.state = new State(car.state);
	}

	/**
	 * Updates the state to represent the effects of the action. Please note,
	 * that the execution is executed with noise specified in
	 * RobotKnowledge.java, so you will most probably get a different result
	 * each time you call this method.
	 * 
	 * @param action
	 */
	public void perform(Action action) {
		state = RobotKnowledge.getStateAfterMove((int) action.getSteering()
				.get(AngleType.DEGREE), state);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Car [state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
