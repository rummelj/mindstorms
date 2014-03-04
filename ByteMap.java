import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper for a two dimensional byte array. Please note, that BooleanMap and
 * ByteMap do not use generics because this would force you to use boxed types
 * which require more space than their primitive equivalents.
 * 
 * @author Johannes
 * 
 */
public class ByteMap {

	byte[][] map;
	int width;
	int height;

	public ByteMap(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.map = new byte[width][height];
	}

	public ByteMap(ByteMap other) {
		super();
		this.width = other.width;
		this.height = other.height;
		this.map = new byte[width][height];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				map[x][y] = other.map[x][y];
			}
		}
	}

	public void set(int x, int y, byte b) {
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
	public boolean isValid(int x, int y) {
		return !(x < 0 || y < 0 || x >= width || y >= height);
	}

	public byte get(int x, int y) {
		return map[x][y];
	}

	/**
	 * Checks if all values in the map are equal to b
	 * 
	 * @param b
	 * @return
	 */
	public boolean all(byte b) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
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
	public void setAll(byte b) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				map[x][y] = b;
			}
		}
	}

	/**
	 * Returns all indices that have value b
	 * 
	 * @param b
	 * @return
	 */
	public List<BytePoint> getAll(byte b) {
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ByteMap [map=");
		builder.append(Arrays.toString(map));
		builder.append(", width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append("]");
		return builder.toString();
	}

}
