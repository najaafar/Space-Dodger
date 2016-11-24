import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

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
	private static JPanel gamePanel;
	private static JPanel blackPanel;
	private static TimerFrame time;
	private static InfoPlayerFrame info;  //other info for display
	//private static MouseListener


	public static void main(String args[]) throws IOException{ 
		host = args[1];
		address = InetAddress.getByName(host);
		socket = new DatagramSocket();

		// default values
		x = 400;
		y = 200;
		username = "player1";
	
		if(args.length < 2){
			System.out.println("Usage: java Main <username> <server ip address>");
			System.exit(1);
		}

		username = args[0];
		host = args[1];

		connectToServer(username);		// attempt to connect to host
		
		System.out.println("Waiting for players...");

		if(startGame()){	// if host has sent message to start the game
			// create game gui
			player = new Player((int) x, (int) y, username);
			game = new GameFrame(player);
			time = new TimerFrame();
			info = new InfoPlayerFrame();
			game.setPreferredSize(new Dimension(500,480));
			JFrame frame = new JFrame("Space Dodger: [ "+username+ " ]");
			mainPanel = new JPanel();
			gamePanel = new JPanel();
			gamePanel.setLayout(new BorderLayout());
			mainPanel.setLayout(new GridLayout(1,2));
			gamePanel.add(game, BorderLayout.CENTER);
			gamePanel.add(time, BorderLayout.NORTH);
			gamePanel.add(info, BorderLayout.SOUTH);
			chat = new ChatPanel(username, host);
			chat.setPreferredSize(new Dimension(500,500));
			
			chat.addMouseListener(new MouseListener() {
                public void mouseReleased(MouseEvent e) {
					
				}
                public void mousePressed(MouseEvent e) {
					
				}
                public void mouseExited(MouseEvent e) {
                    
                }
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().requestFocusInWindow();
                }
                public void mouseClicked(MouseEvent e) {
					
					
				}
            });
			
			game.addMouseListener(new MouseListener() {
                public void mouseReleased(MouseEvent e) {
					
				}
                public void mousePressed(MouseEvent e) {
					
				}
                public void mouseExited(MouseEvent e) {
                    
                }
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().requestFocusInWindow();
                }
                public void mouseClicked(MouseEvent e) {
					
					
				}
            });
			
			time.addMouseListener(new MouseListener() {
                public void mouseReleased(MouseEvent e) {
					
				}
                public void mousePressed(MouseEvent e) {
					
				}
                public void mouseExited(MouseEvent e) {
                    
                }
                public void mouseEntered(MouseEvent e) {
                    e.getComponent().requestFocusInWindow();
                }
                public void mouseClicked(MouseEvent e) {
					
					
				}
            });
			
			mainPanel.add(gamePanel);
			mainPanel.add(chat);
			

			frame.setSize(1000, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(mainPanel);	// get from server
			frame.setResizable(false);
			frame.setVisible(true);
			game.setFocusable(true);
			chat.setFocusable(false);
		}
		updatePlayers();	// update server about current position of player and
							// get information about opponents and asteroids and projectiles
		socket.close();		// game finish
	}


	private static void updatePlayers() throws IOException {
		byte[] message;
		DatagramPacket packet;
		while(true){
			// send packet with player's current coordinates
			if(player.isAlive){
				
				message = new byte[256];
				message = (player.x + "," + player.y + "," + player.username + ",coords").getBytes();
				packet = new DatagramPacket(message, message.length, address, 9000);
				socket.send(packet);
				
			}else{
				
				message = new byte[256];
				message = (player.username + ",dead").getBytes();
				packet = new DatagramPacket(message, message.length, address, 9000);
				socket.send(packet);
				
			}

			// receive a packet from the server
			message = new byte[256];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String from_server = new String(packet.getData(), 0, packet.getLength());

			// if packet contains opponent coordinates
			if(from_server.contains("opponent")){
				if(!from_server.contains("dead")){
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
					}catch(Exception e){
						
					}
					
				}else{
					String[] opponent = (new String(packet.getData(), 0, packet.getLength())).split(",");
					String opponent_name = (opponent[2]).trim();
					
					for(Opponent o : game.opponents){	// remove opponent
						if(((o.getUsername()).trim()).equals(opponent_name)){
							o.changeStatus();
						}
					}
				}
				
			}else if(from_server.contains("projectile")){
				try{
					String[] projectile = (new String(packet.getData(), 0, packet.getLength())).split(",");
					int o_x = Integer.parseInt(projectile[1]);
					int o_y = Integer.parseInt(projectile[2]);
					String projectile_name = (projectile[3]).trim();
						
					Boolean projectile_exists = false;
					for(Projectile_Blaster o : game.projectiles){	// update projectile
						if(((o.getUsername()).trim()).equals(projectile_name)){
							o.updateCoords(o_x, o_y);
							projectile_exists = true;
						}
					}
					if(!projectile_exists)				// add projectile
						game.projectiles.add(new Projectile_Blaster(o_x, o_y, projectile_name));

					game.repaint();
				}catch(Exception e){
					//read projectile failed
				}

			}else if(from_server.contains("asteroid")){// if packet contains asteroid coordinates
				// TODO: minsan hindi nakukuha yung message kahit na-broadcast
					try{
						String[] ast_coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

						int ax = Integer.parseInt(ast_coordinates[0]);
						int ay = Integer.parseInt(ast_coordinates[1]);
						
						System.out.println("Received asteroid coordinates of " + ax + ", " + ay);
						game.addAsteroid(new Asteroid(ax+100, ay-700));
						
					}catch(Exception e){
						
					}
			}else if(from_server.contains("WIN")){// if packet contains asteroid coordinates

					try{
						String[] res = (new String(packet.getData(), 0, packet.getLength())).split(",");

						String name = res[1];
						
						gamePanel.remove(time);
						gamePanel.remove(game);
						gamePanel.remove(info);
						
						blackPanel = new JPanel();
						blackPanel.setBackground(Color.BLACK);
						gamePanel.add(blackPanel, BorderLayout.CENTER);
						
						gamePanel.validate();
						gamePanel.repaint();
						JOptionPane.showMessageDialog(null,"You won, "+name+"! :D");
						break;
						
					}catch(Exception e){
						
					}
			}else if(from_server.contains("LOSE")){// if packet contains asteroid coordinates

					try{
						String[] res = (new String(packet.getData(), 0, packet.getLength())).split(",");

						String name = res[1];
						System.out.println(name + ", you lost. :(");
						gamePanel.remove(time);
						gamePanel.remove(game);
						gamePanel.remove(info);
						
						blackPanel = new JPanel();
						blackPanel.setBackground(Color.BLACK);
						gamePanel.add(blackPanel, BorderLayout.CENTER);
						
						gamePanel.validate();
						gamePanel.repaint();
						JOptionPane.showMessageDialog(null,"Too bad you lost, "+name+"! :(");
						break;
						
					}catch(Exception e){
						
					}
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