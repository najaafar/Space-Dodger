import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Point;
import java.util.concurrent.TimeUnit;


public class GameServer {

    static private ArrayList<PlayerAddress> clientAddresses;
	static private DatagramSocket socket;
	static private ArrayList<Point> points;
	static private ArrayList<Point> asteroids;
	static private int asteroidCount = 2;

	public static void main(String args[]) throws IOException {
		socket = new DatagramSocket(9000);
		clientAddresses = new ArrayList<PlayerAddress>();

        waitForPlayers();
        points = generateRandomPoints(clientAddresses.size());
        asteroids = generateRandomPoints(asteroidCount);
        startGame();
	}

	private static ArrayList<Point> generateRandomPoints(int size){
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

		return (new ArrayList<Point>(set));

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

				// game start
				message = ("g").getBytes();
				packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
				socket.send(packet);
				System.out.println(p.getUsername() + " has entered the game.");


			}
		}
		while(true){
			try {
    			TimeUnit.SECONDS.sleep(3L);

    			for(Point asteroid : asteroids){
					message = new byte[256];
					message = ((int)(asteroid.getX()) + "," + (int)(asteroid.getY())).getBytes();
					for(PlayerAddress p : clientAddresses){
						packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
					socket.send(packet);
					}
				}
			} catch(InterruptedException ex) {
			}


			// receive packet with current coordinates
				message = new byte[256];
				packet = new DatagramPacket(message, message.length);
				socket.receive(packet);
				String[] opponent = (new String(packet.getData(), 0, packet.getLength())).split(",");
				System.out.println(new String(packet.getData(), 0, packet.getLength()));
			//	p.coordinates = new Point(Integer.parseInt(opponent[0]), Integer.parseInt(opponent[1]));


			for(PlayerAddress p : clientAddresses){
				for(PlayerAddress q : clientAddresses){
					message = new byte[256];
					message = (((int) (q.coordinates.getX())) + "," + ((int) (q.coordinates.getY())) + "," + q.getUsername()).getBytes();
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

			if(clientAddresses.size() >= 3){
				System.out.println("3 or more players have joined. Start the game? [Y/N]");
				BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
				char choice = IN.readLine().charAt(0);
				if(choice == 'Y' || choice == 'y') break;
			}
		}
	}
}