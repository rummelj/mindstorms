import java.util.LinkedList;
import java.util.List;

/**
 * Represents the map for the robot.
 * 
 * @author Johannes
 * 
 */
public class Surrounding {

	/**
	 * Initialises a default surrounding. You might want to change this.
	 * 
	 * @return
	 */
	public static Surrounding initSurrounding() {
		// TODO Adapt to your setup.
		Surrounding surrounding = new Surrounding((byte) 37, (byte) 118);
		surrounding.addObstacle(new ByteRectangle(new BytePoint((byte) 23,
				(byte) 31), new BytePoint((byte) 28, (byte) 40)));
		surrounding.addObstacle(new ByteRectangle(new BytePoint((byte) 4,
				(byte) 86), new BytePoint((byte) 12, (byte) 93)));
		surrounding.addObstacle(new ByteRectangle(new BytePoint((byte) 17,
				(byte) 76), new BytePoint((byte) 24, (byte) 81)));
		surrounding.addObstacle(new ByteRectangle(new BytePoint((byte) 7,
				(byte) 58), new BytePoint((byte) 14, (byte) 65)));

		// Set start and goal positions
		surrounding.setStart(new BytePoint((byte) 28, (byte) 8));
		surrounding.setGoal(new BytePoint((byte) 6, (byte) 112));

		surrounding.setInitialHeading(Angle.newDeg(-90));

		return surrounding;
	}

	/**
	 * True where non passable area
	 */
	BooleanMap map;

	/**
	 * A (redundant) list of obstacles for easier distance calculations.
	 */
	List<ByteRectangle> obstacles;

	/**
	 * Initial position
	 */
	BytePoint start;

	/**
	 * Goal position
	 */
	BytePoint goal;

	/**
	 * Initial heading of the robot
	 */
	Angle initialHeading;

	/**
	 * Constructor. The area of the map may not be bigger than 5000 (height *
	 * width)
	 * 
	 * @param width
	 * @param height
	 */
	public Surrounding(byte width, byte height) {
		if (height * width > 5000) {
			throw new IllegalArgumentException(
					"NXT only has 64KB, the maximum area (height*width) for height and width is 2500.");
		}
		this.map = new BooleanMap(width, height);
		this.obstacles = new LinkedList<ByteRectangle>();
	}

	/**
	 * Adds an obstacle to the map.
	 * 
	 * @param obstacle
	 */
	public void addObstacle(ByteRectangle obstacle) {
		BytePoint lowLeft = obstacle.getLowerLeftCorner();
		BytePoint upRight = obstacle.getUpperRightCorner();
		byte xMin = lowLeft.getX();
		byte xMax = upRight.getX();
		byte yMin = lowLeft.getY();
		byte yMax = upRight.getY();
		for (byte x = xMin; x <= xMax; x++) {
			for (byte y = yMin; y <= yMax; y++) {
				map.set(x, y, true);
			}
		}
		this.obstacles.add(obstacle);
	}

	/**
	 * Gets the distance to the closest non passable area (wall or obstacle)
	 * 
	 * @param p
	 *            Point of origin
	 * @return Distance to closest non passable area (wall or obstacle)
	 */
	public double distanceToNonPassableArea(byte x, byte y) {
		return Math.min(distanceToObstacle(x, y), distanceToWall(x, y));
	}

	/**
	 * Checks if a point is inside the map and not in an obstacle
	 * 
	 * @param p
	 * @return true if the point is "empty" = in map && !(in obstacle)
	 */
	public boolean isIn(BytePoint p) {
		return isIn(p.getX(), p.getY());
	}

	/**
	 * Checks if a point is inside the map and not in an obstacle
	 * 
	 * @param x
	 * @param y
	 * @return true if the point is "empty" = in map && !(in obstacle)
	 */
	public boolean isIn(byte x, byte y) {
		return map.isValid(x, y) && !map.get(x, y);
	}

	/**
	 * Converts the map into a boolean map.
	 * 
	 * @return BooleanMap of the current map (copy)
	 */
	public BooleanMap toBooleanMap() {
		return new BooleanMap(map);
	}

	private double distanceToObstacle(byte x, byte y) {
		double min = Double.MAX_VALUE;
		for (ByteRectangle obstacle : obstacles) {
			double thisDistance = getDistance(x, y, obstacle);
			if (thisDistance < min) {
				min = thisDistance;
			}
		}
		return min;
	}

	private double getDistance(byte x, byte y, ByteRectangle obstacle) {
		BytePoint corner = obstacle.getLowerLeftCorner();
		double dx = 0.;
		if (!(x >= corner.getX() && x <= corner.getX() + obstacle.getWidth())) {
			// X coordinate lies outside [corner.x; corner.x + width]
			// p either lies right or left of the rectangle
			dx = Math.min(Math.abs(x - corner.getX()),
					Math.abs(x - (corner.getX() + obstacle.getWidth())));
		}
		double dy = 0.;
		if (!(y >= corner.getY() && y <= corner.getY() + obstacle.getHeight())) {
			// Y coordinate lies outside [corner.y; corner.y + height]
			// p ether lies above or below the rectangle
			dy = Math.min(Math.abs(y - corner.getY()),
					Math.abs(y - (corner.getY() + obstacle.getHeight())));
		}
		// Euclidean distance
		return Math.sqrt(dx * dx + dy * dy);
	}

	private double distanceToWall(byte x, byte y) {
		// The distance to a wall is the minimum of:
		// - The x distance to the left of the map = |p.x - map.minX| = |p.x -
		// 0| = |p.x|
		// - The x distance to the right of the map = |p.x - map.width|
		// - The y distance to the bottom of the map = |p.y - map.minY| = |p.y -
		// 0| = |p.y|
		// - The y distance to the top of the map = |p.y - map.height|
		return Algebra.min(Math.abs(x), Math.abs(x - map.getWidth()),
				Math.abs(y), Math.abs(y - map.getHeight()));
	}

	public BytePoint getStart() {
		return start;
	}

	public void setStart(BytePoint start) {
		this.start = start;
	}

	public BytePoint getGoal() {
		return goal;
	}

	public void setGoal(BytePoint goal) {
		this.goal = goal;
	}

	public Angle getInitialHeading() {
		return initialHeading;
	}

	public void setInitialHeading(Angle initialHeading) {
		this.initialHeading = initialHeading;
	}

}
