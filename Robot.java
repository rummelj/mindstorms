import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

/**
 * Encapsulates the physical robot. Since there is assumed to be only one such
 * robot, this class is static.
 * 
 * @author Johannes
 * 
 */
public final class Robot {

	/**
	 * Do not increase to 900 (which is the maximum with 9 Volts) since when
	 * steering this might be increased.
	 */
	static final int BASE_SPEED = 600;

	// Motors
	static final NXTRegulatedMotor MOTOR_STEERING = Motor.B;
	static final NXTRegulatedMotor MOTOR_RIGHT = Motor.A;
	static final NXTRegulatedMotor MOTOR_LEFT = Motor.C;

	// Sensors
	static final UltrasonicSensor SENSOR_FRONT = new UltrasonicSensor(
			SensorPort.S1);
	static final UltrasonicSensor SENSOR_BACK = new UltrasonicSensor(
			SensorPort.S2);

	/**
	 * How many measures are done from each ultrasonic sensor to average them.
	 */
	static final int MEASURES = 5;
	/**
	 * How many milliseconds does the program wait between each of the MEASURES
	 * measures of the ultrasonic sensors.
	 */
	static final int MILIS_BETWEEN_MEASURES = 20;

	/**
	 * Current steering of the robot. It is assumed that the steering initially
	 * is in a neutral position.
	 */
	static int steering = 0;

	public static void executeAction(Action action) {
		int degrees = (int) action.getSteering().get(AngleType.DEGREE);
		steer(degrees);
		move(degrees, action.getDuration());
	}

	static void steer(int degrees) {
		// Steer to neutral position first
		steerOffset(0);
		// Then to desired position
		steerOffset(degrees);
	}

	@SuppressWarnings("deprecation")
	private static void steerOffset(int degrees) {
		MOTOR_STEERING.setSpeed(600);
		MOTOR_STEERING.rotate(steering - degrees);
		MOTOR_STEERING.lock(100);
		steering = degrees;
		// Wait for the steering to finish
		Delay.msDelay(250);
	}

	private static void move(int degrees, double length) {
		if (length != RobotKnowledge.MOVE_LENGTH_ASSUMPTION) {
			throw new IllegalArgumentException(
					"Only having knowledge about moves that have velocity = "
							+ RobotKnowledge.MOVE_LENGTH_ASSUMPTION);
		}

		// Calculate wheelspeeds based on turning radius. The inner wheel has to
		// turn slower than the outer wheel.
		int speedLeft = (int) (BASE_SPEED * (1 - degrees * 0.006));
		int speedRight = (int) (BASE_SPEED * (1 + degrees * 0.006));
		MOTOR_LEFT.setSpeed(speedLeft);
		MOTOR_RIGHT.setSpeed(speedRight);

		// Move motors for length milliseconds
		MOTOR_LEFT.backward();
		MOTOR_RIGHT.forward();
		Delay.msDelay((long) length);

		// Stop both motors at the same time. Immediate return for the first
		// call is required.
		MOTOR_LEFT.stop(true);
		MOTOR_RIGHT.stop();
	}

	/**
	 * Get the average measure of the front ultrasonic sensor in centimeters.
	 * 
	 * @return
	 */
	public static int measureFront() {
		int sum = 0;
		for (int i = 0; i < MEASURES; i++) {
			sum += SENSOR_FRONT.getDistance();
			Delay.msDelay(MILIS_BETWEEN_MEASURES);
		}
		return sum / MEASURES;
	}

	/**
	 * Get the average measure of the back ultrasonic sensor in centimeters.
	 * 
	 * @return
	 */
	public static int measureBack() {
		int sum = 0;
		for (int i = 0; i < MEASURES; i++) {
			sum += SENSOR_BACK.getDistance();
			Delay.msDelay(MILIS_BETWEEN_MEASURES);
		}
		return sum / MEASURES;
	}

}
