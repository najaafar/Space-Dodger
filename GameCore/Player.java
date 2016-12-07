import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.Color;


public class Player extends Entity{

	int velX = 0, velY = 0;
	int speed = 2;
	String username;
	boolean shoot_yes = false;
	private Image playerImg;
	boolean isAlive;
	boolean shoot_send = false;

	/*  after n collisions, player dies
		int life = 3;
	*/
	
	public Player(int x, int y, String username){
	
		super(x, y);
		this.username = username;
		playerImg = (new ImageIcon("player.png")).getImage();
		isAlive = true;
	
	}

	public void update(){

		y += velY;
		x += velX;

	// comment next line if testing
	//	checkCollisions();	// checks if player is hit by asteroid

	}

	public void draw(Graphics2D g2d){

		g2d.setColor(Color.WHITE);
		g2d.drawString(username, x+15, y+68);
		g2d.drawImage(playerImg,x,y,null);

	}

	public void keyPressed(KeyEvent e){

		if(!isAlive) return;

		int key = e.getKeyCode();

		if(key == KeyEvent.VK_LEFT){

			velX = -speed;

		}else if(key == KeyEvent.VK_RIGHT){

			velX = speed;

		}if(key == KeyEvent.VK_UP){

			velY = -speed;

		}else if(key == KeyEvent.VK_DOWN){

			velY = speed;

		}else if(key == KeyEvent.VK_SPACE){

			shoot_yes = true;

		}

	}

	public void keyReleased(KeyEvent e){

		int key = e.getKeyCode();

		if(key == KeyEvent.VK_LEFT){

			velX = 0;
		}else if(key == KeyEvent.VK_RIGHT){

			velX = 0;

		}if(key == KeyEvent.VK_UP){

			velY = 0;

		}else if(key == KeyEvent.VK_DOWN){

			velY = 0;

		}else if(key == KeyEvent.VK_SPACE){

			shoot_yes = false;
		}

	}

	public boolean isShooting(){

		return shoot_yes;

	}

	public void checkCollisions(){// collision detection with asteroids

		ArrayList<Asteroid> asteroids = GameFrame.getAsteroidList();

		for(int i=0; i<asteroids.size(); i++){

			Asteroid tempAsteroid = asteroids.get(i);

			if(getBounds().intersects(tempAsteroid.getBounds())){
				
				isAlive = false;

			}

		}

		ArrayList<Projectile_Blaster> projectiles = GameFrame.getProjectileList();

	    for(int i=0; i<projectiles.size(); i++){

			Projectile_Blaster tempProjectile = projectiles.get(i);

			if(getBounds().intersects(tempProjectile.getBounds())){

	        	isAlive = false;

			}

		}

	}

	public Rectangle getBounds(){// gets image boundary (to be used for collision detection)

		return new Rectangle(x, y, playerImg.getWidth(null), playerImg.getHeight(null));

	}

	

}
