import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/opensciencemap/VectorTileMap/blob/master/TileMapApp/src/
 * org/osmdroid/utils/DouglasPeuckerReducer.java
 * 
 * Implements Douglas Peucker algorithm, that reduces the number of points in a
 * trace.
 * 
 * @author Johannes
 * 
 */
public final class DouglasPeucker {

	/**
	 * See en.wikipedia.org/wiki/Douglas-Peucker_algorithm
	 * @param points
	 *            If less than 3 points, the algorithm cannot reduce anything.
	 * @param epsilon
	 *            Must be >= 0
	 * @return
	 */
	public static List<BytePoint> reduce(List<BytePoint> points, double epsilon) {
		int n = points.size();
		if (epsilon <= 0 || n < 3) {
			return points;
		}

		boolean[] marked = new boolean[n];
		marked[0] = marked[n - 1] = true;

		reduceRec(points, marked, epsilon, 0, n - 1);

		List<BytePoint> result = new ArrayList<BytePoint>(n);
		for (int i = 0; i < n; i++) {
			if (marked[i]) {
				result.add(points.get(i));
			}
		}
		return result;
	}

	private static void reduceRec(List<BytePoint> points, boolean[] marked,
			double epsilon, int first, int last) {
		if (last <= first + 1) {
			return;
		}

		double maxDistance = -1.;
		int maxIndex = 0;

		ByteLine line = new ByteLine(points.get(first), points.get(last));
		for (int i = first + 1; i < last; i++) {
			BytePoint current = points.get(i);
			double distance = Math.abs(Geometry.getDistance(current, line));
			if (distance > maxDistance) {
				maxDistance = distance;
				maxIndex = i;
			}
		}

		if (maxDistance > epsilon) {
			marked[maxIndex] = true;
			reduceRec(points, marked, epsilon, first, maxIndex);
			reduceRec(points, marked, epsilon, maxIndex, last);
		}
	}

}
