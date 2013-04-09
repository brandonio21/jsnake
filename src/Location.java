
public class Location {

	private int x;
	private int y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void MoveDirection(int dir)
	{
		int xAdd = Direction.getXAdd(dir);
		int yAdd = Direction.getYAdd(dir);
		this.x += xAdd;
		this.y += yAdd;
	}

	@Override
	public int hashCode() {
		return this.x + this.y;
	}

	@Override
	public boolean equals(Object obj) {
		Location compareLoc = (Location)obj;
		return (compareLoc.x == this.x && compareLoc.y == this.y);
	}
	
}
