import java.net.*;
import java.awt.Point;

public class PlayerAddress{
	private InetAddress address;
	private String username;
	private int port;
	private double time; // "score"
	public Point coordinates;

	public PlayerAddress(InetAddress a, String u, int p){
		this.address = a;
		this.username = u;
		this.port = p;
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
}