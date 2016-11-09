import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Point;

public class GameServer {

    static private ArrayList<PlayerAddress> clientAddresses;
	static private DatagramSocket socket;
	static private ArrayList<Point> points;

	public static void main(String args[]) throws IOException {
		socket = new DatagramSocket(9000);
		clientAddresses = new ArrayList<PlayerAddress>();

        waitForPlayers();
        generateRandomPoints(clientAddresses.size());
        startGame();
	}

	private static void generateRandomPoints(int size){
		Set<Point> set = new HashSet<Point>();
		Random position = new Random();
		Point test;

		do{
		    test = new Point();
		    test.x=position.nextInt(400) + 50;
		    test.y=position.nextInt(400) + 50;   
		    set.add(test);     
		}
		while (set.size()<size);

		points = new ArrayList<Point>(set);
	}

	private static void startGame() throws IOException{
		byte message[] = new byte[256];
		DatagramPacket packet = null;
		int i = 0;
		if(clientAddresses != null){
			for(PlayerAddress p : clientAddresses){
				Point rand = points.get(i++);
				p.coordinates = rand;
				message = ((int)(rand.getX()) + "," + (int)(rand.getY())).getBytes();
				packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
				socket.send(packet);

				message = ("g").getBytes();
				packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
				socket.send(packet);
				System.out.println(p.getUsername() + " has entered the game.");
			}
		}
		while(true){
			for(PlayerAddress p : clientAddresses){
				for(PlayerAddress q : clientAddresses){
					message = new byte[256];
					message = (q.getUsername() + "," + (int) (q.coordinates.getX()) + "," + (int) (q.coordinates.getY())).getBytes();
					packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
					socket.send(packet);
				}
			}
			// update coordinates
		}
	}

	private static void waitForPlayers() throws IOException {
		while(true){
			System.out.println("Waiting for players...");
			byte message[] = new byte[256];

			DatagramPacket packet = new DatagramPacket(message, message.length);
			socket.receive(packet);

			InetAddress player_address = packet.getAddress();
			int port = packet.getPort();
			String player = new String(packet.getData());
			clientAddresses.add(new PlayerAddress(player_address, player, port));

			System.out.println(player + " has joined.");

			if(clientAddresses.size() == 3) break; // add time constraint
		}
	}
}