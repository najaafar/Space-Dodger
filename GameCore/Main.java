import javax.swing.JFrame;
import java.io.*;
import java.net.*;
import java.awt.Point;
import java.awt.Graphics2D;

public class Main{

	private static DatagramSocket socket;
	private static int x, y;
	private static String host;
	private static InetAddress address;
	private static String username;

	public static void main(String args[]) throws IOException{
		host = "127.0.0.1";
		address = InetAddress.getByName(host);

		x = 400;
		y = 200;
		username = "player 1";
		socket = new DatagramSocket();
	
		connectToServer();
		
		if(startGame()){
			JFrame frame = new JFrame("Space Dodger");
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(new GameFrame(new Player((int) x, (int) y, username)));	// get from server
			frame.setResizable(false);
			frame.setVisible(true);
		}
		updatePlayers();
		socket.close();
	}

	private static void updatePlayers() throws IOException {
		while(true){
			byte message[] = new byte[256];
			DatagramPacket packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String[] opponent = (new String(packet.getData(), 0, packet.getLength())).split(",");

			String opponent_name = opponent[0];
/*
			int o_x = Integer.parseInt(opponent[1]);
			int o_y = Integer.parseInt(opponent[2]);

			if(!opponent_name.equals(username)){
				Player opp = new Player(o_x, o_y, opponent_name);
				System.out.println("opponent: " + opponent_name + "(" + o_x + "," + o_y + ")");
				// draw
			}
*/
		}
	}

	private static void connectToServer() throws IOException {
		byte message[] = new byte[256];
		System.out.println("Username: ");

		BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
		username = IN.readLine();
		message = username.getBytes();
	
		DatagramPacket packet = new DatagramPacket(message, message.length, address, 9000);	
		socket.send(packet);

		message = new byte[256];
		packet = new DatagramPacket(message, message.length);
		socket.receive(packet);
		String[] coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

		x = Integer.parseInt(coordinates[0]);
		y = Integer.parseInt(coordinates[1]);
		System.out.println(x + ", " + y);
	}

	private static Boolean startGame() throws IOException{
		byte message[] = new byte[256];
		DatagramPacket packet = new DatagramPacket(message, message.length, address, 9000);	

		while(true){
			message = new byte[256];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String start = new String(packet.getData());

			if((start.charAt(0))== 'g') break;
		}
		return true;
	}


}