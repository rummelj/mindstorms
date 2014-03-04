import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lejos.util.Delay;

/**
 * Main method (also executed by the mindstorm)
 * 
 * @author Johannes
 * 
 */
public class Main {

	public static void main(String[] args) {
		// Wrap any exception so you have a chance to view the latest output
		// before the exception occured
		try {
			runProgram();
		} catch (Exception e) {
			s("Exception occured");
			if (!Constants.LOCAL) {
				// Give the user some time to read
				Delay.msDelay(10000);
			}
			throw e;
		}
	}

	public static void runProgram() {
		s("Initialising surrounding");
		Surrounding surrounding = Surrounding.initSurrounding();
		s("Calculating path");
		List<ByteLine> lines = getPath(surrounding);
		s("Initialising particle filter");
		Particles particles = new Particles(surrounding);
		ParticleFilter particleFilter = new ParticleFilter(particles);
		s("Initialising virtual sensor");
		UltrasonicSensor sensor = new UltrasonicSensor(surrounding);

		// Keep reference to referenceCar to not always trigger a calculation
		// when accessing it
		Car referenceCar = particleFilter.getReferenceCar();
		PidController pidController = new PidController();
		// Execute main loop
		while (!goalReached(surrounding, referenceCar)) {
			// Log where the robot currently thinks it is
			sv("Believe");
			s("X = " + Math.round(referenceCar.getState().getPosition().getX()));
			s("Y = " + Math.round(referenceCar.getState().getPosition().getY()));
			s("T = "
					+ Math.round(referenceCar.getState().getHeading()
							.get(AngleType.DEGREE)));

			Action action = pidController.control(referenceCar, lines);
			executeAction(action);

			sv("Updating particles");
			particleFilter.update(action);

			// If the code is executed locally, the reference car is used to get
			// the "actual" sensor data. Therefore the reference car needs to be
			// calculated an additional time.
			// The reference car is not needed by resample if the code is
			// executed on the mindstorm because then the sensor data comes from
			// the actual sensors
			if (Constants.LOCAL) {
				referenceCar = particleFilter.getReferenceCar();
			}
			sv("Resampling");
			particleFilter.resample(referenceCar, sensor);
			sv("Calculating new belief");
			referenceCar = particleFilter.getReferenceCar();
		}

		s("Reached goal, terminating...");
		if (!Constants.LOCAL) {
			Delay.msDelay(10000);
		}
	}

	public static void executeAction(Action action) {
		if (!Constants.LOCAL) {
			Robot.executeAction(action);
		} else {
			s(action);
		}
	}

	private static boolean goalReached(Surrounding surrounding, Car referenceCar) {
		return Geometry.euclideanDist(referenceCar.getState().getPosition(),
				surrounding.getGoal()) < Constants.GOAL_TOLERANCE;
	}

	private static List<ByteLine> getPath(Surrounding surrounding) {
		if (!Constants.USE_PRECALCULATED_PATH) {
			sv("Computing path");
			return computePath(surrounding);
		} else {
			sv("Using precalculated path");
			return getPreCalculatedPath();
		}
	}

	private static List<ByteLine> getPreCalculatedPath() {
		// TODO: Run the program locally and put the result after Douglas
		// Peucker here.
		List<ByteLine> lines = new LinkedList<ByteLine>();
		lines.add(new ByteLine(new BytePoint((byte) 28, (byte) 8),
				new BytePoint((byte) 19, (byte) 9)));
		lines.add(new ByteLine(new BytePoint((byte) 19, (byte) 9),
				new BytePoint((byte) 18, (byte) 57)));
		lines.add(new ByteLine(new BytePoint((byte) 18, (byte) 57),
				new BytePoint((byte) 19, (byte) 65)));
		lines.add(new ByteLine(new BytePoint((byte) 19, (byte) 65),
				new BytePoint((byte) 28, (byte) 74)));
		lines.add(new ByteLine(new BytePoint((byte) 28, (byte) 74),
				new BytePoint((byte) 30, (byte) 79)));
		lines.add(new ByteLine(new BytePoint((byte) 30, (byte) 79),
				new BytePoint((byte) 26, (byte) 85)));
		lines.add(new ByteLine(new BytePoint((byte) 26, (byte) 85),
				new BytePoint((byte) 18, (byte) 87)));
		lines.add(new ByteLine(new BytePoint((byte) 18, (byte) 87),
				new BytePoint((byte) 16, (byte) 95)));
		lines.add(new ByteLine(new BytePoint((byte) 16, (byte) 95),
				new BytePoint((byte) 9, (byte) 99)));
		lines.add(new ByteLine(new BytePoint((byte) 9, (byte) 99),
				new BytePoint((byte) 8, (byte) 104)));
		lines.add(new ByteLine(new BytePoint((byte) 8, (byte) 104),
				new BytePoint((byte) 6, (byte) 106)));
		return lines;
	}

	private static List<ByteLine> computePath(Surrounding surrounding) {
		List<BytePoint> points = aStar(surrounding);
		sv("A*");
		sv(points);
		points = douglasPeucker(points);
		sv("Douglas Peucker");
		sv(points);
		return linify(points);
	}

	private static List<ByteLine> linify(List<BytePoint> points) {
		List<ByteLine> lines = new ArrayList<ByteLine>(points.size() - 1);
		for (int i = 1; i < points.size(); i++) {
			lines.add(new ByteLine(points.get(i - 1), points.get(i)));
		}
		return lines;
	}

	private static List<BytePoint> douglasPeucker(List<BytePoint> points) {
		points = DouglasPeucker.reduce(points,
				Math.max(Constants.DOUGLAS_PEUCKER_TOLERANCE, 1));
		return points;
	}

	private static List<BytePoint> aStar(Surrounding surrounding) {
		List<BytePoint> points = AStar.compute(surrounding,
				Constants.MIN_DISTANCE);
		if (points == null) {
			throw new IllegalArgumentException("Couldn't find path!");
		}
		return points;
	}

	/**
	 * Logs a list of points verbosely
	 * 
	 * @param list
	 */
	private static void sv(List<BytePoint> list) {
		for (BytePoint o : list) {
			sv(o);
		}
	}

	/**
	 * Verbose log
	 * 
	 * @param o
	 */
	private static void sv(Object o) {
		if (Constants.VERBOSE) {
			System.out.println(o.toString());
		}
	}

	/**
	 * Important / nonverbose log
	 * 
	 * @param o
	 */
	private static void s(Object o) {
		System.out.println(o.toString());
	}
}
