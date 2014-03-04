import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper for a two dimensional boolean array. Please note, that BooleanMap and
 * ByteMap do not use generics because this would force you to use boxed types
 * which require more space than their primitive equivalents.
 * 
 * @author Johannes
 * 
 */
public class BooleanMap {

	boolean[][] map;
	byte width;
	byte height;

	public BooleanMap(byte width, byte height) {
		super();
		this.width = width;
		this.height = height;
		this.map = new boolean[width][height];
	}

	public BooleanMap(BooleanMap other) {
		super();
		this.width = other.width;
		this.height = other.height;
		this.map = new boolean[width][height];
		for (byte x = 0; x < map.length; x++) {
			for (byte y = 0; y < map[x].length; y++) {
				map[x][y] = other.map[x][y];
			}
		}
	}

	public void set(byte x, byte y, boolean b) {
		if (isValid(x, y)) {
			map[x][y] = b;
		}
	}

	/**
	 * Checks if (x,y) is a valid index of this map
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isValid(byte x, byte y) {
		return !(x < 0 || y < 0 || x >= width || y >= height);
	}

	public boolean get(byte x, byte y) {
		return map[x][y];
	}

	/**
	 * Sets all values that are false to true and all that are true to false.
	 */
	public void invert() {
		for (byte x = 0; x < map.length; x++) {
			for (byte y = 0; y < map[x].length; y++) {
				map[x][y] = !map[x][y];
			}
		}
	}

	/**
	 * Checks if all values in the map are equal to b
	 * 
	 * @param b
	 * @return
	 */
	public boolean all(boolean b) {
		for (byte x = 0; x < map.length; x++) {
			for (byte y = 0; y < map[x].length; y++) {
				if (map[x][y] != b) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets all values to b.
	 * 
	 * @param b
	 */
	public void setAll(boolean b) {
		for (byte x = 0; x < map.length; x++) {
			for (byte y = 0; y < map[x].length; y++) {
				map[x][y] = b;
			}
		}
	}

	/**
	 * Returns all indices that have value b.
	 * 
	 * @param b
	 * @return
	 */
	public List<BytePoint> getAll(boolean b) {
		List<BytePoint> result = new LinkedList<BytePoint>();
		for (byte x = 0; x < map.length; x++) {
			for (byte y = 0; y < map[x].length; y++) {
				if (map[x][y] == b) {
					result.add(new BytePoint(x, y));
				}
			}
		}
		return result;
	}

	public byte getWidth() {
		return width;
	}

	public void setWidth(byte width) {
		this.width = width;
	}

	public byte getHeight() {
		return height;
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				builder.append(map[x][y] ? "X" : ".");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

}
