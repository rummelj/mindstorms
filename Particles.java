/**
 * Wrapper class for particles to easily access their representation (cars) and
 * probabilities.
 * 
 * @author Johannes
 * 
 */
public class Particles {

	private Car[] cars;
	private double[] probabilities;

	/**
	 * Initialises the particles
	 * 
	 * @param surrounding
	 * @param initialHeading
	 */
	public Particles(Surrounding surrounding) {
		cars = new Car[Constants.NO_OF_PARTICLES];
		probabilities = new double[Constants.NO_OF_PARTICLES];
		for (int i = 0; i < Constants.NO_OF_PARTICLES; i++) {
			cars[i] = new Car(new State(surrounding.getStart().toDoublePoint(),
					surrounding.getInitialHeading()));
			// Uniformly distribute initial particles
			probabilities[i] = 1. / Constants.NO_OF_PARTICLES;
		}
	}

	public Car getCar(int i) {
		if (i < 0 || i >= cars.length) {
			throw new IllegalArgumentException("No such car " + i);
		}
		return cars[i];
	}

	public double getProbability(int i) {
		if (i < 0 || i >= probabilities.length) {
			throw new IllegalArgumentException("No such probability " + i);
		}
		return probabilities[i];
	}

	/**
	 * 
	 * @return Reference
	 */
	public Car[] getCars() {
		return cars;
	}

	/**
	 * 
	 * @return Reference
	 */
	public double[] getProbabilities() {
		return probabilities;
	}

	public void setProbability(int i, double probability) {
		probabilities[i] = probability;
	}

	public void normalizeProbabilities() {
		Algebra.normalize(probabilities);
	}

	/**
	 * Reference semantic.
	 * 
	 * @param i
	 * @param car
	 */
	public void setCar(int i, Car car) {
		cars[i] = car;
	}

}
