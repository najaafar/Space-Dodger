import javax.swing.JFrame;
import java.io.*;
import java.net.*;
import java.awt.Point;


public class Main{

	static private DatagramSocket socket;
	static private int x, y;

	public static void main(String args[]) throws IOException{
		x = 400;
		y = 200;
		socket = new DatagramSocket();
	
		if(connectToServer()){
			// send request to server
			// receive message from server with returned coordinates
					// lagay mo sa list of points?
			// pag sinabi ng server na 3 na, execute JFrame

			JFrame frame = new JFrame("Space Dodger");
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(new GameFrame(new Point(x, y), null));
			frame.setResizable(false);
			frame.setVisible(true);
		}

		socket.close();
	}

	private static Boolean connectToServer() throws IOException {
		String host = "192.168.1.40";
		byte message[] = new byte[256];
		System.out.println("Username: ");

		BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
		String username = IN.readLine();

		message = username.getBytes();
		InetAddress address = InetAddress.getByName(host);
	
		DatagramPacket packet = new DatagramPacket(message, message.length, address, 9000);	
		socket.send(packet);

		message = new byte[256];
		packet = new DatagramPacket(message, message.length);
		socket.receive(packet);
		String[] coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

		x = Integer.parseInt(coordinates[0]);
		y = Integer.parseInt(coordinates[1]);
		System.out.println(x + ", " + y);
		return true;
	}


}