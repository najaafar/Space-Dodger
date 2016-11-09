import javax.swing.*;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class GameFrame extends JPanel implements ActionListener{

	Timer mainTimer;
	Player player;
	Random rand = new Random();
	
	static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	int asteroidCount = 2;
	public static int level = 1;
	
	public GameFrame(Point playerXY, ArrayList<Point> players){
	
		setFocusable(true);
		player = new Player((int) playerXY.getX(), (int) playerXY.getY());	// get form server
		addKeyListener(new KeyAdapt(player));
		
		mainTimer = new Timer(5, this);
		mainTimer.start();
		startGame();
	}

	public void paint(Graphics g){// draw background, player, asteroids
	
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		ImageIcon ic = new ImageIcon("bg.png");
		g2d.drawImage(ic.getImage(), 0, 0, null);
		
		player.draw(g2d);
		
		for(int i=0; i<asteroids.size(); i++){
		
				Asteroid tempAsteroid = asteroids.get(i);
				tempAsteroid.draw(g2d);
		
			}

		
	}
	
	public void actionPerformed(ActionEvent e){
	
		player.update();
		
		for(int i=0; i<asteroids.size(); i++){
		
			Asteroid tempAsteroid = asteroids.get(i);
			tempAsteroid.update();
		
		}
		/*
			place end game condition here
		*/
		
		repaint();
		
	}
	
	public static void addAsteroid(Asteroid a){// adds asteroids
	
		asteroids.add(a);
	
	}
	
	public static ArrayList<Asteroid> getAsteroidList(){// gets list of asteroids
	
		return asteroids;
	
	}
	
	public void startGame(){// starts the game
		
		for(int i=0; i< asteroidCount; i++){
			
			addAsteroid(new Asteroid(rand.nextInt(500), rand.nextInt(500)-700));
		
		}
		
	
	}
	

	
}