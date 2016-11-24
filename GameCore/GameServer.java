import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.Point;
import java.util.concurrent.TimeUnit;


public class GameServer {

	static private GameServer gs;
    static private ArrayList<PlayerAddress> clientAddresses;
	static private ArrayList<Point> asteroids = new ArrayList<>();
	static private DatagramSocket socket;
	static private ArrayList<Point> points;
	static protected int asteroidCount = 1;
	static private int numOfPlayers = 0;
	static private int playerDeathCount = 0;

	public final Runnable sendAsteroid;	// has delay of 2 seconds

	public GameServer(){
		sendAsteroid = new Runnable(){
			public void run(){
				byte message[] = new byte[256];
				DatagramPacket packet = null;

				while(true){
					
					try{
					TimeUnit.SECONDS.sleep(2L);
					// send packet with asteroid coordinates every 2 seconds
					} catch(InterruptedException e){}

					try {

						asteroids.clear();
		    			asteroids = generateRandomPoints(asteroidCount);

		    			for(Point asteroid : asteroids){
							message = new byte[256];
							message = ((int)(asteroid.getX()) + "," + (int)(asteroid.getY()) + ",asteroid").getBytes();
							for(PlayerAddress p : clientAddresses){
								packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
								socket.send(packet);
								System.out.println("Sent asteroid coordinates of " + asteroid.getX() + ", " + asteroid.getY() + " to " + p.getUsername());
							}
						}
					} catch(IOException ex) {}
				}
			}
		};
	}

	public static void main(String args[]) throws IOException {
		gs = new GameServer();
		socket = new DatagramSocket(9000);
		clientAddresses = new ArrayList<PlayerAddress>();

        waitForPlayers();	// complete 3 or more players
        points = generateRandomPoints(clientAddresses.size());	// randomly generate initial coordinates of players
        startGame();
        updateGame();
	}

	private static ArrayList<Point> generateRandomPoints(int size){
		Set<Point> set = new HashSet<Point>();
		Random position = new Random();
		Point test;

		do{
		    test = new Point();
		    test.x = position.nextInt(400) + 50;
		    test.y = position.nextInt(400) + 50;   
		    set.add(test);     
		}
		while (set.size()<size);

		return (new ArrayList<Point>(set));

	}

	private static void startGame() throws IOException{
		byte message[] = new byte[256];
		DatagramPacket packet = null;
		int i = 0;

		// send message to all players that the game will start
		if(clientAddresses != null){
			for(PlayerAddress p : clientAddresses){
				Point rand = points.get(i++);
				System.out.println(rand + "");
				p.changeCoords((int)(rand.getX()), (int)(rand.getY()));
				message = ((int)(p.getCoords().getX()) + "," + (int)(p.getCoords().getY())).getBytes();
				packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
				socket.send(packet);

				// game start
				message = ("g").getBytes();
				packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
				socket.send(packet);
				System.out.println(p.getUsername() + " has entered the game.");
			}
		}
	}


	private static void updateGame() throws IOException{
		byte message[] = new byte[256];
		DatagramPacket packet = null;

		// send asteroid coordinates every 3 seconds
		new Thread(gs.sendAsteroid).start();

		while(true){
			// broadcast players' coordinates
			for(PlayerAddress p : clientAddresses){
				for(PlayerAddress q : clientAddresses){
					if(!p.getUsername().equals(q.getUsername())){
						if(q.getStatus()){
							
							message = new byte[256];
							message = ("opponent," + ((int) (q.getCoords().getX())) + "," + ((int) (q.getCoords().getY())) + "," + q.getUsername()).getBytes();
							packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
							socket.send(packet);
							
						}else{
							
							message = new byte[256];
							message = ("opponent,dead," + q.getUsername()).getBytes();
							packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
							socket.send(packet);
							
						}
					}
				}
			}

			// receive a message
				message = new byte[256];
				packet = new DatagramPacket(message, message.length);
				socket.receive(packet);
				String from_player = new String(packet.getData(), 0, packet.getLength());

			// if message contained coordinates of a player
				if(from_player.contains("coords")){
					String[] coords = (new String(packet.getData(), 0, packet.getLength())).split(",");
					for(PlayerAddress q : clientAddresses){
						if((q.getUsername().trim()).equals((coords[2]).trim())){
							q.changeCoords(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
						}
					}
				}else if(from_player.contains("dead")){
					String[] msg = (new String(packet.getData(), 0, packet.getLength())).split(",");
					for(PlayerAddress q : clientAddresses){
						if((q.getUsername().trim()).equals((msg[0]).trim())){
							if(q.getCheckedStatus() == false){// checks if player has been marked "dead" already
								q.changeStatus();
								q.isChecked();
								playerDeathCount = playerDeathCount + 1;
							}
						}
					}
				}else if(from_player.contains("logout")){
					System.out.println("logout a player");
					String[] msg = (new String(packet.getData(), 0, packet.getLength())).split(",");
					removePlayer(msg[0].trim());
				}
				
		//		System.out.println("Current Death Count: "+playerDeathCount);
				
				if(playerDeathCount == (numOfPlayers - 1)){// checks if only one player is left alive
				
					//JOptionPane.showMessageDialog(null,"All other Players have died! Game over!");
					
					for(PlayerAddress p : clientAddresses){
						for(PlayerAddress q : clientAddresses){
							if(!p.getUsername().equals(q.getUsername())){
								if(q.getStatus()){
									
									message = new byte[256];
									message = ("WIN," + q.getUsername()).getBytes();
									packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
									socket.send(packet);
									
								}else{
									
									message = new byte[256];
									message = ("LOSE," + q.getUsername()).getBytes();
									packet = new DatagramPacket(message, message.length, p.getAddress(), p.getPort());
									socket.send(packet);
									
								}
							}
						}
					}
				}
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

			numOfPlayers = numOfPlayers + 1;

			System.out.println(player + " has joined.");

			if(clientAddresses.size() >= 2){
				System.out.println(numOfPlayers + " players have joined. Start the game? [Y/N]");
				BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
				char choice = IN.readLine().charAt(0);
				if(choice == 'Y' || choice == 'y') break;
			}
		}
	}

	protected static void removePlayer(String name){
		int i = 0;
		
		for(PlayerAddress q : clientAddresses){
			if(name.equals(q.getUsername().trim())){
				System.out.println(name + " removed");
				try{
					clientAddresses.remove(i);
				}catch(ConcurrentModificationException e){
				}
			}
			i++;
		}
	}
}