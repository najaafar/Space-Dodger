import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.Random;
import java.util.ArrayList;

public class Asteroid extends Entity{

	int startX, startY;
	Image asteroidImg;

	public Asteroid(int x, int y){

		super(x,y);
		startY = y;
		startX = x;
		asteroidImg =(new ImageIcon("asteroid.png")).getImage();
	}

	public void update(){

		y += 2;

	/* Uncomment this and implement function if player can shoot projectiles
		checkCollisions();
	*/
		checkOffScreen();
	}

	public void draw(Graphics2D g2d){

		g2d.drawString("(" + x + "," + y + ")", x+15, y+68);
		g2d.drawImage(asteroidImg, x, y, null);

	}

	public void checkOffScreen(){// checks if asteroid goes out of bounds, respawns it back in a random (x,y) position

		if(y >= 700){

			Random rand = new Random();
			y = rand.nextInt(500)-700;
			x = rand.nextInt(500);

		}

	}

	public boolean checkCollisions(){

	    ArrayList<Projectile_Blaster> projectiles = GameFrame.getProjectileList();

	    for(int i=0; i<projectiles.size(); i++){

			Projectile_Blaster tempProjectile = projectiles.get(i);

			if(getBounds().intersects(tempProjectile.getBounds())){

	        	return true;

			}

		}
		return false;
	}

	public Rectangle getBounds(){// gets image boundary (to be used for collision detection)

		return new Rectangle(x, y, asteroidImg.getWidth(null), asteroidImg.getHeight(null));

	}

}
