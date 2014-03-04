import java.util.LinkedList;
import java.util.List;

/**
 * A Star graph search algorithm
 * 
 * @author Johannes
 * 
 */
public final class AStar {

	/**
	 * Runs a star search with euclidean distance as heuristics function. Only
	 * points that are valid in the given surrounding and that have at least
	 * minDistance to any wall or obstacle are considered.
	 * 
	 * @param surrounding
	 * @param minDistance
	 * @return Path or null, if no path found
	 */
	public static List<BytePoint> compute(Surrounding surrounding, int minDistance) {
		// Shortcuts
		BytePoint start = surrounding.getStart();
		BytePoint goal = surrounding.getGoal();

		BooleanMap closedSet = surrounding.toBooleanMap();
		BooleanMap openSet = surrounding.toBooleanMap();
		// Set nothing but start as open
		openSet.setAll(false);
		openSet.set(start.getX(), start.getY(), true);

		// This is done to save up space. Instead of having a Point[][] array,
		// that tells you from where each point was reached, the same
		// information can be stored in two ByteMaps one containing the x and
		// one the y coordinate from where the cell was reached.
		ByteMap cameFromX = new ByteMap(closedSet.getWidth(),
				closedSet.getHeight());
		ByteMap cameFromY = new ByteMap(closedSet.getWidth(),
				closedSet.getHeight());
		cameFromX.setAll(Byte.MIN_VALUE);
		cameFromY.setAll(Byte.MIN_VALUE);

		ByteMap gScores = new ByteMap(closedSet.getWidth(),
				closedSet.getHeight());
		ByteMap fScores = new ByteMap(closedSet.getWidth(),
				closedSet.getHeight());
		gScores.setAll(Byte.MIN_VALUE);
		fScores.setAll(Byte.MIN_VALUE);

		// Start has gScore 0
		// TODO: This is all not very safe, since there might be overflows that
		// are unchecked.
		// If anything goes wrong here take that in mind. F scores are
		// "simulated" as unsigned by subtracting Byte.MIN_VALUE to allow paths
		// of length up to 254, but there could still be overflows on certain
		// maps that require very serpentine like paths. Also there
		// could be overflows in gScore if a distance is greater than 128 for
		// instance on a map of size 128x20
		gScores.set(start.getX(), start.getY(), (byte) 0);
		fScores.set(
				start.getX(),
				start.getY(),
				(byte) (Geometry.euclideanDist(start, goal).byteValue() - Byte.MIN_VALUE));

		while (!openSet.all(false)) {
			BytePoint current = findMinimum(openSet, fScores);
			if (current == null) {
				break;
			} else if (current.equals(goal)) {
				return reconstructPath(cameFromX, cameFromY, goal);
			}

			openSet.set(current.getX(), current.getY(), false);
			closedSet.set(current.getX(), current.getY(), true);

			for (byte neighborX = (byte) (current.getX() - 1); neighborX <= current
					.getX() + 1; neighborX++) {
				for (byte neighborY = (byte) (current.getY() - 1); neighborY <= current
						.getY() + 1; neighborY++) {
					// Check if neighbor is applicable
					if ((neighborX != current.getX() || neighborY != current
							.getY())
							&& surrounding.isIn(neighborX, neighborY)
							&& surrounding.distanceToNonPassableArea(neighborX,
									neighborY) > minDistance
							&& !closedSet.get(neighborX, neighborY)) {

						// Diagonal moves have cost 2
						byte tentativeGScore = (byte) (gScores.get(
								current.getX(), current.getY()) + (current
								.getX() != neighborX
								&& current.getY() != neighborY ? 2 : 1));

						// If neighbor is not already in openSet or has a better
						// gScore offset
						if (!openSet.get(neighborX, neighborY)
								|| tentativeGScore < gScores.get(neighborX,
										neighborY)) {

							// Set came from
							cameFromX.set(neighborX, neighborY,
									(byte) current.getX());
							cameFromY.set(neighborX, neighborY,
									(byte) current.getY());

							// Set new gScore and fScore
							gScores.set(neighborX, neighborY, tentativeGScore);
							fScores.set(
									neighborX,
									neighborY,
									(byte) (tentativeGScore
											+ Geometry.euclideanDist(goal,
													neighborX, neighborY) - Byte.MIN_VALUE));

							// Add neighbor to openSet (if it was already there,
							// nothing is changed)
							openSet.set(neighborX, neighborY, true);
						}
					}
				}
			}
		}
		// No more positions in openSet, no path found
		return null;
	}

	private static BytePoint findMinimum(BooleanMap openSet, ByteMap fScores) {
		byte min = Byte.MAX_VALUE;
		BytePoint minPoint = null;
		for (BytePoint p : openSet.getAll(true)) {
			if (fScores.get(p.getX(), p.getY()) > Byte.MIN_VALUE
					&& fScores.get(p.getX(), p.getY()) < min) {
				min = fScores.get(p.getX(), p.getY());
				minPoint = p;
			}
		}
		return minPoint;
	}

	private static List<BytePoint> reconstructPath(ByteMap cameFromX,
			ByteMap cameFromY, BytePoint current) {
		if (cameFromX.get(current.getX(), current.getY()) != Byte.MIN_VALUE
				&& cameFromY.get(current.getX(), current.getY()) != Byte.MIN_VALUE) {
			List<BytePoint> sub = reconstructPath(cameFromX, cameFromY,
					new BytePoint(cameFromX.get(current.getX(), current.getY()),
							cameFromY.get(current.getX(), current.getY())));
			sub.add(current);
			return sub;
		} else {
			List<BytePoint> result = new LinkedList<BytePoint>();
			result.add(current);
			return result;
		}

	}
}
