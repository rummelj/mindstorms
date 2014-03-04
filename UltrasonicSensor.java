import java.util.List;

/**
 * Simulates an ultrasonic sensor that works on the internal representation of
 * the robot.
 * 
 * @author Johannes
 * 
 */
public class UltrasonicSensor {

	/**
	 * What is measured if no obstacle is detected? 1^=5cm
	 */
	static final int NOTHING_RECOGNIZED_MEASURE = 51;

	private Surrounding surrounding;

	public UltrasonicSensor(Surrounding surrounding) {
		this.surrounding = surrounding;
	}

	/**
	 * Determines what the front sensor would measure given the state of
	 * reference car and the given surrounding.
	 * 
	 * @param referenceCar
	 * @return
	 */
	public int measureFront(Car referenceCar) {
		List<DoubleLine> sensorLines = RobotKnowledge
				.getFrontSensorLines(referenceCar);
		return sense(sensorLines, referenceCar);
	}

	/**
	 * Determines what the back sensor would measure given the state of
	 * reference car and the given surrounding.
	 * 
	 * @param referenceCar
	 * @return
	 */
	public int measureBack(Car referenceCar) {
		List<DoubleLine> sensorLines = RobotKnowledge
				.getBackSensorLines(referenceCar);
		return sense(sensorLines, referenceCar);
	}

	/**
	 * Sends out rays by further and further offsetting points until they hit
	 * something.
	 * 
	 * @param sensorLines
	 * @return
	 */
	int sense(List<DoubleLine> sensorLines, Car referenceCar) {
		// Minimum measure of all sensor lines
		double min = RobotKnowledge.getMaxMeasure();
		DoublePoint referenceCarPosition = referenceCar.getState()
				.getPosition();
		double minDistance = surrounding.distanceToNonPassableArea(
				(byte) referenceCarPosition.getX(),
				(byte) referenceCarPosition.getY());
		for (DoubleLine sensorLine : sensorLines) {
			// Get offset values
			double dx = sensorLine.getP2().getX() - sensorLine.getP1().getX();
			double dy = sensorLine.getP2().getY() - sensorLine.getP1().getY();
			// The ratio of dx and dy is preserved, but either dx or dy (the
			// greater one) is normalized to 1 or -1
			if (Math.abs(dx) > Math.abs(dy)) {
				dy = dy / Math.abs(dx);
				dx = dx > 0 ? 1. : -1.;
			} else {
				dx = dx / Math.abs(dy);
				dy = dy > 0 ? 1. : -1.;
			}

			// Point that is send out and further and further ofsetted by dx and
			// dy
			DoublePoint current = new DoublePoint(sensorLine.getP2());
			// Start with minDistance
			double oneStepDistance = Math.sqrt(dx * dx + dy * dy);
			current.setX(current.getX() + (minDistance / oneStepDistance) * dx);
			current.setY(current.getY() + (minDistance / oneStepDistance) * dy);
			do {
				current.setX(current.getX() + dx);
				current.setY(current.getY() + dy);
			} while (surrounding.isIn(current.toPoint())
					&& Geometry.euclideanDist(sensorLine.getP2(), current) < RobotKnowledge
							.getMaxMeasure());

			double distance = Geometry.euclideanDist(sensorLine.getP2(),
					current);
			if (distance >= RobotKnowledge.getMaxMeasure()) {
				distance = NOTHING_RECOGNIZED_MEASURE;
			}
			if (distance < min) {
				min = distance;
			}
		}
		// Convert to actual centimeters to be comparable with actual
		// measurement.
		return RobotKnowledge.getMeasureFront(min);
	}

}
