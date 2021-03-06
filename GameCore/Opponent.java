import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Opponent extends Entity{
	private String username;
	private Image playerImg; 
	private boolean isAlive;

	public Opponent(int x, int y, String username){
		super(x, y);
		this.username = username;
		playerImg = (new ImageIcon("player.png")).getImage(); 
		isAlive = true;
	}	

	public void updateCoords(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics2D g2d){
		g2d.setColor(Color.WHITE);
		g2d.drawString(username, x+15, y+68);
		g2d.drawImage(playerImg,x,y,null);
	}

	public String getUsername(){
		return this.username;
	} 

	public Rectangle getBounds(){// gets image boundary (to be used for collision detection) 
		return new Rectangle(x, y, playerImg.getWidth(null), playerImg.getHeight(null)); 
	}

	public boolean getStatus(){
		return this.isAlive;
	}

	public void changeStatus(){
		isAlive = false;
	}
}