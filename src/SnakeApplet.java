import java.applet.Applet;
import java.awt.Graphics;


public class SnakeApplet extends Applet{

	private SnakeCanvas c;
	
	public void init()
	{
		c = new SnakeCanvas();
		c.setVisible(true);
		c.setFocusable(true);
		this.add(c);
		this.setVisible(true);
		
		this.setSize(c.getPreferredSize());
	}
	
	public void paint(Graphics g)
	{
		this.setSize(c.getPreferredSize());
	}
}
