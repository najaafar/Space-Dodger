import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;


public class Login{
	
	public static void main(String[] args){
		
		final JFrame login = new JFrame("Login");
		JPanel panel = new JPanel();
		final JLabel label_name = new JLabel("Username:");
		final JLabel label_ip = new JLabel("Server IP:");
		final JTextField loginName = new JTextField(20);
		final JTextField server_ip = new JTextField(20);
		JButton enter = new JButton("CONNECT");

		panel.add(label_name);
		panel.add(loginName);
		panel.add(label_ip);
		panel.add(server_ip);
		panel.add(enter);
		login.setSize(280, 250);
		login.add(panel);
		
		login.setVisible(true);
		login.setResizable(false);
		login.setLocation(500, 500);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setLocationRelativeTo(null);
		
		enter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e){
				try{
					
					ChatClient client = new ChatClient(loginName.getText(), server_ip.getText());
					login.setVisible(false);
					login.dispose();
					
				}catch(UnknownHostException e1){
					
					e1.printStackTrace();
					
				}catch(IOException e1){
					
					e1.printStackTrace();
					
				}
			}

		});

		loginName.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e){

			}

			@Override
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					
					try{
						
						ChatClient client = new ChatClient(loginName.getText(), server_ip.getText());
						login.setVisible(false);
						login.dispose();
						
					}catch(UnknownHostException e1){
						
						e1.printStackTrace();
						
					}catch(IOException e1){
						
						e1.printStackTrace();
						
					}

				}
			}

			@Override
			public void keyReleased(KeyEvent e){

			
			}

		});

	}
}
