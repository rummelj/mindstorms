/**
 * Implements the particle filter.
 * 
 * @author Johannes
 * 
 */
public class ParticleFilter {

	/**
	 * Particles used by the particle filter.
	 */
	Particles particles;

	public ParticleFilter(Particles particles) {
		this.particles = particles;
	}

	/**
	 * Get the car that represents the current set of particles best. (Note:
	 * This is a weighted average and does not necessarily have to be a particle
	 * itself)
	 * 
	 * @return
	 */
	public Car getReferenceCar() {
		double x = 0.;
		double y = 0.;
		double heading = 0.;
		for (int i = 0; i < Constants.NO_OF_PARTICLES; i++) {
			State particleState = particles.getCar(i).getState();
			double particleProbability = particles.getProbability(i);
			x += particleState.getPosition().getX() * particleProbability;
			y += particleState.getPosition().getY() * particleProbability;
			heading += particleState.getHeading().get(AngleType.RADIANS)
					* particleProbability;
		}
		return new Car(new State(new DoublePoint(x, y), new Angle(heading,
				AngleType.RADIANS)));
	}

	/**
	 * Updates the particles by performing action to all of them
	 * 
	 * @param action
	 */
	public void update(Action action) {
		for (Car car : particles.getCars()) {
			car.perform(action);
		}
	}

	/**
	 * Resamples the particles by duplicating ones that have high probability
	 * and dropping ones that have low probability, while keeping their total
	 * count.
	 * 
	 * @param referenceCar
	 *            This is only used if executed locally.
	 * @param sensor
	 */
	public void resample(Car referenceCar, UltrasonicSensor sensor) {
		// Get (actual) sensor data
		int actualFront = 0;
		int actualBack = 0;
		if (!Constants.LOCAL) {
			actualFront = Robot.measureFront();
			actualBack = Robot.measureBack();
		} else {
			actualFront = sensor.measureFront(referenceCar);
			actualBack = sensor.measureBack(referenceCar);
		}

		// Calculate weight of each particle by comparing the actual sensor
		// measurements with the ones that every particle yields.
		double[] weight = new double[Constants.NO_OF_PARTICLES];
		for (int i = 0; i < Constants.NO_OF_PARTICLES; i++) {
			int believedFront = sensor.measureFront(particles.getCar(i));
			int believedBack = sensor.measureBack(particles.getCar(i));
			int errorFront = Math.abs(believedFront - actualFront);
			int errorBack = Math.abs(believedBack - actualBack);
			weight[i] = Algebra.gaussian(Constants.ParticleFilter.ERROR_MEAN,
					Constants.ParticleFilter.ERROR_SDEV, errorFront
							* errorFront + errorBack * errorBack);
		}
		Algebra.normalize(weight);

		// Accumulate weights
		double[] cumulativeErrors = new double[Constants.NO_OF_PARTICLES];
		double sum = 0;
		for (int i = 0; i < weight.length; i++) {
			cumulativeErrors[i] = sum + weight[i];
			sum += weight[i];
		}

		// Select new particles by generating a random value and searching for
		// that value in cumulativeErrors
		Car[] newCars = new Car[Constants.NO_OF_PARTICLES];
		for (int i = 0; i < Constants.NO_OF_PARTICLES; i++) {
			double searchFor = Constants.RND.nextDouble();
			int index = -1;
			while (index < Constants.NO_OF_PARTICLES - 1
					&& cumulativeErrors[++index] < searchFor) {
			}
			newCars[i] = new Car(particles.getCar(index));
			particles.setProbability(i, weight[index]);
		}

		// Update particle probabilities (Used to calculate referenceCar)
		particles.normalizeProbabilities();
		// Update cars
		for (int i = 0; i < Constants.NO_OF_PARTICLES; i++) {
			particles.setCar(i, newCars[i]);
		}
	}
}
