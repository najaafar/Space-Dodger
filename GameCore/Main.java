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
	private static Player player;
	private static GameFrame game;

	public static void main(String args[]) throws IOException{
		host = "127.0.0.1";
		address = InetAddress.getByName(host);

		x = 400;
		y = 200;
		username = "player1";
		socket = new DatagramSocket();
	
		connectToServer();
		
		if(startGame()){
			player = new Player((int) x, (int) y, username);
			game = new GameFrame(player);
			JFrame frame = new JFrame("Space Dodger");
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(game);	// get from server
			frame.setResizable(false);
			frame.setVisible(true);
		}
		updatePlayers();
		socket.close();
	}



	private static void updatePlayers() throws IOException {
		byte[] message;
		DatagramPacket packet;
		while(true){
			// send packet with player's current x, y
		
			System.out.println(game.getPlayerPositionX()+ "," + game.getPlayerPositionX() + "," + game.getPlayerUsername()); // coords do not update??

			message = new byte[256];
			message = (player.x + "," + player.y + "," + player.username).getBytes();
			packet = new DatagramPacket(message, message.length, address, 9000);
			socket.send(packet);
		
			// receive packet with opponents' current coordinates
			message = new byte[256];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String[] opponent = (new String(packet.getData(), 0, packet.getLength())).split(",");

			int o_x = Integer.parseInt(opponent[0]);
			int o_y = Integer.parseInt(opponent[1]);
			String opponent_name = (opponent[2]).trim();


			if(!opponent_name.equals(username)){
				Player opp = new Player(o_x, o_y, opponent_name);
				System.out.println("Opponent: " + opponent_name + " at (" + o_x + "," + o_y + ")");
				// draw
				game.paint()
			}

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