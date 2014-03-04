import java.util.Random;

/**
 * Constant values used throughout the program.
 * 
 * @author Johannes
 * 
 */
public class Constants {

	/**
	 * Set this to false before you deploy to the mindstorm. If true you can run
	 * the whole thing locally to check if a path is found.
	 */
	public static final boolean LOCAL = true;

	/**
	 * Verbose logging
	 */
	public static final boolean VERBOSE = true;

	/**
	 * Use precalculated path to speedup actual execution on MindStorm. You have
	 * to provide the lines by hand in Main.java!
	 */
	public static final boolean USE_PRECALCULATED_PATH = false;

	/**
	 * Random, used for particle filter, movement noise etc.
	 */
	public static Random RND = new Random();

	/**
	 * Minimum distance to walls and obstacles (1 ^= 5cm)
	 */
	public static final int MIN_DISTANCE = 4;

	/**
	 * The number of particles used for the particle filter. The higher this
	 * value is, the longer it will take the robot to resample between
	 * movements.
	 */
	public static final int NO_OF_PARTICLES = 50;

	/**
	 * If the distance of the estimated position of the robot to the goal is
	 * smaller than this, the program terminates. (1 ^= 5cm)
	 */
	public static final double GOAL_TOLERANCE = 5;

	/**
	 * Douglas Peucker reduces the number of points by generating a path where
	 * no point that is left out has a greater distance to the reduced path than
	 * this value.
	 */
	public static final int DOUGLAS_PEUCKER_TOLERANCE = MIN_DISTANCE / 4;

	/**
	 * Constants for PidController.java
	 * 
	 * @author Johannes
	 * 
	 */
	public class PidConstants {
		/**
		 * In my experiments something the ratios between P I and D where always
		 * something like P_VALUE * 7.5 = D_VALUE; P_VALUE = 40 * I_VALUE
		 */
		public static final double P_VALUE = 0.13;
		public static final double I_VALUE = 0.004;

		/**
		 * You'll probably need a relatively high D value since a steering in
		 * the past influences the future very much (Change of heading)
		 */
		public static final double D_VALUE = 1.0;

		/**
		 * The result of the usual PID value is multiplied with this after
		 * rounding to only generate valid steering angles (Multiples of
		 * VALUE_FACTOR). If you change this value, you have to also adapt P, I
		 * and D values.
		 */
		public static final long VALUE_FACTOR = 5;
	}

	/**
	 * Constants for ParticleFilter.java
	 * 
	 * @author Johannes
	 * 
	 */
	public class ParticleFilter {

		/**
		 * Error value with highest probability.
		 */
		public static final double ERROR_MEAN = 0.;

		/**
		 * Standard deviation of the gaussian that is used to estimate the
		 * weight of a particle.
		 */
		public static final double ERROR_SDEV = 100.;

	}
}
