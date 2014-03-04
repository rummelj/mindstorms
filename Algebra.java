/**
 * Bundles algebraic calculations.
 * 
 * @author Johannes
 * 
 */
public final class Algebra {

	/**
	 * Used for gaussian calculation.
	 */
	static final double rootTwoPi = Math.sqrt(2 * Math.PI);

	/**
	 * Value of the given gaussian at position value.
	 * 
	 * @param mean
	 * @param sDev
	 * @param value
	 * @return
	 */
	public static double gaussian(double mean, double sDev, double value) {
		return (1. / (sDev * rootTwoPi))
				* Math.exp(-0.5 * Math.pow((value - mean) / sDev, 2));
	}

	/**
	 * Returns a random value distributed by the given gaussian.
	 * 
	 * @param mean
	 * @param sDev
	 * @return
	 */
	public static double nextGaussian(double mean, double sDev) {
		return (Constants.RND.nextGaussian() + mean) * sDev;
	}

	/**
	 * Normalises a probabilitiy distribution. If all probabilities are 0., a
	 * uniform distribution is assigned.
	 * 
	 * @param probabilities
	 *            InOut parameter
	 */
	public static void normalize(double[] probabilities) {
		double sum = 0.;
		for (double prob : probabilities) {
			sum += prob;
		}
		if (sum > 0) {
			for (int i = 0; i < probabilities.length; i++) {
				probabilities[i] = probabilities[i] / sum;
			}
		} else {
			double uniformValue = 1. / probabilities.length;
			for (int i = 0; i < probabilities.length; i++) {
				probabilities[i] = uniformValue;
			}
		}
	}

	/**
	 * Allows finding minimum of more than two values (unlike in Math.min)
	 * 
	 * @param values
	 * @return
	 */
	public static double min(double... values) {
		double min = Double.MAX_VALUE;
		for (double value : values) {
			if (min > value) {
				min = value;
			}
		}
		return min;
	}

	/**
	 * Allows to find maximum of more than two values (unlike Math.max)
	 * 
	 * @param values
	 * @return
	 */
	public static double max(double... values) {
		double max = Double.MIN_VALUE;
		for (double value : values) {
			if (max < value) {
				max = value;
			}
		}
		return max;
	}
}
