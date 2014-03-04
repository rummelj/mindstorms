import java.util.List;

/**
 * Pid controller
 * 
 * @author Johannes
 * 
 */
public final class PidController {

	/**
	 * Accumulated errors
	 */
	private double integral;

	/**
	 * Previous measurement
	 */
	private double previousError;

	/**
	 * Determines what action should be done in order to stick to the line.
	 * 
	 * @param referenceCar
	 *            The estimated position of the robot
	 * @param lines
	 *            The path to stick to
	 * @return Action to perform
	 */
	public Action control(Car referenceCar, List<ByteLine> lines) {
		// Measure distance to trace
		double measure = Geometry.getDistance(referenceCar.getState()
				.getPosition(), lines);

		// Calculate (signed) difference between current and last measurement
		double derivative = measure - previousError;

		// Update fields
		previousError = measure;
		integral += measure;

		// Calculate PID value
		double result = (Constants.PidConstants.P_VALUE * measure)
				+ (Constants.PidConstants.I_VALUE * integral)
				+ (Constants.PidConstants.D_VALUE * derivative);

		// Round
		result = Math.round(result) * Constants.PidConstants.VALUE_FACTOR;
		// Keep in interval [-MAX_STEERING, MAX_STEERING]
		result = Math.min(result, RobotKnowledge.MAX_STEERING_DEG);
		result = Math.max(result, -RobotKnowledge.MAX_STEERING_DEG);
		return new Action(RobotKnowledge.MOVE_LENGTH_ASSUMPTION, new Angle(
				result, AngleType.DEGREE));
	}
}
