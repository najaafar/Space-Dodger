import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {

    static Vector ClientSockets;
	static Vector PlayerNames;
	static DatagramSocket socket;

	public static void main(String args[]) throws IOException {
		socket = new DatagramSocket(9000);
		
		ClientSockets = new Vector();
        PlayerNames = new Vector();

        waitForPlayers();
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

			Random random = new Random();
			int x = random.nextInt(400 - 100 + 1) + 100;
			int y = random.nextInt(400 - 100 + 1) + 100;

			// For multiple players, pag complete na tsaka mag-assign. Randomize then shuffle.

			System.out.println(player + " has joined. Coordinates are: (" + x + ", " + y + ").");

			message = (x + "," + y).getBytes();
			packet = new DatagramPacket(message, message.length, player_address, port);
			socket.send(packet);
		}
	}
}