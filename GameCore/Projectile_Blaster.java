import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.Random;

public class Projectile_Blaster extends Entity{

  int startX, startY;

  public Projectile_Blaster(int x, int y){

		super(x,y);
		startY = y;
		startX = x;
	}

  public void update(){

		y += -5;

	/* Uncomment this and implement function if player can shoot projectiles
		checkCollisions();
	*/
	}

	public void draw(Graphics2D g2d){

		g2d.drawImage(getProjectileImg(), x, y, null);

	}

  public Image getProjectileImg(){

		ImageIcon ic = new ImageIcon("projectile_blaster.png");
		return ic.getImage();

	}

  public boolean checkOffScreen(){

    if(y <= 0) return true;
    else return false;

  }

  public Rectangle getBounds(){// gets image boundary (to be used for collision detection)

		return new Rectangle(x, y, getProjectileImg().getWidth(null), getProjectileImg().getHeight(null));

	}

}
