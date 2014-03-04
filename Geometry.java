import java.util.List;

/**
 * Encapsulates geometric calculations.
 * 
 * @author Johannes
 * 
 */
public final class Geometry {

	static final double PI_HALF = Math.PI / 2;

	public static double euclideanDist(DoublePoint p1, DoublePoint p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
				+ Math.pow(p1.getY() - p2.getY(), 2));
	}

	public static Double euclideanDist(BytePoint p1, BytePoint p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
				+ Math.pow(p1.getY() - p2.getY(), 2));
	}

	public static double euclideanDist(DoublePoint p1, BytePoint p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
				+ Math.pow(p1.getY() - p2.getY(), 2));
	}

	public static double euclideanDist(BytePoint p, byte x, byte y) {
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
	}

	/**
	 * Gets the distance of a point to a line. Please note that the line is
	 * assumed to be limited. So this is not the distance of a point to a
	 * straight. Please also note that the distance is signed (can be negative)
	 * to determine on which side of the line the given point is.
	 * 
	 * @param p
	 * @param line
	 * @return
	 */
	public static double getDistance(BytePoint p, ByteLine line) {
		return getDistance(p.toDoublePoint(), line);
	}

	/**
	 * Gets the distance of a point to a line. Please note that the line is
	 * assumed to be limited. So this is not the distance of a point to a
	 * straight. Please also note that the distance is signed (can be negative)
	 * to determine on which side of the line the given point is.
	 * 
	 * @param p
	 * @param line
	 * @return
	 */
	public static double getDistance(DoublePoint p, ByteLine l) {
		// Calculate the side lenghts of the triangle that is given by the three
		// points.
		double a = euclideanDist(p, l.getP1());
		double b = euclideanDist(p, l.getP2());
		double c = euclideanDist(l.getP1(), l.getP2());

		// Calculate the angles of that triangle
		// Due to rounding errors this values could become 1.00000004 or
		// -1.0000004
		// Which would lead to NaN passing them into acos
		double betaCos = (b * b - a * a - c * c) / (-2 * a * c);
		betaCos = Math.max(-1., Math.min(1., betaCos));
		double alphaCos = (a * a - b * b - c * c) / (-2 * b * c);
		alphaCos = Math.max(-1, Math.min(1., alphaCos));

		// Alpha and beta are the angles between the line base from either one
		// of the line endpoints and the point whose distance should be found
		double alpha = Math.acos(alphaCos);
		double beta = Math.acos(betaCos);

		// Calculate the (signed) distance to a straight
		double numerator = (l.getP2().getX() - l.getP1().getX())
				* (l.getP1().getY() - p.getY()) - (l.getP1().getX() - p.getX())
				* (l.getP2().getY() - l.getP1().getY());
		double denominator = Math.sqrt(Math.pow(l.getP2().getX()
				- l.getP1().getX(), 2)
				+ Math.pow(l.getP2().getY() - l.getP1().getY(), 2));
		double signedDistance = numerator / denominator;

		if (alpha > PI_HALF || beta > PI_HALF) {
			// One of the angles is bigger than 90 deg, so the distance is not
			// the distance to the straight but the minimum of the distances to
			// either one of the line endpoints.
			// SignedDistance had to be calculated anyway to keep the correct
			// sign in the result.
			return signedDistance > 0 ? Math.min(a, b) : -1 * Math.min(a, b);
		} else {
			// None of the angles is bigger than 90 deg, so the distance is just
			// the distance to a straight.
			return signedDistance;
		}
	}

	/**
	 * Gets the minimum of the distances from p to any of the lines in ls.
	 * 
	 * @param p
	 * @param ls
	 * @return
	 */
	public static double getDistance(DoublePoint p, List<ByteLine> ls) {
		double min = Double.MAX_VALUE;
		for (ByteLine l : ls) {
			double dist = getDistance(p, l);
			if (Math.abs(dist) < Math.abs(min)) {
				min = dist;
			}
		}
		return min;
	}

}
