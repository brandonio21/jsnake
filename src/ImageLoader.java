import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;


public class ImageLoader {

	public static Image getImage(String path)
	{
		Image image = null;
		try
		{
			URL imageURL = SnakeApplet.class.getResource(path);
			image = Toolkit.getDefaultToolkit().getImage(imageURL);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return image;
	}
}
