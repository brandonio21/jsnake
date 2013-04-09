import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//SNAKE -- A challenge to create a fully functional snake game in under two hours
//created by Brandon Milton, BrandonSoft Software Solutions
//http://brandonsoft.com
public class SnakeCanvas extends Canvas
implements Runnable, KeyListener{

	private Graphics bufferGraphics;
	private BufferStrategy bufferStrategy;
	
	private Thread gameThread;

	private final int GRID_SIZE = 50;
	private final int BOX_SIZE = 10;
	private final int STARTING_LENGTH = 20;
	private final int STARTING_SPEED = 100;
	
	private int level = 0;
	private int score = 0;
	private int speed = STARTING_SPEED;
	private int currentLength = STARTING_LENGTH;
	private int[][] grid;
	private ArrayList<Location> snakeLoc;
	private ArrayList<Integer> snakeDir;
	private HashMap<Location, Integer> turnPoints;
	private int currentDirection = Direction.UP;
	private Location dot;
	private boolean lost = false;
	private Image snakeBody;
	private Image snakeHead;
	private Image dotImage;
	
	public SnakeCanvas()
	{
		this.addKeyListener(this);
		grid = new int[GRID_SIZE][GRID_SIZE];
		snakeLoc = new ArrayList<Location>();
		snakeDir = new ArrayList<Integer>();
		turnPoints = new HashMap<Location, Integer>();
		this.setPreferredSize(new Dimension(640, 600));
		
		snakeBody = ImageLoader.getImage("images/body.png");
		snakeHead = ImageLoader.getImage("images/head.png");
		dotImage = ImageLoader.getImage("images/dot.png");
		
		if (gameThread == null)
			gameThread = new Thread(this);
		
		start();
	}
	public void init()
	{
		
	}
	public void paint(Graphics g)
	{
		if (bufferStrategy == null)
		{
			this.createBufferStrategy(2);
			bufferStrategy = this.getBufferStrategy();
		}
		try
		{
			gameThread.start();
		}
		catch (Exception e) {}
	}
	
	public void DoLogic()
	{
		if (lost)
			return;
		//Logic time!
		LoseCheck();
		
		//First, let us check to see if the score means it is time to level up
		if (score % 6 == 0 && score > 5)
		{
			if (speed > 10)
				speed -= 10;
			
			level++;
			score+=2;
		}
		
	
		
		//Let's detect if a snake is on a dot
		LoseCheck();
		DotCheck();
		
		
		
		//Now we need to move the snake
		for (int i = 0; i < currentLength; i++)
		{
			
			if (turnPoints.containsKey(snakeLoc.get(i)))
			{
				Location origLoc = new Location(snakeLoc.get(i).getX(), snakeLoc.get(i).getY());
				snakeDir.set(i, turnPoints.get(snakeLoc.get(i)));
				snakeLoc.get(i).MoveDirection(snakeDir.get(i));
				LoseCheck();
				DotCheck();
				//The block is currently on a turn point
				if (i >= currentLength - 1)
					turnPoints.remove(origLoc);
			}
			else
			{
				snakeLoc.get(i).MoveDirection(snakeDir.get(i));
				LoseCheck();
				DotCheck();
			}
			
		}
		
	}
	
	public void Render()
	{
		bufferStrategy = this.getBufferStrategy();
		bufferGraphics = null;
		try
		{
			bufferGraphics = bufferStrategy.getDrawGraphics();
			bufferGraphics.clearRect(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);
			
			//draw 2 screen
			Draw(bufferGraphics);
		}
		catch (Exception e) {}
		finally
		{
			if (bufferGraphics != null)
				bufferGraphics.dispose();
		}
		
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void Draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		DrawGrid(g2);
		
		for (int i = 0; i < snakeLoc.size(); i++)
		{
			if (i == 0)
			{
				//draw head
				g2.drawImage(getRotatedInstance(snakeHead, Math.toRadians(Direction.degreeImageRotation(snakeDir.get(i)))), snakeLoc.get(i).getX() * BOX_SIZE,  snakeLoc.get(i).getY() * BOX_SIZE, 10, 10, null);

			}
			else
			{
				//draw body
				g2.drawImage(getRotatedInstance(snakeBody, Math.toRadians(Direction.degreeImageRotation(snakeDir.get(i)))), snakeLoc.get(i).getX() * BOX_SIZE,  snakeLoc.get(i).getY() * BOX_SIZE, 10, 10, null);
			}
				
		}
		
		g2.drawImage(dotImage, dot.getX() * BOX_SIZE, dot.getY() * BOX_SIZE, 10, 10, null);
		
		g.drawString("Score: " + score, 10, GRID_SIZE * BOX_SIZE + 15);
		g.drawString("Speed: " + (150 - speed), 10, GRID_SIZE * BOX_SIZE + 30);
		g.drawString("Size: " + currentLength, 10, GRID_SIZE * BOX_SIZE + 45);
		g.drawString("SNAKE - Created in under two hours by Brandon Milton, http://brandonsoft.com", 10, GRID_SIZE * BOX_SIZE + 60);
		if (lost)
		{
			Font oldFont = g.getFont();
			g.setColor(Color.RED);
			g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 30));
			g.drawString("YOU LOST. - Score: " + score + "\n SPACE To restart", 0, (int)this.getPreferredSize().getHeight() / 2);
			g.setColor(Color.BLACK);
			g.setFont(oldFont);
		}
			
	}
	
	@Override
	public void run() {
		while (true)
		{
			DoLogic();
			Render();
			
			//Now we wait.
			Thread.currentThread();
			try
			{
				Thread.sleep(speed);
			}
			catch (Exception e) {}
		}
		
	}
	
	public void DrawGrid(Graphics2D g)
	{
		//First, let's draw the horizontal lines
		for (int x = 0; x <= GRID_SIZE; x+= 1)
		{
			g.drawLine(x * BOX_SIZE, 0, x * BOX_SIZE, GRID_SIZE * BOX_SIZE); //vertical
			g.drawLine(0, x * BOX_SIZE, GRID_SIZE * BOX_SIZE, x * BOX_SIZE); //horizontal
		}
		
		//g.fillRect(0, 0, GRID_SIZE * BOX_SIZE, GRID_SIZE * BOX_SIZE);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		Location frontLoc = snakeLoc.get(0);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (snakeDir.get(0) != Direction.RIGHT && (snakeDir.get(0) != Direction.LEFT))
				turnPoints.put(new Location(frontLoc.getX(), frontLoc.getY()), Direction.RIGHT);
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (snakeDir.get(0) != Direction.LEFT && (snakeDir.get(0) != Direction.RIGHT))
				turnPoints.put(new Location(frontLoc.getX(), frontLoc.getY()), Direction.LEFT);
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (snakeDir.get(0) != Direction.DOWN && (snakeDir.get(0) != Direction.UP))
				turnPoints.put(new Location(frontLoc.getX(), frontLoc.getY()), Direction.DOWN);
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			if (snakeDir.get(0) != Direction.UP && (snakeDir.get(0) != Direction.DOWN))
				turnPoints.put(new Location(frontLoc.getX(), frontLoc.getY()), Direction.UP);
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			if (lost)
				start();
		}
	}
	
	public void GenerateDot()
	{
		Random rnd = new Random();
		int randomX = rnd.nextInt(GRID_SIZE);
		int randomY = rnd.nextInt(GRID_SIZE);
		while (randomX < 0 || randomX > GRID_SIZE || randomY < 0 || randomY > GRID_SIZE || snakeLoc.contains(new Location(randomX, randomY)))
		{
			randomX = rnd.nextInt(GRID_SIZE);
			randomY = rnd.nextInt(GRID_SIZE);
		}
		dot = new Location(randomX, randomY);
	}
	
	public void AddSnakeLength()
	{
		int dir = snakeDir.get(currentLength - 2);
		Location lastSnakeBlock = snakeLoc.get(snakeLoc.size() - 1);
		snakeLoc.add(new Location(lastSnakeBlock.getX() + (Direction.getXAdd(dir) * -1), lastSnakeBlock.getY() + (Direction.getYAdd(dir) * -1)));
		snakeDir.add(dir);
	}
	
	public void DotCheck()
	{
		if (snakeLoc.get(0).equals(dot))
		{
			//the snake is on a dot. Woooo
			currentLength++;
			score+= 2;
			AddSnakeLength();
			
			//generate new dot
			GenerateDot();
			
		}
	}
	
	public void LoseCheck()
	{
		//First let's check to see if we are crashing into eachother! :O
		@SuppressWarnings("unchecked")
		ArrayList<Location> checkList = (ArrayList<Location>)snakeLoc.clone();
		for (int i = 0; i < checkList.size(); i++)
		{
			Location loc = checkList.get(i);
			if (checkList.lastIndexOf(loc) != i)
				//we lose
				lost = true;
		}
		checkList.clear();
//now let's check to see if we're oob
Location frontLoc = snakeLoc.get(0);
if (frontLoc.getX() < 0 || frontLoc.getX() >= GRID_SIZE || frontLoc.getY() < 0 || frontLoc.getY() >= GRID_SIZE)
	lost = true;
	}
	
	public void start()
	{
		snakeLoc.clear();
		snakeDir.clear();
		turnPoints.clear();
		//now we put the snake in its place. >:|
		currentLength = STARTING_LENGTH;
		speed = STARTING_SPEED;
		score = 0;
		for (int i = STARTING_LENGTH; i > 0; i--)
		{
			snakeLoc.add(new Location(i, 0));
			snakeDir.add(Direction.RIGHT);
		}
				
				
		GenerateDot();
				
		lost = false;
	}
	private Image getRotatedInstance(Image img, double theta){
        BufferedImage rotatedImg = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = (Graphics2D)rotatedImg.getGraphics();
        g2.rotate(theta, img.getWidth(this)/2, img.getHeight(this)/2);
        g2.drawImage(img, 0, 0, this);
        return rotatedImg;
    }
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
