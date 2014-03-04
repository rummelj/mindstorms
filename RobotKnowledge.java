import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Holds knowledge about the robot dynamics.
 * 
 * @author Johannes
 * 
 */
public final class RobotKnowledge {

	/**
	 * Every move that is known by the robot was executed with an action having
	 * this length.
	 */
	public static final double MOVE_LENGTH_ASSUMPTION = 1000;

	/**
	 * The robot can steer in the interval of [-MAX_STEERING_DEG;
	 * MAX_STEERING_DEG]
	 */
	static final int MAX_STEERING_DEG = 50;

	/**
	 * The HashMap implementation of NXJ is buggy. It doesn't allow negative
	 * values so every value is configured to be positive by adding
	 * degreeModifier everywhere and subtracting it again when retrieved from
	 * the map
	 * **/
	static final int DEGREE_MODIFIER = 200;

	/**
	 * Every action has noise and these are their means and standard deviations
	 * in X and Y direction and also for the resulting heading degree.
	 */
	static final double X_MOVE_NOISE_MEAN = 0.;
	static final double X_MOVE_NOISE_S_DEV = 2.;
	static final double Y_MOVE_NOISE_MEAN = 0.;
	static final double Y_MOVE_NOISE_S_DEV = 2.;
	static final double THETA_TURN_NOISE_MEAN = 0.;
	// Math.PI / 30 ^= 6 deg
	static final double THETA_TURN_NOISE_S_DEV = Math.PI / 30;

	/**
	 * To simulate the measurement of the ultrasonic sensors. Rays are send out
	 * from the ultrasonic sensor position until they hit a wall or obstacle
	 * with angles (-SECTOR_DEGREES / 2, -SECTOR_DEGREES / 2 + RESOLUTION,
	 * -SECTOR_DEGREES / 2 + 2 * RESOLUTION, ..., SECTOR_DEGREES / 2)
	 */
	static final int FRONT_SENSOR_SECTOR_DEGREES = 30;
	static final int FRONT_SENSOR_SECTOR_RESOLUTION = 20;
	static final int BACK_SENSOR_SECTOR_DEGREES = 30;
	static final int BACK_SENSOR_SECTOR_RESOLUTION = 20;

	/**
	 * The steering of the robot is very sloppy. E.g. if you steer to 25 degree
	 * you might actually end up with a steering of 30 or 20 degree. These are
	 * the probabilities for such a sloppy steering.
	 */
	static final double PROB_OVERSTEER_FIVE_DEGREES = 0.125;
	static final double PROB_UNDERSTEER_FIVE_DEGREES = 0.125;

	/**
	 * Offsets of the mountpoints of the ultrasonic sensor. E.g. if you change
	 * the x coordinates to something else than 0, it is assumed that the
	 * ultrasonic sensor does not exactly point in the same direction as the
	 * robot. This way you could also put the ultrasonic sensors to the side of
	 * the robot.
	 */
	static final DoublePoint FRONT_SENSOR_MOUNT_OFFSET = new DoublePoint(0., 1);
	static final DoublePoint BACK_SENSOR_MOUNT_OFFSET = new DoublePoint(0.,
			-5.4);

	/**
	 * Knows the coordinate offsets after an action taking
	 * MOVE_LENGTH_ASSUMPTION time and the steering angle given in the key.
	 */
	static Map<Integer, DoublePoint> positionKnowledge = new HashMap<Integer, DoublePoint>();

	/**
	 * Knows the offset in heading direction after an action taking
	 * MOVE_LENGTH_ASSUMPTION time and the steering angle given in the key.
	 */
	static Map<Integer, Double> headingKnowledge = new HashMap<Integer, Double>();
	static {
		// These values were measured by hand and don't have to be 100%
		// accurate. This is accommodated by the particle filter.
		// Please note that 1 ^= 5cm

		positionKnowledge.put(-50 + DEGREE_MODIFIER, new DoublePoint(3.8, 0.));
		positionKnowledge.put(-45 + DEGREE_MODIFIER, new DoublePoint(3.9, 0.7));
		positionKnowledge.put(-40 + DEGREE_MODIFIER, new DoublePoint(3.8, 1.3));
		positionKnowledge.put(-35 + DEGREE_MODIFIER, new DoublePoint(3.8, 1.7));
		positionKnowledge.put(-30 + DEGREE_MODIFIER, new DoublePoint(3., 2.9));
		positionKnowledge.put(-25 + DEGREE_MODIFIER, new DoublePoint(2.9, 2.8));
		positionKnowledge.put(-20 + DEGREE_MODIFIER, new DoublePoint(2.2, 3.3));
		positionKnowledge.put(-15 + DEGREE_MODIFIER, new DoublePoint(1.5, 3.6));
		positionKnowledge.put(-10 + DEGREE_MODIFIER, new DoublePoint(0.9, 3.8));
		positionKnowledge.put(-5 + DEGREE_MODIFIER, new DoublePoint(0.6, 5.3));
		positionKnowledge.put(0 + DEGREE_MODIFIER, new DoublePoint(0., 5.));

		positionKnowledge.put(5 + DEGREE_MODIFIER, new DoublePoint(-0.6, 5.3));
		positionKnowledge.put(10 + DEGREE_MODIFIER, new DoublePoint(-0.9, 3.8));
		positionKnowledge.put(15 + DEGREE_MODIFIER, new DoublePoint(-1.5, 3.6));
		positionKnowledge.put(20 + DEGREE_MODIFIER, new DoublePoint(-2.2, 3.3));
		positionKnowledge.put(25 + DEGREE_MODIFIER, new DoublePoint(-2.9, 2.8));
		positionKnowledge.put(30 + DEGREE_MODIFIER, new DoublePoint(-3., 2.9));
		positionKnowledge.put(35 + DEGREE_MODIFIER, new DoublePoint(-3.8, 1.7));
		positionKnowledge.put(40 + DEGREE_MODIFIER, new DoublePoint(-3.8, 1.3));
		positionKnowledge.put(45 + DEGREE_MODIFIER, new DoublePoint(-3.9, 0.7));
		positionKnowledge.put(50 + DEGREE_MODIFIER, new DoublePoint(-3.8, 0.));

		headingKnowledge.put(-50 + DEGREE_MODIFIER, 60.2);
		headingKnowledge.put(-45 + DEGREE_MODIFIER, 54.36);
		headingKnowledge.put(-40 + DEGREE_MODIFIER, 48.53);
		headingKnowledge.put(-35 + DEGREE_MODIFIER, 42.7);
		headingKnowledge.put(-30 + DEGREE_MODIFIER, 36.86);
		headingKnowledge.put(-25 + DEGREE_MODIFIER, 31.03);
		headingKnowledge.put(-20 + DEGREE_MODIFIER, 25.2);
		headingKnowledge.put(-15 + DEGREE_MODIFIER, 19.36);
		headingKnowledge.put(-10 + DEGREE_MODIFIER, 13.53);
		headingKnowledge.put(-5 + DEGREE_MODIFIER, 7.7);
		headingKnowledge.put(0 + DEGREE_MODIFIER, 0.);
		headingKnowledge.put(5 + DEGREE_MODIFIER, -7.7);
		headingKnowledge.put(10 + DEGREE_MODIFIER, -13.53);
		headingKnowledge.put(15 + DEGREE_MODIFIER, -19.36);
		headingKnowledge.put(20 + DEGREE_MODIFIER, -25.2);
		headingKnowledge.put(25 + DEGREE_MODIFIER, -31.03);
		headingKnowledge.put(30 + DEGREE_MODIFIER, -36.86);
		headingKnowledge.put(35 + DEGREE_MODIFIER, -42.7);
		headingKnowledge.put(40 + DEGREE_MODIFIER, -48.53);
		headingKnowledge.put(45 + DEGREE_MODIFIER, -54.36);
		headingKnowledge.put(50 + DEGREE_MODIFIER, -60.2);
	}

	/**
	 * Assumes duration of 1000ms
	 * 
	 * @param start
	 * @return
	 */
	public static State getStateAfterMove(Integer degrees, State start) {
		if (degrees % 5 != 0) {
			throw new IllegalArgumentException("degrees must be divisible by 5");
		}
		if (degrees > MAX_STEERING_DEG || degrees < -MAX_STEERING_DEG) {
			throw new IllegalArgumentException("degrees must be between "
					+ (-MAX_STEERING_DEG) + " and " + MAX_STEERING_DEG
					+ " degrees!");
		}
		degrees = applySloppySteering(degrees);

		// Rotate the known point around (0,0) by start.heading degrees
		DoublePoint known = new DoublePoint(positionKnowledge.get(degrees
				+ DEGREE_MODIFIER));
		double theta = start.getHeading().get(AngleType.RADIANS);
		double xNew = known.getX() * Math.cos(theta) + known.getY()
				* Math.sin(theta);
		double yNew = -known.getX() * Math.sin(theta) + known.getY()
				* Math.cos(theta);
		xNew += start.getPosition().getX();
		yNew += start.getPosition().getY();
		double newTheta = theta
				+ Math.toRadians(headingKnowledge
						.get(degrees + DEGREE_MODIFIER));

		// Add some noise
		xNew += Algebra.nextGaussian(X_MOVE_NOISE_MEAN, X_MOVE_NOISE_S_DEV);
		yNew += Algebra.nextGaussian(Y_MOVE_NOISE_MEAN, Y_MOVE_NOISE_S_DEV);
		newTheta += Algebra.nextGaussian(THETA_TURN_NOISE_MEAN,
				THETA_TURN_NOISE_S_DEV);

		return new State(new DoublePoint(xNew, yNew), new Angle(newTheta,
				AngleType.RADIANS));
	}

	static Integer applySloppySteering(Integer degrees) {
		// The steering is very noisy. In 10% of the cases it actually either
		// steers 5 degrees to much or too little
		double sloppySteering = Constants.RND.nextDouble();
		if (sloppySteering < PROB_UNDERSTEER_FIVE_DEGREES
				&& degrees < MAX_STEERING_DEG) {
			degrees += 5;
		}
		if (sloppySteering > 1. - PROB_OVERSTEER_FIVE_DEGREES
				&& degrees > -MAX_STEERING_DEG) {
			degrees -= 5;
		}
		return degrees;
	}

	/**
	 * Returns the ray lines send out by the front ultrasonic sensor. These
	 * should be interpreted as half straights extending over the second point
	 * of the line.
	 * 
	 * @param car
	 * @return
	 */
	public static List<DoubleLine> getFrontSensorLines(Car car) {
		DoublePoint base = car.getState().getPosition();
		List<DoubleLine> result = new LinkedList<DoubleLine>();
		for (int thetaOffset = -FRONT_SENSOR_SECTOR_DEGREES / 2; thetaOffset <= (FRONT_SENSOR_SECTOR_DEGREES / 2); thetaOffset += FRONT_SENSOR_SECTOR_RESOLUTION) {
			// Rotate mountpoint offset by ray offset + car heading angle
			double theta = car.getState().getHeading().get(AngleType.RADIANS)
					+ Math.toRadians(thetaOffset);
			double xNew = FRONT_SENSOR_MOUNT_OFFSET.getX() * Math.cos(theta)
					+ FRONT_SENSOR_MOUNT_OFFSET.getY() * Math.sin(theta);
			double yNew = -FRONT_SENSOR_MOUNT_OFFSET.getX() * Math.sin(theta)
					+ FRONT_SENSOR_MOUNT_OFFSET.getY() * Math.cos(theta);
			xNew += base.getX();
			yNew += base.getY();
			result.add(new DoubleLine(base, new DoublePoint(xNew, yNew)));
		}
		return result;
	}

	/**
	 * Returns the ray lines send out by the back ultrasonic sensor. These
	 * should be interpreted as half straights extending over the second point
	 * of the line.
	 * 
	 * @param car
	 * @return
	 */
	public static List<DoubleLine> getBackSensorLines(Car car) {
		DoublePoint base = car.getState().getPosition();
		List<DoubleLine> result = new LinkedList<DoubleLine>();
		for (int thetaOffset = -BACK_SENSOR_SECTOR_DEGREES / 2; thetaOffset <= (BACK_SENSOR_SECTOR_DEGREES / 2); thetaOffset += BACK_SENSOR_SECTOR_RESOLUTION) {
			// Rotate mountpoint offset by ray offset + car heading angle
			double theta = car.getState().getHeading().get(AngleType.RADIANS)
					+ Math.toRadians(thetaOffset);
			double xNew = BACK_SENSOR_MOUNT_OFFSET.getX() * Math.cos(theta)
					+ BACK_SENSOR_MOUNT_OFFSET.getY() * Math.sin(theta);
			double yNew = -BACK_SENSOR_MOUNT_OFFSET.getX() * Math.sin(theta)
					+ BACK_SENSOR_MOUNT_OFFSET.getY() * Math.cos(theta);
			xNew += base.getX();
			yNew += base.getY();
			result.add(new DoubleLine(base, new DoublePoint(xNew, yNew)));
		}
		return result;
	}

	/**
	 * Returns the maximum that the ultrasonic sensor can measure. 1 ^= 5cm
	 * 
	 * @return
	 */
	public static double getMaxMeasure() {
		return 40.;
	}

	/**
	 * Returns what the actual robot would measure given the distance determined
	 * from the internal map. Since the internal representation works with units
	 * of 5cm, but the actual robot measures actual centimeters, the value has
	 * to be multiplied by 5.
	 * 
	 * @param distance
	 * @return
	 */
	public static int getMeasureFront(double distance) {
		return (int) (Math.round(distance) * 5);
	}

	/**
	 * See getMeasureFront
	 * 
	 * @param distance
	 * @return
	 */
	public static int getMeasureBack(double distance) {
		return (int) (Math.round(distance) * 5);
	}
}
