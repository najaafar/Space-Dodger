import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.Random;
import java.util.ArrayList;

public class Asteroid extends Entity{

	int startX, startY;

	public Asteroid(int x, int y){

		super(x,y);
		startY = y;
		startX = x;
	}

	public void update(){

		y += 2;

	/* Uncomment this and implement function if player can shoot projectiles
		checkCollisions();
	*/
		checkOffScreen();
	}

	public void draw(Graphics2D g2d){

		g2d.drawImage(getAsteroidImg(), x, y, null);

	}

	public Image getAsteroidImg(){

		ImageIcon ic = new ImageIcon("asteroid.png");
		return ic.getImage();

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

		return new Rectangle(x, y, getAsteroidImg().getWidth(null), getAsteroidImg().getHeight(null));

	}

}
