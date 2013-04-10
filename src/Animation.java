import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;


public class Animation {

	private Location loc;
	
	public Animation(Location l)
	{
		this.loc = l;
	}
	
	public void draw(Graphics2D g2)
	{
		
	}
	
	public void turn()
	{
		
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	
}

class Circle extends Animation
{
	private int width;
	private Color c;
	
	public Circle(Location l, Color c)
	{
		super(l);
		width = 0;
		this.c = c;
	}
	
	public Circle(Location l, Color c, int w)
	{
		super(l);
		width = w;
		this.c = c;
	}
	
	public void draw(Graphics2D g2)
	{
		Color oldColor = g2.getColor();
		g2.setColor(this.c);
		g2.draw(new Ellipse2D.Double(this.getLoc().getX() - (width / 2), this.getLoc().getY() - (width / 2), width, width));
		g2.setColor(oldColor);
	}
	
	public void turn()
	{
		width+=70;
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	
}
