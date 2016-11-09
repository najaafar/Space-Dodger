import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Player extends Entity{

	int velX = 0, velY = 0;
	int speed = 2;
	String username;
	boolean shoot_yes = false;

	/*  after n collisions, player dies
		int life = 3;
	*/
	
	public Player(int x, int y, String username){
	
		super(x, y);
		this.username = username;
	
	}

	public void update(){

		y += velY;
		x += velX;
		
		// send to server

		checkCollisions();// checks if player is hit by asteroid

	}

	public void draw(Graphics2D g2d){

		g2d.drawImage(getPlayerImg(),x,y,null);

	}

	public Image getPlayerImg(){

		ImageIcon ic = new ImageIcon("player.png");
		return ic.getImage();

	}

	public void keyPressed(KeyEvent e){

		int key = e.getKeyCode();

		if(key == KeyEvent.VK_A){

			velX = -speed;

		}else if(key == KeyEvent.VK_D){

			velX = speed;

		}if(key == KeyEvent.VK_W){

			velY = -speed;

		}else if(key == KeyEvent.VK_S){

			velY = speed;

		}else if(key == KeyEvent.VK_SPACE){

			shoot_yes = true;

		}

	}

	public void keyReleased(KeyEvent e){

		int key = e.getKeyCode();

		if(key == KeyEvent.VK_A){

			velX = 0;

		}else if(key == KeyEvent.VK_D){

			velX = 0;

		}if(key == KeyEvent.VK_W){

			velY = 0;

		}else if(key == KeyEvent.VK_S){

			velY = 0;

		}else if(key == KeyEvent.VK_SPACE){

			shoot_yes = false;
		}

	}

	public void checkCollisions(){// collision detection with asteroids

		ArrayList<Asteroid> asteroids = GameFrame.getAsteroidList();

		for(int i=0; i<asteroids.size(); i++){

			Asteroid tempAsteroid = asteroids.get(i);

			if(getBounds().intersects(tempAsteroid.getBounds())){

				JOptionPane.showMessageDialog(null, "You died!");
				System.exit(0);

			}

		}

	}

	public Rectangle getBounds(){// gets image boundary (to be used for collision detection)

		return new Rectangle(x, y, getPlayerImg().getWidth(null), getPlayerImg().getHeight(null));

	}

	//public boolean Shoot(boolean shoot_yes){

}
