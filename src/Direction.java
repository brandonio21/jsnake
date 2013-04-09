
public class Direction {
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	
	public static int getXAdd(int dir)
	{
		if (dir == UP)
			return 0;
		if (dir == DOWN)
			return 0;
		if (dir == RIGHT)
			return 1;
		if (dir == LEFT)
			return -1;
		
		return 0;
	}
	
	public static int getYAdd(int dir)
	{
		if (dir == UP)
			return -1;
		if (dir == DOWN)
			return 1;
		if (dir == RIGHT)
			return 0;
		if (dir == LEFT)
			return 0;
		
		return 0;
	}
	
	public static double degreeImageRotation(int dir)
	{
		if (dir == UP)
			return 0;
		if (dir == DOWN)
			return 180.0;
		if (dir == RIGHT)
			return 90.0;
		if (dir == LEFT)
			return 270.0;
		
		return 0;
	}
}
