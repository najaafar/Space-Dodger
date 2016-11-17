import java.net.*;
import java.awt.Point;

public class PlayerAddress{
	private InetAddress address;
	private String username;
	private int port;
	private double time; // "score"
	private Point coordinates;
	private boolean isAlive;
	public boolean sentStatus = false; // para 1x lang masend yung status

	public PlayerAddress(InetAddress a, String u, int p){
		this.address = a;
		this.username = u;
		this.port = p;
		this.time = 0.0;
		this.coordinates = new Point(0, 0);
		this.isAlive = true;
	}

	public InetAddress getAddress(){
		return this.address;
	}

	public String getUsername(){
		return this.username;
	}

	public int getPort(){
		return this.port;
	}

	public Point getCoords(){
		return coordinates.getLocation();
	}

	public void changeCoords(int x, int y){
		coordinates.setLocation(x, y);
	}

	public boolean getStatus(){
		return this.isAlive;
	}

	public void changeStatus(){
		isAlive = false;
	}
}