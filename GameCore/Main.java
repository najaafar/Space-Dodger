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
	private static int curr_time;
	private static Player player;
	private static GameFrame game;
	private static ChatPanel chat;
	private static JPanel mainPanel;
	private static JPanel gamePanel;
	private static JPanel blackPanel;
	private static TimerFrame time = new TimerFrame();;
	private static InfoPlayerFrame info;  //other info for display


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

		connectToServer(username);// attempt to connect to host
		
		System.out.println("Waiting for players...");

		if(startGame()){	// if host has sent message to start the game

			//show splash screen 
    		SplashScreen splash = new SplashScreen(5000); 
    		splash.showSplashAndExit();

			// create game gui
			player = new Player((int) x, (int) y, username);
			game = new GameFrame(player);
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
			// send packet if player logs out
			if(!chat.login_status){
				message = new byte[256];
				message = (player.username + ",logout").getBytes();
				packet = new DatagramPacket(message, message.length, address, 9000);
				socket.send(packet);
			}

			// send packet with player's projectile
			if(player.isShooting() && player.shoot_send){
				player.shoot_send = false;
				message = new byte[256];
				message = (player.username + ",projectile").getBytes();
				packet = new DatagramPacket(message, message.length, address, 9000);
				socket.send(packet);
			}

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
					int p_x = Integer.parseInt(projectile[1]);
					int p_y = Integer.parseInt(projectile[2]);
					String p_from = (projectile[3]).trim();
						
					System.out.println("Received asteroid coordinates of " + p_x + ", " + p_y + " from " + p_from);
					game.projectiles.add(new Projectile_Blaster(p_x, p_y, p_from));
				}catch(Exception e){
				}

			}else if(from_server.contains("asteroid")){// if packet contains asteroid coordinates
					try{
						String[] ast_coordinates = (new String(packet.getData(), 0, packet.getLength())).split(",");

						int ax = Integer.parseInt(ast_coordinates[0]);
						int ay = Integer.parseInt(ast_coordinates[1]);
						
					//	System.out.println("Received asteroid coordinates of " + ax + ", " + ay);
						game.addAsteroid(new Asteroid(ax-50, ay-700));
						
					}catch(Exception e){
						
					}
			}else if(from_server.contains("WIN")){// if packet contains WIN flag, prompt that the player won

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
			}else if(from_server.contains("LOSE")){// if packet contains LOSE flag, prompt that the player lost

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
						JOptionPane.showMessageDialog(null,"Too bad you lost, "+name+"! :(");
						break;
						
					}catch(Exception e){
						
					}
					
			}
				
			if(from_server.contains("GAME_CLOCK")){// if packet contains GAME_CLOCK flag, update JLabel on TimeFrame

				try{
					String[] game_clock = (new String(packet.getData(), 0, packet.getLength())).split(",");

					int game_time = Integer.parseInt(game_clock[1]);
					System.out.println(game_time);
					
					time.setTime(Integer.toString(game_time));
					gamePanel.validate();
					gamePanel.repaint();
					
				}catch(Exception ex){

				}		
			}
			
			if(from_server.contains("TIME_IS_UP")){// if packet contains TIME_IS_UP flag, display scores, rankings and ends the game

					try{
						String[] res = (new String(packet.getData(), 0, packet.getLength())).split(",");

						String name = res[1];
						
						
						//	fetch scores and rankings and add them to blackPanel
						

						gamePanel.remove(time);
						gamePanel.remove(game);
						gamePanel.remove(info);
						
						blackPanel = new JPanel();
						blackPanel.setBackground(Color.BLUE);
						JLabel sample = new JLabel("TIME IS UP!");
						blackPanel.add(sample);
						gamePanel.add(blackPanel, BorderLayout.CENTER);
						
						gamePanel.validate();
						gamePanel.repaint();
						JOptionPane.showMessageDialog(null,"Time's up!");
						break;
					}catch(Exception e){
						
					}
			}
		
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