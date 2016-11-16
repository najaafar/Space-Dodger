import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;



public class Main{

	private static DatagramSocket socket;
	private static int x, y;
	private static String host;
	private static InetAddress address;
	private static String username;
	private static Player player;
	private static GameFrame game;
	private static ChatPanel chat;
	private static JPanel mainPanel;
	private static String ChatServerAddress = "192.168.0.108";
	

	public static void main(String args[]) throws IOException{
		host = "192.168.0.108";
		address = InetAddress.getByName(host);
		socket = new DatagramSocket();

		// default values
		x = 400;
		y = 200;
		username = "player1";
	
		if(args.length < 1){
			System.out.println("Usage: java Main username");
			System.exit(1);
		}

		username = args[0];

		connectToServer(username);		// attempt to connect to host
		
		if(startGame()){	// if host has sent message to start the game
			// create game gui
			player = new Player((int) x, (int) y, username);
			game = new GameFrame(player);
			game.setPreferredSize(new Dimension(500,500));
			JFrame frame = new JFrame("Space Dodger: "+username);
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new GridLayout(1,2));
			chat = new ChatPanel(username, host);
			chat.setPreferredSize(new Dimension(500,500));
			mainPanel.add(chat);
			mainPanel.add(game);
			
			frame.setSize(1000, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(mainPanel);	// get from server
			frame.setResizable(false);
			frame.setVisible(true);
		}
		updatePlayers();	// update server about current position of player and
							// get information about opponents and asteroids
		socket.close();		// game finish
	}



	private static void updatePlayers() throws IOException {
		byte[] message;
		DatagramPacket packet;
		while(true){
			// send packet with player's current coordinates
			message = new byte[256];
			message = (player.x + "," + player.y + "," + player.username + ",coords").getBytes();
			packet = new DatagramPacket(message, message.length, address, 9000);
			socket.send(packet);
		
			
			// receive a packet from the server
			message = new byte[256];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String from_server = new String(packet.getData(), 0, packet.getLength());

			// if packet contains opponent coordinates
			if(from_server.contains("opponent")){
				try{
					String[] opponent = (new String(packet.getData(), 0, packet.getLength())).split(",");
					int o_x = Integer.parseInt(opponent[1]);
					int o_y = Integer.parseInt(opponent[2]);
					String opponent_name = (opponent[3]).trim();
					
					Boolean opponent_exists = false;
					for(Opponent o : game.opponents){	// update opponent
						if(((o.getUsername()).trim()).equals(opponent_name)){

							o.updateCoords(o_x, o_y);
							opponent_exists = true;
						}
					}
					if(!opponent_exists)				// add opponent
						game.opponents.add(new Opponent(o_x, o_y, opponent_name));

					game.repaint();
				}catch(Exception e){}
			}

			// if packet contains asteroid coordinates
			else if(from_server.contains("asteroid")){
				// TODO: minsan hindi nakukuha yung message kahit na-broadcast
					try{
						String[] ast_coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

						int ax = Integer.parseInt(ast_coordinates[0]);
						int ay = Integer.parseInt(ast_coordinates[1]);
						
						System.out.println("Received asteroid coordinates of " + ax + ", " + ay);
						game.addAsteroid(new Asteroid(ax+100, ay-700));
					}catch(Exception e){}
			}

			// TODO: receive packet with time left and ranking
			
		}
	}

	private static void connectToServer(String username) throws IOException {
		byte message[] = new byte[256];
		message = username.getBytes();
		DatagramPacket packet = new DatagramPacket(message, message.length, address, 9000);	
		socket.send(packet);

		try{
			message = new byte[256];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String[] coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

			x = Integer.parseInt(coordinates[0]);
			y = Integer.parseInt(coordinates[1]);
			
			player.x = x;
			player.y = y;

		}catch(Exception e){}
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