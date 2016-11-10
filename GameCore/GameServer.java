import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Point;

public class GameServer {

    static public ArrayList<PlayerAddress> clientAddresses;
	static public DatagramSocket socket;
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
			ClientThread t = new ClientThread(socket);
			new Thread(t).start();

			System.out.println(player + " has joined.");


			if(packet!=null){
				t.sendMessageToAll(packet);
			} 


			if(clientAddresses.size() == 3) break; // add time constraint
		}
	}
}


class ClientThread  implements Runnable {
	GameServer server;  // an instance of the server to be passed into constructor
	DatagramSocket socket;

	public ClientThread(DatagramSocket socket){ 
		this.socket = socket;
	} // end constructor
	
	/* sends the incoming message to all but who it was received from by checking the port numbers */
	 public void sendMessageToAll (DatagramPacket indata)
	{
	    DatagramPacket inPacket  = indata;
	    byte[] data = new byte[1024];
		String info = new String(indata.getData());
		int incomingPort = inPacket.getPort();  // the incoming port number
		data = info.getBytes();
		 
		System.out.println("info coming in: " + info);
      /* iterate through each client and send them the packet, except who sent it */ 
		for(int i=0; i<server.clientAddresses.size(); i++)
		{
			int prt = server.clientAddresses.get(i).getPort(); //the other clients ports
			if(incomingPort != prt){
				InetAddress ip = server.clientAddresses.get(i).getAddress();
				DatagramPacket output = new DatagramPacket(data, data.length,ip,prt);
				try {
					  socket.send(output);
				 } catch (IOException er) {
		              System.out.println(er);
	             } // end try
		    } // end if
		}
	}// end of sendMessageToAll
	
    
    
	/* override run to and send packet received to method sendMessageToAll */
	public void run(){

		/*
        while(true)
            {  
			 byte[] receiveData = new byte[256];
              // create an empty DatagramPacket packet
              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        
             try {
	         socket.receive(receivePacket);
             } catch (IOException er) {
	              System.out.println(er);
             }
                if (receivePacket != null)
                {  
					sendMessageToAll(receivePacket);
				 }  // end of if
			} // end of while		*/
		} // end of run()
	
} // end of class ClientThread