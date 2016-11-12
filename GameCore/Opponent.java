import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;

public class Opponent extends Entity{
	private String username;
	private Image playerImg;

	public Opponent(int x, int y, String username){
		super(x, y);
		this.username = username;
		playerImg = (new ImageIcon("player.png")).getImage();
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
}